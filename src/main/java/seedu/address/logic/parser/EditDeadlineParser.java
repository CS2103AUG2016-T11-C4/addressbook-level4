package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.EditDeadlineCommand;
import seedu.address.logic.commands.IncorrectCommand;

public class EditDeadlineParser {
    private static final Pattern CMD_PATTERN = Pattern.compile("^(?<index>\\d+)"
                                                               + "(\\s+dd-(?<newDate>[^\\s]+))?"
                                                               + "(\\s+dt-(?<newTime>[^\\s]+))?"
                                                               + "(\\s+n-(?<newName>.+))?"
                                                               + "$");

    private final LocalDateTime referenceDateTime;

    public EditDeadlineParser() {
        this(null);
    }

    public EditDeadlineParser(LocalDateTime referenceDateTime) {
        this.referenceDateTime = referenceDateTime;
    }

    public Command parse(String args) {
        final Matcher matcher = CMD_PATTERN.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditDeadlineCommand.MESSAGE_USAGE));
        }

        final String indexString = matcher.group("index").trim();
        if (!StringUtil.isUnsignedInteger(indexString)) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditDeadlineCommand.MESSAGE_USAGE));
        }
        final int index = Integer.parseInt(indexString);

        final DateTimeParser parser = referenceDateTime != null ? new DateTimeParser(referenceDateTime)
                                                                : new DateTimeParser();
        LocalDate newDate = null;
        LocalTime newTime = null;

        String newName = matcher.group("newName");
        try {
            if (matcher.group("newDate") != null) {
                newDate = parser.parseDate(matcher.group("newDate"));
            }
            if (matcher.group("newTime") != null) {
                newTime = parser.parseTime(matcher.group("newTime"));
            }

        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }

        try {
            return new EditDeadlineCommand(index, newName, newDate, newTime);
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }

}
