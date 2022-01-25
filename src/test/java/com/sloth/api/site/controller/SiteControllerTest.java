package com.sloth.api.site.controller;

import com.sloth.test.base.BaseApiController;
import com.sloth.creator.SiteCreator;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(controllers = SiteController.class)
public class SiteControllerTest extends BaseApiController {

    @MockBean
    private SiteRepository siteRepository;

    @Test
    @DisplayName("모든 사이트 조회 API 테스트")
    public void getAllSiteTest() throws Exception {

        // given
        List<Site> sites = new ArrayList<>();
        sites.add(SiteCreator.create(1L, "인프런"));
        sites.add(SiteCreator.create(2L, "패스트캠퍼스"));
        sites.add(SiteCreator.create(3L, "유튜브"));
        BDDMockito.when(siteRepository.findAll()).thenReturn(sites);

        // when
        ResultActions result = mockMvc.perform(get("/api/site/list")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].siteId").value(equalTo(sites.get(0).getSiteId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].siteName").value(equalTo(sites.get(0).getSiteName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].siteId").value(equalTo(sites.get(1).getSiteId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].siteName").value(equalTo(sites.get(1).getSiteName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].siteId").value(equalTo(sites.get(2).getSiteId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].siteName").value(equalTo(sites.get(2).getSiteName())))
                ;
    }

    @Test
    @DisplayName("사이트 조회 캐시 삭제 API 테스트")
    public void clearSiteCacheTest() throws Exception {

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/site/cache")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(equalTo(HttpStatus.OK.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(equalTo("site list cache clear success")))
        ;

    }

}