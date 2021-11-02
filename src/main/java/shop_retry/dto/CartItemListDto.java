package shop_retry.dto;

import lombok.Data;
import shop_retry.entity.CartItem;

@Data
public class CartItemListDto {

    private Long cartItemId;
    private String itemNm;
    private String imgUrl;
    private int price;
    private int count;


    public static CartItemListDto createCartItemListDto(CartItem cartItem, String imgUrl) {
        CartItemListDto cartItemListDto = new CartItemListDto();
        cartItemListDto.setCartItemId(cartItem.getId());
        cartItemListDto.setItemNm(cartItem.getItem().getItemNm());
        cartItemListDto.setCount(cartItem.getCount());
        cartItemListDto.setImgUrl(imgUrl);
        cartItemListDto.setPrice(cartItem.getItem().getPrice());

        return cartItemListDto;

    }
}
