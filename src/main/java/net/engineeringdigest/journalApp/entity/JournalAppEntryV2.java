package net.engineeringdigest.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
//here @Document mapped java objects into Mongodb collections/documents(single row)
@Document(collection = "journal_Entries")
@Data
public class JournalAppEntryV2 {

    //POJO Class for JournalAppEntry Controller

    //properties
    @Id //unique key
    private ObjectId id;
    @NonNull
    private String title;
    @NonNull
    private String content;
    private LocalDateTime localDateTime;

    //setters and getters
}
