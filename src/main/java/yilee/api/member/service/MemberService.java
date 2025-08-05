package yilee.api.member.service;

import yilee.api.member.domain.Member;
import yilee.api.member.domain.MemberRole;
import yilee.api.member.dto.MemberDto;

import java.util.List;
import java.util.stream.Collectors;

public interface MemberService {
    Long register(MemberDto dto);

    MemberDto find(Long id);

    MemberDto find(String email);

    void modify(MemberDto dto);

    void remove(Long id);

    default MemberDto entityToDto(Member e) {
        List<String> roles = e.getMemberRoleList()
                .stream()
                .map(Enum::name)
                .toList();

        return MemberDto.builder()
                .email(e.getEmail())
                .password(e.getPassword())
                .isDisabled(e.isDisabled())
                .memberRoleList(roles)
                .build();
    }

    default Member dtoToEntity(MemberDto dto) {
        List<MemberRole> roles = dto.getMemberRoleList()
                .stream()
                .map(MemberRole::valueOf)
                .toList();

        return Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .memberRoleList(roles)
                .isDisabled(dto.getIsDisabled())
                .build();
    }
}
