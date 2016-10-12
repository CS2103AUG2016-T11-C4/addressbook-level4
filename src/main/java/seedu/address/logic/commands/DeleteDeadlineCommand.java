package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.DeadlineTask;

public class DeleteDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "del-due";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the deadline identified by the index number used in the filtered deadline listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted deadline: %1$s";

    public final int targetIndex;

    public DeleteDeadlineCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        try {
            final DeadlineTask deletedTask = model.removeDeadlineTask(targetIndex - 1);
            return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, deletedTask));
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

}
