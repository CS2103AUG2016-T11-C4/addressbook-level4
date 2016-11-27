# pyokagan@gmail.com

###### src/main/java/seedu/address/MainApp.java

``` java
/**
 * The main entry point to the application.
 */
public class MainApp extends Application {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public static final Version VERSION = new Version(0, 3, 0, true);

    /** Name of the application */
    public static final String NAME = "Task Tracker";

    private final String configPath;
    private Ui ui;
    private Logic logic;
    private Storage storage;
    private Model model;

    public MainApp() {
        this(null);
    }

    public MainApp(String configPath) {
        this.configPath = configPath;
    }

```
###### src/main/java/seedu/address/MainApp.java

``` java
    private Model initModelManager(Storage storage, ReadOnlyConfig config) {
        Optional<ReadOnlyTaskBook> addressBookOptional;
        ReadOnlyTaskBook initialData;
        try {
            addressBookOptional = storage.readTaskBook();
            if (!addressBookOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with an empty TaskBook");
            }
            initialData = addressBookOptional.orElse(new TaskBook());
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty TaskBook");
            initialData = new TaskBook();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. . Will be starting with an empty TaskBook");
            initialData = new TaskBook();
        }

        return new ModelManager(config, initialData);
    }

```
###### src/main/java/seedu/address/MainApp.java

``` java
    private ConfigStorage initConfigStorage(String configFilePath) {
        if (configFilePath == null) {
            configFilePath = getApplicationParameter("config");
        }
        if (configFilePath == null) {
            configFilePath = Config.DEFAULT_CONFIG_FILE;
        } else {
            logger.info("Custom Config file specified " + configFilePath);
        }

        logger.info("Using config file: " + configFilePath);
        return new JsonConfigStorage(configFilePath);
    }

```
###### src/main/java/seedu/address/logic/LogicManager.java

``` java
    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        final TaskBookChangeListener taskBookListener = new TaskBookChangeListener(model.getAddressBook());
        final Config oldConfig = new Config(model.getConfig());
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        final CommandResult result = command.execute();
        updateConfigStorage(oldConfig);
        updateTaskBookStorage(taskBookListener);
        return result;
    }

    private void updateTaskBookStorage(TaskBookChangeListener listener) {
        try {
            if (listener.getHasChanged()) {
                logger.info("Task book data changed, saving to file");
                storage.saveTaskBook(model.getAddressBook());
            }
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    private void updateConfigStorage(Config oldConfig) {
        final ReadOnlyConfig newConfig = model.getConfig();
        try {
            if (!oldConfig.equals(newConfig)) {
                logger.info("Config changed, saving to file");
                storage.saveConfig(newConfig);
            }

            if (!oldConfig.getTaskBookFilePath().equals(newConfig.getTaskBookFilePath())) {
                logger.info("Task book file path changed, moving task book");
                storage.moveTaskBook(newConfig.getTaskBookFilePath());
            }
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

```
###### src/main/java/seedu/address/logic/LogicManager.java

``` java
    @Override
    public ObservableList<EventTask> getFilteredEventTaskList() {
        return model.getFilteredEventTaskList();
    }

```
###### src/main/java/seedu/address/logic/Logic.java

``` java
    /** Returns the filtered list of tasks */
    ObservableList<Task> getFilteredTaskList();

```
###### src/main/java/seedu/address/logic/Logic.java

``` java
    /** Returns the filtered list of event tasks */
    ObservableList<EventTask> getFilteredEventTaskList();

```
###### src/main/java/seedu/address/logic/parser/AddEventParser.java

``` java
public class AddEventParser {

    private static final Pattern ARG_PATTERN = Pattern.compile("\\s*\"(?<quotedArg>[^\"]+)\"\\s*|\\s*(?<unquotedArg>[^\\s]+)\\s*");

    private final Command incorrectCommand = new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));

    private final LocalDateTime referenceDateTime;

    public AddEventParser() {
        this(null);
    }

    public AddEventParser(LocalDateTime referenceDateTime) {
        this.referenceDateTime = referenceDateTime;
    }

    public Command parse(String str) {
        final ParseResult args;
        try {
            args = parseArguments(str);
        } catch (IllegalValueException e) {
            return incorrectCommand;
        }

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
            return new AddEventCommand(args.name,
                                       LocalDateTime.of(startDate, startTime),
                                       LocalDateTime.of(endDate, endTime));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }

    private static class ParseResult {
        String name;
        String startDate;
        String startTime;
        String endDate;
        String endTime;
    }

    private static ParseResult parseArguments(String str) throws IllegalValueException {
        final ParseResult result = new ParseResult();
        final ArrayList<String> args = splitArgs(str);

        // name
        if (args.isEmpty()) {
            throw new IllegalValueException("expected name");
        }
        result.name = args.remove(0);

        // startDate (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (isDate(args.get(0))) {
            result.startDate = args.remove(0);
        }

        // startTime (optional)
        if (args.isEmpty()) {
            return result;
        }
        if (isTime(args.get(0))) {
            result.startTime = args.remove(0);
        }

        // Check Keyword "to"
        if (args.isEmpty()) {
            throw new IllegalValueException("expected ending time or ending date");
        }
        if (args.get(0).equals("to")) {
            args.remove(0);
        } else {
            throw new IllegalValueException("expected keyword \"to\"");
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
###### src/main/java/seedu/address/logic/parser/EditEventParser.java

``` java
public class EditEventParser {
    private static final Pattern CMD_PATTERN = Pattern.compile("^(?<index>\\d+)"
                                                               + "(\\s+sd-(?<newStartDate>[^\\s]+))?"
                                                               + "(\\s+st-(?<newStartTime>[^\\s]+))?"
                                                               + "(\\s+ed-(?<newEndDate>[^\\s]+))?"
                                                               + "(\\s+et-(?<newEndTime>[^\\s]+))?"
                                                               + "(\\s+n-(?<newName>.+))?"
                                                               + "$");

