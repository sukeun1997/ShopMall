package shop_retry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop_retry.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
