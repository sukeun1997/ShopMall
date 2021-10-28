package shop_retry.dto;

import lombok.Data;
import org.modelmapper.ModelMapper;
import shop_retry.entity.ItemImg;

@Data
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String imgUrl;

    private String oriImgName;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);
    }
}
