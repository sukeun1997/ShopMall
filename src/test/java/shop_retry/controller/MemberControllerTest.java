package shop_retry.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import shop_retry.dto.MemberFormDto;
import shop_retry.entity.Member;
import shop_retry.repository.MemberRepository;
import shop_retry.service.MemberService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    public Member createMember(String email,String password) {
        MemberFormDto member = new MemberFormDto();
        member.setName("홍길동");
        member.setAddress("a");
        member.setEmail(email);
        member.setPassword(password);
        Member member1 = Member.createMember(member, passwordEncoder);
        return memberService.saveMember(member1);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void 로그인_성공_테스트() throws Exception {
        String email = "test@tset.com";
        String password = "1234";
        createMember(email,password);
        mockMvc.perform(formLogin().userParameter("email").loginProcessingUrl("/members/login").user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void 로그인_실패_테스트() throws Exception {
        String email = "test@tset.com";
        String password = "1234";
        createMember(email,password);
        mockMvc.perform(formLogin().userParameter("email").loginProcessingUrl("/members/login").user(email).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }
}