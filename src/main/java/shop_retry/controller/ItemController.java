package shop_retry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import shop_retry.dto.ItemFormDto;
import shop_retry.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String getItem(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String successItem(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,Model model, @RequestParam("itemImgFile") List<MultipartFile> multipartFiles) {

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
}