    private final LocalDateTime referenceDateTime;

    public EditEventParser() {
        this(null);
    }

    public EditEventParser(LocalDateTime referenceDateTime) {
        this.referenceDateTime = referenceDateTime;
    }

    public Command parse(String args) {
        final Matcher matcher = CMD_PATTERN.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE));
        }

        final String indexString = matcher.group("index").trim();
        if (!StringUtil.isUnsignedInteger(indexString)) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE));
        }
        final int index = Integer.parseInt(indexString);

        final DateTimeParser parser = referenceDateTime != null ? new DateTimeParser(referenceDateTime)
                                                                : new DateTimeParser();
        LocalDate newStartDate = null;
        LocalTime newStartTime = null;
        LocalDate newEndDate = null;
        LocalTime newEndTime = null;
        String newName = matcher.group("newName");
        try {
            if (matcher.group("newStartDate") != null) {
                newStartDate = parser.parseDate(matcher.group("newStartDate"));
            }
            if (matcher.group("newStartTime") != null) {
                newStartTime = parser.parseTime(matcher.group("newStartTime"));
            }
            if (matcher.group("newEndDate") != null) {
                newEndDate = parser.parseDate(matcher.group("newEndDate"));
            }
            if (matcher.group("newEndTime") != null) {
                newEndTime = parser.parseTime(matcher.group("newEndTime"));
            }
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }

        try {
            return new EditEventCommand(index, newName, newStartDate, newStartTime, newEndDate, newEndTime);
        } catch (IllegalValueException e) {
            return new IncorrectCommand(e.getMessage());
        }
    }

}
```
###### src/main/java/seedu/address/logic/parser/DateParser.java

``` java
/**
 * A parser for dates in day/month/year format.
 */
public class DateParser {

    private final LocalDate referenceDate;

    private final DateTimeFormatter dateFormatter;

    public DateParser(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
        dateFormatter = new DateTimeFormatterBuilder()
                .appendPattern("d[/M[/uuuu]]")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, this.referenceDate.getMonthValue())
                .parseDefaulting(ChronoField.YEAR, this.referenceDate.getYear())
                .toFormatter();
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public LocalDate parse(String str) throws IllegalValueException {
        final Optional<LocalDate> nameDate = parseAsName(str.trim());
        if (nameDate.isPresent()) {
            return nameDate.get();
        }
        try {
            return LocalDate.parse(str.trim(), dateFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(e.toString());
        }
    }

    private Optional<LocalDate> parseAsName(String name) {
        switch (name) {
        case "tdy": // today
            return Optional.of(referenceDate);
        case "tmr": // tomorrow
            return Optional.of(referenceDate.plusDays(1));
        case "yst": // yesterday
            return Optional.of(referenceDate.minusDays(1));
        default:
            return Optional.absent();
        }
    }

}
```
###### src/main/java/seedu/address/logic/parser/Parser.java

``` java
    /**
     * Parses arguments in the context of the delete event command.
     *
     * @param args full command args string
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

```
###### src/main/java/seedu/address/logic/parser/TimeParser.java

``` java
/**
 * A parser for 12-hour clock times.
 */
public class TimeParser {

    private static final Pattern PATTERN_TIME = Pattern.compile("(?<hour>\\d{1,2})"
            + "(?:[.:](?<minute>\\d{2}))?(?<ampm>am|pm)");

    private final LocalTime referenceTime;

    public TimeParser(LocalTime referenceTime) {
        this.referenceTime = referenceTime;
    }

    public LocalTime getReferenceTime() {
        return referenceTime;
    }

    public LocalTime parse(String str) throws IllegalValueException {
        final Matcher matcher = PATTERN_TIME.matcher(str.trim());
        if (!matcher.matches()) {
            throw new IllegalValueException("invalid time format");
        }

        int hour = Integer.parseInt(matcher.group("hour"));
        if (hour > 12) {
            throw new IllegalValueException("invalid hour: " + hour);
        } else if (hour == 12) {
            hour = 0;
        }

        final int minute = matcher.group("minute") != null ? Integer.parseInt(matcher.group("minute")) : 0;
        if (minute >= 60) {
            throw new IllegalValueException("invalid minute: " + minute);
        }

        final boolean isPM = matcher.group("ampm").equals("pm");
        return LocalTime.of(hour + (isPM ? 12 : 0), minute);
    }

}
```
###### src/main/java/seedu/address/logic/parser/SetDataDirectoryParser.java

``` java
/**
 * Parser for the "setdatadir" command.
 */
public class SetDataDirectoryParser {

    public static final String COMMAND_WORD = "setdatadir";

    public static final String MESSAGE_USAGE = "Parameters: NEW_DIRECTORY\n"
            + "Example: " + COMMAND_WORD + " /my/new/directory";

    private static final String MESSAGE_INVALID_PATH = "%s is not a valid path.";

    private static final String MESSAGE_ABSOLUTE = "%s is not an absolute path.";

    public Command parse(String args) {
        args = args.trim();
        if (args.isEmpty()) {
            return new IncorrectCommand(MESSAGE_USAGE);
        }
        final File newDir;
        try {
            newDir = Paths.get(args).toFile();
        } catch (InvalidPathException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_PATH, args));
        }
        if (!newDir.isAbsolute()) {
            return new IncorrectCommand(String.format(MESSAGE_ABSOLUTE, args));
        }
        return new SetDataDirectoryCommand(newDir);
    }

}
```
###### src/main/java/seedu/address/logic/parser/DateTimeParser.java

``` java
/**
 * A date and time parser. See {@link DateParser} and {@link TimeParser}.
 */
public class DateTimeParser {

    private final DateParser dateParser;

    private final TimeParser timeParser;

    public DateTimeParser() {
        this(LocalDateTime.now());
    }

