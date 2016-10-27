package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;

/**
 * Parses user input.
 */
public class TaskTrackerParser {

    private final SubcommandParser parser = new SubcommandParser()
            .putSubcommand("add", new AddTaskParser())
            .putSubcommand("edit", new EditCommandParser())
            .putSubcommand("del", new DeleteCommandParser())
            .putSubcommand("fin", new MarkFinishedCommandParser())
            .putSubcommand("clear", new ClearCommandParser())
            .putSubcommand("exit", new ExitCommandParser())
            .putSubcommand("help", new HelpCommandParser())
            .putSubcommand("setdatadir", new SetDataDirectoryParser())
            .putSubcommand("undo", new UndoCommandParser())
            .putSubcommand("redo", new RedoCommandParser());

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        try {
            return parser.parse(userInput);
        } catch (ParseException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }

}
