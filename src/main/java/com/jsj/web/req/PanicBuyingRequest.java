package com.jsj.web.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanicBuyingRequest {

    private Integer userId;

    private Integer productId;
}
