package shop_retry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop_retry.entity.Item;

public interface ItemRepository extends JpaRepository<Item,Long>, ItemRepositoryCustom {
}
