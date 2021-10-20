package shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;
import shop.constant.ItemSellStatus;
import shop.entitiy.Item;
import shop.entitiy.QItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    void createItemTest() {
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상풍"+i);
            item.setPrice(1000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item saveItem = itemRepository.save(item);
            System.out.println(saveItem.toString());
        }
    }

    void createItemTest2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상풍"+i);
            item.setPrice(1000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상풍"+i);
            item.setPrice(1000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    void findByItemNameTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        List<Item> itemList1 = itemRepository.findByPriceLessThan(10005);
        itemList.stream().forEach(System.out::println);
        itemList1.stream().forEach(System.out::println);

    }

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("Queryds1 조회 테스트1")
    void queryDSL1Test() {
        this.createItemTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QItem qItem = QItem.item; // item 테이블 매칭
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch(); // 쿼리문 실행

        itemList.stream().forEach(System.out::println);

    }

    @Test
    @DisplayName("상품 Queryds1 조회 테스트2")
    void queryDslTest2() {

        this.createItemTest2();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;

        String itemDetail = "테스트 상품 상세 설명";
        int price = 1003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPageResult = itemRepository.findAll(booleanBuilder,pageable);

        System.out.println("total elements : " + itemPageResult.getTotalElements());

        List<Item> resultItemList = itemPageResult.getContent();

        resultItemList.stream().forEach(System.out::println);

    }
}