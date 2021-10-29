package shop_retry.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop_retry.dto.ItemSearchDto;
import shop_retry.dto.MainItemDto;
import shop_retry.entity.Item;
import shop_retry.service.ItemService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String  main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = itemService.getMainPage(itemSearchDto, pageable);

        // 조회한 상품 데이터 및 페이징 정보를 뷰에 전달
        model.addAttribute("items", items);

        // 검색정보 페이지에 전달
        model.addAttribute("itemSearchDto", itemSearchDto);

        // 상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수
        model.addAttribute("maxPage", 5);
        return "main";
    }
}
