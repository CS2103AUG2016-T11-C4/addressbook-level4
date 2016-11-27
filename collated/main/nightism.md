# smy961025@yeah.net

###### src/main/java/seedu/address/logic/LogicManager.java

``` java
    @Override
    public ObservableList<FloatingTask> getFilteredFloatingTaskList() {
        return model.getFilteredFloatingTaskList();
    }

```
###### src/main/java/seedu/address/logic/LogicManager.java

``` java
    @Override
    public ObservableList<DeadlineTask> getFilteredDeadlineTaskList() {
        return model.getFilteredDeadlineTaskList();
    }

}
```
###### src/main/java/seedu/address/logic/Logic.java

``` java
    /** Returns the filtered list of floating tasks */
    ObservableList<FloatingTask> getFilteredFloatingTaskList();

```
###### src/main/java/seedu/address/logic/Logic.java

``` java
    /** Returns the filtered list of deadline tasks */
    ObservableList<DeadlineTask> getFilteredDeadlineTaskList();

}
```
###### src/main/java/seedu/address/logic/parser/AddDeadlineParser.java

``` java
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

        // There must be at least one of { date, time }.
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

        // date (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (isDate(args.get(0))) {
            result.date = args.remove(0);
        }

        // time (optional)
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
```
###### src/main/java/seedu/address/logic/parser/AddTaskParser.java

``` java
public class AddTaskParser {

    private final AddDeadlineParser addDeadlineParser;

    private final AddEventParser addEventParser;

    private final AddFloatingTaskParser addFloatingTaskParser;

    private final Command incorrectCommand =
            new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            AddTaskCommand.MESSAGE_USAGE
            + AddDeadlineCommand.MESSAGE_USAGE
            + AddEventCommand.MESSAGE_USAGE
            + AddFloatingTaskCommand.MESSAGE_USAGE));

    public AddTaskParser() {
        this(null);
    }

    public AddTaskParser(LocalDateTime referenceDateTime) {
        addDeadlineParser = new AddDeadlineParser(referenceDateTime);
        addEventParser = new AddEventParser(referenceDateTime);
        addFloatingTaskParser = new AddFloatingTaskParser();
    }

    public Command parse(String str) {
        Command cmd;

        cmd = addEventParser.parse(str);
        if (!(cmd instanceof IncorrectCommand)) {
            return cmd;
        }

        cmd = addDeadlineParser.parse(str);
        if (!(cmd instanceof IncorrectCommand)) {
            return cmd;
        }

        cmd = addFloatingTaskParser.parse(str);
        if (!(cmd instanceof IncorrectCommand)) {
            return cmd;
        }

        return incorrectCommand;
    }
}
```
###### src/main/java/seedu/address/logic/parser/EditDeadlineParser.java

``` java
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
```
###### src/main/java/seedu/address/logic/parser/EditFloatingTaskParser.java

``` java
public class EditFloatingTaskParser {
    private static final Pattern CMD_PATTERN = Pattern.compile("^(?<index>\\d+)"
                                                                + "(\\s+n-(?<newName>[^-]+))?"
                                                                + "(\\s+p-(?<newPriority>.+))?"
                                                                + "$");

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
                if (!Priority.isValidPriority(newPriority)) {
                    throw new IllegalValueException(Priority.MESSAGE_PRIORITY_CONSTRAINTS);
                }
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
```
###### src/main/java/seedu/address/logic/parser/AddFloatingTaskParser.java

``` java
public class AddFloatingTaskParser {
    private static final Pattern ARG_PATTERN =
            Pattern.compile("\\s*\"(?<quotedArg>[^\"]+)\"\\s*|\\s*(?<unquotedArg>[^\\s]+)\\s*");

    private final Command incorrectCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                                  AddFloatingTaskCommand.MESSAGE_USAGE));

    private static final Pattern PRIORITY_PATTERN = Pattern.compile("p-(?<priority>\\d)");

    public AddFloatingTaskParser() {
    }

    public Command parse(String str) {
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
        if (isPriorityFormat(args.get(0))) {
            Matcher matcher = PRIORITY_PATTERN.matcher(args.remove(0));
            matcher.matches();
            result.priority = matcher.group("priority");
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

    private static boolean isPriorityFormat(String str) {
        Matcher matcher = PRIORITY_PATTERN.matcher(str.trim());
        return matcher.matches();
    }

}
```
###### src/main/java/seedu/address/logic/commands/AddFloatingTaskCommand.java

