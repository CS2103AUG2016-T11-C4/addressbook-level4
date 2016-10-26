package seedu.address.logic.commands;

public class RedoTraceCommand extends Command {

    public static final String COMMAND_WORD = "redo-trace";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Show a list of redoable actions\n\t" + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        return new CommandResult(model.printRedoables());
    }

}
