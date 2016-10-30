package seedu.address.logic.parser;

import seedu.address.logic.commands.ClearFinishedCommand;

public class ClearFinishedParser implements Parser<ClearFinishedCommand> {
    private final CommandLineParser cmdParser = new CommandLineParser()
                                                    .addArgument("fin");

    @Override
    public ClearFinishedCommand parse(String str) throws ParseException {
        cmdParser.parse(str);
        return new ClearFinishedCommand();
    }
}
