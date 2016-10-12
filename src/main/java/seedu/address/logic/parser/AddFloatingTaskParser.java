package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddFloatingTaskCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.model.task.Priority;

public class AddFloatingTaskParser {
    private static final Pattern ARG_PATTERN = 
            Pattern.compile("\\s*\"(?<quotedArg>[^\"]+)\"\\s*|\\s*(?<unquotedArg>[^\\s]+)\\s*");

    private final Command incorrectCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, 
                                                                  AddFloatingTaskCommand.MESSAGE_USAGE));
    
    public AddFloatingTaskParser() {
    }

    public Command parse(String str)  {
        final ParseResult args;
        try {
            args = parseArguments(str.trim());
        } catch (IllegalValueException e) {
            return incorrectCommand;
        }     

        // There may not be a {priority} argument
        if (args.priority == null) {
            try {
                return new AddFloatingTaskCommand(args.name,
                        Integer.toString(Priority.LOWER_BOUND));
            } catch (IllegalValueException e) {
                return new IncorrectCommand(e.getMessage());
            }
        } else { //args.priority != null
            try {
                return new AddFloatingTaskCommand(args.name, args.priority);
            } catch (IllegalValueException e) {
                return new IncorrectCommand(e.getMessage());
            }
        }

    }

    private static class ParseResult {
        String name;
        String priority;
    }

    private static ParseResult parseArguments(String str) throws IllegalValueException {
        final ParseResult result = new ParseResult();
        final ArrayList<String> args = splitArgs(str);

        // name
        if (args.isEmpty()) {
            throw new IllegalValueException("expected name");
        }
        result.name = args.remove(0);

        // priority (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (Priority.isValidPriority(args.get(0))) {
            result.priority = args.remove(0);
        }
        
        if (!args.isEmpty()) {
            throw new IllegalValueException("too many arguments");
        }
        return result;

    }

    private static ArrayList<String> splitArgs(String str) {
        final Matcher matcher = ARG_PATTERN.matcher(str);
        final ArrayList<String> args = new ArrayList<>();
        int start = 0;
        while (matcher.find(start)) {
            args.add(matcher.group("quotedArg") != null ? matcher.group("quotedArg")
                                                        : matcher.group("unquotedArg"));
            start = matcher.end();
        }
        return args;
    }

}
