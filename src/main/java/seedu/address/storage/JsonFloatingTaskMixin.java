package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;

@JsonPropertyOrder({"name", "priority", "finished"})
public abstract class JsonFloatingTaskMixin {

    JsonFloatingTaskMixin(@JsonProperty("name") Name name,
            @JsonProperty("priority") Priority priority,
            @JsonProperty("finished") boolean finished) {
    }

}
