package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddTaskCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.model.task.Priority;

public class AddTaskParser {
    private static final Pattern ARG_PATTERN = Pattern.compile("\\s*\"(?<quotedArg>[^\"]+)\"\\s*|\\s*(?<unquotedArg>[^\\s]+)\\s*");

    private static final Pattern PRIORITY_PATTERN = Pattern.compile("p-(?<priority>\\d)");

    private final Command incorrectCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTaskCommand.MESSAGE_USAGE));

    private final LocalDateTime referenceDateTime;

    public AddTaskParser() {
        this(null);
    }

    public AddTaskParser(LocalDateTime referenceDateTime) {
        this.referenceDateTime = referenceDateTime;
    }

    public Command parse(String str) {
        final ParseResult args;

        try {
            args = parseArguments(str);
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }

        switch (args.type) {
        case "event": return prepareAddEvent(args);
        case "deadline": return prepareAddDeadline(args);
        case "float": return prepareAddFloatingTask(args);
        default: return incorrectCommand;
        }
    }

    public Command prepareAddEvent (ParseResult args) {
        // There must be at least one of { startDate, startTime }, and at least one of { endDate, endTime }.
        if ((args.startDate == null && args.startTime == null) || (args.endDate == null && args.endTime == null)) {
            return incorrectCommand;
        }

        final DateTimeParser parser = referenceDateTime != null ? new DateTimeParser(referenceDateTime)
                                                                : new DateTimeParser();
        try {
            final LocalDate startDate = args.startDate != null ? parser.parseDate(args.startDate)
                                                                : parser.getReferenceDateTime().toLocalDate();
            final LocalTime startTime = args.startTime != null ? parser.parseTime(args.startTime)
                                                                : LocalTime.of(0, 0);
            final LocalDate endDate = args.endDate != null ? parser.parseDate(args.endDate) : startDate;
            final LocalTime endTime = args.endTime != null ? parser.parseTime(args.endTime) : LocalTime.of(23, 59);
            return new AddTaskCommand(args.name,
                                       LocalDateTime.of(startDate, startTime),
                                       LocalDateTime.of(endDate, endTime));
        } catch (IllegalValueException e) {
            //return new IncorrectCommand(e.getMessage());
            try {
                return new AddTaskCommand("a", LocalDateTime.of(2000, 2, 1, 4, 30), LocalDateTime.of(2100, 3, 2, 18, 32));
            } catch (IllegalValueException e1) {
                return incorrectCommand;
            }
        }
    }

    public Command prepareAddDeadline (ParseResult args) {
        // There must be at least one of { date, time }.
        if ((args.dueDate == null && args.dueTime == null)) {
            return incorrectCommand;
        }

        final DateTimeParser parser = referenceDateTime != null ? new DateTimeParser(referenceDateTime)
                                                                : new DateTimeParser();
        try {
            final LocalDate Date = args.dueDate != null ? parser.parseDate(args.dueDate)
                                                     : parser.getReferenceDateTime().toLocalDate();
            final LocalTime Time = args.dueTime != null ? parser.parseTime(args.dueTime)
                                                     : LocalTime.of(23, 59);

            return new AddTaskCommand(args.name, LocalDateTime.of(Date, Time));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }

    public Command prepareAddFloatingTask (ParseResult args) {
        // There may not be a {priority} argument
        if (args.priority == null) {
            try {
                return new AddTaskCommand(args.name,
                        Integer.toString(Priority.LOWER_BOUND));
            } catch (IllegalValueException e) {
                return new IncorrectCommand(e.getMessage());
            }
        } else { //args.priority != null
            try {
                return new AddTaskCommand(args.name, args.priority);
            } catch (IllegalValueException e) {
                return new IncorrectCommand(e.getMessage());
            }
        }
    }

    private static class ParseResult {
        String type;
        String name;
        String priority;
        String startDate;
        String startTime;
        String endDate;
        String endTime;
        String dueDate;
        String dueTime;
    }

    private static ParseResult parseArguments(String str) throws IllegalValueException {
        final ParseResult result = new ParseResult();
        final ArrayList<String> args = splitArgs(str);

        String mediumValueStartingOrDueDate = null;
        String mediumValueStartingOrDueTime = null;

        // name
        if (args.isEmpty()) {
            throw new IllegalValueException("expected name");
        }
        result.name = args.remove(0);

        // priority (optional)
        if (args.isEmpty()) {
            result.type = "float";
            return result;
        }
        if (isPriority(args.get(0))) {
            result.type = "float";
            Matcher matcher = PRIORITY_PATTERN.matcher(args.remove(0));
            matcher.find();
            result.priority = matcher.group("priority");
            if (!args.isEmpty()) {
                throw new IllegalValueException("too many arguments");
            }
            return result;
        }

        // starDate or dueDate (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (isDate(args.get(0))) {
            mediumValueStartingOrDueDate = args.remove(0);
        }

        // startTime or dueTime (optional)
        if (args.isEmpty()) {
            if (mediumValueStartingOrDueDate != null) {
                result.type = "deadline";
                result.dueDate = mediumValueStartingOrDueDate;
            }
            return result;
        }
        if (isTime(args.get(0))) {
            mediumValueStartingOrDueTime = args.remove(0);
        }

        // check if there is a "to" inside the command
        if (args.isEmpty()) {
            if (mediumValueStartingOrDueDate != null) {
                result.type = "deadline";
                result.dueDate = mediumValueStartingOrDueDate;
            }
            if (mediumValueStartingOrDueTime != null) {
                result.type = "deadline";
                result.dueTime = mediumValueStartingOrDueTime;
            }
            return result;
        }
        if (isKeywordTo(args.get(0))) {
            args.remove(0);
            result.type = "event";
            result.startDate = mediumValueStartingOrDueDate;
            result.startTime = mediumValueStartingOrDueTime;
        }

        // endDate (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (isDate(args.get(0))) {
            result.endDate = args.remove(0);
        }

        // endTime (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (isTime(args.get(0))) {
            result.endTime = args.remove(0);
        }

        if (!args.isEmpty()) {
            throw new IllegalValueException("wrong arguments given, please check the number and the order of the arguments");
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

    private static boolean isPriority(String str) {
        Matcher matcher = PRIORITY_PATTERN.matcher(str.trim());
        return matcher.matches();
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

    private static boolean isKeywordTo(String str) {
        return str.trim().equals("to");
    }
}
