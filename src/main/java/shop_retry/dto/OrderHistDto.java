package shop_retry.dto;


import lombok.Data;

import shop_retry.constant.OrderStatus;
import shop_retry.entity.Order;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderHistDto {

    private Long orderId;
    private String  orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public static OrderHistDto CreateOrderHistDto(Order order) {
        OrderHistDto orderHistDto = new OrderHistDto();
        orderHistDto.setOrderId(order.getId());
        orderHistDto.setOrderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        orderHistDto.setOrderStatus(order.getOrderStatus());
        return orderHistDto;
    }

    public void addOrderItemDtoLis(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}