    public DateTimeParser(LocalDateTime referenceDateTime) {
        assert referenceDateTime != null;
        dateParser = new DateParser(referenceDateTime.toLocalDate());
        timeParser = new TimeParser(referenceDateTime.toLocalTime());
    }

    public LocalDateTime getReferenceDateTime() {
        return LocalDateTime.of(dateParser.getReferenceDate(), timeParser.getReferenceTime());
    }

    public LocalDate parseDate(String str) throws IllegalValueException {
        return dateParser.parse(str);
    }

    public LocalTime parseTime(String str) throws IllegalValueException {
        return timeParser.parse(str);
    }

}
```
###### src/main/java/seedu/address/logic/commands/EditEventCommand.java

``` java
public class EditEventCommand extends Command {

    public static final String COMMAND_WORD = "edit-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the event identified by the index number used in the filtered event listing.\n"
            + "Parameters: INDEX [sd-NEW_START_TIME | st-NEW_START_DATE | ed-NEW_END_DATE | et-NEW_END_TIME | n-NEW_NAME]"
            + "Example: " + COMMAND_WORD + " 1 st-4pm et-8pm";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Event edited: %1$s";

    public final int targetIndex;

    public final Name newName;

    public final LocalDate newStartDate;

    public final LocalTime newStartTime;

    public final LocalDate newEndDate;

    public final LocalTime newEndTime;

    public EditEventCommand(int targetIndex, String newName, LocalDate newStartDate, LocalTime newStartTime,
                            LocalDate newEndDate, LocalTime newEndTime) throws IllegalValueException {
        this.targetIndex = targetIndex;
        this.newName = newName != null ? new Name(newName) : null;
        this.newStartDate = newStartDate;
        this.newStartTime = newStartTime;
        this.newEndDate = newEndDate;
        this.newEndTime = newEndTime;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public LocalDate getNewStartDate() {
        return newStartDate;
    }

    public LocalTime getNewStartTime() {
        return newStartTime;
    }

    public LocalDate getNewEndDate() {
        return newEndDate;
    }

    public LocalTime getNewEndTime() {
        return newEndTime;
    }

    @Override
    public CommandResult execute() {
        EventTask oldEventTask;
        try {
            oldEventTask = model.getEventTask(targetIndex - 1);
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        EventTask newEventTask;
        try {
            newEventTask = new EventTask(
                    newName != null ? newName : oldEventTask.name,
                    LocalDateTime.of(
                            newStartDate != null ? newStartDate : oldEventTask.getStart().toLocalDate(),
                            newStartTime != null ? newStartTime : oldEventTask.getStart().toLocalTime()
                    ),
                    LocalDateTime.of(
                            newEndDate != null ? newEndDate : oldEventTask.getEnd().toLocalDate(),
                            newEndTime != null ? newEndTime : oldEventTask.getEnd().toLocalTime()
                    )
            );
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(e.getMessage());
        }

        try {
            model.setEventTask(targetIndex - 1, newEventTask);
        } catch (IllegalValueException e) {
            throw new AssertionError("The target event cannot be missing", e);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, newEventTask));
    }

}
```
###### src/main/java/seedu/address/logic/commands/AddEventCommand.java

``` java
/**
 * Adds an event task to the task book.
 */
public class AddEventCommand extends AddTaskCommand {

    public static final String MESSAGE_USAGE = "Parameters for adding event: \"NAME\" <STARTING_DATE> <STARTING_TIME> to <ENDING_DATE> <ENDING_TIME>\n"
            + "Example: " + COMMAND_WORD + " \"Event Name\" 12/12/2016 12pm to 2pm \n";

    private final EventTask eventTask;

    public AddEventCommand(String name, LocalDateTime start, LocalDateTime end) throws IllegalValueException {
        eventTask = new EventTask(name, start, end);
    }

```
###### src/main/java/seedu/address/logic/commands/AddEventCommand.java

``` java
    @Override
    public CommandResult execute() {
        assert model != null;
        model.addEventTask(eventTask);
        return new CommandResult(String.format(MESSAGE_SUCCESS, eventTask));
    }

}
```
###### src/main/java/seedu/address/logic/commands/SetDataDirectoryCommand.java

``` java
/**
 * Command that changes the directory where application data is stored.
 */
public class SetDataDirectoryCommand extends Command {

    private static final String MESSAGE_CHANGE = "Data directory changed to: %s";

    private final File newDir;

    public SetDataDirectoryCommand(File newDir) {
        assert newDir.isAbsolute();
        this.newDir = newDir;
    }

    @Override
    public CommandResult execute() {
        final File newTaskBookFile = new File(newDir, "taskbook.json");
        model.setTaskBookFilePath(newTaskBookFile.getAbsolutePath());
        return new CommandResult(String.format(MESSAGE_CHANGE, newDir.getAbsolutePath()));
    }

}
```
###### src/main/java/seedu/address/logic/commands/DeleteEventCommand.java

``` java
public class DeleteEventCommand extends Command {

    public static final String COMMAND_WORD = "del-event";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event identified by the index number used in the filtered event listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted event: %1$s";

    public final int targetIndex;

    public DeleteEventCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        try {
            final EventTask deletedTask = model.removeEventTask(targetIndex - 1);
            return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, deletedTask));
        } catch (IllegalValueException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

}
```
###### src/main/java/seedu/address/model/filter/NameContainsKeywordsPredicate.java

``` java
public class NameContainsKeywordsPredicate implements Predicate<Task> {
    private final Set<String> keywords;

