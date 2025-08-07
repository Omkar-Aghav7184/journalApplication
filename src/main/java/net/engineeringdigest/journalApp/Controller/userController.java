package net.engineeringdigest.journalApp.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.Response.WeatherEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "User Management", description = "User profile, update, delete, and greeting APIs." )
public class userController {

    //Injects properties from userService
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    //Get all Users if user role is Admin

    @GetMapping
    @Operation(
            summary = "Get all users (Admin only)",
            description = "Fetches a list of all users. Accessible only to users with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    })
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Update current user",
            description = "Updates the currently logged-in user's username and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Invalid or missing token")
    })
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
        log.info("User updated: {}", userName);
        return new ResponseEntity<>(userInDb, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete current user",
            description = "Deletes the currently logged-in user from the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Invalid or missing token")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUserById(@RequestBody User user)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        userRepository.deleteByUsername(userName);
        log.info("User Deleted: {}", userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Greet user with weather info",
            description = "Returns a personalized greeting along with real-time weather info for Mumbai"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Greeting with weather info returned"),
            @ApiResponse(responseCode = "404", description = "User not found or weather API error")
    })
    @GetMapping("/greeting")
    public ResponseEntity<String> greetings()
    {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            WeatherEntity weatherOutput = weatherService.getWeather("Mumbai");
            String feelsLike="";
            String observTime="";
            String weatherDesc="";
            if(weatherOutput!=null)
            {
                feelsLike=", weather feels like "+weatherOutput.getCurrent().getFeelslike();
                observTime=" at "+ weatherOutput.getCurrent().getObservationTime();
                weatherDesc=weatherOutput.getCurrent().getWeatherDescriptions().toString();
            }

            return  new ResponseEntity("Hi "+ userName+" Champion"+ feelsLike+observTime+" at location"+weatherDesc,HttpStatus.OK);
        }
        catch (Exception e)
        {
            log.error("Error fetching greeting or weather",e);
          return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
