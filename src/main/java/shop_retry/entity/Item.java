package shop_retry.entity;

import lombok.Data;
import shop_retry.constant.ItemSellStatus;
import shop_retry.dto.ItemFormDto;

import javax.persistence.*;

@Entity
@Data
@Table(name = "item")

public class Item extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;       //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name = "price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.setItemNm(itemFormDto.getItemNm());
        this.setItemDetail(itemFormDto.getItemDetail());
        this.setPrice(itemFormDto.getPrice());
        this.setItemSellStatus(itemFormDto.getItemSellStatus());
        this.setStockNumber(itemFormDto.getStockNumber());
    }

    public void removeStock(int count) {
        int restStock = this.stockNumber - count;

        if (restStock < 0) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고 수량 " + this.stockNumber);
        }
        this.stockNumber = restStock;
    }
}
