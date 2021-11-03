package com.sloth.api.login.service;

import com.sloth.domain.member.constant.SocialType;
import com.sloth.exception.InvalidParameterException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SocialApiServiceFactory {

    private static Map<String, SocialApiSerivce> socialApiServices;

    public SocialApiServiceFactory(Map<String, SocialApiSerivce> socialApiSerivces) {
        this.socialApiServices = socialApiSerivces;
    }

    public static SocialApiSerivce getSocialApiService(SocialType socialType) {


        String socialApiServiceBeanName = "";

        if(SocialType.GOOGLE.equals(socialType)) {
            socialApiServiceBeanName = "googleApiServiceImpl";
        } else if(SocialType.KAKAO.equals(socialType)) {
            socialApiServiceBeanName = "kakaoApiServiceImpl";
        } else if(SocialType.APPLE.equals(socialType)) {
            socialApiServiceBeanName = "appleApiServiceImpl";
        }

        SocialApiSerivce socialApiSerivce = socialApiServices.get(socialApiServiceBeanName);

        if(socialApiSerivce == null){
            throw new InvalidParameterException("잘못된 소셜 로그인 타입입니다.");
        }

        return socialApiSerivce;
    }

}
