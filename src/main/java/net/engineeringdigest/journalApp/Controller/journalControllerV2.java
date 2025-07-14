package net.engineeringdigest.journalApp.Controller;

import com.sun.org.apache.bcel.internal.generic.ARETURN;
import net.engineeringdigest.journalApp.entity.JournalAppEntry;
import net.engineeringdigest.journalApp.entity.JournalAppEntryV2;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/journalAppV2")
public class journalControllerV2 {

    //Create 5 API's: 1)GetAll() 2)GetById() 3)Post() 4)Put() 5)Delete()
    //Injects from JournalEntryService
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<JournalAppEntryV2>> getAllJournalEntriesOfUsers()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalAppEntryV2> all = user.getJournalEntries();
        if(all!=null && !all.isEmpty())
        {
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<JournalAppEntryV2> createNewEntry(@RequestBody JournalAppEntryV2 journalAppEntryV2)
    {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            JournalAppEntryV2 myEntry = journalEntryService.saveEntry(journalAppEntryV2,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalAppEntryV2> getJournalAppEntryById(@PathVariable ObjectId myId)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        User user = userService.findByUserName(userName);
        List<JournalAppEntryV2> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty())
        {
            Optional<JournalAppEntryV2> entryById = journalEntryService.getEntryById(myId);
            if(entryById.isPresent())
            {
                return new ResponseEntity<>(entryById.get(),HttpStatus.OK);
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalAppEntry(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removedId = journalEntryService.deleteEntryById(myId, userName);
        if(removedId)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/id/{id}")
    public ResponseEntity<JournalAppEntryV2> updateJournalAppEntry(@RequestBody JournalAppEntryV2 newEntry, @PathVariable ObjectId id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalAppEntryV2> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty())
        {
            Optional<JournalAppEntryV2> entryById = journalEntryService.getEntryById(id);
            if(entryById.isPresent())
            {
                JournalAppEntryV2 oldEntry = entryById.get();
                oldEntry.setTitle(newEntry.getTitle()!=null && !newEntry.getTitle().isEmpty()?newEntry.getTitle(): oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent()!=null && !newEntry.getContent().isEmpty()?newEntry.getContent(): oldEntry.getContent());
                JournalAppEntryV2 updated = journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(updated,HttpStatus.OK);
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
