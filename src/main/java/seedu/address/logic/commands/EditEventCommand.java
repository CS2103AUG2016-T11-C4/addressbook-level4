package seedu.address.logic.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.Name;

public class EditEventCommand extends Command {

    public static final String COMMAND_WORD = "edit-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the event identified by the index number used in the filtered event listing.\n"
            + "Parameters: INDEX [sd-NEW_START_TIME | st-NEW_START_DATE | ed-NEW_END_DATE | et-NEW_END_TIME | n-NEW_NAME]"
            + "Example: " + COMMAND_WORD + " 1 st-4pm et-8pm";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Event edited: %1$s";

    public final int targetIndex;

    public final Name newName;

    public final LocalDate newStartDate;

    public final LocalTime newStartTime;

    public final LocalDate newEndDate;

    public final LocalTime newEndTime;

    public EditEventCommand(int targetIndex, String newName, LocalDate newStartDate, LocalTime newStartTime,
                            LocalDate newEndDate, LocalTime newEndTime) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.newName = newName != null ? new Name(newName) : null;
        this.newStartDate = newStartDate;
        this.newStartTime = newStartTime;
        this.newEndDate = newEndDate;
        this.newEndTime = newEndTime;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public LocalDate getNewStartDate() {
        return newStartDate;
    }

    public LocalTime getNewStartTime() {
        return newStartTime;
    }

    public LocalDate getNewEndDate() {
        return newEndDate;
    }

    public LocalTime getNewEndTime() {
        return newEndTime;
    }

    @Override
    public CommandResult execute() {
        EventTask oldEventTask;
        try {
            oldEventTask = model.getEventTask(targetIndex - 1);
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        EventTask newEventTask;
        try {
            newEventTask = new EventTask(
                    newName != null ? newName : oldEventTask.name,
                    LocalDateTime.of(
                            newStartDate != null ? newStartDate : oldEventTask.getStart().toLocalDate(),
                            newStartTime != null ? newStartTime : oldEventTask.getStart().toLocalTime()
                    ),
                    LocalDateTime.of(
                            newEndDate != null ? newEndDate : oldEventTask.getEnd().toLocalDate(),
                            newEndTime != null ? newEndTime : oldEventTask.getEnd().toLocalTime()
                    )
            );
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(e.getMessage());
        }

        try {
            model.setEventTask(targetIndex - 1, newEventTask);
        } catch (IllegalValueException e) {
            throw new AssertionError("The target event cannot be missing", e);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, newEventTask));
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
