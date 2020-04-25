package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    private String strMessage = null;


    public WelcomeController(@Value("${welcome.message}") String arg1){
        this.strMessage = arg1;
    }
    
    @GetMapping("/")
    public String sayHello(){
        return strMessage;
    }

}

