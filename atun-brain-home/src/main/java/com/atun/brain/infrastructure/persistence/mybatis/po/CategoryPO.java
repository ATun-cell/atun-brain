package com.atun.brain.infrastructure.persistence.mybatis.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 类别 PO
 *
 * @author lij
 * @date 2026/02/03
 */
@Data
public class CategoryPO {
    
    private Long id;
    private String name;
    private String type;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
