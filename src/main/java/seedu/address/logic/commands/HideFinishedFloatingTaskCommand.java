package seedu.address.logic.commands;

import seedu.address.model.filter.FloatingTaskFinishedPredicate;

public class HideFinishedFloatingTaskCommand extends Command {

    public static final String COMMAND_WORD = "hide-finished";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Hide all finished floating tasks from the user interface.\n"
            + "Example: " + COMMAND_WORD + " f"; // TODO replace " f" after IndexPrefix.java added

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "All finished Floating Task hided.";

    @Override
    public CommandResult execute() {
        model.setFloatingTaskFilter(new FloatingTaskFinishedPredicate());
        return new CommandResult(MESSAGE_EDIT_TASK_SUCCESS);
    }
}
