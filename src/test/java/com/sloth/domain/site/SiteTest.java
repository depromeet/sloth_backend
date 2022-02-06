package com.sloth.domain.site;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SiteTest {

    @Test
    @DisplayName("사이트 생성자 테스트")
    public void Site() {

        // given
        Site site = new Site("인프런");

        // then
        Assertions.assertThat(site.getSiteName()).isEqualTo("인프런");

    }

}