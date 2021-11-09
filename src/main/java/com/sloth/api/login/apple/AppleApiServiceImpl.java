package com.sloth.api.login.apple;

import com.google.gson.*;
import com.sloth.api.login.service.SocialApiSerivce;
import com.sloth.config.auth.dto.OAuthAttributes;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AppleApiServiceImpl implements SocialApiSerivce {

    private final PasswordEncoder passwordEncoder;

    @Value("${my-pass}")
    private String pass;

    @Override
    public OAuthAttributes getUserInfo(String idToken) {
        //public String userIdFromApple(String idToken) {
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            throw new BusinessException("FAILED_TO_VALIDATE_APPLE_LOGIN");
        }

        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray keyArray = (JsonArray) keys.get("keys");


        //클라이언트로부터 가져온 identity token String decode
        String[] decodeArray = idToken.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        //apple에서 제공해주는 kid값과 일치하는지 알기 위해
        JsonElement kid = ((JsonObject) parser.parse(header)).get("kid");
        JsonElement alg = ((JsonObject) parser.parse(header)).get("alg");

        //써야하는 Element (kid, alg 일치하는 element)
        JsonObject avaliableObject = null;
        for (int i = 0; i < keyArray.size(); i++) {
            JsonObject appleObject = (JsonObject) keyArray.get(i);
            JsonElement appleKid = appleObject.get("kid");
            JsonElement appleAlg = appleObject.get("alg");

            if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
                avaliableObject = appleObject;
                break;
            }
        }

        //일치하는 공개키 없음
        if (ObjectUtils.isEmpty(avaliableObject))
            throw new BusinessException("FAILED_TO_VALIDATE_APPLE_LOGIN");

        PublicKey publicKey = this.getPublicKey(avaliableObject);

        //--> 여기까지 검증

        Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(idToken).getBody();
        JsonObject userInfoObject = (JsonObject) parser.parse(new Gson().toJson(userInfo));
        JsonElement appleAlg = userInfoObject.get("sub");
        JsonElement appleEmail = userInfoObject.get("email");
        String email = appleEmail.getAsString();
        String userId = appleAlg.getAsString();

        //return userId;

        return OAuthAttributes.builder()
                .email(email)
                .name(userId)
                .socialType(SocialType.APPLE)
                .password(passwordEncoder.encode(pass))
                .build();
    }

    public PublicKey getPublicKey(JsonObject object) {
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new BusinessException("FAILED_TO_VALIDATE_APPLE_LOGIN");
        }
    }
}