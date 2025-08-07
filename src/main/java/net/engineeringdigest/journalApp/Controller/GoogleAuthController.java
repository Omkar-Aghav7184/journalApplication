package net.engineeringdigest.journalApp.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/google")
@Slf4j
@Tag(name = "Google Authentication", description = "Handles Google OAuth login and JWT generation.")
public class GoogleAuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientID;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;

    @Operation(
            summary = "Google OAuth Callback",
            description = "Handles the callback from Google OAuth, registers the user if not already in DB, and returns a JWT token."
    )
    @GetMapping("/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code)
    {
        try{
            String tokenEndpoint=  "https://oauth2.googleapis.com/token";
            MultiValueMap<String,String> params= new LinkedMultiValueMap<>();
            params.add("code",code);
            params.add("client_id",clientID);
            params.add("client_secret",clientSecret);
            params.add("redirect_uri", "https://developers.google.com/oauthplayground");
            params.add("grant_type","authorization_code");

            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String,String>> request= new HttpEntity<>(params,headers);

            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            String idToken=(String) (tokenResponse.getBody().get("id_token"));

            String userInfoUrl= "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);

            if(userInfoResponse.getStatusCode()== HttpStatus.OK)
            {
                Map<String,Object> userInfo=userInfoResponse.getBody();
                String email= (String) userInfo.get("email");
                UserDetails userDetails=null;
                try{
                    userDetails=userDetailsService.loadUserByUsername(email);
                }
                catch (Exception e)
                {
                    User user=new User();
                    user.setEmail(email);
                    user.setUsername(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user.setRoles(Arrays.asList("User"));
                    userRepository.save(user);
                }
                String jwtToken=jwtUtils.generateToken(email);
                return ResponseEntity.ok(Collections.singletonMap("token",jwtToken));
            }
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        catch (Exception e)
        {
            log.info("client_id: {}", clientID);
            log.info("client_secret: {}", clientSecret);
            log.info("auth_code: {}", code);
            log.error("Exception occurred while handleGoogleCallback",e);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
/*

https://accounts.google.com/o/oauth2/auth?
client_id=YOUR_CLIENT_ID
    &redirect_uri=YOUR_REDIRECT_URI
    &response_type=code
    &scope=email profile
    &access_type=offline
    &prompt=consent

*/
