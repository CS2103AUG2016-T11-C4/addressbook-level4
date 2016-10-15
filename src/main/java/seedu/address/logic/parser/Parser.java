package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddDeadlineCommand;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.AddFloatingTaskCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteDeadlineCommand;
import seedu.address.logic.commands.DeleteEventCommand;
import seedu.address.logic.commands.DeleteFloatingTaskCommand;
import seedu.address.logic.commands.EditDeadlineCommand;
import seedu.address.logic.commands.EditEventCommand;
import seedu.address.logic.commands.EditFloatingTaskCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;

/**
 * Parses user input.
 */
public class Parser {

	/**
	 * Used for initial separation of command word and args.
	 */
	private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

	private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

	private static final Pattern KEYWORDS_ARGS_FORMAT = Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one
																											// or
																											// more
																											// keywords
																											// separated
																											// by
																											// whitespace

	private static final Pattern PERSON_DATA_ARGS_FORMAT = // '/' forward
															// slashes are
															// reserved for
															// delimiter
															// prefixes
			Pattern.compile("(?<name>[^/]+)");

	public Parser() {
	}

	/**
	 * Parses user input into command for execution.
	 *
	 * @param userInput
	 *            full user input string
	 * @return the command based on the user input
	 */
	public Command parseCommand(String userInput) {
		final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
		}

		final String commandWord = matcher.group("commandWord");
		final String arguments = matcher.group("arguments");
		switch (commandWord) {

		case AddCommand.COMMAND_WORD:
			return prepareAdd(arguments);

		case SelectCommand.COMMAND_WORD:
			return prepareSelect(arguments);

		case DeleteCommand.COMMAND_WORD:
			return prepareDelete(arguments);

		case DeleteFloatingTaskCommand.COMMAND_WORD:
			return prepareDeleteFloatingTask(arguments);

		case EditFloatingTaskCommand.COMMAND_WORD:
			return new EditFloatingTaskParser().parse(arguments);

		case DeleteEventCommand.COMMAND_WORD:
			return prepareDeleteEvent(arguments);

		case EditEventCommand.COMMAND_WORD:
			return new EditEventParser().parse(arguments);

		case DeleteDeadlineCommand.COMMAND_WORD:
			return prepareDeleteDeadline(arguments);

		case EditDeadlineCommand.COMMAND_WORD:
			return new EditDeadlineParser().parse(arguments);

		case ClearCommand.COMMAND_WORD:
			return new ClearCommand();

		case FindCommand.COMMAND_WORD:
			return prepareFind(arguments);

		case ListCommand.COMMAND_WORD:
			return new ListCommand();

		case ExitCommand.COMMAND_WORD:
			return new ExitCommand();

		case HelpCommand.COMMAND_WORD:
			return new HelpCommand();

		default:
			return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
		}
	}

	/**
	 * Parses arguments in the context of the add Task command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareAdd(String args) {
		args = args.trim();
		final Matcher matcher = PERSON_DATA_ARGS_FORMAT.matcher(args.trim());// Todos:
																				// replace
																				// with
																				// TASK_DATA_ARGS_FORMAT
		// Validate arg string format
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
		}
		String type = idTaskType(args);
		switch (type) {
		case ("float"):
			return new AddFloatingTaskParser().parse(args);

		case ("deadline"):
			return new AddDeadlineParser().parse(args);

		case ("event"):
			return new AddEventParser().parse(args);
		}
		return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
	}

	/**
	 * Identifies the type of task.
	 * 
	 * @param args
	 * @return type of task as String
	 */
	private String idTaskType(String args) {

		// float, no date or time
		// deadline, has date or/and time
		// event has date and time and to keyword
		String[] s = args.split(" ");

		if (isEvent(s)) {
			return "event";
		}
		
		if (isDeadline(s)) {
			return "deadline";
		}
		
		if (isFloat(s)) {
			return "float";
		}

		return null;
	}

	public int wordCount(String s) {
		if (s == null) {
			return 0;
		} else {
			return s.trim().split("\\s+").length;
		}
	}
	
	/**
	 * Checks if arguments contains `to`, at least one LocalDate or LocalTime before `to` and at least one LocalDate or LocalTime after `to`. 
	 * @param args
	 * @return true if task is an event
	 */
	private boolean isEvent(String[] args) {
		boolean passCheck = false;
		// max no. of args for event is 6 (name, sd, st, ed, et, loc)
		if (args.length <= 6) {
			passCheck = true;
		}
		int toIndex = 0;
		// check for `to`
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("to")) {
				passCheck = true;
				toIndex = i;
				break;
			}
		}
		
		// check for at least one date/time input before `to`
		if (passCheck) {
			passCheck=LocalDateTimePresenceChecker(0, toIndex, args);
		}
		// check for at least one date/time input after `to`
		if (passCheck) {
			passCheck=LocalDateTimePresenceChecker(toIndex, args.length, args);
		}
		return passCheck;
	}
	
	private boolean isDeadline(String[] args) {
		boolean passCheck = false;
		//max no. of arguments for deadline is 3 (name, dd, dt)  
		if(args.length<=3) {
			passCheck=true;
		}
		//check for at least one date or time after name
		if(passCheck) {
			passCheck=LocalDateTimePresenceChecker(1, args.length, args);
		}
		return passCheck;
	}
	
	private boolean isFloat(String[] args) {
		
		//max no. of arguments for float is 2 (name, p-)
		if(args.length==2) {
			return args[1].matches("\\d");
		}
		if(args.length==1) {
			return true;
		}
		return false; 
	}
	/**
	 * Checks for the presence of at least one LocalDate or LocalTime object that can be formed from a String array
	 */
	private boolean LocalDateTimePresenceChecker(int startIndex, int endIndex, String[] args) {
		DateParser isDate = new DateParser(LocalDate.now());
		TimeParser isTime = new TimeParser(LocalTime.now());
		for (int i = startIndex; i < endIndex; i++) {
			try {
				if (isDate.parse(args[i]) instanceof LocalDate || isTime.parse(args[i]) instanceof LocalTime) {
					return true;
				}
			} catch (IllegalValueException e) {
				//do nothing.
			}
		} 
		return false;	
	}

	/**
	 * Parses arguments in the context of the delete person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareDelete(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
		}

		return new DeleteCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the delete floating task command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareDeleteFloatingTask(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteFloatingTaskCommand.MESSAGE_USAGE));
		}
		return new DeleteFloatingTaskCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the delete event command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareDeleteEvent(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEventCommand.MESSAGE_USAGE));
		}
		return new DeleteEventCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the delete deadline command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareDeleteDeadline(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(
					String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteDeadlineCommand.MESSAGE_USAGE));
		}
		return new DeleteDeadlineCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the select person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareSelect(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
		}

		return new SelectCommand(index.get());
	}

	/**
	 * Returns the specified index in the {@code command} IF a positive unsigned
	 * integer is given as the index. Returns an {@code Optional.empty()}
	 * otherwise.
	 */
	private Optional<Integer> parseIndex(String command) {
		final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
		if (!matcher.matches()) {
			return Optional.empty();
		}

		String index = matcher.group("targetIndex");
		if (!StringUtil.isUnsignedInteger(index)) {
			return Optional.empty();
		}
		return Optional.of(Integer.parseInt(index));

	}

	/**
	 * Parses arguments in the context of the find person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareFind(String args) {
		final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
		}

		// keywords delimited by whitespace
		final String[] keywords = matcher.group("keywords").split("\\s+");
		final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
		return new FindCommand(keywordSet);
	}

}