``` java
/**
 * Adds an event task to the task book.
 */
public class AddFloatingTaskCommand extends AddTaskCommand {

    public static final String MESSAGE_USAGE = "Parameters for adding floating task: \"NAME\" [p-Priority] \n"
            + "Example: " + COMMAND_WORD + " \"Floating Task Name\" p-3 \n";

    private final FloatingTask floatingTask;

    public AddFloatingTaskCommand(String name, String priority) throws IllegalValueException {
        this.floatingTask = new FloatingTask(new Name(name), new Priority(priority));
    }

    @Override
    public FloatingTask getTask() {
        return floatingTask;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        model.addFloatingTask(floatingTask);
        return new CommandResult(String.format(MESSAGE_SUCCESS, floatingTask));
    }

}
```
###### src/main/java/seedu/address/logic/commands/MarkDeadlineFinishedCommand.java

``` java
public class MarkDeadlineFinishedCommand extends Command {

    public static final String COMMAND_WORD = "fin-deadline";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Mark a deadline as finished and hide it from the deadline list view. \n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_MARK_TASK_FINISHED_SUCCESS = "Deadline task finished: %1$s";

    private final int targetIndex;

    public MarkDeadlineFinishedCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        DeadlineTask oldDeadlineTask;
        try {
            oldDeadlineTask = model.getDeadlineTask(targetIndex - 1);
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        DeadlineTask finishedDeadlineTask;
        finishedDeadlineTask = new DeadlineTask(oldDeadlineTask.name,
                                                oldDeadlineTask.getDue(),
                                                true);

        try {
            model.setDeadlineTask(targetIndex - 1, finishedDeadlineTask);
        } catch (IllegalValueException e) {
            throw new AssertionError("The target deadline cannot be missing", e);
        }

        return new CommandResult(String.format(MESSAGE_MARK_TASK_FINISHED_SUCCESS, finishedDeadlineTask));

    }
}
```
###### src/main/java/seedu/address/logic/commands/EditFloatingTaskCommand.java

``` java
public class EditFloatingTaskCommand extends Command {

    public static final String COMMAND_WORD = "edit-float";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the floating task identified by the index number used in the filtered floating task listing.\n"
            + "Parameters: INDEX [n-NAME] [p-PRIORITY]"
            + "Example: " + COMMAND_WORD + " 1 p-2";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Floting task edited: %1$s";

    public final int targetIndex;

    public final Name newName;

    public final Priority newPriority;

    public EditFloatingTaskCommand(int targetIndex, String newName, String newPriority) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.newName = newName != null ? new Name(newName) : null;
        this.newPriority = newPriority != null ? new Priority(newPriority) : null;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public Name getNewName() {
        return this.newName;
    }

    public Priority getNewPriority() {
        return this.newPriority;
    }

    @Override
    public CommandResult execute() {
        FloatingTask oldFloatingTask;
        try {
            oldFloatingTask = model.getFloatingTask(targetIndex - 1);
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        FloatingTask newFloatingTask;

        newFloatingTask = new FloatingTask(
                newName != null ? newName : oldFloatingTask.name,
                newPriority != null ? newPriority : oldFloatingTask.getPriority()
        );

        try {
            model.setFloatingTask(targetIndex - 1, newFloatingTask);
        } catch (IllegalValueException e) {
            throw new AssertionError("The target floating task cannot be missing", e);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, newFloatingTask));
    }

}
```
###### src/main/java/seedu/address/logic/commands/AddTaskCommand.java

``` java
/**
 * Adds an event task to the task book.
 */
public abstract class AddTaskCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an task to the TaskTracker.\n";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";

    public abstract Task getTask();

    @Override
    public abstract CommandResult execute();

}
```
###### src/main/java/seedu/address/logic/commands/DeleteDeadlineCommand.java

``` java
public class DeleteDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "del-deadline";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the deadline identified by the index number used in the filtered deadline listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted deadline: %1$s";

    private final int targetIndex;

    public DeleteDeadlineCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        try {
            final DeadlineTask deletedTask = model.removeDeadlineTask(targetIndex - 1);
            return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, deletedTask));
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

}
```
###### src/main/java/seedu/address/logic/commands/DeleteFloatingTaskCommand.java

