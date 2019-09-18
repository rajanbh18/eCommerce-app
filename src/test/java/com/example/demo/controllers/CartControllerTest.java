package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    @Before
    public void setup(){

        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void testAddToCartHappyPath(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);

        Item item = new Item();
        item.setName("mobile");
        item.setPrice(BigDecimal.TEN);

        Optional<Item> optionalItem = Optional.of(item);

        Mockito.when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(TestUtils.createUser());
        Mockito.when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(optionalItem);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cart = responseEntity.getBody();
        Assert.assertEquals("mobile", cart.getItems().get(0).getName());
        Assert.assertEquals(BigDecimal.TEN, cart.getTotal());
    }

    @Test
    public void testAddToCartForNullUser(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        Assert.assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testAddToCartForNullItem(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);

        Mockito.when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(TestUtils.createUser());
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        Assert.assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testRemoveHappyPath(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(1);

        Item item = new Item();
        item.setName("mobile");
        item.setPrice(BigDecimal.TEN);

        Optional<Item> optionalItem = Optional.of(item);

        Mockito.when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(TestUtils.createUser());
        Mockito.when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(optionalItem);
        ResponseEntity<Cart> addResponse = cartController.addTocart(modifyCartRequest);
        Assert.assertEquals(200, addResponse.getStatusCodeValue());
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cart = responseEntity.getBody();
        Assert.assertEquals(0, cart.getItems().size());
        Assert.assertEquals(BigDecimal.ZERO, cart.getTotal());

    }
}
