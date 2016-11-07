package seedu.address.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.common.base.MoreObjects;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.MappedList;
import seedu.address.model.compare.DeadlineTaskDueComparator;
import seedu.address.model.compare.EventTaskStartEndComparator;
import seedu.address.model.compare.FloatingTaskPriorityComparator;
import seedu.address.model.filter.TaskPredicate;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;

/**
 * A wrapper around a task book that gives each task a stable index, and adds filtering and sorting of the
 * tasks lists.
 *
 * Having a stable index (called the workingIndex) means that when a task is deleted, or is modified such
 * that its position on the sorted list changes, the indexes of the rest of the tasks will still remain the
 * same.
 */
public class WorkingTaskBook {
    public static final Comparator<FloatingTask> DEFAULT_FLOATING_TASK_COMPARATOR = new FloatingTaskPriorityComparator();
    public static final Comparator<DeadlineTask> DEFAULT_DEADLINE_TASK_COMPARATOR = new DeadlineTaskDueComparator();
    public static final Comparator<EventTask> DEFAULT_EVENT_TASK_COMPARATOR = new EventTaskStartEndComparator();

    private final TaskBook taskBook;
    private final WorkingItemList<FloatingTask> workingFloatingTasks;
    private final WorkingItemList<DeadlineTask> workingDeadlineTasks;
    private final WorkingItemList<EventTask> workingEventTasks;
    private final SimpleObjectProperty<TaskPredicate> taskPredicate = new SimpleObjectProperty<>();

    public WorkingTaskBook(ReadOnlyTaskBook taskBook) {
        this.taskBook = new TaskBook(taskBook);
        this.workingFloatingTasks = new WorkingItemList<>(this.taskBook.getFloatingTasks());
        this.workingFloatingTasks.setComparator(DEFAULT_FLOATING_TASK_COMPARATOR);
        this.workingDeadlineTasks = new WorkingItemList<>(this.taskBook.getDeadlineTasks());
        this.workingDeadlineTasks.setComparator(DEFAULT_DEADLINE_TASK_COMPARATOR);
        this.workingEventTasks = new WorkingItemList<>(this.taskBook.getEventTasks());
        this.workingEventTasks.setComparator(DEFAULT_EVENT_TASK_COMPARATOR);
    }

    public WorkingTaskBook(WorkingTaskBook toBeCopied) {
        this(toBeCopied.taskBook);
        resetData(toBeCopied);
    }

    public ReadOnlyTaskBook getTaskBook() {
        return taskBook;
    }

    public void resetData(ReadOnlyTaskBook toBeCopied) {
        taskBook.resetData(toBeCopied);
        workingFloatingTasks.repopulate();
        workingDeadlineTasks.repopulate();
        workingEventTasks.repopulate();
    }

    public void resetData(WorkingTaskBook toBeCopied) {
        this.taskBook.resetData(toBeCopied.taskBook);
        workingFloatingTasks.resetData(toBeCopied.workingFloatingTasks);
        workingDeadlineTasks.resetData(toBeCopied.workingDeadlineTasks);
        workingEventTasks.resetData(toBeCopied.workingEventTasks);
    }

    /**
     * Returns the {@link TaskPredicate} used to filter tasks in the working lists as a read-only property.
     */
    public ReadOnlyProperty<TaskPredicate> taskPredicateProperty() {
        return taskPredicate;
    }

    /**
     * Returns the {@link TaskPredicate} used to filter tasks in the working lists.
     */
    public TaskPredicate getTaskPredicate() {
        return taskPredicate.get();
    }

    /**
     * Sets the {@link TaskPredicate} used to filter tasks in the working lists.
     * If the filter is null, no filter is applied.
     */
    public void setTaskPredicate(TaskPredicate taskPredicate) {
        workingFloatingTasks.setPredicate(taskPredicate != null ? taskPredicate::test : null);
        workingDeadlineTasks.setPredicate(taskPredicate != null ? taskPredicate::test : null);
        workingEventTasks.setPredicate(taskPredicate != null ? taskPredicate::test : null);
        this.taskPredicate.set(taskPredicate);
    }

    /// Floating tasks

    public int addFloatingTask(FloatingTask floatingTask) {
        taskBook.addFloatingTask(floatingTask);
        return workingFloatingTasks.add(floatingTask, taskBook.getFloatingTasks().size() - 1);
    }

