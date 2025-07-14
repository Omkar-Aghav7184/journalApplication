package net.engineeringdigest.journalApp.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
public class User {
    //POJO Model with @Data
    //properties
    @Id
    private ObjectId objectId;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;
    @DBRef
    private List<JournalAppEntryV2> journalEntries=new ArrayList<>();
    //to authorize various roles based on userName
    private List<String> roles=new ArrayList<>();
}
