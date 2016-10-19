package seedu.address.logic.commands;

import java.time.LocalDateTime;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.DeadlineTask;

/**
 * Adds an deadline task to the task book.
 */
public class AddDeadlineCommand extends AddTaskCommand {

    public static final String MESSAGE_USAGE = "Parameters for adding deadline: \"NAME\" <DATE> <TIME> \n"
            + "Example: " + COMMAND_WORD + " \"Deadline Name\" 12/12/2016 2pm \n";

    private final DeadlineTask deadlineTask;

    public AddDeadlineCommand(String name, LocalDateTime due) throws IllegalValueException {
        deadlineTask = new DeadlineTask(name, due);
    }

    @Override
    public DeadlineTask getTask() {
        return deadlineTask;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        model.addDeadlineTask(deadlineTask);
        return new CommandResult(String.format(MESSAGE_SUCCESS, deadlineTask));
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
