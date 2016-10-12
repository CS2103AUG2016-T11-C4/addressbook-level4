package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskBook newData);

    /** Returns the TaskBook */
    ReadOnlyTaskBook getAddressBook();

    /** Deletes the given task. */
    void deleteTask(Task target) throws TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task);

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<Task>} */
    UnmodifiableObservableList<Task> getFilteredTaskList();

    /**
     * Updates the filter of the filtered task list to filter by the given predicate.
     *
     * If predicate is null, all tasks will be shown.
     */
    void setFilter(Predicate<Task> predicate);

    //// Floating Tasks
    
    /* Adds the given floating task */
    void addFloatingTask(FloatingTask floatingTask);
    
    /** Retrieves the given Floating task from the specified index in the filtered Floating task list */
    FloatingTask getFloatingTask(int indexInFilteredList) throws IllegalValueException;
    
    /** Removes the given Floating task and returns it. */
    FloatingTask removeFloatingTask(int indexInFilteredList) throws IllegalValueException;
    
    /** Replaces the given Floating task with a new Floating task */
    void setFloatingTask(int indexInFilteredList, FloatingTask newFloatingTask) throws IllegalValueException;
    
    /** Returns the filtered Floating task list as an unmodifiable ObservableList */
    ObservableList<FloatingTask> getFilteredFloatingTaskList();
    
    /**
     * Updates the filter of the filtered Floating task list to filter by the given predicate.
     *
     * If predicate is null, the filtered Floating task list will be populated with all Floating tasks.
     */
    void setFloatingTaskFilter(Predicate<? super FloatingTask> predicate);

}
