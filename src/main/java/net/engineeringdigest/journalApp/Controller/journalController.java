package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.JournalAppEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestController
@RequestMapping("/_journalApp")
public class journalController {

    //Take field of map treat as Table of journalEntries
    private Map<Long,JournalAppEntry> journalEntries=new HashMap<>();

    //Access All Entries of JournalApp
    @GetMapping
    public List<JournalAppEntry> getAllEntries()
    {
        return new ArrayList<>(journalEntries.values());
    }
    //Access specific JournalEntry from JournalApp
    @GetMapping("/id/{myId}")
    public JournalAppEntry getEntryId(@PathVariable Long myId)
    {
        return journalEntries.get(myId);
    }
    //create a new Entry into JournalApp
    @PostMapping
    public boolean addJournalEntry(@RequestBody JournalAppEntry myEntry)
    {
        journalEntries.put(myEntry.getId(),myEntry);
        return true;
    }
    //delete Entries from JournalApp
    @DeleteMapping("/id/{myId}")
    public JournalAppEntry deleteJournalEntry(@PathVariable Long myId)
    {
        return journalEntries.remove(myId);
    }
    //update journalApp.return entry which we delete
    @PutMapping("/id/{id}")
    public JournalAppEntry updateJournalEntry(@PathVariable Long id, @RequestBody JournalAppEntry myEntry)
    {
        return journalEntries.put(id,myEntry);
    }
}
