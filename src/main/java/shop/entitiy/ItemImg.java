package shop.entitiy;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import shop.repository.ItemImgRepository;
import shop.service.FileService;

import javax.persistence.*;

@Entity
@Data
@Table(name = "item_img")
public class ItemImg extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "item_img_id")
    private Long id;

    private String imgName; // 이미지 파일명

    private String oriImgName;  // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로

    private String repimgYn; // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
