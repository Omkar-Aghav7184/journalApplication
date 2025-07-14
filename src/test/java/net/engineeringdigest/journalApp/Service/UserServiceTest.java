package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    /*
    @BeforeEach
    @BeforeAll
    @AfterEach
    @AfterAll
    void beforeSetup()
    {

    }
    */
    //userService saveTestNewUser
    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testFindByUserNameArgs(User user)
    {
        assertTrue(userService.saveTestNewUser(user));
    }

    @ParameterizedTest
    @ValueSource(strings ={
            "Shyam",
            "Omkar",
            "ram"
    })
    public void testSaveNewUser(String name)
    {
        User user = userRepository.findByUsername(name);
        assertNotNull(user,"failed for user "+name+" should not null!");
    }

    @Test
    public void testTrue()
    {
        assertTrue(5>3);
        User user = userRepository.findByUsername("Shyam");
        assertTrue(!user.getJournalEntries().isEmpty(),"failed for user "+user.getUsername()+"journal entries are empty");
    }

    //simple test wit add
    @Disabled
    @ParameterizedTest
    @CsvSource({
            "4,2,2",
            "7,5,2",
            "7,3,4"
    })
    public void testwithAdd(int expected,int a,int b)
    {
        assertEquals(expected,a+b);
    }
}
