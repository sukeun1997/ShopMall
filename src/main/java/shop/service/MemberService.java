package shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.entitiy.Member;
import shop.repository.MemberRepository;

@Service // Bean 등록을 위해 (서비스 레이어)
@Transactional // 에러 발생시 변경된 데이터를 이전 상태로 롤백
@RequiredArgsConstructor // final 이나 @Nonnull 이 붙은 필드에 생성자를 생성
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalArgumentException("이미 가입된 회원입니다.");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String Email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(Email);

        if (member == null) {
            throw new UsernameNotFoundException(Email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build(); // UserDetail -> User 변환
    }
}
