package shop_retry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import shop_retry.dto.CartItemDto;
import shop_retry.dto.CartItemListDto;
import shop_retry.service.CartService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@Transactional
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public @ResponseBody
    ResponseEntity addCart(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {

        StringBuilder sb = new StringBuilder();

        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append(fieldError);
            }

            return new ResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long CartId;

        try {
            CartId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(CartId, HttpStatus.OK);

    }

    @GetMapping("/cart")
    public String cartList(Model model, Principal principal) {

        List<CartItemListDto> cartItemListDtoList = cartService.getCartList(principal);

        model.addAttribute("cartItems", cartItemListDtoList);

        return "cart/cartList";
    }


    @PatchMapping("/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity updateCartItemCount(@PathVariable Long cartItemId, int count, Principal principal) {

        Long updateCartItemId = cartService.updateCartItem(cartItemId, count, principal);


        return new ResponseEntity(updateCartItemId, HttpStatus.OK);

    }
}