``` java
public class DeleteFloatingTaskCommand extends Command {

    public static final String COMMAND_WORD = "del-float";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the floating task identified by the index number used in the filtered floating task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted floating task: %1$s";

    public final int targetIndex;

    public DeleteFloatingTaskCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        try {
            final FloatingTask deletedTask = model.removeFloatingTask(targetIndex - 1);
            return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, deletedTask));
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

}
```
###### src/main/java/seedu/address/logic/commands/EditDeadlineCommand.java

``` java
public class EditDeadlineCommand extends Command {

    public static final String COMMAND_WORD = "edit-deadline";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the deadline identified by the index number used in the filtered deadline listing.\n"
            + "Parameters: INDEX [dd-NEW_DUE_DATE] [dt-NEW_DUE_TIME] [n-NEW_NAME]"
            + "Example: " + COMMAND_WORD + " 1 dd-12/12/2017 dt-8pm";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Deadline edited: %1$s";

    private final int targetIndex;

    private final Name newName;

    private final LocalDate newDate;

    private final LocalTime newTime;

    public EditDeadlineCommand(int targetIndex, String newName, LocalDate newDate, LocalTime newTime) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.newName = newName != null ? new Name(newName) : null;
        this.newDate = newDate;
        this.newTime = newTime;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public LocalDate getNewDate() {
        return newDate;
    }

    public LocalTime getNewTime() {
        return newTime;
    }

    @Override
    public CommandResult execute() {
        DeadlineTask oldDeadlineTask;
        try {
            oldDeadlineTask = model.getDeadlineTask(targetIndex - 1);
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        DeadlineTask newDeadlineTask;
        newDeadlineTask = new DeadlineTask(
                newName != null ? newName : oldDeadlineTask.name,
                LocalDateTime.of(
                        newDate != null ? newDate : oldDeadlineTask.getDue().toLocalDate(),
                        newTime != null ? newTime : oldDeadlineTask.getDue().toLocalTime()
                )
        );

        try {
            model.setDeadlineTask(targetIndex - 1, newDeadlineTask);
        } catch (IllegalValueException e) {
            throw new AssertionError("The target deadline cannot be missing", e);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, newDeadlineTask));
    }

}
```
###### src/main/java/seedu/address/logic/commands/AddDeadlineCommand.java

``` java
/**
 * Adds an deadline task to the task book.
 */
public class AddDeadlineCommand extends AddTaskCommand {

    public static final String MESSAGE_USAGE = "Parameters for adding deadline: \"NAME\" <DATE> <TIME> \n"
            + "Example: " + COMMAND_WORD + " \"Deadline Name\" 12/12/2016 2pm \n";

    private final DeadlineTask deadlineTask;

    public AddDeadlineCommand(String name, LocalDateTime due) throws IllegalValueException {
        deadlineTask = new DeadlineTask(name, due);
    }

    @Override
    public DeadlineTask getTask() {
        return deadlineTask;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        model.addDeadlineTask(deadlineTask);
        return new CommandResult(String.format(MESSAGE_SUCCESS, deadlineTask));
    }

}
```
###### src/main/java/seedu/address/model/filter/DeadlineTaskFinished.java

``` java
public class DeadlineTaskFinished implements Predicate<DeadlineTask> {

    @Override
    public boolean test(DeadlineTask task) {
        return !task.isFinished();
    }

}
```
###### src/main/java/seedu/address/model/TaskBook.java

``` java
    @Override
    public ObservableList<FloatingTask> getFloatingTasks() {
        return FXCollections.unmodifiableObservableList(floatingTasks);
    }

    public void setFloatingTasks(Collection<FloatingTask> floatingTasks) {
        this.floatingTasks.setAll(floatingTasks);
    }

    public void addFloatingTask(FloatingTask floatingTask) {
        floatingTasks.add(floatingTask);
    }

    /**
     * Remove the FloatingTask at position `index` in the list. Return the removed FloatingTask.
     */
    public FloatingTask removeFloatingTask(int index) {
        return floatingTasks.remove(index);
    }

    public void setFloatingTask(int index, FloatingTask newFloatingTask) {
        floatingTasks.set(index, newFloatingTask);
    }

    //// event task operations

```
###### src/main/java/seedu/address/model/TaskBook.java

