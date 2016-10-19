package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
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
    ReadOnlyTaskBook getTaskBook();

    /** Deletes the given task. */
    void deleteTask(Task target) throws TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task);
    
    //undo and redo
    /** Undo prev action that modifies data**/
    Command undo();
    
    /** reset stack of redoable actions when a non undo modifying data command is called**/
    void resetRedoables();
    
    void recordStateBeforeChange(Command command);
    
    /** redo previous undo **/
    Command redo();
    /////////////end of undo and redo//////////
    
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

    //// Event Tasks

    /** Adds the given event task */
    void addEventTask(EventTask eventTask);

    /** Retrieves the given event task from the specified index in the filtered event task list */
    EventTask getEventTask(int indexInFilteredList) throws IllegalValueException;

    /** Removes the given event task and returns it. */
    EventTask removeEventTask(int indexInFilteredList) throws IllegalValueException;

    /** Replaces the given event task with a new event task */
    void setEventTask(int indexInFilteredList, EventTask newEventTask) throws IllegalValueException;

    /** Returns the filtered event task list as an unmodifiable ObservableList */
    ObservableList<EventTask> getFilteredEventTaskList();

    /**
     * Updates the filter of the filtered event task list to filter by the given predicate.
     *
     * If predicate is null, the filtered event task list will be populated with all event tasks.
     */
    void setEventTaskFilter(Predicate<? super EventTask> predicate);

    //// Deadline Tasks

    /** Adds the given deadline task */
    void addDeadlineTask(DeadlineTask deadlineTask);

    /** Retrieves the given deadline task from the specified index in the filtered deadline task list */
    DeadlineTask getDeadlineTask(int indexInFilteredList) throws IllegalValueException;

    /** Removes the given deadline task and returns it. */
    DeadlineTask removeDeadlineTask(int indexInFilteredList) throws IllegalValueException;

    /** Replaces the given deadline task with a new deadline task */
    void setDeadlineTask(int indexInFilteredList, DeadlineTask newDeadlineTask) throws IllegalValueException;

    /** Returns the filtered deadline task list as an unmodifiable ObservableList */
    ObservableList<DeadlineTask> getFilteredDeadlineTaskList();

    /**
     * Updates the filter of the filtered deadline task list to filter by the given predicate.
     *
     * If predicate is null, the filtered deadline task list will be populated with all deadline tasks.
     */
    void setDeadlineTaskFilter(Predicate<? super DeadlineTask> predicate);
    
 }
