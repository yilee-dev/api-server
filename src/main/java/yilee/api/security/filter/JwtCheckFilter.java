package yilee.api.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import yilee.api.member.dto.MemberDto;
import yilee.api.utils.JwtUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtCheckFilter extends OncePerRequestFilter {
    private static String[] whiteList = {
            "/",
            "/api/sign-up",
            "/api/login",
            "/css/*", "/js/*", "/*.ico"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String requestURI = request.getRequestURI();
        if (PatternMatchUtils.simpleMatch(whiteList, requestURI)) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        try {
            String[] split = authorization.split(" ");

            if (split[0].equals("Bearer")) {
                Map<String, Object> claims = JwtUtils.validateToken(split[1]);

                log.info("JWT Claims: {}", claims);

                String email = (String) claims.get("email");
                String pw = (String) claims.get("password");
                List<String> roles = (List<String>) claims.get("roles");
                Boolean isDisabled = (Boolean) claims.get("isDisabled");

                MemberDto dto = MemberDto.builder()
                        .email(email)
                        .password(pw)
                        .memberRoleList(roles)
                        .isDisabled(isDisabled)
                        .build();

                List<GrantedAuthority> authorities = dto.getMemberRoleList()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(dto, pw, authorities);
                SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(usernamePasswordAuthenticationToken);

                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            log.error("Jwt Check Error...");
            log.error(e.getMessage());

            GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
            Gson gson = gsonHttpMessageConverter.getGson();

            String jsonStr = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter writer = response.getWriter();
            writer.println(jsonStr);
            writer.close();
        }
    }
}
