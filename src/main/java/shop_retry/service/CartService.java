package shop_retry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import shop_retry.dto.CartItemDto;
import shop_retry.dto.CartItemListDto;
import shop_retry.dto.CartOrderDto;
import shop_retry.dto.OrderDto;
import shop_retry.entity.*;
import shop_retry.repository.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemImgRepository itemImgRepository;
    private final OrderService orderService;

    //TODO
    public Long addCart(CartItemDto cartItemDto, String email) {

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        CartItem cartItem = CartItem.createCartItem(item, cart, cartItemDto.getCount());
        cartItemRepository.save(cartItem);

        return cart.getId();
    }

    @Transactional(readOnly = true)
    public List<CartItemListDto> getCartList(Principal principal) {
        List<CartItemListDto> cartItemListDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(principal.getName());

        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            return cartItemListDtoList;
        }

        List<CartItem> cartItems = cartItemRepository.findByCartIdOrderByRegTimeDesc(cart.getId());

        for (CartItem cartItem : cartItems) {
            ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(cartItem.getItem().getId(), "Y");
            CartItemListDto cartItemListDto = CartItemListDto.createCartItemListDto(cartItem, itemImg.getImgUrl());
            cartItemListDtoList.add(cartItemListDto);
        }

        return cartItemListDtoList;
    }

    public Long updateCartItem(Long cartItemId, int count, Principal principal) {

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        if (!validateCartItem(cartItemId, principal)) {
            throw new IllegalArgumentException("변경 권한이 없습니다.");
        }

        cartItem.setCount(count);

        return cartItemId;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, Principal principal) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();
        Member curMember = memberRepository.findByEmail(principal.getName());

        if (!StringUtils.equals(curMember.getId(), savedMember.getId())) {
            return false;
        }

        return true;
    }

    public void deleteCartItem(Long cartItemId, Principal principal) {

        if (!validateCartItem(cartItemId, principal)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);

    }

    public Long orderItems(CartOrderDto cartOrderDtoList, Principal principal) {
        Long orderId;

        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList.getCartOrderDtoList()) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = OrderDto.createOrderDto(cartItem.getItem().getId(), cartItem.getCount());
            orderDtoList.add(orderDto);
            cartItemRepository.delete(cartItem);
        }

        orderId = orderService.createOrder(orderDtoList, principal);


        return orderId;

    }
}
