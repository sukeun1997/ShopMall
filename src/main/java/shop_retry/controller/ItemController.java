package shop_retry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import shop_retry.dto.ItemFormDto;
import shop_retry.dto.ItemSearchDto;
import shop_retry.entity.Item;
import shop_retry.repository.ItemRepository;
import shop_retry.service.ItemService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping(value = "/admin/item/new")
    public String getItem(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String successItem(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> multipartFiles) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (multipartFiles.get(0).isEmpty() || multipartFiles.size() == 0) {
            model.addAttribute("errorMessage", "대표 사진 등록은 필수  입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, multipartFiles);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping({"/admin/items", "/admin/items/{page}"})
    public String getItems(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminPage(itemSearchDto, pageable);

        // 조회한 상품 데이터 및 페이징 정보를 뷰에 전달
        model.addAttribute("items", items);

        // 검색정보 페이지에 전달
        model.addAttribute("itemSearchDto", itemSearchDto);

        // 상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    // 관리자 페이지 상품 정보 수정 들어가기
    @GetMapping("/admin/item/{itemId}")
    public String getItemDetailAdmin(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemFormDto item = itemService.itemDtl(itemId);
            model.addAttribute("itemFormDto", item);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @GetMapping("/item/{itemId}")
    public String getItemDetailUser(@PathVariable("itemId") Long itemId, Model model) {


            ItemFormDto item = itemService.itemDtl(itemId);
            model.addAttribute("item", item);

        return "item/itemDtl";
    }

    @PostMapping("/admin/item/{itemid}")
    public String updateItem(
                             @Valid ItemFormDto itemFormDto,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }
}
