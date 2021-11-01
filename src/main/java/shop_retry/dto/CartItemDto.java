package shop_retry.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartItemDto {

    @NotNull(message = "상품 아이디 필수 입력 값 입니다.")
    private Long itemId;

    @Min(value = 1, message = "상품 수량은 최소 1개 이상 이여야 합니다.")
    private int count;


}
