package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class UserControllerTest {

    private UserController userController;


    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);

    @Before
    public void before(){
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void testCreateUserHappyPath(){
        Mockito.when(encoder.encode("test1234")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("test1234");
        createUserRequest.setConfirmPassword("test1234");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        Assert.assertEquals(0, user.getId());
        Assert.assertEquals("test", user.getUsername());
        Assert.assertEquals("thisIsHashed", user.getPassword());
    }
    @Test
    public void testCreateUserUnHappyPath(){
        Mockito.when(encoder.encode("test1234")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("test12");
        createUserRequest.setConfirmPassword("test12");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testFindByUserNameHappyPath(){
        User user = new User();
        user.setUsername("test");
        Mockito.when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<User> userResponse = userController.findByUserName("test");
        Assert.assertEquals(200, userResponse.getStatusCodeValue());
        User responseBody = userResponse.getBody();
        Assert.assertNotNull(responseBody);
        Assert.assertEquals("test", responseBody.getUsername());

    }

    @Test
    public void testFindByUserNameUnHappyPath(){
        ResponseEntity<User> userResponse = userController.findByUserName("test");
        Assert.assertEquals(404, userResponse.getStatusCodeValue());
    }
}
