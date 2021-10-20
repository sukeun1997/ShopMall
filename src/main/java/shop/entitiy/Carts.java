package shop.entitiy;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Data
@ToString
public class Carts extends BaseEntity{

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
