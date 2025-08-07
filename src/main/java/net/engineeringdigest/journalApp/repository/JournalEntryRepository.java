package net.engineeringdigest.journalApp.repository;
import net.engineeringdigest.journalApp.entity.JournalAppEntryV2;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalAppEntryV2, ObjectId> {
}
