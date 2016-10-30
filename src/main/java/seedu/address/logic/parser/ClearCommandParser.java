package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;

public class ClearCommandParser implements Parser<Command> {

    private final OverloadParser<Command> overloadParser;

    public ClearCommandParser() {
        overloadParser = new OverloadParser<Command>()
                         .addParser("Clear all the task in Task Tracker.", new ClearAllParser());
    }

    @Override
    public Command parse(String str) throws ParseException {
        return overloadParser.parse(str);
    }
}
