package seedu.address.model;

import java.util.Comparator;
import java.util.Optional;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.config.ReadOnlyConfig;
import seedu.address.model.filter.TaskPredicate;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.TaskSelect;

/**
 * Read only view of a model component.
 */
public interface ReadOnlyModel {

    //// Config

    /** Returns current config as a read-only view */
    ReadOnlyConfig getConfig();

    /** Returns configured task book file path. */
    String getTaskBookFilePath();

    //// Task Book

    /** Returns the TaskBook */
    ReadOnlyTaskBook getTaskBook();

    /** Return the total number of tasks sotred in sotrage file. */
    int size();

    //// Task Select

    /** Returns the task being selected, if any. */
    Optional<TaskSelect> getTaskSelect();

    //// Task Filtering

    /** Returns the {@link TaskPredicate} used to filter tasks as a read-only property. */
    ReadOnlyProperty<TaskPredicate> taskPredicateProperty();

    /** Returns the {@link TaskPredicate} used to filter tasks. */
    TaskPredicate getTaskPredicate();

    //// Floating Tasks

    /** Retrieves the given floating task given its working index. */
    FloatingTask getFloatingTask(int workingIndex) throws IllegalValueException;

    /** Returns the filtered Floating task list as an unmodifiable ObservableList */
    ObservableList<IndexedItem<FloatingTask>> getFloatingTaskList();

    /** Returns the comparator used to sort the floating task list. */
    Comparator<? super FloatingTask> getFloatingTaskComparator();

    //// Deadline Tasks

    /** Retrieves the given deadline task from the specified index in the filtered deadline task list */
    DeadlineTask getDeadlineTask(int workingIndex) throws IllegalValueException;

    /** Returns the filtered deadline task list as an unmodifiable ObservableList */
    ObservableList<IndexedItem<DeadlineTask>> getDeadlineTaskList();

    /** Returns the comparator used to sort the deadline task list. */
    Comparator<? super DeadlineTask> getDeadlineTaskComparator();

    //// Event Tasks

    /** Retrieves the given event task with the specified working index */
    EventTask getEventTask(int workingIndex) throws IllegalValueException;

    /** Returns the filtered event task list as an unmodifiable ObservableList */
    ObservableList<IndexedItem<EventTask>> getEventTaskList();

    /** Returns the comparator used to sort the event task list. */
    Comparator<? super EventTask> getEventTaskComparator();

    //// Undo/Redo

    /**
     * Returns true if the model differs from the current commit's recorded model.
     */
    boolean hasUncommittedChanges();

}
