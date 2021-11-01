package shop_retry.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop_retry.entity.Member;
import shop_retry.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {



    List<Order> findByMemberEmailOrderByOrderDateDesc(String email, Pageable pageable);

    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    Long countOrder(@Param("email") String email);

    Member findByMemberEmail(String email);
}
