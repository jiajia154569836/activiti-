package com.controller;

import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    @Qualifier("service1")
    private  UserService userService;



    @GetMapping
    public String user(Map<String, Object> map)
    {
        map.put("name", "Clark");

        System.out.println("============================================");

        User user = new User();
        user.setUsername("123");
        user.setPassword("123");
        user = userService.query(user);
        System.out.println("ID为"+user.getId());

        return "home/index";
    }

    //http://localhost:8080/home/22
    @GetMapping("/{id}")
    public String helloId(@PathVariable String id, Model model)
    {
        System.out.println("id"+id);
        model.addAttribute("name","DD");

        return "home/index";
    }
}
