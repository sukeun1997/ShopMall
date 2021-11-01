package shop_retry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop_retry.entity.ItemImg;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    ItemImg findByItemIdAndRepimgYn(Long item_id, String repimgYn);
}
