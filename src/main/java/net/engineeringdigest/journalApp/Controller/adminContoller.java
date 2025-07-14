package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.JournalAppEntryV2;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class adminContoller {

    @Autowired
    private UserService userService;
    @GetMapping("/getDetails")
    public ResponseEntity<List<User>> getAllEntriesOfAllUsers()
    {
        List<User> all = userService.getAll();
        if(all!=null && !all.isEmpty())
        {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/create-admin")
    public ResponseEntity<User> createNewAdmin(@RequestBody User user)
    {
        try
        {
            userService.saveAdminUser(user);
            return  new ResponseEntity<>(user,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
