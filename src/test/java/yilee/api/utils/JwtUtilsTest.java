package yilee.api.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {
    @Autowired
    JwtUtils jwtUtils;

    @Test
    void test() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "youngin.lee@donghee.co.kr");
        claims.put("password", "DHD@ngh222");
        claims.put("roles", List.of("USER", "MANAGER"));
        claims.put("isDisabled", false);

        String accessToken = jwtUtils.generateToken(claims, 15);

        Map<String, Object> validateClaims = jwtUtils.validateToken(accessToken);

        Assertions.assertThat(validateClaims.get("email")).isEqualTo(claims.get("email"));
        Assertions.assertThat(validateClaims.get("password")).isEqualTo(claims.get("password"));
        Assertions.assertThat(validateClaims.get("roles")).isEqualTo(claims.get("roles"));
        Assertions.assertThat(validateClaims.get("isDisabled")).isEqualTo(claims.get("isDisabled"));
    }

}