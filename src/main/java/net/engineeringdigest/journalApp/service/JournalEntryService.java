package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalAppEntryV2;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {

    //Injects from JournalEntryRepo
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private  UserService userService;

    //getAll method
    public List<JournalAppEntryV2> getAll()
    {
        return journalEntryRepository.findAll();
    }
    //@Transactional annotation provides atomicity & isolation
    @Transactional
    //create new entry into App
    public JournalAppEntryV2 saveEntry(JournalAppEntryV2 journalAppEntryV2,String userName)
    {
        try{
            User user = userService.findByUserName(userName);
            journalAppEntryV2.setLocalDateTime(LocalDateTime.now());
            JournalAppEntryV2 saved = journalEntryRepository.save(journalAppEntryV2);
            user.getJournalEntries().add(saved);
            //user.setUsername(null);
            userService.saveAllUsers(user);
            return saved;
        }
        catch (Exception e)
        {
            log.error("An error occurred ",e);
            throw  new RuntimeException("An error occured while saving the entry into users",e);
        }

    }
    //save entries which are updated
    public JournalAppEntryV2 saveEntry(JournalAppEntryV2 journalAppEntryV2)
    {
        return journalEntryRepository.save(journalAppEntryV2);
    }

    //getEntryById()
    public Optional<JournalAppEntryV2> getEntryById(ObjectId objectId)
    {
        return journalEntryRepository.findById(objectId);
    }

    //deleteById()
    @Transactional
    public  boolean  deleteEntryById(ObjectId objectId, String userName)
    {
        boolean removed=false;
        try{
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(objectId));
            if(removed)
            {
                //Save updated user without the journal entry reference
                userService.saveAllUsers(user);
                // Delete the actual journal entry document
                journalEntryRepository.deleteById(objectId);
            }
        }catch (Exception e)
        {
           log.error("An error occurred ",e);
            throw new RuntimeException("An error is occured during deleting Journal Id from user",e);
        }
        return removed;
    }
    /*
    public User findByUserName(String UserName)
    {
        return userService.findByUserName(UserName);
    }

     */
}
