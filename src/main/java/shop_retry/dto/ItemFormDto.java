package shop_retry.dto;

import lombok.Data;
import org.modelmapper.ModelMapper;

import shop_retry.constant.ItemSellStatus;
import shop_retry.entity.Item;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "아이템 이름은 필수 입력 값 입니다")
    private String itemNm;

    @NotBlank(message = "아이템 상세 설명은 필수 입력 값 입니다.")
    private String itemDetail;

    @Min(value = 1, message = "1이상 입력해 주세요")
    private int stockNumber;

    @Min(value = 1, message = "1이상 입력해 주세요")
    private int price;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
      return modelMapper.map(item,ItemFormDto.class);
    }
}
