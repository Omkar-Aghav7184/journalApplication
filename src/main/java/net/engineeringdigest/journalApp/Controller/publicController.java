package net.engineeringdigest.journalApp.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.LoginDTO;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.dto.UserDTO;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name="Public Access", description = "Signup, Login, and Health-Check")
public class publicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JWTUtils jwtUtils;

    @GetMapping("/health-check")
    @Operation(summary = "Check application health status")
    public String healthCheck(){
        log.info("Health is Ok!");
        return "Health-check is fine(:)!";
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user account")
    public ResponseEntity<User> signupUser(@RequestBody UserDTO user)
    {
        try{
            User newUser= new User();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            newUser.setEmail(user.getEmail());
            newUser.setSentimentAnalysis(user.isSentimentAnalysis());
            userService.saveNewUser(newUser);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    @Operation(summary ="Login User ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Login successful, JWT returned"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content)
    })
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO user)
    {
        try{
            User newUser= new User();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(newUser.getUsername(),newUser.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(newUser.getUsername());
            String jwtToken = jwtUtils.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwtToken, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            log.error("Exception occurred while createAuthenticationToken ",e);
            return new ResponseEntity<>("Invalid username and password. Pleas try again!",HttpStatus.BAD_REQUEST);
        }
    }
    /*
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
    */
}