``` java
    @Override
    public ObservableList<DeadlineTask> getDeadlineTasks() {
        return FXCollections.unmodifiableObservableList(deadlineTasks);
    }

    public void setDeadlineTasks(Collection<DeadlineTask> deadlineTasks) {
        this.deadlineTasks.setAll(deadlineTasks);
    }

    public void addDeadlineTask(DeadlineTask deadlineTask) {
        deadlineTasks.add(deadlineTask);
    }

    /**
     * Removes the DeadlineTask at position `index` in the list. Returns the removed DeadlineTask.
     */
    public DeadlineTask removeDeadlineTask(int index) {
        return deadlineTasks.remove(index);
    }

    public void setDeadlineTask(int index, DeadlineTask newDeadlineTask) {
        deadlineTasks.set(index, newDeadlineTask);
    }

    //// util methods

```
###### src/main/java/seedu/address/model/task/DeadlineTask.java

``` java
public class DeadlineTask extends Task {

    private static final String FMT_STRING = "DeadlineTask[name=%s, due=%s, finished=%s]";

    private final LocalDateTime due;

    private final boolean finished;

    public DeadlineTask(Name name, LocalDateTime due, boolean finished) {
        super(name);
        assert due != null;
        this.due = due;
        this.finished = finished;
    }

    public DeadlineTask(Name name, LocalDateTime due) {
        this(name, due, false);
    }

    public DeadlineTask(String name, LocalDateTime due) throws IllegalValueException {
        this(new Name(name), due, false);
    }

    public LocalDateTime getDue() {
        return due;
    }

    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
               || (other instanceof DeadlineTask
               && name.equals(((DeadlineTask)other).name)
               && due.equals(((DeadlineTask)other).due));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, due);
    }

    @Override
    public String toString() {
        return String.format(FMT_STRING, name, due, finished);
    }

}
```
###### src/main/java/seedu/address/model/task/Priority.java

``` java
public class Priority {

    public static final int UPPER_BOUND = 5;
    public static final int LOWER_BOUND = 0;

    public static final String MESSAGE_PRIORITY_CONSTRAINTS =
            "priority should be an integer ranges from " + LOWER_BOUND + " to " + UPPER_BOUND + ".";
    public static final String PRIORITY_VALIDATION_REGEX = "\\p{Digit}";

    private String priority;

    /**
     * Validates given priority.
     *
     * @throws IllegalValueException if given priority string is invalid.
     */
    public Priority (String priority) throws IllegalValueException {
        assert priority != null;
        priority = priority.trim();
        if (!isValidPriority(priority)) {
            throw new IllegalValueException(MESSAGE_PRIORITY_CONSTRAINTS);
        }
        this.priority = priority;
    }

    /**
     * Returns true if a given string is a valid priority.
     */
    public static boolean isValidPriority(String test) {
        if (test.matches(PRIORITY_VALIDATION_REGEX)) {
            return (Integer.parseInt(test) >= LOWER_BOUND)
                    && (Integer.parseInt(test) <= UPPER_BOUND);
        } else {
            return false;
        }
    }

    public int toInteger() {
        return Integer.parseInt(this.priority);
    }

    @Override
    public String toString() {
        return priority;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                && this.priority.equals(((Priority) other).priority)); // state check
    }

    @Override
    public int hashCode() {
        return priority.hashCode();
    }

}
```
###### src/main/java/seedu/address/model/task/FloatingTask.java

``` java
public class FloatingTask extends Task {

    private static final String FMT_STRING = "FloatingTask[name=%s, priority=%s]";

    private Priority priority;

    public FloatingTask(Name name, Priority priority) {
        super(name);
        assert priority != null;
        this.priority = priority;
    }

    public FloatingTask(String name, Priority priority) throws IllegalValueException {
        this(new Name(name), priority);
    }

    public FloatingTask(String name) throws IllegalValueException {
        this(new Name(name), new Priority("0"));
    }

    public Priority getPriority() {
        return this.priority;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
            || (other instanceof FloatingTask
            && name.equals(((FloatingTask)other).name)
            && priority.equals(((FloatingTask)other).priority));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority);
    }

    @Override
    public String toString() {
        return String.format(FMT_STRING, name, priority);
    }
}
```
###### src/main/java/seedu/address/model/ModelManager.java

