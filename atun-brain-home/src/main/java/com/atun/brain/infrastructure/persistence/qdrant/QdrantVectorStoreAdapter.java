package com.atun.brain.infrastructure.persistence.qdrant;

import com.atun.brain.infrastructure.config.QdrantConfig;
import com.atun.brain.infrastructure.persistence.qdrant.dto.VectorData;
import com.atun.brain.infrastructure.persistence.qdrant.dto.VectorSearchResult;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import io.qdrant.client.PointIdFactory;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections.*;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import io.qdrant.client.grpc.Points.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static io.qdrant.client.ConditionFactory.match;
import static io.qdrant.client.ConditionFactory.matchKeyword;
import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;

/**
 * Qdrant 向量存储适配器实现（异步版本）
 *
 * @author lij
 * @date 2026/02/03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QdrantVectorStoreAdapter implements VectorStoreAdapter {
    
    private final QdrantClient qdrantClient;
    private final QdrantConfig.QdrantProperties qdrantProperties;
    
    @Override
    @Async
    public void initCollection(String collectionName, int vectorSize) {
        if (!collectionExists(collectionName)) {
            ListenableFuture<CollectionOperationResponse> guavaFuture = qdrantClient.createCollectionAsync(
                    collectionName,
                    VectorParams.newBuilder()
                            .setDistance(Distance.Cosine)
                            .setSize(vectorSize).build()
            );
            
            // 使用 Guava 的工具类添加回调
            Futures.addCallback(guavaFuture, new FutureCallback<>() {
                @Override
                public void onSuccess(CollectionOperationResponse result) {
                    log.info("Qdrant 集合创建成功: {}", collectionName);
                }
                
                @Override
                public void onFailure(Throwable t) {
                    log.error("创建 Qdrant 集合失败: {}", collectionName, t);
                }
            }, MoreExecutors.directExecutor());
        }
    }
    
    @Override
    public void storeVector(String collectionName, String id, List<Float> vector, Map<String, Object> payload) {
        log.debug("存储向量到 Qdrant: collection={}, id={}", collectionName, id);
        
        try {
            // 构建点数据
            PointStruct point = PointStruct.newBuilder()
                .setId(id(UUID.fromString(id)))
                .setVectors(vectors(vector))
                .putAllPayload(convertPayload(payload))
                .build();
            
            // 异步存储到 Qdrant
            ListenableFuture<UpdateResult> updateResultListenableFuture =
                    qdrantClient.upsertAsync(collectionName, Collections.singletonList(point));
            Futures.addCallback(updateResultListenableFuture, new FutureCallback<UpdateResult>() {
                @Override
                public void onSuccess(UpdateResult result) {
                    log.info("存储向量成功: id={}", id);
                }
                @Override
                public void onFailure(Throwable t) {
                    log.error("存储向量失败: id={}", id, t);
                }
            },  MoreExecutors.directExecutor());
        } catch (Exception e) {
            log.error("存储向量异常: id={}", id, e);
            throw new RuntimeException("存储向量失败", e);
        }
    }
    
    @Override
    public void batchStoreVectors(String collectionName, List<VectorData> vectors) {
        log.info("批量存储向量: collection={}, count={}", collectionName, vectors.size());
        
        try {
            List<PointStruct> points = vectors.stream()
                .map(v -> PointStruct.newBuilder()
                    .setId(id(UUID.fromString(v.getId())))
                    .setVectors(vectors(v.getVector()))
                    .putAllPayload(convertPayload(v.getPayload()))
                    .build())
                .collect(Collectors.toList());
            
            // 异步批量存储
            ListenableFuture<UpdateResult> updateResultListenableFuture =
                    qdrantClient.upsertAsync(collectionName, points);
            Futures.addCallback(updateResultListenableFuture, new FutureCallback<UpdateResult>() {
                @Override
                public void onSuccess(UpdateResult result) {
                    log.info("批量存储向量成功");
                }
                @Override
                public void onFailure(Throwable t) {
                    log.error("批量存储向量异常");
                }
            },  MoreExecutors.directExecutor());
            
        } catch (Exception e) {
            log.error("批量存储向量异常", e);
            throw new RuntimeException("批量存储向量失败", e);
        }
    }
    
    @Override
    public List<VectorSearchResult> searchSimilar(String collectionName, List<Float> queryVector, int limit) {
        return searchSimilarWithFilter(collectionName, queryVector, null, limit);
    }
    
    @Override
    public List<VectorSearchResult> searchSimilarWithFilter(String collectionName, List<Float> queryVector,
                                                       Map<String, Object> filter, int limit) {
        try {
            log.debug("向量相似度搜索: collection={}, limit={}", collectionName, limit);
            
            SearchPoints.Builder searchBuilder = SearchPoints.newBuilder()
                .setCollectionName(collectionName)
                .addAllVector(queryVector)
                .setLimit(limit)
                .setWithPayload(WithPayloadSelector.newBuilder().setEnable(true).build());
            
            // 添加过滤条件
            if (filter != null && !filter.isEmpty()) {
                Filter.Builder filterBuilder = Filter.newBuilder();
                filter.forEach((key, value) -> {
                    if (value instanceof Long) {
                        filterBuilder.addMust(match(key, (Long) value));
                    } else if (value instanceof String) {
                        filterBuilder.addMust(matchKeyword(key, (String) value));
                    }
                });
                searchBuilder.setFilter(filterBuilder.build());
            }
            
            // 搜索操作需要同步返回结果
            ListenableFuture<List<ScoredPoint>> searchFuture = qdrantClient.searchAsync(searchBuilder.build());
            List<ScoredPoint> results = Futures.getUnchecked(searchFuture);
            
            List<VectorSearchResult> searchResults = results.stream()
                .map(point -> new VectorSearchResult(
                    point.getId().getUuid(),
                    point.getScore(),
                    convertPayloadToMap(point.getPayloadMap())
                ))
                .collect(Collectors.toList());
            
            log.debug("搜索完成，找到 {} 条结果", searchResults.size());
            return searchResults;
            
        } catch (Exception e) {
            log.error("向量搜索失败", e);
            throw new RuntimeException("向量搜索失败", e);
        }
    }
    
    @Override
    public VectorData getVector(String collectionName, String id) {
        try {
            log.debug("获取向量: collection={}, id={}", collectionName, id);
            
            ListenableFuture<List<RetrievedPoint>> retrieveFuture = qdrantClient.retrieveAsync(
                collectionName,
                Collections.singletonList(id(UUID.fromString(id))),
                true,
                true,
                null
            );
            
            List<RetrievedPoint> points = Futures.getUnchecked(retrieveFuture);
            
            if (points.isEmpty()) {
                return null;
            }
            
            RetrievedPoint point = points.get(0);
            return new VectorData(
                id,
                point.getVectors().getVector().getDataList(),
                convertPayloadToMap(point.getPayloadMap())
            );
            
        } catch (Exception e) {
            log.error("获取向量失败: id={}", id, e);
            return null;
        }
    }
    
    @Override
    public void deleteVector(String collectionName, String id) {
        log.debug("删除向量: collection={}, id={}", collectionName, id);
        
        try {
            // 1) 当点 id 是 UUID 时
            PointId pointId = PointIdFactory.id(UUID.fromString(id));
            // 异步删除
            ListenableFuture<UpdateResult> deleteFuture = qdrantClient.deleteAsync(
                collectionName,
                Collections.singletonList(id(UUID.fromString(id)))
            );
            
            Futures.addCallback(deleteFuture, new FutureCallback<UpdateResult>() {
                @Override
                public void onSuccess(UpdateResult result) {
                    log.debug("向量删除成功: id={}", id);
                }
                
                @Override
                public void onFailure(Throwable t) {
                    log.error("删除向量失败: id={}", id, t);
                }
            }, MoreExecutors.directExecutor());
        } catch (Exception e) {
            log.error("删除向量异常: id={}", id, e);
            throw new RuntimeException("删除向量失败", e);
        }
    }
    
    @Override
    public boolean collectionExists(String collectionName) {
        try {
            ListenableFuture<List<String>> listFuture = qdrantClient.listCollectionsAsync();
            List<String> response = Futures.getUnchecked(listFuture);
            return response.stream()
                .anyMatch(c -> c.equals(collectionName));
        } catch (Exception e) {
            log.error("检查集合是否存在失败", e);
            return false;
        }
    }
    
    /**
     * 将 Java Map 转换为 Qdrant Payload
     */
    private Map<String, JsonWithInt.Value> convertPayload(Map<String, Object> payload) {
        if (payload == null) {
            return new HashMap<>();
        }
        
        Map<String, JsonWithInt.Value> result = new HashMap<>();
        payload.forEach((key, val) -> {
            if (val instanceof String) {
                result.put(key, value((String) val));
            } else if (val instanceof Long) {
                result.put(key, value((Long) val));
            } else if (val instanceof Integer) {
                result.put(key, value(((Integer) val).longValue()));
            } else if (val instanceof Double) {
                result.put(key, value((Double) val));
            } else if (val instanceof Boolean) {
                result.put(key, value((Boolean) val));
            } else {
                result.put(key, value(val.toString()));
            }
        });
        return result;
    }
    
    /**
     * 将 Qdrant Payload 转换为 Java Map
     */
    private Map<String, Object> convertPayloadToMap(Map<String, JsonWithInt.Value> payload) {
        Map<String, Object> result = new HashMap<>();
        payload.forEach((key, val) -> {
            if (val.hasStringValue()) {
                result.put(key, val.getStringValue());
            } else if (val.hasIntegerValue()) {
                result.put(key, val.getIntegerValue());
            } else if (val.hasBoolValue()) {
                result.put(key, val.getBoolValue());
            } else if (val.hasDoubleValue()) {
                result.put(key, val.getDoubleValue());
            }
        });
        return result;
    }
}
