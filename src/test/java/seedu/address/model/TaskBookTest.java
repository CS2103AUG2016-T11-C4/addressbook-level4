package seedu.address.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.TypicalFloatingTasks;

public class TaskBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TaskBook taskBook;

    private static TypicalFloatingTasks typicalFloatingTasks = new TypicalFloatingTasks();

    @Before
    public void setupTaskBook() {
        taskBook = new TaskBook();
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), taskBook.getTasks());
        assertEquals(Collections.emptyList(), taskBook.getFloatingTasks());
    }

    @Test
    public void setFloatingTasks() {
        taskBook.setFloatingTasks(typicalFloatingTasks.getFloatingTasks());
        assertEquals(typicalFloatingTasks.getFloatingTasks(), taskBook.getFloatingTasks());
    }

    @Test
    public void addFloatingTask_appendsToList() {
        final ArrayList<FloatingTask> expected = new ArrayList<>();
        taskBook.addFloatingTask(typicalFloatingTasks.readABook);
        expected.add(typicalFloatingTasks.readABook);
        assertEquals(expected, taskBook.getFloatingTasks());
        taskBook.addFloatingTask(typicalFloatingTasks.buyAHelicopter);
        expected.add(typicalFloatingTasks.buyAHelicopter);
        assertEquals(expected, taskBook.getFloatingTasks());
    }

    @Test
    public void removeFloatingTask_removesFromList() {
        final ArrayList<FloatingTask> expected = new ArrayList<>(typicalFloatingTasks.getFloatingTasks());
        expected.remove(0);
        taskBook.setFloatingTasks(typicalFloatingTasks.getFloatingTasks());
        taskBook.removeFloatingTask(0);
        assertEquals(expected, taskBook.getFloatingTasks());
    }

}