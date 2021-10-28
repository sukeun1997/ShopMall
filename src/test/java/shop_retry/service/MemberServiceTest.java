package shop_retry.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import shop_retry.dto.MemberFormDto;
import shop_retry.entity.Member;
import shop_retry.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EntityManager entityManager;

    public Member CreateMember() {
        MemberFormDto member = new MemberFormDto();
        member.setEmail("test@test.com");
        member.setPassword("1234");
        member.setAddress("a");
        member.setName("홍길동");
        return Member.createMember(member,passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void createUser() {
        Member member = CreateMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getEmail(),savedMember.getEmail());
        assertEquals(member.getName(),savedMember.getName());
        assertEquals(member.getPassword(),savedMember.getPassword());

    }

    @Test
    @DisplayName("auditing 테스트")
    @WithMockUser(username = "길동", roles = "user")
    void auditorTest() {
        Member member = new Member();
        memberRepository.save(member);
        entityManager.flush();
        entityManager.clear();

        Member member1 = memberRepository.findById(member.getId()).orElseThrow(EntityNotFoundException::new);

        System.out.println(member1.getRegTime());
        System.out.println(member1.getUpdateTime());
        System.out.println(member1.getCreatedBy());
        System.out.println(member1.getModifiedBy());
    }
}