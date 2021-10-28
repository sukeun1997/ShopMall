package shop_retry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop_retry.dto.ItemSearchDto;
import shop_retry.entity.Item;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
