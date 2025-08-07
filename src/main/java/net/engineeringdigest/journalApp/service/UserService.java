package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserService {

    //Injects methods from UserRepository
    @Autowired
    private UserRepository userRepository;

    private  static  final PasswordEncoder passwordEncode= new BCryptPasswordEncoder();

    //Logging purpose(logger)
    //private static  final Logger logger= LoggerFactory.getLogger(UserService.class);

    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public User saveAllUsers(User user)
    {
        return userRepository.save(user);
    }

    public boolean saveNewUser(User user)
    {
        try{
            user.setPassword(passwordEncode.encode(user.getPassword()));
            user.setRoles(Arrays.asList("User"));
            userRepository.save(user);
            return true;
        }
        catch (Exception e)
        {
           log.error("Error occured for {}:",user.getUsername(),e);
            log.warn("Warn occured for {}:",user.getUsername(),e);
            log.info("Info occured for {}:",user.getUsername(),e);
            log.debug("Debug occured for {}:",user.getUsername(),e);
            log.trace("Trace occured for {}:",user.getUsername(),e);
        }
        return false;
    }
    public boolean saveTestNewUser(User user)
    {
        try{
            user.setPassword(passwordEncode.encode(user.getPassword()));
            user.setRoles(Arrays.asList("User"));
            userRepository.save(user);
            return true;
        }
        catch (Exception e)
        {
            return  false;
        }
    }

    public User saveAdminUser(User user)
    {
        user.setPassword(passwordEncode.encode(user.getPassword()));
        user.setRoles(Arrays.asList("User","ADMIN"));
        return  userRepository.save(user);
    }

    public User findByUserName(String Username)
    {
        return userRepository.findByUsername(Username);
    }

}
