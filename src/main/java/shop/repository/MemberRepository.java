package shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.entitiy.Member;


public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByEmail(String email); // 회원 가입시 중복 회원 검사를 위한 메소드
}