    public FloatingTask getFloatingTask(int workingIndex) throws IllegalValueException {
        return workingFloatingTasks.get(workingIndex);
    }

    public FloatingTask removeFloatingTask(int workingIndex) throws IllegalValueException {
        final int sourceIndex = workingFloatingTasks.getSourceIndex(workingIndex);
        final FloatingTask removedFloating = taskBook.removeFloatingTask(sourceIndex);
        workingFloatingTasks.remove(workingIndex);
        return removedFloating;
    }

    /**
     * Remove all the floating tasks that satisfy the given predicate.
     * @param predicate The filter used to judge whether a floating task needs to be deleted or not.
     * @throws IllegalValueException Will be throw during iteration if internal error happens.
     */
    public void removeFloatingTasks(TaskPredicate predicate) {
        int iterator = 1;
        while (iterator < workingFloatingTasks.size()) {
            FloatingTask toBeTested;
            try {
                toBeTested = workingFloatingTasks.get(iterator);
                if (predicate.test(toBeTested)) {
                    removeFloatingTask(iterator);
                }
            } catch (IllegalValueException e) {
                ; // skip this index if this item is already deleted
            }
            iterator = iterator + 1;
        }
    }

    public void setFloatingTask(int workingIndex, FloatingTask newFloatingTask) throws IllegalValueException {
        final int sourceIndex = workingFloatingTasks.getSourceIndex(workingIndex);
        taskBook.setFloatingTask(sourceIndex, newFloatingTask);
        workingFloatingTasks.set(workingIndex, newFloatingTask);
    }

    public ObservableList<IndexedItem<FloatingTask>> getFloatingTaskList() {
        return workingFloatingTasks.getWorkingItemList();
    }

    /**
     * Returns the comparator used to sort floating tasks in the floating task working list.
     */
    public Comparator<? super FloatingTask> getFloatingTaskComparator() {
        return workingFloatingTasks.getComparator();
    }

    /**
     * Sets the comparator used to sort floating tasks in the floating task working list.
     * The comparator must not be null.
     */
    public void setFloatingTaskComparator(Comparator<? super FloatingTask> comparator) {
        workingFloatingTasks.setComparator(comparator);
    }

    /// Deadline tasks

    public int addDeadlineTask(DeadlineTask deadlineTask) {
        taskBook.addDeadlineTask(deadlineTask);
        return workingDeadlineTasks.add(deadlineTask, taskBook.getDeadlineTasks().size() - 1);
    }

    public DeadlineTask getDeadlineTask(int workingIndex) throws IllegalValueException {
        return workingDeadlineTasks.get(workingIndex);
    }

    public DeadlineTask removeDeadlineTask(int workingIndex) throws IllegalValueException {
        final int sourceIndex = workingDeadlineTasks.getSourceIndex(workingIndex);
        final DeadlineTask removedDeadline = taskBook.removeDeadlineTask(sourceIndex);
        workingDeadlineTasks.remove(workingIndex);
        return removedDeadline;
    }

    /**
     * Remove all the deadline tasks that satisfy the given predicate.
     * @param predicate The filter used to judge whether a deadline task needs to be deleted or not.
     * @throws IllegalValueException Will be throw during iteration if internal error happens.
     */
    public void removeDeadlineTasks(TaskPredicate predicate) {
        int iterator = 1;
        while (iterator < workingDeadlineTasks.size()) {
            DeadlineTask toBeTested;
            try {
                toBeTested = workingDeadlineTasks.get(iterator);
                if (predicate.test(toBeTested)) {
                    removeDeadlineTask(iterator);
                }
            } catch (IllegalValueException e) {
                ; // skip this index if this item is already deleted
            }
            iterator = iterator + 1;
        }
    }

    public void setDeadlineTask(int workingIndex, DeadlineTask newDeadlineTask) throws IllegalValueException {
        final int sourceIndex = workingDeadlineTasks.getSourceIndex(workingIndex);
        taskBook.setDeadlineTask(sourceIndex, newDeadlineTask);
        workingDeadlineTasks.set(workingIndex, newDeadlineTask);
    }

