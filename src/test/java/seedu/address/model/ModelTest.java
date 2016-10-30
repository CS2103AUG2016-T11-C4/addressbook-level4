package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ClearAllCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandException;
import seedu.address.model.Model.Commit;
import seedu.address.model.ModelManager.HeadAtBoundaryException;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.TaskSelect;
import seedu.address.model.task.TaskType;
import seedu.address.model.task.TypicalDeadlineTasks;
import seedu.address.model.task.TypicalEventTasks;
import seedu.address.model.task.TypicalFloatingTasks;

public class ModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TypicalFloatingTasks tpflt = new TypicalFloatingTasks();

    private TypicalEventTasks tpent = new TypicalEventTasks();

    private TypicalDeadlineTasks tpdue = new TypicalDeadlineTasks();

    private Model model;

    @Before
    public void setupModel() {
        model = new ModelManager();
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), model.getFloatingTaskList());
        assertEquals(Collections.emptyList(), model.getEventTaskList());
        assertEquals(Collections.emptyList(), model.getDeadlineTaskList());
        assertEquals(WorkingTaskBook.DEFAULT_FLOATING_TASK_COMPARATOR, model.getFloatingTaskComparator());
        assertEquals(WorkingTaskBook.DEFAULT_DEADLINE_TASK_COMPARATOR, model.getDeadlineTaskComparator());
        assertEquals(WorkingTaskBook.DEFAULT_EVENT_TASK_COMPARATOR, model.getEventTaskComparator());
        assertEquals(Optional.empty(), model.getTaskSelect());
        assertFalse(model.hasUncommittedChanges());
        assertEquals(model.getTaskBookFilePath(), "data/taskbook.json");
    }

    @Test
    public void addFloatingTask_appendsFloatingTask() throws Exception {
        assertEquals(1, model.addFloatingTask(tpflt.readABook));
        assertEquals(tpflt.readABook, model.getFloatingTask(1));
        assertEquals(2, model.addFloatingTask(tpflt.buyAHelicopter));
        // Given the corresponding workindIndexes, getFloatingTask() returns the tasks we expect
        assertEquals(tpflt.readABook, model.getFloatingTask(1));
        assertEquals(tpflt.buyAHelicopter, model.getFloatingTask(2));
        // However, the list itself is sorted according to DEFAULT_FLOATING_TASK_COMPARATOR
        List<TestIndexedItem<FloatingTask>> expected = new TestIndexedItemListBuilder<FloatingTask>()
                                                        .add(2, tpflt.buyAHelicopter)
                                                        .add(1, tpflt.readABook)
                                                        .build();
        assertEquals(expected, model.getFloatingTaskList());
        // Task selection is on the most recently added task
        assertEquals(Optional.of(new TaskSelect(TaskType.FLOAT, 2)), model.getTaskSelect());
    }

    @Test
    public void getFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getFloatingTask(0);
    }

    @Test
    public void removeFloatingTask_emptiesIndexInFilteredList() throws Exception {
        assertEquals(1, model.addFloatingTask(tpflt.readABook));
        assertEquals(2, model.addFloatingTask(tpflt.buyAHelicopter));
        model.setFloatingTaskPredicate(floatingTask -> floatingTask.equals(tpflt.buyAHelicopter));
        assertEquals(tpflt.buyAHelicopter, model.removeFloatingTask(1));
        model.setFloatingTaskPredicate(null);
        assertEquals(Arrays.asList(tpflt.readABook), unindexList(model.getFloatingTaskList()));
    }

    @Test
    public void removeFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeFloatingTask(1);
    }

    @Test
    public void setFloatingTask_withNewItemThatChangesListPosition_retainsWorkingIndex() throws Exception {
        List<TestIndexedItem<FloatingTask>> expected;
        // We use a comparator that sorts by name only
        model.setFloatingTaskComparator((a, b) -> a.getName().toString().compareTo(b.getName().toString()));
        assertEquals(1, model.addFloatingTask(tpflt.eatAnApple));
        assertEquals(2, model.addFloatingTask(tpflt.readABook));
        expected = new TestIndexedItemListBuilder<FloatingTask>()
                .add(1, tpflt.eatAnApple)
                .add(2, tpflt.readABook)
                .build();
        assertEquals(expected, model.getFloatingTaskList());
        // Now we set item 2 to something that will push it to the top of the list (because of the sorting)
        // However, the working index is still the same.
        model.setFloatingTask(2, tpflt.buyAHelicopter);
        expected = new TestIndexedItemListBuilder<FloatingTask>()
                .add(2, tpflt.buyAHelicopter)
                .add(1, tpflt.eatAnApple)
                .build();
        assertEquals(expected, model.getFloatingTaskList());
        // Task selection is on the most recently edited task
        assertEquals(Optional.of(new TaskSelect(TaskType.FLOAT, 2)), model.getTaskSelect());
    }

    @Test
    public void setFloatingTask_replacesIndexInFilteredList() throws Exception {
        assertEquals(1, model.addFloatingTask(tpflt.readABook));
        assertEquals(2, model.addFloatingTask(tpflt.buyAHelicopter));
        model.setFloatingTaskPredicate(floatingTask -> floatingTask.equals(tpflt.buyAHelicopter));
        model.setFloatingTask(1, tpflt.readABook);
        model.setFloatingTaskPredicate(null);
        assertEquals(Arrays.asList(tpflt.readABook, tpflt.readABook), unindexList(model.getFloatingTaskList()));
    }

    @Test
    public void setFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setFloatingTask(1, tpflt.readABook);
    }

    @Test
    public void addEventTask_appendsEventTask() throws Exception {
        assertEquals(1, model.addEventTask(tpent.launchNuclearWeapons));
        assertEquals(tpent.launchNuclearWeapons, model.getEventTask(1));
        assertEquals(2, model.addEventTask(tpent.lunchWithBillGates));
        // Given the corresponding working indexes, getEventTask() returns the tasks we expect.
        assertEquals(tpent.launchNuclearWeapons, model.getEventTask(1));
        assertEquals(tpent.lunchWithBillGates, model.getEventTask(2));
        // However, their actual position in the list depends on DEFAULT_EVENT_TASK_COMPARATOR
        List<TestIndexedItem<EventTask>> expected = new TestIndexedItemListBuilder<EventTask>()
                .add(2, tpent.lunchWithBillGates)
                .add(1, tpent.launchNuclearWeapons)
                .build();
        assertEquals(expected, model.getEventTaskList());
        // Task selection is on the most recently added task
        assertEquals(Optional.of(new TaskSelect(TaskType.EVENT, 2)), model.getTaskSelect());
    }

    @Test
    public void getEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getEventTask(1);
    }

    @Test
    public void removeEventTask_removesIndexInFilteredList() throws Exception {
        model.addEventTask(tpent.lunchWithBillGates);
        model.addEventTask(tpent.launchNuclearWeapons);
        model.setEventTaskPredicate(eventTask -> eventTask.equals(tpent.launchNuclearWeapons));
        assertEquals(tpent.launchNuclearWeapons, model.removeEventTask(1));
        model.setEventTaskPredicate(null);
        assertEquals(Arrays.asList(tpent.lunchWithBillGates), unindexList(model.getEventTaskList()));
    }

    @Test
    public void removeEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeEventTask(1);
    }

    @Test
    public void setEventTask_withNewItemThatChangesListPosition_retainsWorkingIndex() throws Exception {
        List<TestIndexedItem<EventTask>> expected;
        // We use a comparator that sorts by name only
        model.setEventTaskComparator((a, b) -> a.getName().toString().compareTo(b.getName().toString()));
        assertEquals(1, model.addEventTask(tpent.launchNuclearWeapons));
        assertEquals(2, model.addEventTask(tpent.lunchWithBillGates));
        expected = new TestIndexedItemListBuilder<EventTask>()
                .add(1, tpent.launchNuclearWeapons)
                .add(2, tpent.lunchWithBillGates)
                .build();
        assertEquals(expected, model.getEventTaskList());
        // Now we set item 2 to something that will push it to the top of the list (because of the sorting)
        // However, the working index is still the same.
        model.setEventTask(2, tpent.doHomework);
        expected = new TestIndexedItemListBuilder<EventTask>()
                .add(2, tpent.doHomework)
                .add(1, tpent.launchNuclearWeapons)
                .build();
        assertEquals(expected, model.getEventTaskList());
        // Task selection is on the edited task
        assertEquals(Optional.of(new TaskSelect(TaskType.EVENT, 2)), model.getTaskSelect());
    }

    @Test
    public void setEventTask_replacesIndexInFilteredList() throws Exception {
        assertEquals(1, model.addEventTask(tpent.lunchWithBillGates));
        assertEquals(2, model.addEventTask(tpent.launchNuclearWeapons));
        model.setEventTaskPredicate(eventTask -> eventTask.equals(tpent.launchNuclearWeapons));
        model.setEventTask(1, tpent.lunchWithBillGates);
        model.setEventTaskPredicate(null);
        assertEquals(Arrays.asList(tpent.lunchWithBillGates, tpent.lunchWithBillGates),
                    unindexList(model.getEventTaskList()));
    }

    @Test
    public void setEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setEventTask(1, tpent.lunchWithBillGates);
    }

    @Test
    public void addDeadlineTask_appendsDeadlineTask() throws Exception {
        assertEquals(1, model.addDeadlineTask(tpdue.assembleTheMissiles));
        assertEquals(tpdue.assembleTheMissiles, model.getDeadlineTask(1));
        assertEquals(2, model.addDeadlineTask(tpdue.speechTranscript));
        // Given their corresponding working indexes, getDeadlineTask() returns the tasks we expect.
        assertEquals(tpdue.assembleTheMissiles, model.getDeadlineTask(1));
        assertEquals(tpdue.speechTranscript, model.getDeadlineTask(2));
        // However, their actual position in the list depends on DEFAULT_DEADLINE_TASK_COMPARATOR
        List<TestIndexedItem<DeadlineTask>> expected = new TestIndexedItemListBuilder<DeadlineTask>()
                .add(2, tpdue.speechTranscript)
                .add(1, tpdue.assembleTheMissiles)
                .build();
        assertEquals(expected, model.getDeadlineTaskList());
    }

    @Test
    public void getDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getDeadlineTask(1);
    }

    @Test
    public void removeDeadlineTask_removesIndexInFilteredList() throws Exception {
        assertEquals(1, model.addDeadlineTask(tpdue.speechTranscript));
        assertEquals(2, model.addDeadlineTask(tpdue.assembleTheMissiles));
        model.setDeadlineTaskPredicate(deadlineTask -> deadlineTask.equals(tpdue.assembleTheMissiles));
        assertEquals(tpdue.assembleTheMissiles, model.removeDeadlineTask(1));
        model.setDeadlineTaskPredicate(null);
        assertEquals(Arrays.asList(tpdue.speechTranscript), unindexList(model.getDeadlineTaskList()));
    }

    @Test
    public void removeDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeDeadlineTask(1);
    }

    @Test
    public void setDeadlineTask_withNewItemThatChangesListPosition_retainsWorkingIndex() throws Exception {
        List<TestIndexedItem<DeadlineTask>> expected;
        // We use a comparator that sorts by name only
        model.setDeadlineTaskComparator((a, b) -> a.getName().toString().compareTo(b.getName().toString()));
        assertEquals(1, model.addDeadlineTask(tpdue.completeHomework));
        assertEquals(2, model.addDeadlineTask(tpdue.speechTranscript));
        expected = new TestIndexedItemListBuilder<DeadlineTask>()
                .add(1, tpdue.completeHomework)
                .add(2, tpdue.speechTranscript)
                .build();
        assertEquals(expected, model.getDeadlineTaskList());
        // Now we set item 2 to something that will push it to the top of the list (because of the sorting)
        // However, the working index is still the same.
        model.setDeadlineTask(2, tpdue.assembleTheMissiles);
        expected = new TestIndexedItemListBuilder<DeadlineTask>()
                .add(2, tpdue.assembleTheMissiles)
                .add(1, tpdue.completeHomework)
                .build();
        assertEquals(expected, model.getDeadlineTaskList());
        // Task selection is on the edited task
        assertEquals(Optional.of(new TaskSelect(TaskType.DEADLINE, 2)), model.getTaskSelect());
    }

    @Test
    public void setDeadlineTask_replacesIndexInFilteredList() throws Exception {
        assertEquals(1, model.addDeadlineTask(tpdue.speechTranscript));
        assertEquals(2, model.addDeadlineTask(tpdue.assembleTheMissiles));
        model.setDeadlineTaskPredicate(deadlineTask -> deadlineTask.equals(tpdue.assembleTheMissiles));
        model.setDeadlineTask(1, tpdue.speechTranscript);
        model.setDeadlineTaskPredicate(null);
        assertEquals(Arrays.asList(tpdue.speechTranscript, tpdue.speechTranscript),
                unindexList(model.getDeadlineTaskList()));
    }

    @Test
    public void setDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setDeadlineTask(1, tpdue.speechTranscript);
    }

    @Test
    public void undo_redo_throws_HeadAtBoundaryException() throws HeadAtBoundaryException {
        thrown.expect(HeadAtBoundaryException.class);
        model.undo();

        thrown.expect(HeadAtBoundaryException.class);
        model.redo();
    }

    @Test
    public void hasUncommitedChanges_manages_clear() throws CommandException {
        //check for empty taskbook
        assertEquals(new TaskBook(), model.getTaskBook());

        //clear an empty taskbook
        Command clear = new ClearAllCommand();
        clear.execute(model);
        assertFalse(model.hasUncommittedChanges());

        //clear a non-empty taskbook
        model.addFloatingTask(tpflt.buyAHelicopter);
        model.recordState("dummy command");
        clear.execute(model);
        assertTrue(model.hasUncommittedChanges());
    }

    @Test
    public void recordStateAndUndo_Redo_properlyUndosConsecutiveAdds() throws Exception {
        //create expected TaskBook
        TaskBook dummybook = new TaskBook();

        //Model task book is initially empty
        assertEquals(dummybook, model.getTaskBook());

        //We 'execute' command1
        model.addFloatingTask(tpflt.buyAHelicopter);
        final Commit command1Commit = model.recordState("command1");

        //update expected TaskBook
        dummybook.addFloatingTask(tpflt.buyAHelicopter);
        assertEquals(dummybook, model.getTaskBook());

        //We 'execute' command2
        model.addFloatingTask(tpflt.readABook);
        final Commit command2Commit = model.recordState("command2");

        //update expected TaskBook
        dummybook.addFloatingTask(tpflt.readABook);
        assertEquals(dummybook, model.getTaskBook());

        //We 'execute' command3
        model.addEventTask(tpent.launchNuclearWeapons);
        final Commit command3Commit = model.recordState("command3");

        //update expected TaskBook
        dummybook.addEventTask(tpent.launchNuclearWeapons);
        TaskBook dummybook3 = new TaskBook(dummybook); //dummybook3 has float:[buyAHelicopter, readAbook], event:[launchNuclearWeapons]

        assertEquals(dummybook, model.getTaskBook());

        //undo command 3
        assertEquals(command3Commit, model.undo());

        //update expected TaskBook
        dummybook.removeEventTask(0);
        TaskBook dummybook2 = new TaskBook(dummybook); //dummybook2 has float:[buyAHelicopter, readAbook]

        //test
        assertEquals(dummybook, model.getTaskBook());

        //undo command 2
        assertEquals(command2Commit, model.undo());

        //test with hasUncommittedChanges
        assertFalse(model.hasUncommittedChanges());

        //update expected taskbook
        dummybook.removeFloatingTask(1);
        TaskBook dummybook1 = new TaskBook(dummybook); //dummybook1 has float:[buyAHelicopter]

        //test
        assertEquals(dummybook, model.getTaskBook());

        //undo command 1
        assertEquals(command1Commit, model.undo());

        //test with hasUncommittedChanges
        assertFalse(model.hasUncommittedChanges());

        //update expected taskbook
        dummybook.removeFloatingTask(0); //empty taskbook

        //test
        assertEquals(new TaskBook(), dummybook);
        assertEquals(dummybook, model.getTaskBook());

        //consecutive redos
        //redo command1
        assertEquals(command1Commit, model.redo());
        assertEquals(dummybook1, model.getTaskBook());

        //redo command2
        assertEquals(command2Commit, model.redo());
        assertEquals(dummybook2, model.getTaskBook());

        //test with hasUncommittedChanges
        assertFalse(model.hasUncommittedChanges());

        //redo command3
        assertEquals(command3Commit, model.redo());
        assertEquals(dummybook3, model.getTaskBook());

        //no more redos expected
        thrown.expect(HeadAtBoundaryException.class);
        model.redo();
    }

    @Test
    public void recordStateAndUndo_properlyManagesStack() throws Exception {
        // Model task book is initially empty
        assertEquals(new TaskBook(), model.getTaskBook());

        // We "execute" command1, adding a floating task
        model.addFloatingTask(tpflt.buyAHelicopter);
        final Commit command1Commit = model.recordState("command 1");

        // Undo command1
        assertEquals(command1Commit, model.undo());
        assertEquals(new TaskBook(), model.getTaskBook()); // Model task book is back to being empty

        // We "execute" command2, adding an event
        model.addEventTask(tpent.lunchWithBillGates);
        final Commit command2Commit = model.recordState("command 2");

        // Undo command2
        assertEquals(command2Commit, model.undo());
        assertEquals(new TaskBook(), model.getTaskBook()); // Model task book is back to being empty

        // At this point, we should not be able to undo anymore.
        thrown.expect(HeadAtBoundaryException.class);
        model.undo();

        //test with hasUncommittedChanges
        assertFalse(model.hasUncommittedChanges());

        //We "execute" command 3, adding an event
        model.addEventTask(tpent.launchNuclearWeapons);
        final Commit command3Commit = model.recordState("command 3");

        //undo command3
        assertEquals(command3Commit, model.undo());
        assertEquals(new TaskBook(), model.getTaskBook()); // Model task book is back to being empty

        // We "execute" 4, adding a floating task
        model.addFloatingTask(tpflt.buyAHelicopter);
        final Commit command4Commit = model.recordState("command 4");

        // Undo command4
        assertEquals(command4Commit, model.undo());
        assertEquals(new TaskBook(), model.getTaskBook()); // Model task book is back to being empty

        // We "execute" command5, adding an event
        model.addEventTask(tpent.lunchWithBillGates);
        final Commit command5Commit = model.recordState("command 5");

        // Undo command5
        assertEquals(command5Commit, model.undo());
        assertEquals(new TaskBook(), model.getTaskBook()); // Model task book is back to being empty
    }

    @Test
    public void undoRedo_restoresWorkingIndexes() throws Exception {
        List<TestIndexedItem<FloatingTask>> expected1, expected2;
        model.setFloatingTaskComparator((a, b) -> a.getName().toString().compareTo(b.getName().toString()));
        assertEquals(1, model.addFloatingTask(tpflt.readABook));
        assertEquals(2, model.addFloatingTask(tpflt.eatAnApple));
        expected1 = new TestIndexedItemListBuilder<FloatingTask>()
                .add(2, tpflt.eatAnApple)
                .add(1, tpflt.readABook)
                .build();
        assertEquals(expected1, model.getFloatingTaskList());
        model.recordState("add two tasks");

        model.setFloatingTask(1, tpflt.buyAHelicopter);
        expected2 = new TestIndexedItemListBuilder<FloatingTask>()
                                                        .add(1, tpflt.buyAHelicopter)
                                                        .add(2, tpflt.eatAnApple)
                                                        .build();
        assertEquals(expected2, model.getFloatingTaskList());
        model.recordState("edit a task");

        model.undo();
        assertEquals(expected1, model.getFloatingTaskList());

        model.redo();
        assertEquals(expected2, model.getFloatingTaskList());
    }

    @Test
    public void hasUncommittedChanges_withModifiedConfig_returnsTrue() {
        model.setTaskBookFilePath("a");
        assertTrue(model.hasUncommittedChanges());
    }

    @Test
    public void undoRedo_withModifiedConfig_restoresConfig() throws Exception {
        model.setTaskBookFilePath("a");
        model.recordState("modified config");
        model.undo();
        assertEquals("data/taskbook.json", model.getTaskBookFilePath());
        model.redo();
        assertEquals("a", model.getTaskBookFilePath());
    }

    @Test
    public void hasUncommittedChanges_withModifiedTaskSelect_returnsFalse() {
        // Task selection is not counted as something that deserves a commit
        model.setTaskSelect(Optional.of(new TaskSelect(TaskType.EVENT, 1)));
        assertFalse(model.hasUncommittedChanges());
    }

    @Test
    public void undoRedo_withModifiedTaskSelect_restoresTaskSelect() throws Exception {
        model.setTaskSelect(Optional.of(new TaskSelect(TaskType.EVENT, 1)));
        model.recordState("modified task select");
        model.undo();
        assertEquals(Optional.empty(), model.getTaskSelect());
        model.redo();
        assertEquals(Optional.of(new TaskSelect(TaskType.EVENT, 1)), model.getTaskSelect());
    }

    private <E> List<E> unindexList(List<IndexedItem<E>> list) {
        return list.stream().map(x -> x.getItem()).collect(Collectors.toList());
    }
}
