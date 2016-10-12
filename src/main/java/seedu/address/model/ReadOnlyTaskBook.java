package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Task;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyTaskBook {

    /**
     * Returns an unmodifiable view of tasks list
     */
    ObservableList<Task> getTasks();

    /**
    * Returns an unmodifiable view of the FloatingTasks list.
    */
    ObservableList<FloatingTask> getFloatingTasks();

    /**
     * Returns an unmodifiable view of the EventTasks list.
     */
    ObservableList<EventTask> getEventTasks();

    /**
     * Returns an unmodifiable view of the DeadlineTasks list.
     */
    ObservableList<DeadlineTask> getDeadlineTasks();

}
