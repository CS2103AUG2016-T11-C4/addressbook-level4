package seedu.address.logic.commands;

import java.time.LocalDateTime;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.EventTask;

/**
 * Adds an event task to the task book.
 */
public class AddEventCommand extends AddTaskCommand {

    public static final String MESSAGE_USAGE = "Parameters for adding event: \"NAME\" <STARTING_DATE> <STARTING_TIME> to <ENDING_DATE> <ENDING_TIME>\n"
            + "Example: " + COMMAND_WORD + " \"Event Name\" 12/12/2016 12pm to 2pm \n";

    private final EventTask eventTask;

    public AddEventCommand(String name, LocalDateTime start, LocalDateTime end) throws IllegalValueException {
        eventTask = new EventTask(name, start, end);
    }

    @Override
    public EventTask getTask() {
        return eventTask;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        model.addEventTask(eventTask);
        return new CommandResult(String.format(MESSAGE_SUCCESS, eventTask));
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

