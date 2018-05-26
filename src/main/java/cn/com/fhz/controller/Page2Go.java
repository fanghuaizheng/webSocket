package cn.com.fhz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by woni on 18/5/24.
 */
@Controller
public class Page2Go {
    @RequestMapping("hello")
    public String getHello(){
        return "hello";
    }
}