    public NameContainsKeywordsPredicate(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Task task) {
        return keywords.stream()
                .filter(keyword -> StringUtil.containsIgnoreCase(task.name.toString(), keyword))
                .findAny()
                .isPresent();
    }

}
```
###### src/main/java/seedu/address/model/TaskBook.java

``` java
    public void resetData(ReadOnlyTaskBook newData) {
        setTasks(newData.getTasks());
    }

    //// task operations

    @Override
    public ObservableList<Task> getTasks() {
        return FXCollections.unmodifiableObservableList(tasks);
    }

    public void setTasks(List<Task> persons) {
        this.tasks.setAll(persons);
    }

    /**
     * Adds a task to the task book.
     * Also checks the new tasks's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

```
###### src/main/java/seedu/address/model/TaskBook.java

``` java
    @Override
    public ObservableList<EventTask> getEventTasks() {
        return FXCollections.unmodifiableObservableList(eventTasks);
    }

    public void setEventTasks(Collection<EventTask> eventTasks) {
        this.eventTasks.setAll(eventTasks);
    }

    public void addEventTask(EventTask eventTask) {
        eventTasks.add(eventTask);
    }

    /**
     * Removes the EventTask at position `index` in the list. Returns the removed EventTask.
     */
    public EventTask removeEventTask(int index) {
        return eventTasks.remove(index);
    }

    public void setEventTask(int index, EventTask newEventTask) {
        eventTasks.set(index, newEventTask);
    }

    ////deadline task operations

```
###### src/main/java/seedu/address/model/config/ReadOnlyConfig.java

``` java
/**
 * Unmodifiable view of a {@link Config} object.
 */
public interface ReadOnlyConfig {

    /**
     * Returns the configured log level.
     */
    Level getLogLevel();

    /**
     * Returns the configured task book file path.
     */
    String getTaskBookFilePath();

}
```
###### src/main/java/seedu/address/model/config/Config.java

``` java
    public Config(ReadOnlyConfig config) {
        this();
        resetData(config);
    }

    public void resetData(ReadOnlyConfig config) {
        setLogLevel(config.getLogLevel());
        setTaskBookFilePath(config.getTaskBookFilePath());
    }

```
###### src/main/java/seedu/address/model/config/Config.java

``` java
    @Override
    public String getTaskBookFilePath() {
        return taskBookFilePath;
    }

    public void setTaskBookFilePath(String taskBookFilePath) {
        this.taskBookFilePath = taskBookFilePath;
    }

```
###### src/main/java/seedu/address/model/task/EventTask.java

``` java
public final class EventTask extends Task {

    private static final String FMT_STRING = "EventTask[name=%s, duration=%s]";

    private final LocalDateTimeDuration duration;

    public EventTask(Name name, LocalDateTimeDuration duration) {
        super(name);
        assert duration != null;
        this.duration = duration;
    }

    public EventTask(String name, LocalDateTimeDuration duration) throws IllegalValueException {
        this(new Name(name), duration);
    }

    public EventTask(Name name, LocalDateTime start, LocalDateTime end) throws IllegalValueException {
        this(name, new LocalDateTimeDuration(start, end));
    }

    public EventTask(String name, LocalDateTime start, LocalDateTime end) throws IllegalValueException {
        this(name, new LocalDateTimeDuration(start, end));
    }

    public LocalDateTimeDuration getDuration() {
        return duration;
    }

    public LocalDateTime getStart() {
        return duration.getStart();
    }

    public LocalDateTime getEnd() {
        return duration.getEnd();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
               || (other instanceof EventTask
               && name.equals(((EventTask)other).name)
               && duration.equals(((EventTask)other).duration));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration);
    }

    @Override
    public String toString() {
        return String.format(FMT_STRING, name, duration);
    }

}
```
###### src/main/java/seedu/address/model/ModelManager.java

``` java
/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Config config;
    private final TaskBook taskBook;
    private final FilteredList<Task> filteredTasks;
    private final FilteredList<FloatingTask> filteredFloatingTasks;
    private final FilteredList<EventTask> filteredEventTasks;
    private final FilteredList<DeadlineTask> filteredDeadlineTasks;

    /**
     * Initializes a ModelManager with the given config and TaskBook
     * TaskBook and its variables should not be null
     */
    public ModelManager(ReadOnlyConfig config, ReadOnlyTaskBook taskBook) {
        super();
        assert taskBook != null;

        logger.fine("Initializing with config: " + config + " and task book: " + taskBook);

        this.config = new Config(config);
        this.taskBook = new TaskBook(taskBook);
        this.filteredTasks = new FilteredList<>(this.taskBook.getTasks());
        this.filteredFloatingTasks = new FilteredList<>(this.taskBook.getFloatingTasks());
        this.filteredEventTasks = new FilteredList<>(this.taskBook.getEventTasks());
        this.filteredDeadlineTasks = new FilteredList<>(this.taskBook.getDeadlineTasks());
    }

```
###### src/main/java/seedu/address/model/ModelManager.java

``` java
    @Override
    public ReadOnlyConfig getConfig() {
        return config;
    }

    @Override
    public String getTaskBookFilePath() {
        return config.getTaskBookFilePath();
    }

    @Override
    public void setTaskBookFilePath(String filePath) {
        config.setTaskBookFilePath(filePath);
    }

    /// Task Book

    @Override
    public void resetData(ReadOnlyTaskBook newData) {
        taskBook.resetData(newData);
        indicateTaskBookChanged();
    }

