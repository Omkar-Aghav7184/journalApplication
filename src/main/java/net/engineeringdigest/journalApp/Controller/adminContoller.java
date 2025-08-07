package net.engineeringdigest.journalApp.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalAppEntryV2;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.dto.UserDTO;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Controller", description = "Admin APIs for user and cache management")
public class adminContoller {

    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @Operation(summary = "Get all registered users", description = "Returns a list of all registered users from database")
    @GetMapping("/getDetails")
    public ResponseEntity<List<User>> getAllEntriesOfAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a new admin user", description = "Creates a new admin user in the system")
    @PostMapping("/create-admin")
    public ResponseEntity<User> createNewAdmin(@Valid @RequestBody UserDTO userdto) {
        try {
            // Convert DTO to User
            User user = new User();
            user.setUsername(userdto.getUsername());
            user.setPassword(userdto.getPassword());
            user.setEmail(userdto.getEmail());
            user.setSentimentAnalysis(userdto.isSentimentAnalysis());

            userService.saveAdminUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Clear application-level cache", description = "Manually clears the in-memory application cache")
    @GetMapping("/clear-app-cache")
    public ResponseEntity<String> clearCache() {
        appCache.init();
        return ResponseEntity.ok("Cache cleared");
    }


}
