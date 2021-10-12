package com.sloth.api.oauth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController { // access token 받기 위한 임시 컨트롤러

    @RequestMapping("/my-login")
    public String loginPage(Model model) {

        // 구글 로그인 URL 생성
        String googleUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
                + "scope=https://www.googleapis.com/auth/userinfo.email+"+"https://www.googleapis.com/auth/userinfo.profile"
                + "&response_type=code"
                + "&state=security_token%3D138r5719ru3e1%26url%3Dhttps://oauth2.example.com/token"
                + "&client_id=" + "151935241850-4jk4v4dj9bihjm5fpj3dva28oke08l6n.apps.googleusercontent.com"
                + "&redirect_uri=" + "http://localhost:8080/login/oauth2/code/google"
                + "&access_type=offline";

        model.addAttribute("googleUrl", googleUrl);

        return googleUrl;

    }

    @RequestMapping("/login/oauth2/code/google")
    public void googleLogin(@RequestParam("code") String authorize_code, HttpSession session, Model model) throws Exception {

        // 코드 확인
        System.out.println("autorize_code : " + authorize_code);

        // Access Token 발급
        JsonNode jsonToken = getAccessToken(authorize_code);
        String accessToken = jsonToken.get("access_token").toString();
        String refreshToken = "";
        if(jsonToken.has("refresh_token")) {
            refreshToken = jsonToken.get("refresh_token").toString();
        }
        String expiresTime = jsonToken.get("expires_in").toString();
        System.out.println("Access Token : " + accessToken);
        System.out.println("Refresh Token : " + refreshToken);
        System.out.println("Expires Time : " + expiresTime);
    }

    public static JsonNode getAccessToken(String authorize_code) {

        final String RequestUrl = "https://www.googleapis.com/oauth2/v4/token";

        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", "151935241850-4jk4v4dj9bihjm5fpj3dva28oke08l6n.apps.googleusercontent.com"));
        postParams.add(new BasicNameValuePair("client_secret", "aroKVgxziypO3MlN7kxvUWLJ"));
        postParams.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/login/oauth2/code/google")); // 리다이렉트 URI
        postParams.add(new BasicNameValuePair("code", authorize_code)); // 로그인 과정중 얻은 code 값

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(RequestUrl);
        JsonNode returnNode = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));
            final HttpResponse response = client.execute(post);

            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnNode;

    }

    public JsonNode getGoogleUserInfo(String access_Token) { // GoogleFeignClient 로 개선 완료
        final String reqUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet httpGet = new HttpGet(reqUrl);

        JsonNode returnNode = null;

        httpGet.addHeader("Authorization", "Bearer" + access_Token);

        try {
            final HttpResponse response = client.execute(httpGet);
            final int responseCode = response.getStatusLine().getStatusCode();

            log.info("this is response = " + response);

            ObjectMapper objectMapper = new ObjectMapper();
            returnNode = objectMapper.readTree(response.getEntity().getContent());

            log.info("this is returnNode = " + returnNode);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnNode;
    }
}
