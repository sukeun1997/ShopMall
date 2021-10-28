package shop_retry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop_retry.entity.ItemImg;
import shop_retry.repository.ItemImgRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {


    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    public Long saveItemImg(ItemImg itemImg, MultipartFile multipartFile) throws IOException {
        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!oriImgName.isEmpty()) {
            imgName = uploadFile(itemImgLocation, oriImgName, multipartFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        // 상품 이미지 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
        return itemImg.getId();
    }

    private String uploadFile(String itemImgLocation, String oriImgName, byte[] fileBytes) throws IOException {

        UUID uuid = UUID.randomUUID();
        String imgName = uuid + oriImgName.substring(oriImgName.lastIndexOf("."));
        String fileUploadUrl = itemImgLocation + "/" + imgName;
        FileOutputStream fileOutputStream = new FileOutputStream(fileUploadUrl);
        fileOutputStream.write(fileBytes);
        fileOutputStream.close();
        return imgName;
    }

}
