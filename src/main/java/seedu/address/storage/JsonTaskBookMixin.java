package seedu.address.storage;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Task;

public abstract class JsonTaskBookMixin {
    JsonTaskBookMixin(@JsonProperty("tasks") List<Task> tasks,
                      @JsonProperty("floatingTasks") List<FloatingTask> floatingTasks,
                      @JsonProperty("eventTasks") List<EventTask> eventTasks,
                      @JsonProperty("deadlineTasks") List<DeadlineTask> deadlineTasks) {
    }
}
