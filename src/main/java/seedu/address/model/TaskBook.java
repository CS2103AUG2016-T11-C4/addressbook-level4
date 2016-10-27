package seedu.address.model;

import java.util.Collection;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;

/**
 * Manages lists of {@link FloatingTask}, {@link DeadlineTask} and {@link EventTask}.
 */
public class TaskBook implements ReadOnlyTaskBook {

    private final ObservableList<FloatingTask> floatingTasks;

    private final ObservableList<DeadlineTask> deadlineTasks;

    private final ObservableList<EventTask> eventTasks;

    public TaskBook() {
        floatingTasks = FXCollections.observableArrayList();
        deadlineTasks = FXCollections.observableArrayList();
        eventTasks = FXCollections.observableArrayList();
    }

    /**
     * Tasks are copied into this TaskBook.
     */
    public TaskBook(ReadOnlyTaskBook toBeCopied) {

        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of this TaskBook with {@code newData}.
     */
    public void resetData(ReadOnlyTaskBook newData) {
        setFloatingTasks(newData.getFloatingTasks());
        setDeadlineTasks(newData.getDeadlineTasks());
        setEventTasks(newData.getEventTasks());
    }

    //// floating task operations

    @Override
    public ObservableList<FloatingTask> getFloatingTasks() {
        return FXCollections.unmodifiableObservableList(floatingTasks);
    }

    public void setFloatingTasks(Collection<FloatingTask> floatingTasks) {
        this.floatingTasks.setAll(floatingTasks);
    }

    public void addFloatingTask(FloatingTask floatingTask) {
        floatingTasks.add(floatingTask);
    }

    /**
     * Remove the FloatingTask at position `index` in the list. Return the removed FloatingTask.
     */
    public FloatingTask removeFloatingTask(int index) {
        return floatingTasks.remove(index);
    }

    public void setFloatingTask(int index, FloatingTask newFloatingTask) {
        floatingTasks.set(index, newFloatingTask);
    }

    //// deadline task operations

    @Override
    public ObservableList<DeadlineTask> getDeadlineTasks() {
        return FXCollections.unmodifiableObservableList(deadlineTasks);
    }

    public void setDeadlineTasks(Collection<DeadlineTask> deadlineTasks) {
        this.deadlineTasks.setAll(deadlineTasks);
    }

    public void addDeadlineTask(DeadlineTask deadlineTask) {
        deadlineTasks.add(deadlineTask);
    }

    /**
     * Removes the DeadlineTask at position `index` in the list. Returns the removed DeadlineTask.
     */
    public DeadlineTask removeDeadlineTask(int index) {
        return deadlineTasks.remove(index);
    }

    public void setDeadlineTask(int index, DeadlineTask newDeadlineTask) {
        deadlineTasks.set(index, newDeadlineTask);
    }

    //// event task operations

    @Override
    public ObservableList<EventTask> getEventTasks() {
        return FXCollections.unmodifiableObservableList(eventTasks);
    }

    public void setEventTasks(Collection<EventTask> eventTasks) {
        this.eventTasks.setAll(eventTasks);
    }

    public void addEventTask(EventTask eventTask) {
        eventTasks.add(eventTask);
    }

    /**
     * Removes the EventTask at position `index` in the list. Returns the removed EventTask.
     */
    public EventTask removeEventTask(int index) {
        return eventTasks.remove(index);
    }

    public void setEventTask(int index, EventTask newEventTask) {
        eventTasks.set(index, newEventTask);
    }

    //// util methods

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("floatingTasks", floatingTasks)
            .add("deadlineTasks", deadlineTasks)
            .add("eventTasks", eventTasks)
            .toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskBook // instanceof handles nulls
                && this.floatingTasks.equals(((TaskBook) other).floatingTasks)
                && this.deadlineTasks.equals(((TaskBook) other).deadlineTasks)
                && this.eventTasks.equals(((TaskBook) other).eventTasks)
                );
    }

    @Override
    public int hashCode() {
        return Objects.hash(floatingTasks, deadlineTasks, eventTasks);
    }
}
