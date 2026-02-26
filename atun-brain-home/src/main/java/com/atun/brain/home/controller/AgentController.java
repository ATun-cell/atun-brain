package com.atun.brain.home.controller;

import com.atun.brain.agent.core.AgentOrchestrator;
import com.atun.brain.agent.core.model.AgentRequest;
import com.atun.brain.agent.core.model.AgentResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Agent 对话交互接口
 * 提供 REST API 用于与 Agent 进行对话
 *
 * @author atun-brain
 * @since 1.0
 */
@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AgentController {

    private final AgentOrchestrator agentOrchestrator;

    /**
     * 对话请求
     */
    @Data
    public static class ChatRequest {
        /**
         * 用户 ID（必填）
         */
        private Long userId;

        /**
         * 会话 ID（可选，不传则每次对话都是独立的）
         */
        private String sessionId;

        /**
         * 用户消息内容（必填）
         */
        private String message;
    }

    /**
     * 对话响应
     */
    @Data
    public static class ChatResponse {
        /**
         * Agent 回复内容
         */
        private String content;

        /**
         * 会话 ID（用于后续对话）
         */
        private String sessionId;

        /**
         * 是否调用了工具
         */
        private boolean toolExecuted;

        public ChatResponse(String content, String sessionId, boolean toolExecuted) {
            this.content = content;
            this.sessionId = sessionId;
            this.toolExecuted = toolExecuted;
        }
    }

    /**
     * 简单对话接口
     * POST /api/agent/chat
     *
     * @param request 对话请求
     * @return Agent 回复
     */
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        AgentRequest agentRequest = AgentRequest.builder()
                .userId(request.getUserId())
                .userMessage(request.getMessage())
                .build();

        AgentResponse response = agentOrchestrator.process(agentRequest);

        return new ChatResponse(
                response.message(),
                "default",
                response.hasToolCalls()
        );
    }

    /**
     * 带会话的对话接口（保持上下文）
     * POST /api/agent/chat/session
     *
     * @param request 对话请求
     * @return Agent 回复
     */
    @PostMapping("/chat/session")
    public ChatResponse chatWithSession(@RequestBody ChatRequest request) {
        String sessionId = request.getSessionId();
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = "session_" + System.currentTimeMillis();
        }

        AgentRequest agentRequest = AgentRequest.builder()
                .userId(request.getUserId())
                .sessionId(sessionId)
                .userMessage(request.getMessage())
                .build();

        AgentResponse response = agentOrchestrator.process(agentRequest);

        return new ChatResponse(
                response.message(),
                sessionId,
                response.hasToolCalls()
        );
    }

    /**
     * 健康检查接口
     * GET /api/agent/health
     *
     * @return 健康状态
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