    public ObservableList<IndexedItem<DeadlineTask>> getDeadlineTaskList() {
        return workingDeadlineTasks.getWorkingItemList();
    }

    /**
     * Returns the comparator used to sort deadline tasks in the deadline task working list.
     */
    public Comparator<? super DeadlineTask> getDeadlineTaskComparator() {
        return workingDeadlineTasks.getComparator();
    }

    /**
     * Sets the comparator used to sort deadline tasks in the deadline task working list.
     * The comparator must not be null.
     */
    public void setDeadlineTaskComparator(Comparator<? super DeadlineTask> comparator) {
        workingDeadlineTasks.setComparator(comparator);
    }

    /// Event tasks

    public int addEventTask(EventTask eventTask) {
        taskBook.addEventTask(eventTask);
        return workingEventTasks.add(eventTask, taskBook.getEventTasks().size() - 1);
    }

    public EventTask getEventTask(int workingIndex) throws IllegalValueException {
        return workingEventTasks.get(workingIndex);
    }

    public EventTask removeEventTask(int workingIndex) throws IllegalValueException {
        final int sourceIndex = workingEventTasks.getSourceIndex(workingIndex);
        final EventTask removedEvent = taskBook.removeEventTask(sourceIndex);
        workingEventTasks.remove(workingIndex);
        return removedEvent;
    }

    /**
     * Remove all the events tasks that satisfy the given predicate.
     * @param predicate The filter used to judge whether an event task needs to be deleted or not.
     * @throws IllegalValueException Will be throw during iteration if internal error happens.
     */
    public void removeEventTasks(TaskPredicate predicate) {
        int iterator = 1;
        while (iterator < workingEventTasks.size()) {
            EventTask toBeTested;
            try {
                toBeTested = workingEventTasks.get(iterator);
                if (predicate.test(toBeTested)) {
                    removeEventTask(iterator);
                }
            } catch (IllegalValueException e) {
                ; // skip this index if this item is already deleted
            }
            iterator = iterator + 1;
        }
    }

    public void setEventTask(int workingIndex, EventTask newEventTask) throws IllegalValueException {
        final int sourceIndex = workingEventTasks.getSourceIndex(workingIndex);
        taskBook.setEventTask(sourceIndex, newEventTask);
        workingEventTasks.set(workingIndex, newEventTask);
    }

    public ObservableList<IndexedItem<EventTask>> getEventTaskList() {
        return workingEventTasks.getWorkingItemList();
    }

    /**
     * Returns the comparator used to stort event tasks in the event task working list.
     */
    public Comparator<? super EventTask> getEventTaskComparator() {
        return workingEventTasks.getComparator();
    }

    /**
     * Sets the comparator used to sort event tasks in the event task working list.
     * The comparator must not be null.
     */
    public void setEventTaskComparator(Comparator<? super EventTask> comparator) {
        workingEventTasks.setComparator(comparator);
    }

    /**
     * Pre-conditions: E is immutable.
     */
    private static class WorkingItemList<E> {
        /**
         * The source list which we will generate the workingItemList from when {@link #repopulate()} is called.
         */
        private final ObservableList<E> sourceList;

        /**
         * Our base internal working item list. To keep indexes stable, items are never deleted from this
         * list unless the list is repopulated.
         */
        private final ObservableList<WorkingItem<E>> workingItemList = FXCollections.observableArrayList();

        /**
         * Filters away the "deleted" items from the workingItemList.
         */
        private final FilteredList<WorkingItem<E>> filteredWorkingItemList = new FilteredList<>(workingItemList, x -> x.item.isPresent());

        /**
         * Sorts the filteredWorkingItemList.
         */
        private final SortedList<WorkingItem<E>> sortedWorkingItemList = new SortedList<>(filteredWorkingItemList);

        /**
         * The comparator used to sort the workingItemList
         */
        private Comparator<? super E> comparator;

        /**
         * The predicate used to filter the sourceList in {@link #repopulate()}.
         */
        private Predicate<? super E> predicate;

        private WorkingItemList(ObservableList<E> sourceList) {
            this.sourceList = sourceList;
            repopulate();
        }

        private void resetData(WorkingItemList<E> toBeCopied) {
            this.workingItemList.setAll(toBeCopied.workingItemList);
            this.sortedWorkingItemList.setComparator(toBeCopied.sortedWorkingItemList.getComparator());
            this.comparator = toBeCopied.comparator;
            this.predicate = toBeCopied.predicate;
        }

