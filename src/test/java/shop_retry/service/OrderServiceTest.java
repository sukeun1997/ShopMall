package shop_retry.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import shop_retry.constant.ItemSellStatus;
import shop_retry.constant.OrderStatus;
import shop_retry.dto.OrderDto;
import shop_retry.entity.Item;
import shop_retry.entity.Member;
import shop_retry.entity.Order;
import shop_retry.repository.ItemRepository;
import shop_retry.repository.MemberRepository;
import shop_retry.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class OrderServiceTest {

    Member member;
    Item item;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private static MemberService memberService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        member = new Member();
        member.setEmail("test@test.com");
        memberRepository.save(member);

        item = saveItem();

    }

    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    @Test
    void cancelOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.createOrder(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        orderService.cancelOrder(orderId);

        assertEquals(order.getOrderStatus(), OrderStatus.CANCEL);
        assertEquals(item.getStockNumber(), 100);

    }
}