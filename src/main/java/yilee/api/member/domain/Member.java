package yilee.api.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<MemberRole> memberRoleList = new ArrayList<>();

    private boolean isDisabled;

    public void updatePw(String password) {
        this.password = password;
    }

    public void updateRoles(List<MemberRole> memberRoleList) {
        this.memberRoleList = memberRoleList;
    }

    public void disabled(boolean disabled) {
        isDisabled = disabled;
    }
}
