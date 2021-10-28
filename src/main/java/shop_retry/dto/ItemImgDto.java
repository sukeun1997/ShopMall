package shop_retry.dto;

import org.modelmapper.ModelMapper;
import shop_retry.entity.ItemImg;

public class ItemImgDto {

    private Long id;

    private String imgName;

    private String imgUrl;

    private String oriImgName;

    private String repImgYn;

    private ModelMapper modelMapper = new ModelMapper();

    public ItemImg of() {
        return modelMapper.map(this, ItemImg.class);
    }
}
