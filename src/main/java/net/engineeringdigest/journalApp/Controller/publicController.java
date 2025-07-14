package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class publicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Health-check is fine(:)!";
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> createNewUser(@RequestBody User newUser)
    {
        try{
            userService.saveNewUser(newUser);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
