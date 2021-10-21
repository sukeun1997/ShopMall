package shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.entitiy.ItemImg;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemid);
}
