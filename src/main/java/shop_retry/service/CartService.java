package shop_retry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop_retry.dto.CartItemDto;
import shop_retry.entity.Cart;
import shop_retry.entity.CartItem;
import shop_retry.entity.Item;
import shop_retry.entity.Member;
import shop_retry.repository.CartItemRepository;
import shop_retry.repository.CartRepository;
import shop_retry.repository.ItemRepository;
import shop_retry.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

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
}
