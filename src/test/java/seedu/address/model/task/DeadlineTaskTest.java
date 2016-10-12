package seedu.address.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.time.TypicalLocalDateTimes.UNIX_EPOCH;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class DeadlineTaskTest {
    private DeadlineTask deadlineTask;

    @Before
    public void setupDeadlineTask() throws Exception {
        final Name name = new Name("Deadline Task Name");
        final LocalDateTime due = UNIX_EPOCH.plusDays(1);
        deadlineTask = new DeadlineTask(name, due);
    }

    @Test
    public void getDue_returnsDue() throws Exception {
        final LocalDateTime expected = UNIX_EPOCH.plusDays(1);
        final LocalDateTime actual = deadlineTask.getDue();
        assertEquals(expected, actual);
    }

    @Test
    public void equals_isEqual_returnsTrue() throws Exception {
        final DeadlineTask other = new DeadlineTask("Deadline Task Name", UNIX_EPOCH.plusDays(1));
        assertTrue(deadlineTask.equals(other));
        assertTrue(deadlineTask.hashCode() == other.hashCode());
    }

    @Test
    public void equals_notEqual_returnsFalse() throws Exception {
        final DeadlineTask other = new DeadlineTask(new Name("Event Task Name"), UNIX_EPOCH.plusDays(2));
        assertFalse(deadlineTask.equals(other));
        assertFalse(deadlineTask.hashCode() == other.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        final String expected = "DeadlineTask[name=Deadline Task Name, "
                                + "due=1970-01-02T00:00]";
        final String actual = deadlineTask.toString();
        assertEquals(expected, actual);
    }
}
