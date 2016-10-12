package seedu.address.model;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskBook taskBook;
    private final FilteredList<Task> filteredTasks;
    private final FilteredList<FloatingTask> filteredFloatingTasks;
    
    /**
     * Initializes a ModelManager with the given TaskBook
     * TaskBook and its variables should not be null
     */
    public ModelManager(ReadOnlyTaskBook taskBook) {
        super();
        assert taskBook != null;

        logger.fine("Initializing with task book: " + taskBook);

        this.taskBook = new TaskBook(taskBook);
        this.filteredTasks = new FilteredList<>(this.taskBook.getTasks());
        this.filteredFloatingTasks = new FilteredList<>(this.taskBook.getFloatingTasks());
    }

    public ModelManager() {
        this(new TaskBook());
    }

    @Override
    public void resetData(ReadOnlyTaskBook newData) {
        taskBook.resetData(newData);
        indicateTaskBookChanged();
    }

    @Override
    public ReadOnlyTaskBook getAddressBook() {
        return taskBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskBookChanged() {
        raise(new TaskBookChangedEvent(taskBook));
    }

    @Override
    public synchronized void deleteTask(Task target) throws TaskNotFoundException {
        taskBook.removeTask(target);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized void addTask(Task person) {
        taskBook.addTask(person);
        setFilter(null);
        indicateTaskBookChanged();
    }

    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<Task> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void setFilter(Predicate<Task> predicate) {
        filteredTasks.setPredicate(predicate);
    }
    
    //// Floating tasks

    @Override
    public synchronized void addFloatingTask(FloatingTask floatingTask) {
        taskBook.addFloatingTask(floatingTask);
        setFloatingTaskFilter(null);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized FloatingTask getFloatingTask(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredFloatingTasks.get(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    private int getFloatingTaskSourceIndex(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredFloatingTasks.getSourceIndex(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    @Override
    public synchronized FloatingTask removeFloatingTask(int indexInFilteredList) throws IllegalValueException {
        final FloatingTask removedFloating = taskBook.removeFloatingTask(getFloatingTaskSourceIndex(indexInFilteredList));
        indicateTaskBookChanged();
        return removedFloating;
    }

    @Override
    public synchronized void setFloatingTask(int indexInFilteredList, FloatingTask newFloatingTask)
            throws IllegalValueException {
        taskBook.setFloatingTask(getFloatingTaskSourceIndex(indexInFilteredList), newFloatingTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<FloatingTask> getFilteredFloatingTaskList() {
        return filteredFloatingTasks;
    }

    @Override
    public void setFloatingTaskFilter(Predicate<? super FloatingTask> predicate) {
        filteredFloatingTasks.setPredicate(predicate);
    }
    

}
