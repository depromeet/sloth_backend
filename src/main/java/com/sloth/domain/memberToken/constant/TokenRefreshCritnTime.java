package com.sloth.domain.memberToken.constant;

import lombok.Getter;

/**
 * 리프레시 토큰 만료 시간 늘려주는 기준 enum
 */
@Getter
public enum TokenRefreshCritnTime {

    HOURS_72(72);

    TokenRefreshCritnTime(int refreshCritnTime) {
        this.refreshCritnTime = refreshCritnTime;
    }

    private int refreshCritnTime;

}
