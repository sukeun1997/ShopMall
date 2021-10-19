package shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.entitiy.Carts;
public interface CartRepository extends JpaRepository<Carts,Long> {

}
