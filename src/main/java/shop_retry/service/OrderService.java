package shop_retry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop_retry.dto.OrderDto;
import shop_retry.entity.Item;
import shop_retry.entity.Member;
import shop_retry.entity.Order;
import shop_retry.entity.OrderItem;
import shop_retry.repository.ItemRepository;
import shop_retry.repository.MemberRepository;
import shop_retry.repository.OrderItemRepository;
import shop_retry.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    //todo
    public Long createOrder(OrderDto orderDto, String email) {

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
