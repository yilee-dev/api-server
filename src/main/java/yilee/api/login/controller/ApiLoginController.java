package yilee.api.login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yilee.api.login.dto.LoginDto;
import yilee.api.member.dto.MemberContext;
import yilee.api.member.dto.MemberDto;
import yilee.api.member.service.MemberService;
import yilee.api.utils.JwtUtils;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiLoginController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginDto loginDto
            ) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        MemberContext mx = (MemberContext) authenticate.getPrincipal();

        Map<String, Object> claims = mx.getClaims();

        String accessToken = JwtUtils.generateToken(claims, 10);
        String refreshToken = JwtUtils.generateToken(claims, 24 * 60);

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        return ResponseEntity.ok(claims);
    }


    @PostMapping("/api/sign-up")
    public ResponseEntity<Map<String, Long>> signUp(@RequestBody MemberDto memberDto) {
        Long registeredId = memberService.register(memberDto);
        return ResponseEntity.ok(Map.of("id", registeredId));
    }

}
