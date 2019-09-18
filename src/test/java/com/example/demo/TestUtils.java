package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObject(Object target, String fieldName, Object toInject){
        boolean wasPrivate = false;
        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            if (!declaredField.isAccessible()){
                declaredField.setAccessible(true);
                wasPrivate= true;
            }
            declaredField.set(target, toInject);
            if (wasPrivate){
                declaredField.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static CreateUserRequest createUserRequest(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("test1234");
        request.setConfirmPassword("test1234");
        return request;
    }

    public static User createUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("test1234");
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        return user;
    }
}
