package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TaskBook;
import seedu.address.model.TaskBookBuilder;
import seedu.address.model.config.Config;

public class ClearCommandTest {

    @Test
    public void execute_clearsTasksInModel() {
        final TaskBook taskBook = new TaskBookBuilder().addTypicalTasks().build();
        final Model model = new ModelManager(new Config(), taskBook);
        final ClearCommand command = new ClearCommand();
        command.setData(model);
        final CommandResult result = command.execute();
        assertEquals(new TaskBook(), model.getTaskBook());
        assertEquals("Task book has been cleared!", result.feedbackToUser);
    }

}
