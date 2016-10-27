package seedu.address.logic.parser;

import seedu.address.logic.commands.UndoCommand;

public class UndoCommandParser implements Parser<UndoCommand> {

    private final CommandLineParser cmdParser = new CommandLineParser();

    @Override
    public UndoCommand parse(String str) throws ParseException {
        cmdParser.parse(str);
        return new UndoCommand();
    }

}
