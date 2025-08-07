package net.engineeringdigest.journalApp.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.engineeringdigest.journalApp.dto.JournalEntriesDTO;
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


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/journalAppV2")
@Tag(name = "Journal Management", description = "Manage journal entries (CRUD)")
public class journalControllerV2 {

    //Create 5 API's: 1)GetAll() 2)GetById() 3)Post() 4)Put() 5)Delete()
    //Injects from JournalEntryService
    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;


    @Operation(summary = "Get all Journal Entries Of Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of journals fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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

    @Operation(summary = "Create a new journal entry")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Journal created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided")
    })
    @PostMapping()
    public ResponseEntity<JournalAppEntryV2> createNewEntry(@Valid @RequestBody JournalEntriesDTO journalDTO)
    {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            JournalAppEntryV2 entry = new JournalAppEntryV2();
            entry.setTitle(journalDTO.getTitle());
            entry.setContent(journalDTO.getContent());
            entry.setSentiment(journalDTO.getSentiment());
            entry.setLocalDateTime(journalDTO.getDate());
            JournalAppEntryV2 myEntry = journalEntryService.saveEntry(entry,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get a journal entry by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Journal entry found"),
            @ApiResponse(responseCode = "404", description = "Journal not found or unauthorized")
    })
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

    @Operation(summary = "Delete a journal entry by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Journal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Journal not found or unauthorized")
    })
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

    @Operation(summary = "Update a journal entry by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Journal updated successfully"),
            @ApiResponse(responseCode = "404", description = "Journal not found or unauthorized")
    })
    @PutMapping("/id/{id}")
    public ResponseEntity<JournalAppEntryV2> updateJournalAppEntry(@Valid @RequestBody JournalEntriesDTO  journalDTO, @PathVariable ObjectId id)
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
                oldEntry.setTitle(journalDTO.getTitle()!=null && !journalDTO.getTitle().isEmpty()?journalDTO.getTitle(): oldEntry.getTitle());
                oldEntry.setContent(journalDTO.getContent()!=null && !journalDTO.getContent().isEmpty()?journalDTO.getContent(): oldEntry.getContent());
                oldEntry.setSentiment(journalDTO.getSentiment()!=null?journalDTO.getSentiment(): oldEntry.getSentiment());
                JournalAppEntryV2 updated = journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(updated,HttpStatus.OK);
            }
        }
        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
