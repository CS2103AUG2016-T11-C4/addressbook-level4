package seedu.address.logic.commands;

import java.util.EmptyStackException;

public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Undos previous action that modifies task book information.\n\t"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        try {
            String undoneAction = model.undo();
            return new CommandResult("Successfully undid previous " + undoneAction);
        } catch (EmptyStackException e) {
            return new CommandResult("No actions to undo.");
        }
    }

}
