package seedu.address.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FloatingTaskTest {
    private FloatingTask floatingTask;

    @Before
    public void setupFloatingTask() throws Exception {
        final Name name = new Name("Floating Task Name");
        Priority priority = new Priority("3");
        floatingTask = new FloatingTask(name, priority);
    }

    @Test
    public void getPriority_returnsDue() throws Exception {
        final Priority expected = new Priority("3");
        final Priority actual = floatingTask.getPriority();
        assertEquals(expected, actual);
    }

    @Test
    public void equals_isEqual_returnsTrue() throws Exception {
        final FloatingTask other = new FloatingTask("Floating Task Name", new Priority("3"));
        assertTrue(floatingTask.equals(other));
        assertTrue(floatingTask.hashCode() == other.hashCode());
    }

    @Test
    public void equals_notEqual_returnsFalse() throws Exception {
        final FloatingTask other = new FloatingTask(new Name("Floating Task Name"), new Priority("1"));
        assertFalse(floatingTask.equals(other));
        assertFalse(floatingTask.hashCode() == other.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        final String expected = "FloatingTask[name=Floating Task Name, "
                                + "priority=3]";
        final String actual = floatingTask.toString();
        assertEquals(expected, actual);
    }
}