```
###### src/main/java/seedu/address/model/ModelManager.java

``` java
    @Override
    public synchronized void deleteTask(Task target) throws TaskNotFoundException {
        taskBook.removeTask(target);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized void addTask(Task person) {
        taskBook.addTask(person);
        setFilter(null);
        indicateTaskBookChanged();
    }

    //=========== Filtered Task List Accessors ===============================================================

```
###### src/main/java/seedu/address/model/ModelManager.java

``` java
    @Override
    public synchronized void addEventTask(EventTask eventTask) {
        taskBook.addEventTask(eventTask);
        setEventTaskFilter(null);
        indicateTaskBookChanged();
    }

```
###### src/main/java/seedu/address/model/ModelManager.java

``` java
    @Override
    public synchronized EventTask removeEventTask(int indexInFilteredList) throws IllegalValueException {
        final EventTask removedEvent = taskBook.removeEventTask(getEventTaskSourceIndex(indexInFilteredList));
        indicateTaskBookChanged();
        return removedEvent;
    }

    @Override
    public synchronized void setEventTask(int indexInFilteredList, EventTask newEventTask)
            throws IllegalValueException {
        taskBook.setEventTask(getEventTaskSourceIndex(indexInFilteredList), newEventTask);
        indicateTaskBookChanged();
    }

    @Override
    public ObservableList<EventTask> getFilteredEventTaskList() {
        return filteredEventTasks;
    }

    @Override
    public void setEventTaskFilter(Predicate<? super EventTask> predicate) {
        filteredEventTasks.setPredicate(predicate);
    }

    //// Deadline tasks

```
###### src/main/java/seedu/address/model/Model.java

``` java
/**
 * The API of the Model component.
 */
public interface Model {

    /// Config

    /** Returns current config as a read-only view */
    ReadOnlyConfig getConfig();

    /** Returns configured task book file path. */
    String getTaskBookFilePath();

    /** Sets configured task book file path */
    void setTaskBookFilePath(String taskBookFilePath);

    /// Task Book

```
###### src/main/java/seedu/address/model/Model.java

``` java
    /** Returns the TaskBook */
    ReadOnlyTaskBook getAddressBook();

    /** Deletes the given task. */
    void deleteTask(Task target) throws TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task);

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<Task>} */
    UnmodifiableObservableList<Task> getFilteredTaskList();

    /**
     * Updates the filter of the filtered task list to filter by the given predicate.
     *
     * If predicate is null, all tasks will be shown.
     */
    void setFilter(Predicate<Task> predicate);

    //// Floating Tasks

```
###### src/main/java/seedu/address/model/Model.java

``` java
    /** Adds the given event task */
    void addEventTask(EventTask eventTask);

    /** Retrieves the given event task from the specified index in the filtered event task list */
    EventTask getEventTask(int indexInFilteredList) throws IllegalValueException;

    /** Removes the given event task and returns it. */
    EventTask removeEventTask(int indexInFilteredList) throws IllegalValueException;

    /** Replaces the given event task with a new event task */
    void setEventTask(int indexInFilteredList, EventTask newEventTask) throws IllegalValueException;

    /** Returns the filtered event task list as an unmodifiable ObservableList */
    ObservableList<EventTask> getFilteredEventTaskList();

    /**
     * Updates the filter of the filtered event task list to filter by the given predicate.
     *
     * If predicate is null, the filtered event task list will be populated with all event tasks.
     */
    void setEventTaskFilter(Predicate<? super EventTask> predicate);

    //// Deadline Tasks

```
###### src/main/java/seedu/address/model/TaskBookChangeListener.java

``` java
public class TaskBookChangeListener extends ObservableListChangeListener {

    public TaskBookChangeListener(ReadOnlyTaskBook taskBook) {
        super(taskBook.getTasks(), taskBook.getFloatingTasks(), taskBook.getDeadlineTasks(),
              taskBook.getEventTasks());
    }

}
```
###### src/main/java/seedu/address/model/ReadOnlyTaskBook.java

``` java
    /**
     * Returns an unmodifiable view of the EventTasks list.
     */
    ObservableList<EventTask> getEventTasks();

```
###### src/main/java/seedu/address/ui/DeadlineTaskListCard.java

``` java
public class DeadlineTaskListCard extends UiPart<Pane> {

    private static final String FXML = "/view/DeadlineTaskListCard.fxml";

    @FXML
    private Label indexLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label dueLabel;

    @FXML
    private Label finishedLabel;

    /**
     * @param deadlineTask The deadline task to display. Can be null to not display anything.
     */
    public DeadlineTaskListCard(DeadlineTask deadlineTask, int index) {
        super(FXML);
        if (deadlineTask != null) {
            indexLabel.setText(index + ". ");
            nameLabel.setText(deadlineTask.name.toString());
            dueLabel.setText(deadlineTask.getDue().toString());
            finishedLabel.setText(String.valueOf(deadlineTask.isFinished()));
        } else {
            getRoot().setVisible(false);
        }
    }

}
```
###### src/main/java/seedu/address/ui/MainWindow.java

``` java
    void fillInnerParts(Config config, Logic logic) {
        floatingTaskListPane = new FloatingTaskListPane(logic.getFilteredFloatingTaskList());
        floatingTaskListRegion.setNode(floatingTaskListPane.getRoot());
        eventTaskListPane = new EventTaskListPane(logic.getFilteredEventTaskList());
        eventTaskListRegion.setNode(eventTaskListPane.getRoot());
        deadlineTaskListPane = new DeadlineTaskListPane(logic.getFilteredDeadlineTaskList());
        deadlineTaskListRegion.setNode(deadlineTaskListPane.getRoot());
        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.setNode(resultDisplay.getRoot());
        commandBox = new CommandBox(resultDisplay, logic);
        commandBoxPlaceholder.setNode(commandBox.getRoot());
        statusBarFooter = new StatusBarFooter(config.getTaskBookFilePath());
        statusbarPlaceholder.setNode(statusBarFooter.getRoot());
    }

```
###### src/main/java/seedu/address/ui/EventTaskListCard.java

``` java
public class EventTaskListCard extends UiPart<Pane> {

    private static final String FXML = "/view/EventTaskListCard.fxml";

    @FXML
    private Label indexLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label startLabel;

    @FXML
    private Label endLabel;

