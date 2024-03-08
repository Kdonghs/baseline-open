package com.baseline.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter @Setter
public class OAuth {
    @ApiModelProperty(value = "클라이언트 ID", example = "cms")
    private String clientId;

    @ApiModelProperty("클라이언트 시크릿 키")
    private String clientSecret;

    @ApiModelProperty(value = "인증 방식", example = "password")
    private String grantType;

    @ApiModelProperty(value = "로그인ID. `GIP) user.uid`, 이메일 로그인은 비밀번호")
    @NotNull
    private String uid;

    @ApiModelProperty(value = "비밀번호. `GIP) user.getIdToken()` promise")
    @NotNull
    private String idToken;

    @ApiModelProperty(value = "OAuth Resource 제공자. `GIP) credential.providerId`", example = "google.com")
    private String provider;

    public Map<String, String> toMap() {
        return Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "grant_type", grantType,
                "username", uid,
                "password", idToken
        );
    }
}
