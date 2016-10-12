package seedu.address.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import seedu.address.model.task.Name;

@JsonPropertyOrder({"name", "due"})
public abstract class JsonDeadlineTaskMixin {

    JsonDeadlineTaskMixin(@JsonProperty("name") Name name,
                       @JsonProperty("due") LocalDateTime due) {
    }

}
