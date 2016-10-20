package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;

public class EditFloatingTaskCommand extends Command {

    public static final String COMMAND_WORD = "edit-float";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the floating task identified by the index number used in the filtered floating task listing.\n"
            + "Parameters: INDEX [n-NAME] [p-PRIORITY]"
            + "Example: " + COMMAND_WORD + " 1 p-2";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Floting task edited: %1$s";

    public final int targetIndex;

    public final Name newName;

    public final Priority newPriority;

    public EditFloatingTaskCommand(int targetIndex, String newName, String newPriority) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.newName = newName != null ? new Name(newName) : null;
        this.newPriority = newPriority != null ? new Priority(newPriority) : null;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public Name getNewName() {
        return this.newName;
    }

    public Priority getNewPriority() {
        return this.newPriority;
    }

    @Override
    public CommandResult execute() {
        FloatingTask oldFloatingTask;
        try {
            oldFloatingTask = model.getFloatingTask(targetIndex - 1);
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        FloatingTask newFloatingTask;

        newFloatingTask = new FloatingTask(
                newName != null ? newName : oldFloatingTask.name,
                newPriority != null ? newPriority : oldFloatingTask.getPriority()
        );

        try {
            model.setFloatingTask(targetIndex - 1, newFloatingTask);
        } catch (IllegalValueException e) {
            throw new AssertionError("The target floating task cannot be missing", e);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, newFloatingTask));
    }

}
