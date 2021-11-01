package shop_retry.dto;

import lombok.Data;
import shop_retry.entity.OrderItem;

@Data
public class OrderItemDto {

    private String itemNm;
    private String imgUrl;
    private int orderPrice;
    private int count;

    public static OrderItemDto createOrderItemDto(OrderItem orderItem, String imgUrl) {
        OrderItemDto orderItemDto = new OrderItemDto();

        orderItemDto.itemNm = orderItem.getItem().getItemNm();
        orderItemDto.count = orderItem.getCount();
        orderItemDto.orderPrice = orderItem.getOrderPrice();
        orderItemDto.imgUrl = imgUrl;
        return orderItemDto;
    }

}
