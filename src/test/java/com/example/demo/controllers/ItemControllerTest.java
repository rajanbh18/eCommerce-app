package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void testGetItemsByName(){
        Item item = new Item();
        item.setName("mobile");
        item.setPrice(BigDecimal.TEN);

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Mockito.when(itemRepository.findByName("mobile")).thenReturn(itemList);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("mobile");
        Item responseItem = responseEntity.getBody().get(0);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        Assert.assertEquals("mobile", responseItem.getName());
    }
}
