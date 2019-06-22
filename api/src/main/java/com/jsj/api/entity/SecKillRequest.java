package com.jsj.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jiangshenjie
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecKillRequest implements Serializable {
    Long id;
    BuyInformation buyInformation;
}
