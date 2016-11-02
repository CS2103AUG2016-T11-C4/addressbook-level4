package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.filter.DeadlineTaskFinishedPredicate;

public class HideFinishedDeadlineCommand implements Command {

    public static final String COMMAND_WORD = "hide-finished";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " d"
            + ": Hide all finished deadline tasks from the user interface.\n"
            + "Example: " + COMMAND_WORD + " d"; // TODO replace " d" after IndexPrefix.java added

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "All finished Deadlines hided.";

    @Override
    public CommandResult execute(Model model) {
        model.setDeadlineTaskPredicate((new DeadlineTaskFinishedPredicate()).negate());
        return new CommandResult(MESSAGE_EDIT_TASK_SUCCESS);
    }
}
