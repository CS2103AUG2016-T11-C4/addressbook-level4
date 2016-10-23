package seedu.address.logic.commands;

import seedu.address.model.ModelManager.HeadAtBoundaryException;

public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Redo the previous undo.\n\t" + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        Command action;
        try {
            action = model.redo();
        } catch (HeadAtBoundaryException e) {
            return new CommandResult("No undos to redo.");
        }
        return new CommandResult("Redid " + action.toString());
    }

}