    /**
     * @param eventTask The event task to display. Can be null to not display anything.
     */
    public EventTaskListCard(EventTask eventTask, int index) {
        super(FXML);
        if (eventTask != null) {
            indexLabel.setText(index + ". ");
            nameLabel.setText(eventTask.name.toString());
            startLabel.setText(eventTask.getStart().toString());
            endLabel.setText(eventTask.getEnd().toString());
        } else {
            getRoot().setVisible(false);
        }
    }

}
```
###### src/main/java/seedu/address/ui/ResultDisplay.java

``` java
/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Pane> {

    private static final String FXML = "/view/ResultDisplay.fxml";

    @FXML
    private TextArea resultDisplay;

    private final StringProperty displayed = new SimpleStringProperty("");

    public ResultDisplay() {
        super(FXML);
        resultDisplay.textProperty().bind(displayed);
    }

```
###### src/main/java/seedu/address/ui/HelpWindow.java

``` java
/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    private static final String ICON = "/images/help_icon.png";
    private static final String FXML = "/view/HelpWindow.fxml";
    private static final String TITLE = "Help";
    private static final String USERGUIDE_URL =
            "https://github.com/se-edu/addressbook-level4/blob/master/docs/UserGuide.md";

    @FXML
    private WebView webView;

    public HelpWindow() {
        super(FXML);
        getRoot().initModality(Modality.WINDOW_MODAL);
        FxViewUtil.setStageIcon(getRoot(), ICON);
        webView.getEngine().load(USERGUIDE_URL);
    }

}
```
###### src/main/java/seedu/address/ui/EventTaskListPane.java

``` java
public class EventTaskListPane extends UiPart<Pane> {

    private static final String FXML = "/view/EventTaskListPane.fxml";

    @FXML
    private ListView<EventTask> eventTaskListView;

    public EventTaskListPane(ObservableList<EventTask> eventTaskList) {
        super(FXML);
        eventTaskListView.setItems(eventTaskList);
        eventTaskListView.setCellFactory(listView -> new EventTaskListCell());
    }

    private static class EventTaskListCell extends ListCell<EventTask> {
        @Override
        protected void updateItem(EventTask eventTask, boolean empty) {
            super.updateItem(eventTask, empty);
            final EventTaskListCard card = new EventTaskListCard(eventTask, getIndex() + 1);
            setGraphic(card.getRoot());
        }
    }

}
```
###### src/main/java/seedu/address/ui/DeadlineTaskListPane.java

``` java
public class DeadlineTaskListPane extends UiPart<Pane> {

    private static final String FXML = "/view/DeadlineTaskListPane.fxml";

    @FXML
    private ListView<DeadlineTask> deadlineTaskListView;

    public DeadlineTaskListPane(ObservableList<DeadlineTask> deadlineTaskList) {
        super(FXML);
        deadlineTaskListView.setItems(deadlineTaskList);
        deadlineTaskListView.setCellFactory(listView -> new DeadlineTaskListCell());
    }

    private static class DeadlineTaskListCell extends ListCell<DeadlineTask> {
        @Override
        protected void updateItem(DeadlineTask deadlineTask, boolean empty) {
            super.updateItem(deadlineTask, empty);
            final DeadlineTaskListCard card = new DeadlineTaskListCard(deadlineTask, getIndex() + 1);
            setGraphic(card.getRoot());
        }
    }

}
```
###### src/main/java/seedu/address/ui/UiPart.java

``` java
/**
 * Base class for UI parts.
 * A 'UI part' represents a distinct part of the UI. e.g. Windows, dialogs, panels, status bars, etc.
 */
public class UiPart<T> {

    private final FXMLLoader loader;

    public UiPart(URL url) {
        assert url != null;
        loader = new FXMLLoader(url);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception occurred while loading " + url + ": " + e);
        }
        EventsCenter.getInstance().registerHandler(this);
    }

    public UiPart(String name) {
        this(MainApp.class.getResource(name));
    }

    public T getRoot() {
        return loader.getRoot();
    }
}
```
###### src/main/java/seedu/address/ui/UiRegion.java

``` java
public class UiRegion extends Region {

    public UiRegion() {
        super();
        setMinHeight(Region.USE_PREF_SIZE);
    }

    public Node getNode() {
        return getChildren().size() > 0 ? getChildren().get(0) : null;
    }

    public void setNode(Node child) {
        getChildren().clear();
        getChildren().add(child);
    }

    @Override
    protected void layoutChildren() {
        for (Node node : getChildren()) {
            layoutInArea(node, 0, 0, getWidth(), getHeight(), 0, HPos.LEFT, VPos.TOP);
        }
    }

}
```
###### src/main/java/seedu/address/storage/JsonStorageModule.java

``` java
public class JsonStorageModule extends Module {

    @Override
    public String getModuleName() {
        return "JsonStorageModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(LocalDateTimeDuration.class, JsonLocalDateTimeDurationMixin.class);
        context.setMixInAnnotations(Name.class, JsonNameMixin.class);
        context.setMixInAnnotations(Priority.class, JsonPriorityMixin.class);
        context.setMixInAnnotations(Task.class, JsonTaskMixin.class);
        context.setMixInAnnotations(FloatingTask.class, JsonFloatingTaskMixin.class);
        context.setMixInAnnotations(EventTask.class, JsonEventTaskMixin.class);
        context.setMixInAnnotations(DeadlineTask.class, JsonDeadlineTaskMixin.class);
        context.setMixInAnnotations(TaskBook.class, JsonTaskBookMixin.class);
    }

}
```
###### src/main/java/seedu/address/storage/JsonEventTaskMixin.java

``` java
@JsonPropertyOrder({"name", "start", "end"})
public abstract class JsonEventTaskMixin {

    JsonEventTaskMixin(@JsonProperty("name") Name name, @JsonProperty("start") LocalDateTime start,
                       @JsonProperty("end") LocalDateTime end) {
    }

