package com.techsphere.techsphere.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techsphere.techsphere.service.PostService;

@RestController
@RequestMapping("/api/v1")
public class HomeRestController {
    @SuppressWarnings("unused")
    @Autowired
    private PostService postService;

    Logger logger = LoggerFactory.getLogger(HomeRestController.class);

    @GetMapping("/")
    public String home(){
        logger.error("This is a test error log");
        return "sample response";
    }
  
    
}
