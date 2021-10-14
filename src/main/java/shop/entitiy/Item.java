package shop.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.constant.ItemSellStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;            // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemName;    // 상품명

    @Column(name = "price", nullable = false)
    private int price;          // 가격

    @Column(nullable = false)
    private int stockNumber;    // 재고수량

    @Lob // 255개 이상의 문자를 저장하고 싶을때 지정
    @Column(nullable = false)
    private String itemDetail;  // 상품 설명

    @Enumerated(EnumType.ORDINAL)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private LocalDateTime regTime; //등록시간

    private LocalDateTime updateTime; // 수정 시간


}