``` java
    @Override
    public synchronized void addFloatingTask(FloatingTask floatingTask) {
        taskBook.addFloatingTask(floatingTask);
        setFloatingTaskFilter(null);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized FloatingTask getFloatingTask(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredFloatingTasks.get(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    private int getFloatingTaskSourceIndex(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredFloatingTasks.getSourceIndex(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    @Override
    public synchronized FloatingTask removeFloatingTask(int indexInFilteredList) throws IllegalValueException {
        final FloatingTask removedFloating = taskBook.removeFloatingTask(getFloatingTaskSourceIndex(indexInFilteredList));
        indicateTaskBookChanged();
        return removedFloating;
    }

    @Override
    public synchronized void setFloatingTask(int indexInFilteredList, FloatingTask newFloatingTask)
            throws IllegalValueException {
        taskBook.setFloatingTask(getFloatingTaskSourceIndex(indexInFilteredList), newFloatingTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<FloatingTask> getFilteredFloatingTaskList() {
        return filteredFloatingTasks;
    }

    @Override
    public void setFloatingTaskFilter(Predicate<? super FloatingTask> predicate) {
        filteredFloatingTasks.setPredicate(predicate);
    }

    //// Event tasks

```
###### src/main/java/seedu/address/model/ModelManager.java

``` java
    private int getEventTaskSourceIndex(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredEventTasks.getSourceIndex(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

```
###### src/main/java/seedu/address/model/ModelManager.java

``` java
    @Override
    public synchronized void addDeadlineTask(DeadlineTask deadlineTask) {
        assert deadlineTask.isFinished() == false;
        taskBook.addDeadlineTask(deadlineTask);
        setDeadlineTaskFilter(null);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized DeadlineTask getDeadlineTask(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredDeadlineTasks.get(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    private int getDeadlineTaskSourceIndex(int indexInFilteredList) throws IllegalValueException {
        try {
            return filteredDeadlineTasks.getSourceIndex(indexInFilteredList);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalValueException("invalid index");
        }
    }

    @Override
    public synchronized DeadlineTask removeDeadlineTask(int indexInFilteredList) throws IllegalValueException {
        final DeadlineTask removedDeadline = taskBook.removeDeadlineTask(getDeadlineTaskSourceIndex(indexInFilteredList));
        indicateTaskBookChanged();
        return removedDeadline;
    }

    @Override
    public synchronized void setDeadlineTask(int indexInFilteredList, DeadlineTask newDeadlineTask)
            throws IllegalValueException {
        taskBook.setDeadlineTask(getDeadlineTaskSourceIndex(indexInFilteredList), newDeadlineTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<DeadlineTask> getFilteredDeadlineTaskList() {
        return filteredDeadlineTasks;
    }

    @Override
    public void setDeadlineTaskFilter(Predicate<? super DeadlineTask> predicate) {
        filteredDeadlineTasks.setPredicate(predicate);
    }

}
```
###### src/main/java/seedu/address/model/Model.java

``` java
    /* Adds the given floating task */
    void addFloatingTask(FloatingTask floatingTask);

    /** Retrieves the given Floating task from the specified index in the filtered Floating task list */
    FloatingTask getFloatingTask(int indexInFilteredList) throws IllegalValueException;

    /** Removes the given Floating task and returns it. */
    FloatingTask removeFloatingTask(int indexInFilteredList) throws IllegalValueException;

    /** Replaces the given Floating task with a new Floating task */
    void setFloatingTask(int indexInFilteredList, FloatingTask newFloatingTask) throws IllegalValueException;

    /** Returns the filtered Floating task list as an unmodifiable ObservableList */
    ObservableList<FloatingTask> getFilteredFloatingTaskList();

    /**
     * Updates the filter of the filtered Floating task list to filter by the given predicate.
     *
     * If predicate is null, the filtered Floating task list will be populated with all Floating tasks.
     */
    void setFloatingTaskFilter(Predicate<? super FloatingTask> predicate);

    //// Event Tasks

```
###### src/main/java/seedu/address/model/Model.java

