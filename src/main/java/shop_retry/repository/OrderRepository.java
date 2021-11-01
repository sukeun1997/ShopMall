package shop_retry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop_retry.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
