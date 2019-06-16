package com.jsj.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyInformation {
    private Long userId;
    private Long productId;
    private Integer buyNumber;
}
