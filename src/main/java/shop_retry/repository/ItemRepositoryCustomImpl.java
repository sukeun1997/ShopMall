package shop_retry.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;
import shop_retry.constant.ItemSellStatus;
import shop_retry.dto.ItemSearchDto;
import shop_retry.entity.Item;
import shop_retry.entity.QItem;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        QueryResults<Item> results = queryFactory.selectFrom(QItem.item)
                .where(RegCheck(itemSearchDto.getSearchDateType())
                        , StatusCheck(itemSearchDto.getSearchSellStatus())
                        , ItemNmCheck(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        Long totalCount = results.getTotal();
        List<Item> itemList = results.getResults();
        return new PageImpl<>(itemList, pageable, totalCount);
    }

    private BooleanExpression ItemNmCheck(String searchBy, String searchQuery) {
        if (StringUtils.equals("createdBy",searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("itemNm",searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        }
        return null;
    }

    private BooleanExpression StatusCheck(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression RegCheck(String searchDateType) {


        LocalDateTime dateTime = LocalDateTime.now();
        if(StringUtils.equals(searchDateType,"1d")) {
            dateTime.minusDays(1);
        } else if (StringUtils.equals(searchDateType,"1w")) {
            dateTime.minusWeeks(1);
        } else if (StringUtils.equals(searchDateType,"1m")) {
            dateTime.minusMonths(1);
        } else if (StringUtils.equals(searchDateType,"6m")) {
            dateTime.minusMonths(6);
        } else {
            return null;
        }
        return QItem.item.regTime.after(dateTime);
    }

}
