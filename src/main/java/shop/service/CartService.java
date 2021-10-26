package shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.DTO.CartItemDto;
import shop.entitiy.Cart;
import shop.entitiy.CartItem;
import shop.entitiy.Item;
import shop.entitiy.Member;
import shop.repository.CartItemRepository;
import shop.repository.CartRepository;
import shop.repository.ItemRepository;
import shop.repository.MemberRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        // Cart 가 없으면 새로운 장바구니 생성
        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        // 장바구니에 해당 아이템 id 에 해당하는 물품이 cart 에 있는지 확인
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null) { // 장바구니에 이미 기존 물품이 있을시
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else { // 장바구니에 담을 물품ㅇ ㅣ없을시
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }
}
