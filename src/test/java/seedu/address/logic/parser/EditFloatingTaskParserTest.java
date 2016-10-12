package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditFloatingTaskCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;

public class EditFloatingTaskParserTest {

    private EditFloatingTaskParser parser;

    @Before
    public void setupParser() {
        parser = new EditFloatingTaskParser();
    }

    @Test
    public void parse() {
        // No modification
        assertParse("1", 1, null, null);
        
        // name
        
        try {
            assertParse("1 n-new name", 1, new Name("new name"), null);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        
        // priority
        try {
            assertParse("1 p-2", 1, null, new Priority("2"));
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        
        // All arguments
        try {
            assertParse("3 n-thisname p-5", 3, new Name("thisname"), new Priority("5"));
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }

        // Invalid flags
        assertIncorrect("5 invalid-flag");
    }

    private void assertParse(String args, int targetIndex, Name name, Priority priority) {
        
        final Command command = parser.parse(args);
        assertTrue(command instanceof EditFloatingTaskCommand);
        
        assertEquals(targetIndex, ((EditFloatingTaskCommand)command).getTargetIndex());
        assertEquals(name, ((EditFloatingTaskCommand)command).getNewName());
        assertEquals(priority, ((EditFloatingTaskCommand)command).getNewPriority());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}