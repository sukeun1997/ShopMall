package shop_retry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import shop_retry.constant.OrderStatus;
import shop_retry.dto.OrderDto;
import shop_retry.dto.OrderHistDto;
import shop_retry.dto.OrderItemDto;
import shop_retry.entity.*;
import shop_retry.repository.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
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

    @Transactional(readOnly = true)
    public Page<OrderHistDto> orderHist(Pageable pageable, Principal principal) {

        List<Order> orderList = orderRepository.findByMemberEmailOrderByOrderDateDesc(principal.getName(), pageable);
        Long totalCount = orderRepository.countOrder(principal.getName());

        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        for (Order order : orderList) {
            OrderHistDto orderHistDto = OrderHistDto.CreateOrderHistDto(order);
            for (OrderItem orderItem : order.getOrderItemList()) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = OrderItemDto.createOrderItemDto(orderItem
                        ,itemImg.getImgUrl());
                orderHistDto.addOrderItemDtoLis(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<>(orderHistDtos,pageable,totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateCancelPerform(Long orderId, Principal principal) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(principal.getName());
        if(!StringUtils.equals(member.getEmail(), order.getMember().getEmail())) {
            return false;
        }
        return true;
    }

    public Long cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        for (OrderItem orderItem : order.getOrderItemList()) {
            Item item = itemRepository.findById(orderItem.getItem().getId()).orElseThrow(EntityNotFoundException::new);
            item.addStock(orderItem.getCount());
        }
        order.setOrderStatus(OrderStatus.CANCEL);
        return orderId;
    }
}
