package shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.entitiy.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
}
