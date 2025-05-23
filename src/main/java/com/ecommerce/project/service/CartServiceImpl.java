package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.MyAPIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;


    public CartServiceImpl(CartRepository cartRepository, AuthUtil authUtil, ProductRepository productRepository, CartItemRepository cartItemRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.authUtil = authUtil;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        log.debug("Inside CartServiceImpl for add Product in Cart {}", productId);
        System.out.println("Inside CartServiceImpl for add Product in Cart "+ productId);
        //create cart or find existing cart
        Cart cart = createCart();
        //retrieve the product details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId, "productId"));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        log.debug("Product Id is -> {}", productId);
        System.out.println("cartItem is Null");
        if(cartItem != null){
            throw new MyAPIException("Product " + product.getProductName() + " already exists in the cart");
        }

        //perform validations
        if(product.getQuantity() == 0){
            throw new MyAPIException(product.getProductName() + " is not available");
        }
        if(product.getQuantity() < quantity){
            throw new MyAPIException("Please, make an order of the " + product.getProductName() + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        //create cart items
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        //saving the cart item
        cartItemRepository.save(newCartItem);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItemList = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItemList.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productDTOStream.toList());
        //return updated cart
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if(carts.isEmpty()){
            throw new MyAPIException("No Cart Exists");
        }

        return carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> {
                        ProductDTO productDTO = modelMapper.map(p.getProduct(), ProductDTO.class);
                        productDTO.setQuantity(p.getQuantity());
                        return productDTO;
                    })
                    .collect(Collectors.toList());

            cartDTO.setProducts(products);
            return cartDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public CartDTO getCarts(String authenticatedEmail, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndById(authenticatedEmail,cartId);
        if(cart == null){
            throw new ResourceNotFoundException("cartId", cartId, "Cart");
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream().map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                .toList();
        cartDTO.setProducts(products);
        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId, "cartId"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId, "productId"));

        if(product.getQuantity() == 0){
            throw new MyAPIException(product.getProductName() + " is not available");
        }

        if(product.getQuantity() < quantity){
            throw new MyAPIException("Please, make an order of the " + product.getProductName()
                + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if(cartItem == null){
            throw new MyAPIException("Product " + product.getProductName() + " not available in the cart item");
        }

        int newQuantity = cartItem.getQuantity() + quantity;
        if(newQuantity < 0){
            throw new MyAPIException("Cart quantity can not be empty");
        }

        if(newQuantity == 0){
            deleteProductFromCart(cartId, productId);
        }
        else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItemList = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItemList.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });

        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId, "cartId"));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null){
            throw new ResourceNotFoundException("Product", productId, "productId");
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product" + cartItem.getProduct().getProductName() + " removed from the cart !!";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", cartId, "cartId"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId, "productId"));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null){
            throw new MyAPIException("Product " + product.getProductName() + " not available in the cart Items");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getProductPrice() * cartItem.getQuantity());

        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItemRepository.save(cartItem);
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        cartRepository.save(cart);
        return cart;
    }
}
