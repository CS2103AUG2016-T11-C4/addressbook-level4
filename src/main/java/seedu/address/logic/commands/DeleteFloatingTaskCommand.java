package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.FloatingTask;

public class DeleteFloatingTaskCommand extends Command {

    public static final String COMMAND_WORD = "del-task";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the floating task identified by the index number used in the filtered floating task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted floating task: %1$s";

    public final int targetIndex;

    public DeleteFloatingTaskCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        try {
            final FloatingTask deletedTask = model.removeFloatingTask(targetIndex - 1);
            return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, deletedTask));
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

}
