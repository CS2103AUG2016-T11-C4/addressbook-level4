package seedu.address.logic.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.Name;

public class EditDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "edit-due";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the deadline identified by the index number used in the filtered deadline listing.\n"
            + "Parameters: INDEX [dd-NEW_DUE_DATE] [dt-NEW_DUE_TIME] [n-NEW_NAME]"
            + "Example: " + COMMAND_WORD + " 1 dd-12/12/2017 dt-8pm";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Deadline edited: %1$s";

    public final int targetIndex;

    public final Name newName;

    public final LocalDate newDate;

    public final LocalTime newTime;

    public EditDeadlineCommand(int targetIndex, String newName, LocalDate newDate, LocalTime newTime) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.newName = newName != null ? new Name(newName) : null;
        this.newDate = newDate;
        this.newTime = newTime;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public LocalDate getNewDate() {
        return newDate;
    }

    public LocalTime getNewTime() {
        return newTime;
    }

    @Override
    public CommandResult execute() {
        DeadlineTask oldDeadlineTask;
        try {
            oldDeadlineTask = model.getDeadlineTask(targetIndex - 1);
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        DeadlineTask newDeadlineTask;
        newDeadlineTask = new DeadlineTask(
                newName != null ? newName : oldDeadlineTask.name,
                LocalDateTime.of(
                        newDate != null ? newDate : oldDeadlineTask.getDue().toLocalDate(),
                        newTime != null ? newTime : oldDeadlineTask.getDue().toLocalTime()
                )
        );

        try {
            model.setDeadlineTask(targetIndex - 1, newDeadlineTask);
        } catch (IllegalValueException e) {
            throw new AssertionError("The target deadline cannot be missing", e);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, newDeadlineTask));
    }

}
