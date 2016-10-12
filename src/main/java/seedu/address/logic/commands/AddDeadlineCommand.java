package seedu.address.logic.commands;

import java.time.LocalDateTime;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.DeadlineTask;

/**
 * Adds an deadline task to the task book.
 */
public class AddDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "due";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a deadline to the task book. "
            + "Parameters: \"NAME\" <DUE_DATE> <DUE_TIME>\n"
            + "Example: " + COMMAND_WORD
            + " \"Speech Transcript\" 11pm";

    public static final String MESSAGE_SUCCESS = "New deadline added: %1$s";

    private final DeadlineTask deadlineTask;

    public AddDeadlineCommand(String name, LocalDateTime due) throws IllegalValueException {
        deadlineTask = new DeadlineTask(name, due);
    }

    public DeadlineTask getDeadlineTask() {
        return deadlineTask;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        model.addDeadlineTask(deadlineTask);
        return new CommandResult(String.format(MESSAGE_SUCCESS, deadlineTask));
    }

}
