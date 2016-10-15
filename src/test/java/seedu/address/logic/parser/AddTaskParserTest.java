package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.time.TypicalLocalDateTimes.PI_DAY;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddTaskCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Priority;

public class AddTaskParserTest {
    private AddTaskParser parser;

    @Before
    public void setupParser() {
        parser = new AddTaskParser(PI_DAY);
    }

    @Test
    public void parseAddEventCommand() {
        // All arguments provided
        assertAddEventParse("\"a\" 1/2/2000 4:30am to 2/3/2100 6:32pm", "a", LocalDateTime.of(2000, 2, 1, 4, 30),
                    LocalDateTime.of(2100, 3, 2, 18, 32));

        // If startDate is not given, then it is the current date
        assertAddEventParse("\"a\" 7:23am to 4/6/2654 8:24pm", "a", LocalDateTime.of(2653, 9, 15, 7, 23),
                LocalDateTime.of(2654, 6, 4, 20, 24));

        // If startTime is not given, then it is 12am
        assertAddEventParse("\"a\" 3/4/2015 to 4/5/2015 7pm", "a", LocalDateTime.of(2015, 4, 3, 0, 0),
                LocalDateTime.of(2015, 5, 4, 19, 0));

        // If endDate is not given, then it is the same as startDate
        assertAddEventParse("\"a\" 1/2/2000 5:41am to 6:32pm", "a", LocalDateTime.of(2000, 2, 1, 5, 41),
                LocalDateTime.of(2000, 2, 1, 18, 32));

        // If endTime is not given, then it is 11.59pm
        assertAddEventParse("\"a\" 8/7/2014 12:45am to 9/7/2014", "a",
                LocalDateTime.of(2014, 7, 8, 0, 45), LocalDateTime.of(2014, 7, 9, 23, 59));

        // Must have at least one of { startDate, startTime }, and at least one of { endDate, endTime }
        assertIncorrect("\"a\" 1/2/2000 4:30am to");
        assertIncorrect("\"a\" to 1/2/2000 4:30am to");
        assertIncorrect("");

        // Too many arguments
        assertIncorrect("\"a\" 4/5/2016 7:23am 6/7/2017 8:42pm extraArg");
    }

    @Test
    public void parseAddDeadlineCommand() {
        // All arguments provided
        assertAddDeadlineParse("\"a\" 1/2/2000 4:30am", "a", LocalDateTime.of(2000, 2, 1, 4, 30));

        // If date is not given, then it is the current date
        assertAddDeadlineParse("\"a\" 9:56am", "a", LocalDateTime.of(2653, 9, 15, 9, 56));

        // If time is not given, then it is 11:59pm
        assertAddDeadlineParse("\"a\" 3/4/2015", "a", LocalDateTime.of(2015, 4, 3, 23, 59));

        // Must have at least one of { date, time }
        assertIncorrect("");

        // Too many arguments
        assertIncorrect("\"a\" 8/1/2012 1:49am extraArg");
    }

    @Test
    public void parseAddFloatingTaskCommand() {
        // All arguments provided
        assertAddFloatingTaskParse("\"a\" p-3", "a", "3");

        // If priority is not given, then it default "0"
        assertAddFloatingTaskParse("\"a\"", "a", "0");

        // Too many arguments
        assertIncorrect("\"a\" 4 extraArg");
    }

    private void assertAddEventParse(String args, String name, LocalDateTime start, LocalDateTime end) {
        final EventTask expected;
        try {
            expected = new EventTask(name, start, end);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        final Command command = parser.parse(args);
        assertTrue(command instanceof AddTaskCommand);
        assertEquals(expected, ((AddTaskCommand)command).getTask());
    }

    private void assertAddDeadlineParse(String args, String name, LocalDateTime due) {
        final DeadlineTask expected;
        try {
            expected = new DeadlineTask(name, due);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }

        final Command command = parser.parse(args);

        assertTrue(command instanceof AddTaskCommand);
        assertEquals(expected, ((AddTaskCommand)command).getTask());
    }

    private void assertAddFloatingTaskParse(String args, String name, String priorityString) {
        final FloatingTask expected;
        final Priority priority;
        try {
            priority = new Priority(priorityString);
            expected = new FloatingTask(name, priority);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        final Command command = parser.parse(args);
        assertTrue(command instanceof AddTaskCommand);
        assertEquals(expected, ((AddTaskCommand)command).getTask());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }
}
