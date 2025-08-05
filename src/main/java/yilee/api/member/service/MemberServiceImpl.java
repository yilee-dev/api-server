package yilee.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yilee.api.member.domain.Member;
import yilee.api.member.domain.MemberRole;
import yilee.api.member.dto.MemberDto;
import yilee.api.member.repository.MemberRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long register(MemberDto dto) {
        boolean empty = memberRepository.findByEmail(dto.getEmail())
                .isEmpty();

        if (!empty) {
            throw new IllegalArgumentException("Already Exists Member");
        }

        Member member = dtoToEntity(dto);
        member.updatePw(passwordEncoder.encode(dto.getPassword()));

        Member saved = memberRepository.save(member);
        return saved.getId();
    }

    @Override
    public MemberDto find(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No Such member"));

        return entityToDto(member);
    }

    @Override
    public MemberDto find(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No Such member"));

        return entityToDto(member);
    }

    @Override
    @Transactional
    public void modify(MemberDto dto) {
        Member member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("No Such member"));

        member.updatePw(passwordEncoder.encode(dto.getPassword()));
        member.updateRoles(dtoToEntity(dto).getMemberRoleList());
        member.disabled(dto.getIsDisabled());
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No Such member"));

        member.disabled(true);
    }
}
