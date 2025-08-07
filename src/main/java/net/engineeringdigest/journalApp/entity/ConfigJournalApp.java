package net.engineeringdigest.journalApp.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="config_journal_entries")
@Data
@NoArgsConstructor
public class ConfigJournalApp {

    private String apiKey;
    private String apiURL;
}
