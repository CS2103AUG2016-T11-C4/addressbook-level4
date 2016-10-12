package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.EditFloatingTaskCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;

public class EditFloatingTaskParser {
    private static final Pattern CMD_PATTERN = Pattern.compile("^(?<index>\\d+)"
                                                                + "(\\s+n-(?<newName>[^-]+))?"
                                                                + "(\\s+p-(?<newPriority>.+))?"
                                                                + "$");
    public EditFloatingTaskParser() {
    }


    public Command parse(String args) {
        final Matcher matcher = CMD_PATTERN.matcher(args.trim());
        
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditFloatingTaskCommand.MESSAGE_USAGE));
        }

        final String indexString = matcher.group("index").trim();
        if (!StringUtil.isUnsignedInteger(indexString)) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditFloatingTaskCommand.MESSAGE_USAGE));
        }
        final int index = Integer.parseInt(indexString);

        String newName = matcher.group("newName");
        String newPriority = null;
        
        try {
            if (matcher.group("newPriority") != null) {
                newPriority = matcher.group("newPriority");
                if (!Priority.isValidPriority(newPriority)) 
                    throw new IllegalValueException(Priority.MESSAGE_PRIORITY_CONSTRAINTS);
            }
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
        
        try {
            return new EditFloatingTaskCommand(index, newName, newPriority);
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());  
            
        }
    }
 
 }