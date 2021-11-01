package shop_retry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import shop_retry.dto.OrderDto;
import shop_retry.dto.OrderHistDto;
import shop_retry.service.OrderService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@Transactional
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {

        StringBuilder sb = new StringBuilder();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long OrderId;
        try {
            OrderId = orderService.createOrder(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(OrderId, HttpStatus.OK);
    }

    @GetMapping({"/orders", "/orders/{page}"})
    public String getOrderHist(@PathVariable("page") Optional<Integer> page, Model model,Principal principal) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<OrderHistDto>  orders = orderService.orderHist(pageable, principal);
        model.addAttribute("orders", orders);
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

}
