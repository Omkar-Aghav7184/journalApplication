package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.ConfigJournalApp;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppEntryRepository extends MongoRepository<ConfigJournalApp, ObjectId> {
}
