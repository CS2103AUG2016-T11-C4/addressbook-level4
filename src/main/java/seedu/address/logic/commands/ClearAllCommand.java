package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.TaskBook;

/**
 * Clears the task book.
 */
public class ClearAllCommand implements Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Task book has been cleared!";

    public ClearAllCommand() {}

    @Override
    public CommandResult execute(Model model) {
        assert model != null;
        model.resetTaskBook(new TaskBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
