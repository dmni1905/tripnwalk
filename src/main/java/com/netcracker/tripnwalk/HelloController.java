package com.netcracker.tripnwalk;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by DesiresDesigner on 3/17/16.
 */
@Controller
public class HelloController {

    @RequestMapping("/")
    public String hello(Model model) {
        return "hello";
    }
}
