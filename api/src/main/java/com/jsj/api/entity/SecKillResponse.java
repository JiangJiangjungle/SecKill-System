package com.jsj.api.entity;

import com.jsj.api.BuyResultEnum;
import lombok.Data;

/**
 * @author jiangshenjie
 */
@Data
public class SecKillResponse {
    Long id;
    BuyInformation buyInformation;
    BuyResultEnum buyResultEnum;

    public SecKillResponse(Long id, BuyInformation buyInformation, BuyResultEnum buyResultEnum) {
        this.id = id;
        this.buyInformation = buyInformation;
        this.buyResultEnum = buyResultEnum;
    }
}
