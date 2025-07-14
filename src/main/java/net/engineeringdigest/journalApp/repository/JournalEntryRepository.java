package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalAppEntryV2;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/*Ques: How is JournalEntryRepository being injected via @Autowired into JournalEntryService
even though @Component is not used on the interface?
Ans: Because Spring Data MongoDB automatically creates a proxy bean for your repository at runtime.
You don’t need to annotate repository interfaces with @Component — Spring does that for you behind the scenes.*/

public interface JournalEntryRepository extends MongoRepository<JournalAppEntryV2, ObjectId> {
}
