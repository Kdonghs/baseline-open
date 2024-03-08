package com.baseline.domain;

import com.baseline.entity.MemberEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

@SuppressWarnings("deprecation")
public class Member {
    private Member() {}
    @Getter @Setter
    @ToString
    public static class SignUpReq extends OAuth {
        @ApiModelProperty("표시용 이름. `GIP) user.displayName`")
        private String displayName;

        @ApiModelProperty("이메일 주소. `GIP) user.email`")
        private String email;

        @ApiModelProperty("휴대폰 번호")
        private String mobile;

        @ApiModelProperty("프로필 사진 URL. `GIP) user.photoURL`")
        private String photoUrl;
    }

    @Getter @Setter
    public static class OAuthRes {
        private String accessToken;
        private String tokenType;
        private String refreshToken;
        private Integer expiresIn;
        private String scope;
        private String jti;

        public OAuthRes(OAuth2AccessToken token) {
            this.accessToken = token.getValue();
            this.tokenType = token.getTokenType();
            this.refreshToken = token.getRefreshToken().getValue();
            this.expiresIn = token.getExpiresIn();
            this.scope = String.join(" ", token.getScope());
            this.jti = token.getAdditionalInformation().containsKey("jti") ?
                    token.getAdditionalInformation().get("jti").toString() : "";
        }
    }

    @Getter
    @Setter
    public static class updateMemberReq{
        private MemberEntity memberEntity;
    }

    @Data
    public static class UpdateDetails {
        private String name;
        private String belongs;
        private String department;
        private String position;
        private String mobile;
        private String officeNumber;
    }

    @Data
    @Builder
    public static class getDetails{
        private String email;
        private String conferenceEmail;
        private String name;
        private String mobile;
        private String officeNumber;
        private String belongs;
        private String department;
        private String position;
    }
}
