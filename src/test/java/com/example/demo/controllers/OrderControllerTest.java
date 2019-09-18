package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void testCreateOrder(){

        Item item = new Item();
        item.setName("mobile");
        item.setPrice(BigDecimal.TEN);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        User user = TestUtils.createUser();
        user.getCart().setItems(itemList);
        Mockito.when(userRepository.findByUsername("test")).thenReturn(user);
        final ResponseEntity<UserOrder> response = orderController.submit("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        UserOrder userOrder = response.getBody();
        Assert.assertEquals("test", userOrder.getUser().getUsername());
        Assert.assertEquals("mobile", userOrder.getItems().get(0).getName());
    }

    @Test
    public void testOrdersForUser(){
        Item item = new Item();
        item.setName("mobile");
        item.setPrice(BigDecimal.TEN);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        User user = TestUtils.createUser();
        user.getCart().setItems(itemList);

        UserOrder userOrder = new UserOrder();
        userOrder.setItems(itemList);
        userOrder.setUser(user);

        List<UserOrder> userOrderList = new ArrayList<>();
        userOrderList.add(userOrder);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(user);
        Mockito.when(orderRepository.findByUser(user)).thenReturn(userOrderList);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        UserOrder responseUserOrder = response.getBody().get(0);
        Assert.assertEquals("test", responseUserOrder.getUser().getUsername());
        Assert.assertEquals("mobile", responseUserOrder.getItems().get(0).getName());
    }
}
