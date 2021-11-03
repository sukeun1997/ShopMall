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
import shop_retry.dto.CartOrderDto;
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

        try {
            cartService.updateCartItem(cartItemId, count, principal);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(cartItemId, HttpStatus.OK);

    }

    @DeleteMapping("/cartItem/{cartItemId}")
    @ResponseBody
    public ResponseEntity deleteCartItem(@PathVariable Long cartItemId, Principal principal) {

        try {
            cartService.deleteCartItem(cartItemId, principal);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }


    @PostMapping("/cart/orders")
    public @ResponseBody ResponseEntity cartItemOrder(@RequestBody CartOrderDto cartOrderDto, Principal principal) {

        Long orderId;
        if (cartOrderDto.getCartOrderDtoList() == null || cartOrderDto.getCartOrderDtoList().size() == 0) {

            return new ResponseEntity("주문 상품을 선택 해 주세요.", HttpStatus.BAD_REQUEST);
        }

        for (CartOrderDto orderDto : cartOrderDto.getCartOrderDtoList()) {
            if (!cartService.validateCartItem(orderDto.getCartItemId(), principal)) {
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.BAD_REQUEST);
            }
        }

        orderId = cartService.orderItems(cartOrderDto, principal);

        return new ResponseEntity(orderId, HttpStatus.OK);
    }

}
