package seedu.address.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.events.ui.UpdateTaskBookListViewEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.config.Config;
import seedu.address.model.config.ReadOnlyConfig;
import seedu.address.model.filter.TaskPredicate;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.TaskSelect;
import seedu.address.model.task.TaskType;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Config config;
    private final WorkingTaskBook workingTaskBook;
    private Optional<TaskSelect> taskSelect = Optional.empty();

    //for undo
    private ArrayList<Commit> commits = new ArrayList<Commit>();
    private int head = -1; //head points to a the current commit which holds the TaskBook displayed by the UI

    /**
     * Initializes a ModelManager with the given config and TaskBook
     * TaskBook and its variables should not be null
     */
    public ModelManager(ReadOnlyConfig config, ReadOnlyTaskBook taskBook) {
        super();
        assert taskBook != null;

        logger.fine("Initializing with config: " + config + " and task book: " + taskBook);

        this.config = new Config(config);
        this.workingTaskBook = new WorkingTaskBook(taskBook);
        recordState("initial commit");
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
        workingTaskBook.resetData(newData);
        setTaskSelect(Optional.empty());
        indicateTaskBookChanged();
    }

    @Override
    public ReadOnlyTaskBook getTaskBook() {
        return workingTaskBook.getTaskBook();
    }

    @Override
    public int size() {
        return workingTaskBook.size();
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskBookChanged() {
        raise(new TaskBookChangedEvent(workingTaskBook.getTaskBook()));
    }

    /** Raise an event to indicate the list view in UI has changed */
    private void indicateUIChanged() {
        raise(new UpdateTaskBookListViewEvent(this));
    }

    //// Task select

    @Override
    public Optional<TaskSelect> getTaskSelect() {
        return taskSelect;
    }

    @Override
    public void setTaskSelect(Optional<TaskSelect> taskSelect) {
        assert taskSelect != null;
        this.taskSelect = taskSelect;
    }

    //// Task filtering

    @Override
    public ReadOnlyProperty<TaskPredicate> taskPredicateProperty() {
        return workingTaskBook.taskPredicateProperty();
    }

    @Override
    public TaskPredicate getTaskPredicate() {
        return workingTaskBook.getTaskPredicate();
    }

    @Override
    public void setTaskPredicate(TaskPredicate taskFilter) {
        workingTaskBook.setTaskPredicate(taskFilter);
        setTaskSelect(Optional.empty());
        indicateUIChanged();
    }

    //// Floating tasks

    @Override
    public synchronized int addFloatingTask(FloatingTask floatingTask) {
        final int workingIndex = workingTaskBook.addFloatingTask(floatingTask);
        indicateTaskBookChanged();
        setTaskSelect(Optional.of(new TaskSelect(TaskType.FLOAT, workingIndex)));
        indicateUIChanged();
        return workingIndex;
    }

    @Override
    public synchronized FloatingTask getFloatingTask(int workingIndex) throws IllegalValueException {
        return workingTaskBook.getFloatingTask(workingIndex);
    }

    @Override
    public synchronized FloatingTask removeFloatingTask(int workingIndex) throws IllegalValueException {
        final FloatingTask removedFloating = workingTaskBook.removeFloatingTask(workingIndex);
        indicateTaskBookChanged();
        if (getTaskSelect().equals(Optional.of(new TaskSelect(TaskType.FLOAT, workingIndex)))) {
            setTaskSelect(Optional.empty());
        }
        indicateUIChanged();
        return removedFloating;
    }

    @Override
    public synchronized void removeFloatingTasks(TaskPredicate predicate) {
        workingTaskBook.removeFloatingTasks(predicate);
        indicateTaskBookChanged();
        setTaskSelect(Optional.empty());
        indicateUIChanged();
    }

    @Override
    public synchronized void setFloatingTask(int workingIndex, FloatingTask newFloatingTask)
            throws IllegalValueException {
        workingTaskBook.setFloatingTask(workingIndex, newFloatingTask);
        indicateTaskBookChanged();
        setTaskSelect(Optional.of(new TaskSelect(TaskType.FLOAT, workingIndex)));
        indicateUIChanged();
    }

    @Override
    public ObservableList<IndexedItem<FloatingTask>> getFloatingTaskList() {
        return workingTaskBook.getFloatingTaskList();
    }

    @Override
    public Comparator<? super FloatingTask> getFloatingTaskComparator() {
        return workingTaskBook.getFloatingTaskComparator();
    }

    @Override
    public void setFloatingTaskComparator(Comparator<? super FloatingTask> comparator) {
        workingTaskBook.setFloatingTaskComparator(comparator);
        setTaskSelect(Optional.empty());
        indicateUIChanged();
    }

    //// Deadline tasks

    @Override
    public synchronized int addDeadlineTask(DeadlineTask deadlineTask) {
        final int workingIndex = workingTaskBook.addDeadlineTask(deadlineTask);
        indicateTaskBookChanged();
        setTaskSelect(Optional.of(new TaskSelect(TaskType.DEADLINE, workingIndex)));
        indicateUIChanged();
        return workingIndex;
    }

    @Override
    public synchronized DeadlineTask getDeadlineTask(int workingIndex) throws IllegalValueException {
        return workingTaskBook.getDeadlineTask(workingIndex);
    }

    @Override
    public synchronized DeadlineTask removeDeadlineTask(int workingIndex) throws IllegalValueException {
        final DeadlineTask removedDeadline = workingTaskBook.removeDeadlineTask(workingIndex);
        indicateTaskBookChanged();
        if (getTaskSelect().equals(Optional.of(new TaskSelect(TaskType.DEADLINE, workingIndex)))) {
            setTaskSelect(Optional.empty());
        }
        indicateUIChanged();
        return removedDeadline;
    }

    @Override
    public synchronized void removeDeadlineTasks(TaskPredicate predicate) {
        workingTaskBook.removeDeadlineTasks(predicate);
        indicateTaskBookChanged();
        setTaskSelect(Optional.empty());
        indicateUIChanged();
    }

    @Override
    public synchronized void setDeadlineTask(int workingIndex, DeadlineTask newDeadlineTask)
            throws IllegalValueException {
        workingTaskBook.setDeadlineTask(workingIndex, newDeadlineTask);
        indicateTaskBookChanged();
        setTaskSelect(Optional.of(new TaskSelect(TaskType.DEADLINE, workingIndex)));
        indicateUIChanged();
    }

    @Override
    public ObservableList<IndexedItem<DeadlineTask>> getDeadlineTaskList() {
        return workingTaskBook.getDeadlineTaskList();
    }

    @Override
    public Comparator<? super DeadlineTask> getDeadlineTaskComparator() {
        return workingTaskBook.getDeadlineTaskComparator();
    }

    @Override
    public void setDeadlineTaskComparator(Comparator<? super DeadlineTask> comparator) {
        workingTaskBook.setDeadlineTaskComparator(comparator);
        setTaskSelect(Optional.empty());
        indicateUIChanged();
    }

    //// Event tasks

    @Override
    public synchronized int addEventTask(EventTask eventTask) {
        final int workingIndex = workingTaskBook.addEventTask(eventTask);
        indicateTaskBookChanged();
        setTaskSelect(Optional.of(new TaskSelect(TaskType.EVENT, workingIndex)));
        indicateUIChanged();
        return workingIndex;
    }

    @Override
    public synchronized EventTask getEventTask(int workingIndex) throws IllegalValueException {
        return workingTaskBook.getEventTask(workingIndex);
    }

    @Override
    public synchronized EventTask removeEventTask(int workingIndex) throws IllegalValueException {
        final EventTask removedEvent = workingTaskBook.removeEventTask(workingIndex);
        indicateTaskBookChanged();
        if (getTaskSelect().equals(Optional.of(new TaskSelect(TaskType.EVENT, workingIndex)))) {
            setTaskSelect(Optional.empty());
        }
        indicateUIChanged();
        return removedEvent;
    }

    @Override
    public synchronized void removeEventTasks(TaskPredicate predicate) {
        workingTaskBook.removeEventTasks(predicate);
        indicateTaskBookChanged();
        setTaskSelect(Optional.empty());
        indicateUIChanged();
    }

    @Override
    public synchronized void setEventTask(int workingIndex, EventTask newEventTask)
            throws IllegalValueException {
        workingTaskBook.setEventTask(workingIndex, newEventTask);
        indicateTaskBookChanged();
        setTaskSelect(Optional.of(new TaskSelect(TaskType.EVENT, workingIndex)));
        indicateUIChanged();
    }

    @Override
    public ObservableList<IndexedItem<EventTask>> getEventTaskList() {
        return workingTaskBook.getEventTaskList();
    }

    @Override
    public Comparator<? super EventTask> getEventTaskComparator() {
        return workingTaskBook.getEventTaskComparator();
    }

    @Override
    public void setEventTaskComparator(Comparator<? super EventTask> comparator) {
        workingTaskBook.setEventTaskComparator(comparator);
        setTaskSelect(Optional.empty());
        indicateUIChanged();
    }

    ////undo redo

    @Override
    public Commit undo() throws HeadAtBoundaryException {
        if (head <= 0) {
            throw new HeadAtBoundaryException();
        }
        final Commit undoneCommit = commits.get(head);
        head--;
        Commit commit = commits.get(head);
        workingTaskBook.resetData(commit.workingTaskBook);
        config.resetData(commit.config);
        setTaskSelect(commit.taskSelect);
        return undoneCommit;
    }

    @Override
    public Commit recordState(String name) {
        assert name != null;
        //clear redoable, which are the commits above head
        commits.subList(head + 1, commits.size()).clear();
        final Commit newCommit = new Commit(name, workingTaskBook, getConfig(), getTaskSelect());
        commits.add(newCommit);
        head = commits.size() - 1;
        return newCommit;
    }

    @Override
    public Commit redo() throws HeadAtBoundaryException {
        if (head >= commits.size() - 1) {
            throw new HeadAtBoundaryException();
        }
        head++;
        Commit commit = commits.get(head);
        workingTaskBook.resetData(commit.workingTaskBook);
        config.resetData(commit.config);
        setTaskSelect(commit.taskSelect);
        return commit;
    }

    /**
     * check if taskBook has changed.
     * @return true if TaskBook changed
     */
    @Override
    public boolean hasUncommittedChanges() {
        return !getTaskBook().equals(commits.get(head).getTaskBook())
                || !Objects.equals(getConfig(), commits.get(head).config);
    }

    private class Commit implements Model.Commit {
        private String name;
        private final WorkingTaskBook workingTaskBook;
        private final Config config;
        private final Optional<TaskSelect> taskSelect;

        private Commit(String name, WorkingTaskBook workingTaskBook, ReadOnlyConfig config,
                       Optional<TaskSelect> taskSelect) {
            this.name = name;
            this.workingTaskBook = new WorkingTaskBook(workingTaskBook);
            this.config = new Config(config);
            this.taskSelect = taskSelect;
        }

        @Override
        public String getName() {
            return name;
        }

        private ReadOnlyTaskBook getTaskBook() {
            return workingTaskBook.getTaskBook();
        }

        @Override
        public boolean equals(Object other) {
            return other == this
                   || (other instanceof Commit
                   && name.equals(((Commit)other).name)
                   && workingTaskBook.equals(((Commit)other).workingTaskBook)
                   );
        }
    }

    public class HeadAtBoundaryException extends Exception {
        public HeadAtBoundaryException() {
            super();
        }
    }
}
