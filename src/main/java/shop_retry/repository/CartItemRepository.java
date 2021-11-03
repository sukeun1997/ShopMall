package shop_retry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop_retry.entity.Cart;
import shop_retry.entity.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {


    List<CartItem> findByCartIdOrderByRegTimeDesc(Long cartId);

    Cart findByCartId(Long cartId);
}
