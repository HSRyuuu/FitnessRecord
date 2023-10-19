package com.example.fitnessrecord.domain.social.kakao.model;

import com.example.fitnessrecord.domain.user.persist.User;
import com.example.fitnessrecord.domain.user.type.UserType;
import com.example.fitnessrecord.global.util.PasswordUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.Data;

@Data
public class KakaoProfile {
    private Integer id;
    private LocalDateTime connectedAt;
    private String email;
    private String nickname;

    public KakaoProfile(String jsonResponseBody){
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonResponseBody);

        this.id = element.getAsJsonObject().get("id").getAsInt();

        String connected_at = element.getAsJsonObject().get("connected_at").getAsString();
        connected_at = connected_at.substring(0, connected_at.length() - 1);
        LocalDateTime connectDateTime = LocalDateTime.parse(connected_at, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.connectedAt = connectDateTime;

        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
        this.nickname = properties.getAsJsonObject().get("nickname").getAsString();

        JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
        this.email = kakaoAccount.getAsJsonObject().get("email").getAsString();
    }

    public static User toEntity(KakaoProfile kakaoProfile){
        String password = UUID.randomUUID().toString().substring(0, 6); //임의의 비밀번호
        String encPassword = PasswordUtils.encPassword(password);

        return User.builder()
            .email(kakaoProfile.getEmail())
            .password(encPassword)
            .nickname(kakaoProfile.getNickname())
            .emailAuthYn(true)
            .emailAuthDateTime(LocalDateTime.now())
            .userType(UserType.KAKAO)
            .createdAt(LocalDateTime.now())
            .lastModifiedAt(LocalDateTime.now())
            .build();
    }

}
