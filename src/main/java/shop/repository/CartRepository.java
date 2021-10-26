package shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.entitiy.Cart;
public interface CartRepository extends JpaRepository<Cart,Long> {

    // memberId 에 해당하는 장바구니 검색
    Cart findByMemberId(Long memberId);
}