    @JsonIgnore
    abstract LocalDateTimeDuration getDuration();

}
```
###### src/main/java/seedu/address/storage/config/ConfigStorage.java

``` java
/**
 * Represents a storage for {@link seedu.address.model.config.Config}
 */
public interface ConfigStorage {

    /**
     * Returns the file path of the config file.
     */
    String getConfigFilePath();

    /**
     * Returns the Config data as a {@link ReadOnlyConfig}.
     * Returns {@code Optional.empty()} if config file was not found.
     * @throws DataConversionException if the data in storage cannot be parsed.
     * @throws IOException if an IO error occurred while reading from the storage.
     */
    Optional<ReadOnlyConfig> readConfig() throws DataConversionException, IOException;

    /**
     * @see #readConfig()
     */
    Optional<ReadOnlyConfig> readConfig(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyConfig} to the storage.
     * @param config cannot be null.
     * @throws IOException if an IO error occurred while writing to the storage.
     */
    void saveConfig(ReadOnlyConfig config) throws IOException;

    /**
     * @see #saveConfig(ReadOnlyConfig)
     */
    void saveConfig(ReadOnlyConfig config, String filePath) throws IOException;

}
```
###### src/main/java/seedu/address/storage/config/JsonConfigModule.java

``` java
/**
 * Jackson module for serialization/deserialization of {@link Config} objects.
 */
public class JsonConfigModule extends Module {

    @Override
    public String getModuleName() {
        return "JsonConfigModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        final SimpleSerializers serializers = new SimpleSerializers();
        final SimpleDeserializers deserializers = new SimpleDeserializers();

        serializers.addSerializer(Level.class, new ToStringSerializer());
        deserializers.addDeserializer(Level.class, new JsonLevelDeserializer(Level.class));

        context.addSerializers(serializers);
        context.addDeserializers(deserializers);
        context.setMixInAnnotations(Config.class, JsonConfigMixin.class);
    }

}
```
###### src/main/java/seedu/address/storage/config/JsonConfigStorage.java

``` java
public class JsonConfigStorage implements ConfigStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonConfigStorage.class);

    private final String filePath;

    private final ObjectMapper objectMapper;

    public JsonConfigStorage(String filePath, ObjectMapper objectMapper) {
        assert !CollectionUtil.isAnyNull(filePath, objectMapper);
        this.filePath = filePath;
        this.objectMapper = objectMapper;
    }

    public JsonConfigStorage(String filePath) {
        this(filePath, initDefaultObjectMapper());
    }

    private static ObjectMapper initDefaultObjectMapper() {
        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .registerModule(new Jdk8Module())
                .registerModule(new JsonConfigModule());
    }

    @Override
    public String getConfigFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyConfig> readConfig() throws DataConversionException, IOException {
        return readConfig(filePath);
    }

    @Override
    public Optional<ReadOnlyConfig> readConfig(String filePath) throws DataConversionException, IOException {
        assert filePath != null;
        final File configFile = new File(filePath);
        if (!configFile.exists()) {
            logger.info("Config file " + configFile + " not found");
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(configFile, Config.class));
        } catch (JsonProcessingException e) {
            throw new DataConversionException(e);
        }
    }

    @Override
    public void saveConfig(ReadOnlyConfig config) throws IOException {
        saveConfig(config, filePath);
    }

    @Override
    public void saveConfig(ReadOnlyConfig config, String filePath) throws IOException {
        assert !CollectionUtil.isAnyNull(config, filePath);
        final File file = new File(filePath);
        FileUtil.createIfMissing(file);
        objectMapper.writeValue(file, config);
    }

}
```
###### src/main/java/seedu/address/storage/config/JsonLevelDeserializer.java

``` java
public class JsonLevelDeserializer extends FromStringDeserializer<Level> {

    public JsonLevelDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    protected Level _deserialize(String value, DeserializationContext ctxt) throws IOException {
        return Level.parse(value);
    }

}
```
###### src/main/java/seedu/address/storage/JsonLocalDateTimeDurationMixin.java

``` java
@JsonPropertyOrder({"start", "end"})
public abstract class JsonLocalDateTimeDurationMixin {

    public JsonLocalDateTimeDurationMixin(@JsonProperty("start") LocalDateTime start,
                                          @JsonProperty("end") LocalDateTime end) {
    }

    @JsonIgnore
    abstract List<TemporalUnit> getUnits();

}
```
###### src/main/java/seedu/address/storage/JsonNameMixin.java

``` java
public abstract class JsonNameMixin {
    @JsonCreator
    JsonNameMixin(String name) {}

