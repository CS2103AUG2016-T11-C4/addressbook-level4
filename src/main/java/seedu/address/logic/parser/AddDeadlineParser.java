package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddDeadlineCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;

public class AddDeadlineParser {

    private static final Pattern ARG_PATTERN = Pattern.compile("\\s*\"(?<quotedArg>[^\"]+)\"\\s*|\\s*(?<unquotedArg>[^\\s]+)\\s*");

    private final Command incorrectCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDeadlineCommand.MESSAGE_USAGE));

    private final LocalDateTime referenceDateTime;

    public AddDeadlineParser() {
        this(null);
    }

    public AddDeadlineParser(LocalDateTime referenceDateTime) {
        this.referenceDateTime = referenceDateTime;
    }

    public Command parse(String str) {
        final ParseResult args;
        try {
            args = parseArguments(str);
        } catch (IllegalValueException e) {
            return incorrectCommand;
        }

        // There must be at least one of { Date, Time }.
        if ((args.date == null && args.time == null)) {
            return incorrectCommand;
        }

        final DateTimeParser parser = referenceDateTime != null ? new DateTimeParser(referenceDateTime)
                                                                : new DateTimeParser();
        try {
            final LocalDate Date = args.date != null ? parser.parseDate(args.date)
                                                     : parser.getReferenceDateTime().toLocalDate();
            final LocalTime Time = args.time != null ? parser.parseTime(args.time)
                                                     : LocalTime.of(23, 59);

            return new AddDeadlineCommand(args.name, LocalDateTime.of(Date, Time));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }

    private static class ParseResult {
        String name;
        String date;
        String time;
    }

    private static ParseResult parseArguments(String str) throws IllegalValueException {
        final ParseResult result = new ParseResult();
        final ArrayList<String> args = splitArgs(str);

        // name
        if (args.isEmpty()) {
            throw new IllegalValueException("expected name");
        }
        result.name = args.remove(0);

        // Date (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (isDate(args.get(0))) {
            result.date = args.remove(0);
        }

        // Time (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (isTime(args.get(0))) {
            result.time = args.remove(0);
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

    private static boolean isDate(String str) {
        final DateTimeParser parser = new DateTimeParser();
        try {
            parser.parseDate(str);
            return true;
        } catch (IllegalValueException e) {
            return false;
        }
    }

    private static boolean isTime(String str) {
        final DateTimeParser parser = new DateTimeParser();
        try {
            parser.parseTime(str);
            return true;
        } catch (IllegalValueException e) {
            return false;
        }
    }

}
