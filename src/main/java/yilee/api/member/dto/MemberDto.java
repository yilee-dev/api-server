package yilee.api.member.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yilee.api.member.domain.MemberRole;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class MemberDto {
    private Long id;

    private String email;

    private String password;

    private List<String> memberRoleList;

    private Boolean isDisabled;
}
