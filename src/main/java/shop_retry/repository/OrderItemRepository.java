package shop_retry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop_retry.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
