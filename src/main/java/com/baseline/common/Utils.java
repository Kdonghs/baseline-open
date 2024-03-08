package com.baseline.common;

import com.baseline.common.errorcode.CommonErrorCode;
import com.baseline.common.exception.UnauthorizedException;
import com.baseline.auth.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@SuppressWarnings("deprecation")
@Slf4j
public class Utils {
    private Utils() {}

    public static AuthUser currentUser() {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) ((OAuth2AuthenticationDetails)oAuth2Authentication.getDetails()).getDecodedDetails();
        AuthUser user = new AuthUser(map.get("id") != null ? (int) map.get("id") : 0);

        if (user.getId() == 0) {
            throw new UnauthorizedException(CommonErrorCode.UNAUTHORIZED);
        }

        return user;
    }

    public static LocalDateTime convertStringToLocalDateTime(String src) {
        if (ObjectUtils.isEmpty(src)) return null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(src, formatter);
        } catch(Exception e) {
            log.error("Failed to convert string to datetime.", e);
            throw e;
        }
    }

}
