package seedu.address.logic.parser;

import seedu.address.logic.commands.RedoCommand;

public class RedoCommandParser implements Parser<RedoCommand> {

    private final CommandLineParser cmdParser = new CommandLineParser();

    @Override
    public RedoCommand parse(String str) throws ParseException {
        cmdParser.parse(str);
        return new RedoCommand();
    }

}
