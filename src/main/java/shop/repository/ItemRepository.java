package shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import shop.entitiy.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,QuerydslPredicateExecutor<Item>,ItemRepositoryCustom {

    List<Item> findByItemNm(String itemName);

    List<Item> findByPriceLessThan(int price);
}
