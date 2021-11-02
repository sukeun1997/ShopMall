package shop_retry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop_retry.dto.CartItemDto;
import shop_retry.dto.CartItemListDto;
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
}
