package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @GetMapping
    public String hello(Map<String, Object> map)
    {
        map.put("name", "Clark");

        System.out.println("================哈哈哈============================");

        return "activiti/activitiProccessImagePage";
       // return "activiti/activitiMainPage";
        //return "home/editor";
        //return "activiti-watchimage/goViewPng/20001";
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
