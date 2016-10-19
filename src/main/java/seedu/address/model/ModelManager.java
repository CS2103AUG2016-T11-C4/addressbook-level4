package seedu.address.model;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
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
    private final FilteredList<EventTask> filteredEventTasks;
    private final FilteredList<DeadlineTask> filteredDeadlineTasks;

    //for undo
    public Stack<TaskBook> stateStack = new Stack<TaskBook>();//a stack of past TaskBook states
    public final Stack<Command> modifyingDataCommandHistory = new Stack<Command>();

    //for redo
    public Stack<TaskBook> undoneStates = new Stack<TaskBook>();
    public Stack<Command> undoneCommands = new Stack<Command>();

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
        this.filteredEventTasks = new FilteredList<>(this.taskBook.getEventTasks());
        this.filteredDeadlineTasks = new FilteredList<>(this.taskBook.getDeadlineTasks());
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
    public ReadOnlyTaskBook getTaskBook() {
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

    //=============for undo and redo===================================
    @Override
    public Command undo() throws EmptyStackException {
        undoneStates.push(new TaskBook(getTaskBook()));
        TaskBook prevState = stateStack.pop();
        resetData(prevState);
        Command undoneAction = modifyingDataCommandHistory.pop();
        undoneCommands.push(undoneAction);
        return undoneAction;
    }

    @Override
    public void resetRedoables() {
        undoneCommands = new Stack<Command>();
        undoneStates = new Stack<TaskBook>();
    }

    @Override
    public void recordStateBeforeChange(Command command) {
        TaskBook state = new TaskBook(getTaskBook());
        stateStack.push(state);
        modifyingDataCommandHistory.push(command);
    }

    @Override
    public Command redo() throws EmptyStackException {

        TaskBook state = undoneStates.pop();
        Command action = undoneCommands.pop();
        recordStateBeforeChange(action);
        resetData(state);
        return action;
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

    //// Event tasks

    @Override
    public synchronized void addEventTask(EventTask eventTask) {
        taskBook.addEventTask(eventTask);
        setEventTaskFilter(null);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized EventTask getEventTask(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredEventTasks.get(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    private int getEventTaskSourceIndex(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredEventTasks.getSourceIndex(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    @Override
    public synchronized EventTask removeEventTask(int indexInFilteredList) throws IllegalValueException {
        final EventTask removedEvent = taskBook.removeEventTask(getEventTaskSourceIndex(indexInFilteredList));
        indicateTaskBookChanged();
        return removedEvent;
    }

    @Override
    public synchronized void setEventTask(int indexInFilteredList, EventTask newEventTask)
            throws IllegalValueException {
        taskBook.setEventTask(getEventTaskSourceIndex(indexInFilteredList), newEventTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<EventTask> getFilteredEventTaskList() {
        return filteredEventTasks;
    }

    @Override
    public void setEventTaskFilter(Predicate<? super EventTask> predicate) {
        filteredEventTasks.setPredicate(predicate);
    }

    //// Deadline tasks

    @Override
    public synchronized void addDeadlineTask(DeadlineTask deadlineTask) {
        taskBook.addDeadlineTask(deadlineTask);
        setDeadlineTaskFilter(null);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized DeadlineTask getDeadlineTask(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredDeadlineTasks.get(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    private int getDeadlineTaskSourceIndex(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredDeadlineTasks.getSourceIndex(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    @Override
    public synchronized DeadlineTask removeDeadlineTask(int indexInFilteredList) throws IllegalValueException {
        final DeadlineTask removedDeadline = taskBook.removeDeadlineTask(getDeadlineTaskSourceIndex(indexInFilteredList));
        indicateTaskBookChanged();
        return removedDeadline;
    }

    @Override
    public synchronized void setDeadlineTask(int indexInFilteredList, DeadlineTask newDeadlineTask)
            throws IllegalValueException {
        taskBook.setDeadlineTask(getDeadlineTaskSourceIndex(indexInFilteredList), newDeadlineTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<DeadlineTask> getFilteredDeadlineTaskList() {
        return filteredDeadlineTasks;
    }

    @Override
    public void setDeadlineTaskFilter(Predicate<? super DeadlineTask> predicate) {
        filteredDeadlineTasks.setPredicate(predicate);
    }

}