    @JsonValue
    public abstract String toString();
}
```
###### src/main/java/seedu/address/storage/JsonTaskBookStorage.java

``` java
public class JsonTaskBookStorage implements TaskBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonTaskBookStorage.class);

    private String filePath;

    private final ObjectMapper objectMapper;

    public JsonTaskBookStorage(String filePath, ObjectMapper objectMapper) {
        assert !CollectionUtil.isAnyNull(filePath, objectMapper);
        this.filePath = filePath;
        this.objectMapper = objectMapper;
    }

    public JsonTaskBookStorage(String filePath) {
        this(filePath, initDefaultObjectMapper());
    }

    private static ObjectMapper initDefaultObjectMapper() {
        return new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new JavaTimeModule())
            .registerModule(new JsonStorageModule());
    }

    @Override
    public String getTaskBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook() throws DataConversionException, IOException {
        return readTaskBook(filePath);
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook(String filePath) throws DataConversionException, IOException {
        assert filePath != null;
        final File taskBookFile = new File(filePath);
        if (!taskBookFile.exists()) {
            logger.info("TaskBook file " + taskBookFile + " not found");
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(taskBookFile, TaskBook.class));
        } catch (JsonProcessingException e) {
            throw new DataConversionException(e);
        }
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook) throws IOException {
        saveTaskBook(taskBook, filePath);
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook, String filePath) throws IOException {
        assert !CollectionUtil.isAnyNull(taskBook, filePath);
        final File file = new File(filePath);
        FileUtil.createIfMissing(file);
        objectMapper.writeValue(file, taskBook);
    }

    @Override
    public void moveTaskBook(String newFilePath) throws IOException {
        assert newFilePath != null;
        final File file = new File(filePath);
        final File newFile = new File(newFilePath);
        if (FileUtil.isFileExists(newFile)) {
            throw new IOException(newFilePath + " already exists.");
        }
        Files.createParentDirs(newFile);
        Files.move(file, newFile);
        filePath = newFilePath;
    }

}
```
###### src/main/java/seedu/address/storage/JsonTaskMixin.java

``` java
public abstract class JsonTaskMixin {
    JsonTaskMixin(@JsonProperty("name") Name name) {}
}
```
###### src/main/java/seedu/address/storage/TaskBookStorage.java

``` java
    /**
     * Moves the task book to the newFilePath.
     * @throws IOException if an IO error occurred while moving the task book file.
     *         The configured task book file path will remain unchanged.
     */
    void moveTaskBook(String newFilePath) throws IOException;

}
```
###### src/main/java/seedu/address/storage/StorageManager.java

``` java
/**
 * Manages storage of TaskBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);

    private final ConfigStorage configStorage;

    private final TaskBookStorage taskBookStorage;

    public StorageManager(ConfigStorage configStorage, TaskBookStorage taskBookStorage) {
        super();
        this.configStorage = configStorage;
        this.taskBookStorage = taskBookStorage;
    }

    public StorageManager(ConfigStorage configStorage, String taskBookFilePath) {
        this(configStorage, new JsonTaskBookStorage(taskBookFilePath));
    }

    public StorageManager(String configFilePath, String taskBookFilePath) {
        this(new JsonConfigStorage(configFilePath), taskBookFilePath);
    }

    // ================ ConfigStorage methods =========================

    @Override
    public String getConfigFilePath() {
        return configStorage.getConfigFilePath();
    }

    @Override
    public Optional<ReadOnlyConfig> readConfig() throws DataConversionException, IOException {
        return configStorage.readConfig();
    }

    @Override
    public Optional<ReadOnlyConfig> readConfig(String filePath) throws DataConversionException, IOException {
        return configStorage.readConfig(filePath);
    }

    @Override
    public void saveConfig(ReadOnlyConfig config) throws IOException {
        configStorage.saveConfig(config);
    }

    @Override
    public void saveConfig(ReadOnlyConfig config, String filePath) throws IOException {
        configStorage.saveConfig(config, filePath);
    }

    // ================ TaskBook methods ==============================

```
###### src/main/java/seedu/address/storage/StorageManager.java

``` java
    @Override
    public void moveTaskBook(String newFilePath) throws IOException {
        logger.fine("Attempting to move task book from " + taskBookStorage.getTaskBookFilePath() + " to "
                    + newFilePath);
        taskBookStorage.moveTaskBook(newFilePath);
    }

}
```
###### src/main/java/seedu/address/commons/core/Version.java

``` java
    @Override
    public int compareTo(Version other) {
        return this.major != other.major ? this.major - other.major
               : this.minor != other.minor ? this.minor - other.minor
               : this.patch != other.patch ? this.patch - other.patch
               : this.isEarlyAccess == other.isEarlyAccess() ? 0
               : this.isEarlyAccess ? -1 : 1;
    }

```
###### src/main/java/seedu/address/commons/time/LocalDateTimeDuration.java

``` java
/**
 * Represents a period bounded by two LocalDateTimes.
 *
 * Guarantees: Immutable POJO with non-null values. End LocalDateTime is after start LocalDateTime.
 */
public final class LocalDateTimeDuration implements Comparable<LocalDateTimeDuration>, TemporalAmount {

    private static final String FMT_STRING = "LocalDateTimeDuration[start=%s, end=%s]";

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final Duration duration;

    public LocalDateTimeDuration(LocalDateTime start, LocalDateTime end) throws IllegalValueException {
        assert !CollectionUtil.isAnyNull(start, end);
        if (end.isBefore(start)) {
            throw new IllegalValueException("end datetime must be after start datetime");
        }
        this.start = start;
        this.end = end;
        this.duration = Duration.between(start, end);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public Temporal addTo(Temporal temporal) {
        return temporal.plus(duration);
    }

    @Override
    public long get(TemporalUnit unit) {
        return duration.get(unit);
    }

    @Override
    public List<TemporalUnit> getUnits() {
        return duration.getUnits();
    }

    @Override
    public Temporal subtractFrom(Temporal temporal) {
        return temporal.minus(duration);
    }

    @Override
    public int compareTo(LocalDateTimeDuration other) {
        int cmp = start.compareTo(other.start);
        if (cmp != 0) {
            return cmp;
        }
        return start.compareTo(other.end);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
               || (other instanceof LocalDateTimeDuration
               && start.equals(((LocalDateTimeDuration)other).start)
               && end.equals(((LocalDateTimeDuration)other).end));
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return String.format(FMT_STRING, start, end);
    }

}
```
###### src/main/java/seedu/address/commons/util/ObservableListChangeListener.java

``` java
/**
 * A utility class that tracks whether ObservableList(s) have changed.
 */
public class ObservableListChangeListener {

    private boolean hasChanged = false;

    private final ObservableList<?>[] lists;

    public ObservableListChangeListener(ObservableList<?>... lists) {
        // Store a reference to the observable lists so they do not get GC'd
        this.lists = lists;
        // Install our listeners on the observable lists
        for (ObservableList<?> list : lists) {
            list.addListener((ListChangeListener.Change<? extends Object> change) -> {
                hasChanged = true;
            });
        }
    }

    public void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    public boolean getHasChanged() {
        return hasChanged;
    }

}
```
