package net.engineeringdigest.journalApp.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import net.engineeringdigest.journalApp.enums.Sentiment;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntriesDTO {

    @NotEmpty
    @Schema(description = "Title of the journal entry")
    private String title;

    @NotEmpty
    @Schema(description = "Content of the journal entry")
    private String content;

    @Schema(description = "Sentiment detected in the entry (optional)")
    private Sentiment sentiment;

    @Schema(description = "Date and time of the journal entry (e.g. 2025-08-04T10:30:00)")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
}
