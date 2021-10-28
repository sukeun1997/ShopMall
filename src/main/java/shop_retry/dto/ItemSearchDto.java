package shop_retry.dto;

import lombok.Data;
import shop_retry.constant.ItemSellStatus;

@Data
public class ItemSearchDto {
    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private String searchBy;
    private String searchQuery = "";
}
