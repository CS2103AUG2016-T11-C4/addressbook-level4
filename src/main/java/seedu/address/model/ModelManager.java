package seedu.address.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.MappedList;
import seedu.address.logic.commands.Command;
import seedu.address.model.config.Config;
import seedu.address.model.config.ReadOnlyConfig;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Config config;
    private final TaskBook taskBook;

    private final ItemMappingList<FloatingTask> filteredFloatingTasks;
    private final ItemMappingList<DeadlineTask> filteredDeadlineTasks;
    private final ItemMappingList<EventTask> filteredEventTasks;

    //for undo
    private ArrayList<Commit> commits = new ArrayList<Commit>();
    private int head; //head points to a the current commit which holds the TaskBook displayed by the UI

    /**
     * Initializes a ModelManager with the given config and TaskBook
     * TaskBook and its variables should not be null
     */
    public ModelManager(ReadOnlyConfig config, ReadOnlyTaskBook taskBook) {
        super();
        assert taskBook != null;

        logger.fine("Initializing with config: " + config + " and task book: " + taskBook);

        this.config = new Config(config);
        this.taskBook = new TaskBook(taskBook);
        this.filteredFloatingTasks = new ItemMappingList<>(this.taskBook.getFloatingTasks());
        this.filteredDeadlineTasks = new ItemMappingList<>(this.taskBook.getDeadlineTasks());
        this.filteredEventTasks = new ItemMappingList<>(this.taskBook.getEventTasks());
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
        filteredFloatingTasks.repopulate();
        filteredDeadlineTasks.repopulate();
        filteredEventTasks.repopulate();
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

    //// Floating tasks

    @Override
    public synchronized void addFloatingTask(FloatingTask floatingTask) {
        taskBook.addFloatingTask(floatingTask);
        filteredFloatingTasks.add(floatingTask, taskBook.getFloatingTasks().size() - 1);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized FloatingTask getFloatingTask(int indexInFilteredList) throws IllegalValueException {
        return filteredFloatingTasks.get(indexInFilteredList);
    }

    @Override
    public synchronized FloatingTask removeFloatingTask(int indexInFilteredList) throws IllegalValueException {
        final int sourceIndex = filteredFloatingTasks.getSourceIndex(indexInFilteredList);
        final FloatingTask removedFloating = taskBook.removeFloatingTask(sourceIndex);
        filteredFloatingTasks.remove(indexInFilteredList);
        indicateTaskBookChanged();
        return removedFloating;
    }

    @Override
    public synchronized void removeFloatingTask(Predicate<FloatingTask> predicate) {
        final Collection<FloatingTask> toRemove = taskBook.removeFloatingTask(predicate);
        filteredFloatingTasks.removeAll(toRemove);
        indicateTaskBookChanged();
        return;
    }

    @Override
    public synchronized void setFloatingTask(int indexInFilteredList, FloatingTask newFloatingTask)
            throws IllegalValueException {
        final int sourceIndex = filteredFloatingTasks.getSourceIndex(indexInFilteredList);
        taskBook.setFloatingTask(sourceIndex, newFloatingTask);
        filteredFloatingTasks.set(indexInFilteredList, newFloatingTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<Optional<FloatingTask>> getFilteredFloatingTaskList() {
        return filteredFloatingTasks.getObservableListView();
    }

    @Override
    public void setFloatingTaskFilter(Predicate<? super FloatingTask> predicate) {
        filteredFloatingTasks.setFilter(predicate);
    }

    @Override
    public void setFloatingTaskSortComparator(Comparator<? super FloatingTask> comparator) {
        filteredFloatingTasks.setSortComparator(comparator);
    }

    //// Deadline tasks

    @Override
    public synchronized void addDeadlineTask(DeadlineTask deadlineTask) {
        assert deadlineTask.isFinished() == false;
        taskBook.addDeadlineTask(deadlineTask);
        filteredDeadlineTasks.add(deadlineTask, taskBook.getDeadlineTasks().size() - 1);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized DeadlineTask getDeadlineTask(int indexInFilteredList) throws IllegalValueException {
        return filteredDeadlineTasks.get(indexInFilteredList);
    }

    @Override
    public synchronized DeadlineTask removeDeadlineTask(int indexInFilteredList) throws IllegalValueException {
        final int sourceIndex = filteredDeadlineTasks.getSourceIndex(indexInFilteredList);
        final DeadlineTask removedDeadline = taskBook.removeDeadlineTask(sourceIndex);
        filteredDeadlineTasks.remove(indexInFilteredList);
        indicateTaskBookChanged();
        return removedDeadline;
    }

    @Override
    public synchronized void removeDeadlineTask(Predicate<DeadlineTask> predicate) {
        final Collection<DeadlineTask> toRemove = taskBook.removeDeadlineTask(predicate);
        filteredDeadlineTasks.removeAll(toRemove);
        indicateTaskBookChanged();
        return;
    }

    @Override
    public synchronized void setDeadlineTask(int indexInFilteredList, DeadlineTask newDeadlineTask)
            throws IllegalValueException {
        final int sourceIndex = filteredDeadlineTasks.getSourceIndex(indexInFilteredList);
        taskBook.setDeadlineTask(sourceIndex, newDeadlineTask);
        filteredDeadlineTasks.set(indexInFilteredList, newDeadlineTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<Optional<DeadlineTask>> getFilteredDeadlineTaskList() {
        return filteredDeadlineTasks.getObservableListView();
    }

    @Override
    public void setDeadlineTaskFilter(Predicate<? super DeadlineTask> predicate) {
        filteredDeadlineTasks.setFilter(predicate);
    }

    @Override
    public void setDeadlineTaskSortComparator(Comparator<? super DeadlineTask> comparator) {
        filteredDeadlineTasks.setSortComparator(comparator);
    }

    //// Event tasks

    @Override
    public synchronized void addEventTask(EventTask eventTask) {
        taskBook.addEventTask(eventTask);
        filteredEventTasks.add(eventTask, taskBook.getEventTasks().size() - 1);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized EventTask getEventTask(int indexInFilteredList) throws IllegalValueException {
        return filteredEventTasks.get(indexInFilteredList);
    }

    @Override
    public synchronized EventTask removeEventTask(int indexInFilteredList) throws IllegalValueException {
        final int sourceIndex = filteredEventTasks.getSourceIndex(indexInFilteredList);
        final EventTask removedEvent = taskBook.removeEventTask(sourceIndex);
        filteredEventTasks.remove(indexInFilteredList);
        indicateTaskBookChanged();
        return removedEvent;
    }

    @Override
    public synchronized void removeEventTask(Predicate<EventTask> predicate) {
        final Collection<EventTask> toRemove = taskBook.removeEventTask(predicate);
        filteredEventTasks.removeAll(toRemove);
        indicateTaskBookChanged();
        return;
    }

    @Override
    public synchronized void setEventTask(int indexInFilteredList, EventTask newEventTask)
            throws IllegalValueException {
        final int sourceIndex = filteredEventTasks.getSourceIndex(indexInFilteredList);
        taskBook.setEventTask(sourceIndex, newEventTask);
        filteredEventTasks.set(indexInFilteredList, newEventTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<Optional<EventTask>> getFilteredEventTaskList() {
        return filteredEventTasks.getObservableListView();
    }

    @Override
    public void setEventTaskFilter(Predicate<? super EventTask> predicate) {
        filteredEventTasks.setFilter(predicate);
    }

    @Override
    public void setEventTaskSortComparator(Comparator<? super EventTask> comparator) {
        filteredEventTasks.setSortComparator(comparator);
    }

    private static class ItemMapping<E> {
        final Optional<E> value;
        Optional<Integer> sourceIndex;

        ItemMapping(Optional<E> value, Optional<Integer> sourceIndex) {
            this.value = value;
            this.sourceIndex = sourceIndex;
        }

        ItemMapping(E value, int sourceIndex) {
            this(Optional.of(value), Optional.of(sourceIndex));
        }

        static <E> ItemMapping<E> empty() {
            return new ItemMapping<E>(Optional.empty(), Optional.empty());
        }
    }

    private static class ItemMappingList<E> {
        private final ObservableList<E> sourceList;
        private final ObservableList<ItemMapping<E>> filteredList;
        private Predicate<? super E> filter;
        private Comparator<? super E> sortComparator;

        ItemMappingList(ObservableList<E> sourceList) {
            this.sourceList = sourceList;
            this.filteredList = FXCollections.observableArrayList();
            repopulate();
        }

        ObservableList<Optional<E>> getObservableListView() {
            return new MappedList<>(filteredList, itemMapping -> itemMapping.value);
        }

        void setFilter(Predicate<? super E> filter) {
            this.filter = filter;
            repopulate();
        }

        void setSortComparator(Comparator<? super E> comparator) {
            this.sortComparator = comparator;
            repopulate();
        }

        int getSourceIndex(int filteredIndex) throws IllegalValueException {
            try {
                return filteredList.get(filteredIndex).sourceIndex.get();
            } catch (IndexOutOfBoundsException | NoSuchElementException e) {
                throw new IllegalValueException("invalid index");
            }
        }

        E get(int filteredIndex) throws IllegalValueException {
            try {
                return filteredList.get(filteredIndex).value.get();
            } catch (IndexOutOfBoundsException | NoSuchElementException e) {
                throw new IllegalValueException("invalid index");
            }
        }

        void set(int filteredIndex, E item) throws IllegalValueException {
            final int sourceIndex = getSourceIndex(filteredIndex);
            try {
                filteredList.set(filteredIndex, new ItemMapping<>(item, sourceIndex));
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalValueException("invalid index");
            }
        }

        void add(E item, int sourceIndex) {
            filteredList.add(new ItemMapping<>(item, sourceIndex));
        }

        void remove(int filteredIndex) throws IllegalValueException {
            int deletedSourceIndex = getSourceIndex(filteredIndex);
            filteredList.set(filteredIndex, ItemMapping.empty());
            // Adjust mapping of sourceIndexes
            for (ItemMapping<E> item : filteredList) {
                if (item.sourceIndex.isPresent() && item.sourceIndex.get() > deletedSourceIndex) {
                    item.sourceIndex = Optional.of(item.sourceIndex.get() - 1);
                }
            }
        }

        void removeAll(Collection<E> toRemove) {
            for (E item : toRemove) {
                if (filteredList.contains(item)) {
                    filteredList.set(filteredList.indexOf(item), ItemMapping.empty());
                }
            }
            for (ItemMapping<E> item : filteredList) {
                if (item.sourceIndex.isPresent()) {
                    item.sourceIndex = Optional.of(item.sourceIndex.get() - 1);
                }
            }
        }

        void repopulate() {
            ArrayList<ItemMapping<E>> newFilteredList = new ArrayList<>();
            for (int i = 0; i < sourceList.size(); i++) {
                final E item = sourceList.get(i);
                if (filter != null && !filter.test(item)) {
                    continue;
                }
                newFilteredList.add(new ItemMapping<E>(item, i));
            }
            if (sortComparator != null) {
                newFilteredList.sort((a, b) -> sortComparator.compare(a.value.get(), b.value.get()));
            }
            filteredList.setAll(newFilteredList);
        }
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
        resetTaskBook(commit.getTaskBook());
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
        resetTaskBook(commit.getTaskBook());
        return commit.getCommand();
    }

    /**
     * check if taskBook has changed.
     * @return true if TaskBook changed
     */
    @Override
    public boolean hasUncommittedChanges() {
        return !(this.taskBook.equals(commits.get(head).getTaskBook()));
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
