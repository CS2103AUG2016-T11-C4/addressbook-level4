package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;

/**
 * Unmodifiable view of a task book.
 */
public interface ReadOnlyTaskBook {

    /**
     * Returns an unmodifiable view of the FloatingTasks list.
     */
    ObservableList<FloatingTask> getFloatingTasks();

    /**
     * Returns an unmodifiable view of the DeadlineTasks list.
     */
    ObservableList<DeadlineTask> getDeadlineTasks();

    /**
     * Returns an unmodifiable view of the EventTasks list.
     */
    ObservableList<EventTask> getEventTasks();

}
