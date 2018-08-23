package com.controller;

import com.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProjectController {

    @RequestMapping(value = "project")
    public String hello(Map<String, Object> map, Model model)
    {
        map.put("name", "Clark");

        List<User> list = new ArrayList<>();
        User user = new User();
        user.setId("1");
        user.setPassword("2");
        user.setUsername("3");
        list.add(user);
       model.addAttribute("list",list);
        System.out.println("============================================");

        return "project/home";
    }
}