``` java
    /** Adds the given deadline task */
    void addDeadlineTask(DeadlineTask deadlineTask);

    /** Retrieves the given deadline task from the specified index in the filtered deadline task list */
    DeadlineTask getDeadlineTask(int indexInFilteredList) throws IllegalValueException;

    /** Removes the given deadline task and returns it. */
    DeadlineTask removeDeadlineTask(int indexInFilteredList) throws IllegalValueException;

    /** Replaces the given deadline task with a new deadline task */
    void setDeadlineTask(int indexInFilteredList, DeadlineTask newDeadlineTask) throws IllegalValueException;

    /** Returns the filtered deadline task list as an unmodifiable ObservableList */
    ObservableList<DeadlineTask> getFilteredDeadlineTaskList();

    /**
     * Updates the filter of the filtered deadline task list to filter by the given predicate.
     *
     * If predicate is null, the filtered deadline task list will be populated with all deadline tasks.
     */
    void setDeadlineTaskFilter(Predicate<? super DeadlineTask> predicate);

}
```
###### src/main/java/seedu/address/model/ReadOnlyTaskBook.java

``` java
    /**
    * Returns an unmodifiable view of the FloatingTasks list.
    */
    ObservableList<FloatingTask> getFloatingTasks();

```
###### src/main/java/seedu/address/model/ReadOnlyTaskBook.java

``` java
    /**
     * Returns an unmodifiable view of the DeadlineTasks list.
     */
    ObservableList<DeadlineTask> getDeadlineTasks();

}
```
###### src/main/java/seedu/address/ui/FloatingTaskListCard.java

``` java
public class FloatingTaskListCard extends UiPart<Pane> {

    private static final String FXML = "/view/FloatingTaskListCard.fxml";

    @FXML
    private Label indexLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priorityLabel;

    /**
     * @param floatingTask: The floating task to display. Can be null to not display anything.
     */
    public FloatingTaskListCard(FloatingTask floatingTask, int index) {
        super(FXML);
        if (floatingTask != null) {
            indexLabel.setText(index + ". ");
            nameLabel.setText(floatingTask.name.toString());
            priorityLabel.setText(floatingTask.getPriority().toString());
        } else {
            getRoot().setVisible(false);
        }
    }

}
```
###### src/main/java/seedu/address/ui/FloatingTaskListPane.java

``` java
public class FloatingTaskListPane extends UiPart<Pane> {

    private static final String FXML = "/view/FloatingTaskListPane.fxml";

    @FXML
    private ListView<FloatingTask> floatingTaskListView;

    public FloatingTaskListPane(ObservableList<FloatingTask> floatingTaskList) {
        super(FXML);
        floatingTaskListView.setItems(floatingTaskList);
        floatingTaskListView.setCellFactory(listView -> new FloatingTaskListCell());
    }

    private static class FloatingTaskListCell extends ListCell<FloatingTask> {
        @Override
        protected void updateItem(FloatingTask floatingTask, boolean empty) {
            super.updateItem(floatingTask, empty);
            final FloatingTaskListCard card = new FloatingTaskListCard(floatingTask, getIndex() + 1);
            setGraphic(card.getRoot());
        }
    }
}
```
###### src/main/java/seedu/address/storage/JsonTaskBookMixin.java

``` java
public abstract class JsonTaskBookMixin {
    JsonTaskBookMixin(@JsonProperty("tasks") List<Task> tasks,
                      @JsonProperty("floatingTasks") List<FloatingTask> floatingTasks,
                      @JsonProperty("eventTasks") List<EventTask> eventTasks,
                      @JsonProperty("deadlineTasks") List<DeadlineTask> deadlineTasks) {
    }
}
```
###### src/main/java/seedu/address/storage/JsonFloatingTaskMixin.java

``` java
@JsonPropertyOrder({"name", "priority"})
public abstract class JsonFloatingTaskMixin {

    JsonFloatingTaskMixin(@JsonProperty("name") Name name,
            @JsonProperty("priority") Priority priority) {
    }

}
```
###### src/main/java/seedu/address/storage/JsonDeadlineTaskMixin.java

``` java
@JsonPropertyOrder({"name", "due", "finished"})
public abstract class JsonDeadlineTaskMixin {

    JsonDeadlineTaskMixin(@JsonProperty("name") Name name,
                       @JsonProperty("due") LocalDateTime due,
                       @JsonProperty("finished") boolean finished) {
    }

}
```
###### src/main/java/seedu/address/storage/JsonPriorityMixin.java

``` java
public abstract class JsonPriorityMixin {
    @JsonCreator
    JsonPriorityMixin(String priority) {}

    @JsonValue
    public abstract String toString();
}
```
