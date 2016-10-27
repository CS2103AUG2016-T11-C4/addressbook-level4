package seedu.address.model;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.model.config.Config;
import seedu.address.model.config.ReadOnlyConfig;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;

/**
 * Represents the in-memory model of the address book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Config config;
    private final TaskBook taskBook;
    private final FilteredList<FloatingTask> filteredFloatingTasks;
    private final FilteredList<DeadlineTask> filteredDeadlineTasks;
    private final FilteredList<EventTask> filteredEventTasks;

    //for undo
    private ArrayList<Commit> commits = new ArrayList<Commit>();
    private int head; //head points to a the current commit which holds the TaskBook displayed by the UI

    /**
     * Initializes a ModelManager with the given config and TaskBook TaskBook
     * and its variables should not be null
     */
    public ModelManager(ReadOnlyConfig config, ReadOnlyTaskBook taskBook) {
        super();
        assert taskBook != null;

        logger.fine("Initializing with config: " + config + " and task book: " + taskBook);

        this.config = new Config(config);
        this.taskBook = new TaskBook(taskBook);
        this.filteredFloatingTasks = new FilteredList<>(this.taskBook.getFloatingTasks());
        this.filteredDeadlineTasks = new FilteredList<>(this.taskBook.getDeadlineTasks());
        this.filteredEventTasks = new FilteredList<>(this.taskBook.getEventTasks());
        recordState(null);
    }

    public ModelManager() {
        this(new Config(), new TaskBook());
    }

    //// Config

    @Override
    public ReadOnlyConfig getConfig() {
        return config;
    }

    @Override
    public String getTaskBookFilePath() {
        return config.getTaskBookFilePath();
    }

    @Override
    public void setTaskBookFilePath(String filePath) {
        config.setTaskBookFilePath(filePath);
    }

    //// Task Book

    @Override
    public void resetTaskBook(ReadOnlyTaskBook newData) {
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

    ////Floating tasks

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

    //// Deadline tasks

    @Override
    public synchronized void addDeadlineTask(DeadlineTask deadlineTask) {
        assert deadlineTask.isFinished() == false;
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

    ////undo redo

    @Override
    public Command undo() throws HeadAtBoundaryException {

        if (head <= 0) {
            throw new HeadAtBoundaryException();
        }
        Command undoneAction = commits.get(head).getCommand();
        head--;
        Commit commit = commits.get(head);
        taskBook.resetData(new TaskBook(commit.getTaskBook()));
        return undoneAction;
    }

    @Override
    public void recordState(Command command) {
        //clear redoable, which are the commits above head
        head ++;
        while (this.head < (commits.size())) {
            commits.remove(head);
        }
        commits.add(new Commit(command, new TaskBook(getTaskBook())));
        head = commits.size() - 1;
    }

    @Override
    public Command redo() throws HeadAtBoundaryException {
        if (head >= commits.size() - 1) {
            throw new HeadAtBoundaryException();
        }
        head++;
        Commit commit = commits.get(head);
        taskBook.resetData(new TaskBook(commit.getTaskBook()));
        return commit.getCommand();
    }

    /**
     * check if taskBook has changed.
     * @return true if TaskBook changed
     */
    @Override
    public boolean hasUncommittedChanges() {
        return !(this.taskBook.equals(commits.get(commits.size() - 1).getTaskBook()));
    }

    private class Commit {
        private TaskBook taskBook;
        private Command command;

        Commit(Command command, TaskBook state) {
            this.taskBook = state;
            this.command = command;
        }

        public TaskBook getTaskBook() {
            return this.taskBook;
        }

        public Command getCommand() {
            return this.command;
        }
    }

    public class HeadAtBoundaryException extends Exception {
        public HeadAtBoundaryException() {
            super();
        }
    }
}
