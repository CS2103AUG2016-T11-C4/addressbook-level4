package seedu.address.logic.commands;

import seedu.address.model.filter.DeadlineNotFinishedPredicate;
import seedu.address.model.filter.EventNotInPastPredicate;
import seedu.address.model.filter.FloatingTaskNotFinishedPredicate;

/**
 * Clears the task book by deleting all finished tasks.
 * Event task is considered as finished if it is past already.
 */
public class ClearFinishedCommand extends Command {

    public static final String COMMAND_WORD = "clear fin";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes all finished task in TaskTracker.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "All finished task have been cleared.";

    @Override
    public CommandResult execute() {
        model.removeFloatingTask(new FloatingTaskNotFinishedPredicate());
        model.removeDeadlineTask(new DeadlineNotFinishedPredicate());
        model.removeEventTask(new EventNotInPastPredicate());
        model.setFloatingTaskFilter(null);
        model.setDeadlineTaskFilter(null);
        model.setEventTaskFilter(null);
        return new CommandResult(MESSAGE_DELETE_TASK_SUCCESS);
    }

}
