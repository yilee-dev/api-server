package yilee.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class MemberContext implements UserDetails {

    private List<GrantedAuthority> authorities;

    private MemberDto memberDto;

    public Map<String, Object> getClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", memberDto.getEmail());
        claims.put("roles", memberDto.getMemberRoleList());
        claims.put("isDisabled", memberDto.getIsDisabled());
        return claims;
    }

    @Override
    public String getPassword() {
        return memberDto.getPassword();
    }

    @Override
    public String getUsername() {
        return memberDto.getEmail();
    }
}
