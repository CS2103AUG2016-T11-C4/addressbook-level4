package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.Name;
import seedu.address.model.task.Task;

/**
 * Adds a person to the task book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the task book. "
            + "Parameters: NAME\n"
            + "Example: " + COMMAND_WORD
            + " John Doe";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";

	@Override
	public CommandResult execute() {
		// TODO Auto-generated method stub
		return null;
	}
}
