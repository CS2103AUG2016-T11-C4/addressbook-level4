package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddFloatingTaskCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Priority;

public class AddFloatingTaskParserTest {

    private AddFloatingTaskParser parser;

    @Before
    public void setupParser() {
        parser = new AddFloatingTaskParser();
    }

    @Test
    public void parse() {
        // All arguments provided
        assertParse("\"a\" 3", "a", "3");

        // If priority is not given, then it default "0"
        assertParse("\"a\"", "a", "0");

        // Too many arguments
        assertIncorrect("\"a\" 4 extraArg");
    }

    private void assertParse(String args, String name, String priorityString) {
        final FloatingTask expected;
        final Priority priority;
        try {
            priority = new Priority(priorityString);
            expected = new FloatingTask(name, priority);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        final Command command = parser.parse(args);
        assertTrue(command instanceof AddFloatingTaskCommand);
        assertEquals(expected, ((AddFloatingTaskCommand)command).getFloatingTask());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}