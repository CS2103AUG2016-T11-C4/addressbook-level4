package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.filter.DeadlineTaskFinishedPredicate;
import seedu.address.model.filter.EventTaskFinishedPredicate;
import seedu.address.model.filter.FloatingTaskFinishedPredicate;

public class ClearFinishedCommand implements Command {

    public static final String COMMAND_WORD = "clear";
    public static final String COMMAND_PARAMETER = "fin";
    public static final String MESSAGE_SUCCESS = "All finished task in Task Tracker have been cleared!";

    public ClearFinishedCommand() {}

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null;
        model.removeFloatingTasks(new FloatingTaskFinishedPredicate());
        model.removeDeadlineTasks(new DeadlineTaskFinishedPredicate());
        model.removeEventTasks(new EventTaskFinishedPredicate());
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
