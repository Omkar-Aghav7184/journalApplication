package net.engineeringdigest.journalApp.APITesting;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class MyClass {

    @GetMapping("/omkarAghav")
    public String sayHello()
    {
        return "Hello Welcome into Omkar's Tukaram Haribhau Aghav World. I am Champion.Shri Swami Samarath (:) !";
    }
}
