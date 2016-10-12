package seedu.address.logic.commands;

import java.time.LocalDateTime;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;

/**
 * Adds an event task to the task book.
 */
public class AddFloatingTaskCommand extends Command {

    public static final String COMMAND_WORD = "task";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an floating task to the task book. "
            + "Parameters: \"NAME\" [PRIORITY]\n"
            + "Example: " + COMMAND_WORD
            + " \"Buy A Helicopter\" 4";

    public static final String MESSAGE_SUCCESS = "New event added: %1$s";

    private final FloatingTask floatingTask;

    public AddFloatingTaskCommand(String name, String priority) throws IllegalValueException {
        this.floatingTask = new FloatingTask(new Name(name), new Priority(priority));
    }

    public FloatingTask getFloatingTask() {
        return floatingTask;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        model.addFloatingTask(floatingTask);
        return new CommandResult(String.format(MESSAGE_SUCCESS, floatingTask));
    }

}