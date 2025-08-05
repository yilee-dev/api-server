package yilee.api.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yilee.api.member.domain.Member;
import yilee.api.member.dto.MemberContext;
import yilee.api.member.dto.MemberDto;
import yilee.api.member.repository.MemberRepository;
import yilee.api.member.service.MemberService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email Not found"));

        MemberDto memberDto = memberService.entityToDto(member);
        List<GrantedAuthority> authorities = memberDto.getMemberRoleList()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        return MemberContext.builder()
                .authorities(authorities)
                .memberDto(memberDto)
                .build();
    }
}
