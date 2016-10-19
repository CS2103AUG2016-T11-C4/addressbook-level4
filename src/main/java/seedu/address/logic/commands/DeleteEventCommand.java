package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.EventTask;

public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "del-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event identified by the index number used in the filtered event listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted event: %1$s";

    public final int targetIndex;

    public DeleteEventCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        try {
            final EventTask deletedTask = model.removeEventTask(targetIndex - 1);
            return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, deletedTask));
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

	@Override
	public boolean modifiesData() {
		return true;
	}

	@Override
	public String getCommandWord() {
		return COMMAND_WORD;
	}
}
