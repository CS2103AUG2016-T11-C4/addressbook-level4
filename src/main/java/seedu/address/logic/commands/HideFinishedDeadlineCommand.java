package seedu.address.logic.commands;

import seedu.address.model.filter.DeadlineNotFinishedPredicate;

public class HideFinishedDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "hide-finished";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " d"
            + ": Hide all finished deadline tasks from the user interface.\n"
            + "Example: " + COMMAND_WORD + " d"; // TODO replace " d" after IndexPrefix.java added

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "All finished Deadlines hided.";

    @Override
    public CommandResult execute() {
        model.setDeadlineTaskFilter(new DeadlineNotFinishedPredicate());
        return new CommandResult(MESSAGE_EDIT_TASK_SUCCESS);
    }
}
