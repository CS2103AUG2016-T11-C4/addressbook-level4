package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public abstract class JsonPriorityMixin {
    @JsonCreator
    JsonPriorityMixin(String priority) {}

    @JsonValue
    public abstract String toString();
}
