package seedu.address.logic.commands;

public class UndoTraceCommand extends Command {
    public static final String COMMAND_WORD = "undo-trace";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Show a list of undoable actions\n\t" + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        return new CommandResult(model.printUndoables());
    }

}
