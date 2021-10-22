package shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.DTO.ItemSearchDto;
import shop.DTO.MainItemDto;
import shop.entitiy.Item;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
