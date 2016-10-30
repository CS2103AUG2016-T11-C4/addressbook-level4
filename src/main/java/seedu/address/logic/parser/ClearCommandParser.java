package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;

public class ClearCommandParser implements Parser<Command> {

    private final OverloadParser<Command> overloadParser = new OverloadParser<Command>()
            .addParser("Clear all tasks in TaskTracker", new ClearAllParser())
            .addParser("Clear all finished tasks in TaskTracker", new ClearFinishedParser());

    @Override
    public Command parse(String str) throws ParseException {
        return overloadParser.parse(str);
    }
}
