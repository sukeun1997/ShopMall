package shop_retry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop_retry.dto.ItemFormDto;
import shop_retry.dto.ItemSearchDto;
import shop_retry.entity.Item;
import shop_retry.entity.ItemImg;
import shop_retry.repository.ItemRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> multipartFiles) throws IOException {

        // 아이템 저장
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        // 이미지 저장
        for (int i = 0; i < multipartFiles.size() ; i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0) {
                itemImg.setRepimgYn("Y");
            } else {
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, multipartFiles.get(i));
        }
        return item.getId();

    }

    //TODO
    @Transactional(readOnly = true)
    public Page<Item> getAdminPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }
}
