package seedu.address.logic.parser;

import seedu.address.logic.commands.ClearAllCommand;

public class ClearAllParser implements Parser<ClearAllCommand> {

    private final CommandLineParser cmdParser = new CommandLineParser();

    @Override
    public ClearAllCommand parse(String str) throws ParseException {
        cmdParser.parse(str);
        return new ClearAllCommand();
    }

}
