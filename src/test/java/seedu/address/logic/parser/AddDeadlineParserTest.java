package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.time.TypicalLocalDateTimes.PI_DAY;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddDeadlineCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.model.task.DeadlineTask;

public class AddDeadlineParserTest {

    private AddDeadlineParser parser;

    @Before
    public void setupParser() {
        /** Pi Day 3:14 15/9/2653 */
        parser = new AddDeadlineParser(PI_DAY);
    }

    @Test
    public void parse() {
        // All arguments provided
        assertParse("\"a\" 1/2/2000 4:30am", "a", LocalDateTime.of(2000, 2, 1, 4, 30));

        // If Date is not given, then it is the current date
        assertParse("\"a\" 9:56am", "a", LocalDateTime.of(2653, 9, 15, 9, 56));

        // If Time is not given, then it is 11:59pm
        assertParse("\"a\" 3/4/2015", "a", LocalDateTime.of(2015, 4, 3, 23, 59));

        // Must have at least one of { Date, Time }
        assertIncorrect("\"a\"");
        assertIncorrect("");

        // Too many arguments
        assertIncorrect("\"a\" 8/1/2012 1:49am extraArg");
    }

    private void assertParse(String args, String name, LocalDateTime due) {
        final DeadlineTask expected;
        try {
            expected = new DeadlineTask(name, due);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }

        final Command command = parser.parse(args);

        assertTrue(command instanceof AddDeadlineCommand);
        assertEquals(expected, ((AddDeadlineCommand)command).getDeadlineTask());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}
