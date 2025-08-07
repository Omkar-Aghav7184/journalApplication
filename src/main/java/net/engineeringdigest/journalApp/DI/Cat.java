package net.engineeringdigest.journalApp.DI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class Cat {

    //Cat is dependent on Dog class
    //Injects beans/objects from dependent class
    @Autowired
    private Dog dog;

    //To take endPoint access
    @GetMapping("ok")
    public String ok()
    {
        return dog.fun();
    }
}
