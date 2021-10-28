package shop_retry.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop_retry.constant.Role;
import shop_retry.dto.MemberFormDto;

import javax.persistence.*;

@Entity
@Data
@Table(name = "member")
@ToString
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;


    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setAddress(memberFormDto.getAddress());
        member.setEmail(memberFormDto.getEmail());
        member.setName(memberFormDto.getName());
        member.setRole(Role.ADMIN);
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        return member;
    }
}
