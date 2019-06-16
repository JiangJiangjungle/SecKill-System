package com.jsj.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecKillRequest implements Serializable {
    Long id;
    Long userId;
    Long productId;
    Integer buyNumber;
}
