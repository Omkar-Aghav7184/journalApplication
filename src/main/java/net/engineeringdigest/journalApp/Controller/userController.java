package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class userController {

    //Injects properties from userService
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    //Get all Users if user role is Admin

    @GetMapping
    public List<User> getAllUsers()
    {
        return userService.getAll();
    }


    //update user entry
    @PutMapping
    public ResponseEntity<User> updatedUser(@RequestBody User user)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>(userInDb, HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUserById(@RequestBody User user)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        userRepository.deleteByUsername(userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