        private ObservableList<IndexedItem<E>> getWorkingItemList() {
            return new MappedList<IndexedItem<E>, WorkingItem<E>>(sortedWorkingItemList, x -> x);
        }

        private void setPredicate(Predicate<? super E> predicate) {
            this.predicate = predicate;
            repopulate();
        }

        private Comparator<? super E> getComparator() {
            return comparator;
        }

        private void setComparator(Comparator<? super E> comparator) {
            assert comparator != null;
            sortedWorkingItemList.setComparator((a, b) -> {
                return comparator.compare(a.getItem(), b.getItem());
            });
            this.comparator = comparator;
            repopulate();
        }

        private WorkingItem<E> getWorkingItem(int workingIndex) throws IllegalValueException {
            if (workingIndex < 0 || workingIndex >= workingItemList.size()
                    || !workingItemList.get(workingIndex).item.isPresent()) {
                throw new IllegalValueException("index does not exist: " + workingIndex);
            } else {
                return workingItemList.get(workingIndex);
            }
        }

        private int getSourceIndex(int workingIndex) throws IllegalValueException {
            return getWorkingItem(workingIndex).sourceIndex;
        }

        private E get(int workingIndex) throws IllegalValueException {
            return getWorkingItem(workingIndex).getItem();
        }

        private void set(int workingIndex, E item) throws IllegalValueException {
            final int sourceIndex = getSourceIndex(workingIndex);
            workingItemList.set(workingIndex, new WorkingItem<>(workingIndex, item, sourceIndex));
        }

        int add(E item, int sourceIndex) {
            final int newWorkingIndex = workingItemList.size();
            final WorkingItem<E> newWorkingItem = new WorkingItem<>(newWorkingIndex, item, sourceIndex);
            workingItemList.add(newWorkingItem);
            return newWorkingIndex;
        }

        void remove(int workingIndex) throws IllegalValueException {
            final int deletedSourceIndex = getSourceIndex(workingIndex);
            workingItemList.set(workingIndex, new WorkingItem<>());
            // Adjust mapping of sourceIndexes
            for (WorkingItem<E> item : workingItemList) {
                if (item.sourceIndex > deletedSourceIndex) {
                    item.sourceIndex--;
                }
            }
        }

        void repopulate() {
            ArrayList<WorkingItem<E>> newWorkingList = new ArrayList<>();
            // Filter the list (while taking note of the source index)
            for (int i = 0; i < sourceList.size(); i++) {
                final E item = sourceList.get(i);
                if (predicate != null && !predicate.test(item)) {
                    continue;
                }
                newWorkingList.add(new WorkingItem<>(0, item, i));
            }
            // Sort the list using our current comparator
            if (sortedWorkingItemList.getComparator() != null) {
                Collections.sort(newWorkingList, sortedWorkingItemList.getComparator());
            }
            // Add our 0-index empty value (so that indexes start at one)
            newWorkingList.add(0, new WorkingItem<>());
            // Re-index the list
            for (int i = 1; i < newWorkingList.size(); i++) {
                final WorkingItem<E> src = newWorkingList.get(i);
                newWorkingList.set(i, new WorkingItem<>(i, src.getItem(), src.sourceIndex));
            }
            workingItemList.setAll(newWorkingList);
        }

        int size () {
            return workingItemList.size();
        }
    }

    private static class WorkingItem<E> implements IndexedItem<E> {
        private final int workingIndex;
        private final Optional<E> item;
        private int sourceIndex;

        private WorkingItem(int workingIndex, Optional<E> item, int sourceIndex) {
            this.workingIndex = workingIndex;
            this.item = item;
            this.sourceIndex = sourceIndex;
        }

        private WorkingItem(int workingIndex, E item, int sourceIndex) {
            this(workingIndex, Optional.of(item), sourceIndex);
        }

        private WorkingItem() {
            this(-1, Optional.empty(), -1);
        }

        @Override
        public int getWorkingIndex() {
            return workingIndex;
        }

        @Override
        public E getItem() {
            return item.get();
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("workingIndex", workingIndex)
                    .add("item", item)
                    .toString();
        }
    }

}
