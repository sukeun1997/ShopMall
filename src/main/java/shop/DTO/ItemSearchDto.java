package shop.DTO;

import lombok.Getter;
import lombok.Setter;
import shop.constant.ItemSellStatus;

@Getter
@Setter
public class ItemSearchDto {

        // all : 상품등록일 전체
        // 1d : 최근 하루
        // 1w : 최근 일주일
        // 1m : 최근 한달
        // 6m : 최근 6개월
        private String searchDateType;

        // 상품 판매상태 기준 조회
        private ItemSellStatus searchSellStatus;

        // 상품 조회 유형 (itemNm : 상품명 , createdBy : 작성자 )
        private String searchBy;

        // 조회할 검색어 저장할 변수수
       private String searchQuery = "";

}