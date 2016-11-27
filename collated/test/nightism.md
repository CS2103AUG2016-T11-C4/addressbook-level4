# smy961025@yeah.net

###### src/test/java/seedu/address/logic/parser/AddTaskParserTest.java

``` java
public class AddTaskParserTest {
    private AddTaskParser parser;

    @Before
    public void setupParser() {
        parser = new AddTaskParser(PI_DAY);
    }

    @Test
    public void parseAddEventCommand() {
        // All arguments provided
        assertAddEventParse("\"a\" 1/2/2000 4:30am to 2/3/2100 6:32pm", "a", LocalDateTime.of(2000, 2, 1, 4, 30),
                    LocalDateTime.of(2100, 3, 2, 18, 32));

        // If startDate is not given, then it is the current date
        assertAddEventParse("\"a\" 7:23am to 4/6/2654 8:24pm", "a", LocalDateTime.of(2653, 9, 15, 7, 23),
                LocalDateTime.of(2654, 6, 4, 20, 24));

        // If startTime is not given, then it is 12am
        assertAddEventParse("\"a\" 3/4/2015 to 4/5/2015 7pm", "a", LocalDateTime.of(2015, 4, 3, 0, 0),
                LocalDateTime.of(2015, 5, 4, 19, 0));

        // If endDate is not given, then it is the same as startDate
        assertAddEventParse("\"a\" 1/2/2000 5:41am to 6:32pm", "a", LocalDateTime.of(2000, 2, 1, 5, 41),
                LocalDateTime.of(2000, 2, 1, 18, 32));

        // If endTime is not given, then it is 11.59pm
        assertAddEventParse("\"a\" 8/7/2014 12:45am to 9/7/2014", "a",
                LocalDateTime.of(2014, 7, 8, 0, 45), LocalDateTime.of(2014, 7, 9, 23, 59));

        // Must have at least one of { startDate, startTime }, and at least one of { endDate, endTime }
        assertIncorrect("\"a\" 1/2/2000 4:30am to");
        assertIncorrect("\"a\" to 1/2/2000 4:30am to");
        assertIncorrect("");

        // Too many arguments
        assertIncorrect("\"a\" 4/5/2016 7:23am 6/7/2017 8:42pm extraArg");
    }

    @Test
    public void parseAddDeadlineCommand() {
        // All arguments provided
        assertAddDeadlineParse("\"a\" 1/2/2000 4:30am", "a", LocalDateTime.of(2000, 2, 1, 4, 30));

        // If date is not given, then it is the current date
        assertAddDeadlineParse("\"a\" 9:56am", "a", LocalDateTime.of(2653, 9, 15, 9, 56));

        // If time is not given, then it is 11:59pm
        assertAddDeadlineParse("\"a\" 3/4/2015", "a", LocalDateTime.of(2015, 4, 3, 23, 59));

        // Must have at least one of { date, time }
        assertIncorrect("");

        // Too many arguments
        assertIncorrect("\"a\" 8/1/2012 1:49am extraArg");
    }

    @Test
    public void parseAddFloatingTaskCommand() {
        // All arguments provided
        assertAddFloatingTaskParse("\"a\" p-3", "a", "3");

        // If priority is not given, then it default "0"
        assertAddFloatingTaskParse("\"a\"", "a", "0");

        // Too many arguments
        assertIncorrect("\"a\" 4 extraArg");
    }

    private void assertAddEventParse(String args, String name, LocalDateTime start, LocalDateTime end) {
        final EventTask expected;
        try {
            expected = new EventTask(name, start, end);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        final Command command = parser.parse(args);
        assertTrue(command instanceof AddTaskCommand);
        assertEquals(expected, ((AddTaskCommand)command).getTask());
    }

    private void assertAddDeadlineParse(String args, String name, LocalDateTime due) {
        final DeadlineTask expected;
        try {
            expected = new DeadlineTask(name, due);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }

        final Command command = parser.parse(args);

        assertTrue(command instanceof AddTaskCommand);
        assertEquals(expected, ((AddTaskCommand)command).getTask());
    }

    private void assertAddFloatingTaskParse(String args, String name, String priorityString) {
        final FloatingTask expected;
        final Priority priority;
        try {
            priority = new Priority(priorityString);
            expected = new FloatingTask(name, priority);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        final Command command = parser.parse(args);
        assertTrue(command instanceof AddTaskCommand);
        assertEquals(expected, ((AddTaskCommand)command).getTask());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }
}
```
###### src/test/java/seedu/address/logic/parser/EditDeadlineParserTest.java

``` java
public class EditDeadlineParserTest {

    private EditDeadlineParser parser;

    @Before
    public void setupParser() {
        parser = new EditDeadlineParser(UNIX_EPOCH);
    }

    @Test
    public void parse() {
        // No modifications
        assertParse("1", 1, null, null);

        // Date
        assertParse("2 dd-4/5/2016", 2, LocalDate.of(2016, 5, 4), null);

        // Time
        assertParse("3 dt-5:32am", 3, null, LocalTime.of(5, 32));

        // Date and Time
        assertParse("4 dd-7/8 dt-4:00pm", 4, LocalDate.of(1970, 8, 7), LocalTime.of(16, 0));

        // Index cannot be negative
        assertIncorrect("-1");

        // Invalid flags
        assertIncorrect("8 invalid-flag");
    }

    private void assertParse(String args, int targetIndex, LocalDate newDate, LocalTime newTime) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof EditDeadlineCommand);
        final EditDeadlineCommand editCommand = (EditDeadlineCommand) command;
        assertEquals(targetIndex, editCommand.getTargetIndex());
        assertEquals(newDate, editCommand.getNewDate());
        assertEquals(newTime, editCommand.getNewTime());

    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}
```
###### src/test/java/seedu/address/logic/parser/AddFloatingTaskParserTest.java

``` java
public class AddFloatingTaskParserTest {

    private AddFloatingTaskParser parser;

    @Before
    public void setupParser() {
        parser = new AddFloatingTaskParser();
    }

    @Test
    public void parse() {
        // All arguments provided
        assertParse("\"a\" p-3", "a", "3");

        // If priority is not given, then it default "0"
        assertParse("\"a\"", "a", "0");

        // Too many arguments
        assertIncorrect("\"a\" 4 extraArg");
    }

    private void assertParse(String args, String name, String priorityString) {
        final FloatingTask expected;
        final Priority priority;
        try {
            priority = new Priority(priorityString);
            expected = new FloatingTask(name, priority);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        final Command command = parser.parse(args);
        assertTrue(command instanceof AddFloatingTaskCommand);
        assertEquals(expected, ((AddFloatingTaskCommand)command).getTask());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}
```
###### src/test/java/seedu/address/logic/parser/AddDeadlineParserTest.java

``` java
public class AddDeadlineParserTest {

    private AddDeadlineParser parser;

    @Before
    public void setupParser() {
        /** Pi Day 3:14 15/9/2653 */
        parser = new AddDeadlineParser(PI_DAY);
    }

    @Test
    public void parse() {
        // All arguments provided
        assertParse("\"a\" 1/2/2000 4:30am", "a", LocalDateTime.of(2000, 2, 1, 4, 30));

        // If date is not given, then it is the current date
        assertParse("\"a\" 9:56am", "a", LocalDateTime.of(2653, 9, 15, 9, 56));

        // If time is not given, then it is 11:59pm
        assertParse("\"a\" 3/4/2015", "a", LocalDateTime.of(2015, 4, 3, 23, 59));

        // Must have at least one of { date, time }
        assertIncorrect("\"a\"");
        assertIncorrect("");

        // Too many arguments
        assertIncorrect("\"a\" 8/1/2012 1:49am extraArg");
    }

    private void assertParse(String args, String name, LocalDateTime due) {
        final DeadlineTask expected;
        try {
            expected = new DeadlineTask(name, due);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }

        final Command command = parser.parse(args);

        assertTrue(command instanceof AddDeadlineCommand);
        assertEquals(expected, ((AddDeadlineCommand)command).getTask());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}
```
###### src/test/java/seedu/address/logic/parser/EditFloatingTaskParserTest.java

``` java
public class EditFloatingTaskParserTest {

    private EditFloatingTaskParser parser;

    @Before
    public void setupParser() {
        parser = new EditFloatingTaskParser();
    }

    @Test
    public void parse() {
        // No modification
        assertParse("1", 1, null, null);

        // name

        try {
            assertParse("1 n-new name", 1, new Name("new name"), null);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }

        // priority
        try {
            assertParse("1 p-2", 1, null, new Priority("2"));
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }

        // All arguments
        try {
            assertParse("3 n-thisname p-5", 3, new Name("thisname"), new Priority("5"));
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }

        // Invalid flags
        assertIncorrect("5 invalid-flag");
    }

    private void assertParse(String args, int targetIndex, Name name, Priority priority) {

        final Command command = parser.parse(args);
        assertTrue(command instanceof EditFloatingTaskCommand);

        assertEquals(targetIndex, ((EditFloatingTaskCommand)command).getTargetIndex());
        assertEquals(name, ((EditFloatingTaskCommand)command).getNewName());
        assertEquals(priority, ((EditFloatingTaskCommand)command).getNewPriority());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}
```
###### src/test/java/seedu/address/model/ModelTest.java

``` java
public class ModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TypicalFloatingTasks tpflt = new TypicalFloatingTasks();

    private TypicalEventTasks tpent = new TypicalEventTasks();

    private TypicalDeadlineTasks tpdue = new TypicalDeadlineTasks();

    private Model model;

    @Before
    public void setupModel() {
        model = new ModelManager();
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), model.getFilteredFloatingTaskList());
        assertEquals(Collections.emptyList(), model.getFilteredEventTaskList());
        assertEquals(Collections.emptyList(), model.getFilteredDeadlineTaskList());
    }

    @Test
    public void addFloatingTask_appendsFloatingTask() throws Exception {
        model.addFloatingTask(tpflt.readABook);
        assertEquals(tpflt.readABook, model.getFloatingTask(0));
        model.addFloatingTask(tpflt.buyAHelicopter);
        assertEquals(tpflt.readABook, model.getFloatingTask(0));
        assertEquals(tpflt.buyAHelicopter, model.getFloatingTask(1));
        assertEquals(tpflt.readABook, model.getFloatingTask(0));
    }

    @Test
    public void getFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getFloatingTask(0);
    }

    @Test
    public void removeFloatingTask_removesIndexInFilteredList() throws Exception {
        model.addFloatingTask(tpflt.readABook);
        model.addFloatingTask(tpflt.buyAHelicopter);
        model.setFloatingTaskFilter(floatingTask -> floatingTask.equals(tpflt.buyAHelicopter));
        model.removeFloatingTask(0);
        model.setFloatingTaskFilter(null);
        assertEquals(Arrays.asList(tpflt.readABook), model.getFilteredFloatingTaskList());
    }

    @Test
    public void removeFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeFloatingTask(0);
    }

    @Test
    public void setFloatingTask_replacesIndexInFilteredList() throws Exception {
        model.addFloatingTask(tpflt.readABook);
        model.addFloatingTask(tpflt.buyAHelicopter);
        model.setFloatingTaskFilter(floatingTask -> floatingTask.equals(tpflt.buyAHelicopter));
        model.setFloatingTask(0, tpflt.readABook);
        model.setFloatingTaskFilter(null);
        assertEquals(Arrays.asList(tpflt.readABook, tpflt.readABook),
                    model.getFilteredFloatingTaskList());
    }

    @Test
    public void setFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setFloatingTask(0, tpflt.readABook);
    }

    @Test
    public void addEventTask_appendsEventTask() throws Exception {
        model.addEventTask(tpent.lunchWithBillGates);
        assertEquals(tpent.lunchWithBillGates, model.getEventTask(0));
        model.addEventTask(tpent.launchNuclearWeapons);
        assertEquals(tpent.lunchWithBillGates, model.getEventTask(0));
        assertEquals(tpent.launchNuclearWeapons, model.getEventTask(1));
    }

```
###### src/test/java/seedu/address/model/ModelTest.java

``` java
    @Test
    public void setEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setEventTask(0, tpent.lunchWithBillGates);
    }

    @Test
    public void addDeadlineTask_appendsDeadlineTask() throws Exception {
        model.addDeadlineTask(tpdue.speechTranscript);
        assertEquals(tpdue.speechTranscript, model.getDeadlineTask(0));
        model.addDeadlineTask(tpdue.assembleTheMissiles);
        assertEquals(tpdue.speechTranscript, model.getDeadlineTask(0));
        assertEquals(tpdue.assembleTheMissiles, model.getDeadlineTask(1));
    }

    @Test
    public void getDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getDeadlineTask(0);
    }

    @Test
    public void removeDeadlineTask_removesIndexInFilteredList() throws Exception {
        model.addDeadlineTask(tpdue.speechTranscript);
        model.addDeadlineTask(tpdue.assembleTheMissiles);
        model.setDeadlineTaskFilter(deadlineTask -> deadlineTask.equals(tpdue.assembleTheMissiles));
        model.removeDeadlineTask(0);
        model.setDeadlineTaskFilter(null);
        assertEquals(Arrays.asList(tpdue.speechTranscript), model.getFilteredDeadlineTaskList());
    }

    @Test
    public void removeDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeDeadlineTask(0);
    }

    @Test
    public void setDeadlineTask_replacesIndexInFilteredList() throws Exception {
        model.addDeadlineTask(tpdue.speechTranscript);
        model.addDeadlineTask(tpdue.assembleTheMissiles);
        model.setDeadlineTaskFilter(deadlineTask -> deadlineTask.equals(tpdue.assembleTheMissiles));
        model.setDeadlineTask(0, tpdue.speechTranscript);
        model.setDeadlineTaskFilter(null);
        assertEquals(Arrays.asList(tpdue.speechTranscript, tpdue.speechTranscript),
                    model.getFilteredDeadlineTaskList());
    }

    @Test
    public void setDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setDeadlineTask(0, tpdue.speechTranscript);
    }

}
```
###### src/test/java/seedu/address/model/TaskBookTest.java

``` java
public class TaskBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TaskBook taskBook;

    private static TypicalFloatingTasks typicalFloatingTasks = new TypicalFloatingTasks();

    private static TypicalEventTasks typicalEventTasks = new TypicalEventTasks();

    private static TypicalDeadlineTasks typicalDeadlineTasks = new TypicalDeadlineTasks();

    @Before
    public void setupTaskBook() {
        taskBook = new TaskBook();
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), taskBook.getTasks());
        assertEquals(Collections.emptyList(), taskBook.getFloatingTasks());
        assertEquals(Collections.emptyList(), taskBook.getEventTasks());
        assertEquals(Collections.emptyList(), taskBook.getDeadlineTasks());
    }

    @Test
    public void setFloatingTasks() {
        taskBook.setFloatingTasks(typicalFloatingTasks.getFloatingTasks());
        assertEquals(typicalFloatingTasks.getFloatingTasks(), taskBook.getFloatingTasks());
    }

    @Test
    public void addFloatingTask_appendsToList() {
        final ArrayList<FloatingTask> expected = new ArrayList<>();
        taskBook.addFloatingTask(typicalFloatingTasks.readABook);
        expected.add(typicalFloatingTasks.readABook);
        assertEquals(expected, taskBook.getFloatingTasks());
        taskBook.addFloatingTask(typicalFloatingTasks.buyAHelicopter);
        expected.add(typicalFloatingTasks.buyAHelicopter);
        assertEquals(expected, taskBook.getFloatingTasks());
    }

    @Test
    public void removeFloatingTask_removesFromList() {
        final ArrayList<FloatingTask> expected = new ArrayList<>(typicalFloatingTasks.getFloatingTasks());
        expected.remove(0);
        taskBook.setFloatingTasks(typicalFloatingTasks.getFloatingTasks());
        taskBook.removeFloatingTask(0);
        assertEquals(expected, taskBook.getFloatingTasks());
    }

```
###### src/test/java/seedu/address/model/TaskBookTest.java

``` java
    @Test
    public void setDeadlineTasks() {
        taskBook.setDeadlineTasks(typicalDeadlineTasks.getDeadlineTasks());
        assertEquals(typicalDeadlineTasks.getDeadlineTasks(), taskBook.getDeadlineTasks());
    }

    @Test
    public void addDeadlineTask_appendsToList() {
        final ArrayList<DeadlineTask> expected = new ArrayList<>();
        taskBook.addDeadlineTask(typicalDeadlineTasks.speechTranscript);
        expected.add(typicalDeadlineTasks.speechTranscript);
        assertEquals(expected, taskBook.getDeadlineTasks());
        taskBook.addDeadlineTask(typicalDeadlineTasks.assembleTheMissiles);
        expected.add(typicalDeadlineTasks.assembleTheMissiles);
        assertEquals(expected, taskBook.getDeadlineTasks());
    }

    @Test
    public void removeDeadlineTask_removesFromList() {
        final ArrayList<DeadlineTask> expected = new ArrayList<>(typicalDeadlineTasks.getDeadlineTasks());
        expected.remove(0);
        taskBook.setDeadlineTasks(typicalDeadlineTasks.getDeadlineTasks());
        taskBook.removeDeadlineTask(0);
        assertEquals(expected, taskBook.getDeadlineTasks());
    }

}
```
###### src/test/java/seedu/address/model/task/FloatingTaskTest.java

``` java
public class FloatingTaskTest {
    private FloatingTask floatingTask;

    @Before
    public void setupFloatingTask() throws Exception {
        final Name name = new Name("Floating Task Name");
        final Priority priority = new Priority("3");
        floatingTask = new FloatingTask(name, priority);
    }

    @Test
    public void getPriority_returnsDue() throws Exception {
        final Priority expected = new Priority("3");
        final Priority actual = floatingTask.getPriority();
        assertEquals(expected, actual);
    }

    @Test
    public void equals_isEqual_returnsTrue() throws Exception {
        final FloatingTask other = new FloatingTask("Floating Task Name", new Priority("3"));
        assertTrue(floatingTask.equals(other));
        assertTrue(floatingTask.hashCode() == other.hashCode());
    }

    @Test
    public void equals_notEqual_returnsFalse() throws Exception {
        final FloatingTask other = new FloatingTask(new Name("Floating Task Name"), new Priority("1"));
        assertFalse(floatingTask.equals(other));
        assertFalse(floatingTask.hashCode() == other.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        final String expected = "FloatingTask[name=Floating Task Name, "
                                + "priority=3]";
        final String actual = floatingTask.toString();
        assertEquals(expected, actual);
    }
}
```
###### src/test/java/seedu/address/model/task/TypicalFloatingTasks.java

``` java
public class TypicalFloatingTasks {

    public final FloatingTask readABook;

    public final FloatingTask buyAHelicopter;

    public TypicalFloatingTasks() {
        try {
            readABook = new FloatingTask("read A Book");
            buyAHelicopter = new FloatingTask("buy A Helicopter", new Priority("4"));
        } catch (IllegalValueException e) {
            throw new AssertionError("this should not happen", e);
        }
    }

    public List<FloatingTask> getFloatingTasks() {
        final FloatingTask[] tasks = {readABook, buyAHelicopter};
        return Arrays.asList(tasks);
    }

}
```
###### src/test/java/seedu/address/model/task/TypicalDeadlineTasks.java

``` java
public class TypicalDeadlineTasks {

    public final DeadlineTask speechTranscript;

    public final DeadlineTask assembleTheMissiles;

    public TypicalDeadlineTasks() {
        try {
            speechTranscript = new DeadlineTask("Speech Transcript", UNIX_EPOCH.plusHours(1));
            assembleTheMissiles = new DeadlineTask("Assemble The Missiles", UNIX_EPOCH.plusHours(2));
        } catch (IllegalValueException e) {
            throw new AssertionError("this should not happen", e);
        }
    }

    public List<DeadlineTask> getDeadlineTasks() {
        final DeadlineTask[] tasks = {speechTranscript, assembleTheMissiles};
        return Arrays.asList(tasks);
    }

}
```
###### src/test/java/seedu/address/model/task/DeadlineTaskTest.java

``` java
public class DeadlineTaskTest {
    private DeadlineTask deadlineTask;

    @Before
    public void setupDeadlineTask() throws Exception {
        final Name name = new Name("Deadline Task Name");
        final LocalDateTime due = UNIX_EPOCH.plusDays(1);
        deadlineTask = new DeadlineTask(name, due);
    }

    @Test
    public void getDue_returnsDue() throws Exception {
        final LocalDateTime expected = UNIX_EPOCH.plusDays(1);
        final LocalDateTime actual = deadlineTask.getDue();
        assertEquals(expected, actual);
    }

    @Test
    public void isFinished_returnsFinished() {
        final boolean expected = false;
        final boolean actual = deadlineTask.isFinished();
        assertEquals(expected, actual);
    }

    @Test
    public void equals_isEqual_returnsTrue() throws Exception {
        final DeadlineTask other = new DeadlineTask("Deadline Task Name", UNIX_EPOCH.plusDays(1));
        assertTrue(deadlineTask.equals(other));
        assertTrue(deadlineTask.hashCode() == other.hashCode());
    }

    @Test
    public void equals_notEqual_returnsFalse() throws Exception {
        final DeadlineTask other = new DeadlineTask(new Name("Deadline Task Name"), UNIX_EPOCH.plusDays(2));
        assertFalse(deadlineTask.equals(other));
        assertFalse(deadlineTask.hashCode() == other.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        final String expected = "DeadlineTask[name=Deadline Task Name, "
                                + "due=1970-01-02T00:00, "
                                + "finished=false]";
        final String actual = deadlineTask.toString();
        assertEquals(expected, actual);
    }
}
```
###### src/test/java/seedu/address/storage/JsonFloatingTaskMixinTest.java

``` java
public class JsonFloatingTaskMixinTest {

    private static ObjectMapper objectMapper;

    public static final FloatingTask TEST_EVENT;
    public static final String TEST_EVENT_JSON = "{\"name\":\"event name\","
                                                 + "\"priority\":\"3\"}";

    static {
        try {
            TEST_EVENT = new FloatingTask(new Name("event name"), new Priority("3"));
        } catch (IllegalValueException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void setupObjectMapper() {
        final SimpleModule module = new SimpleModule();
        module.setMixInAnnotation(Name.class, JsonNameMixin.class);
        module.setMixInAnnotation(Priority.class, JsonPriorityMixin.class);
        module.setMixInAnnotation(FloatingTask.class, JsonFloatingTaskMixin.class);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules().registerModule(module);
    }

    @Test
    public void serialize() throws Exception {
        final String expected = TEST_EVENT_JSON;
        final String actual = objectMapper.writeValueAsString(TEST_EVENT);
        assertEquals(expected, actual);
    }

    @Test
    public void deserialize() throws Exception {
        final FloatingTask expected = TEST_EVENT;
        final FloatingTask actual = objectMapper.readValue(TEST_EVENT_JSON, FloatingTask.class);
        assertEquals(expected, actual);
    }

}
```
###### src/test/java/seedu/address/storage/JsonDeadlineTaskMixinTest.java

``` java
public class JsonDeadlineTaskMixinTest {

    private static ObjectMapper objectMapper;

    public static final DeadlineTask TEST_DEADLINE1;
    public static final DeadlineTask TEST_DEADLINE2;
    public static final String TEST_DEADLINE1_JSON = "{\"name\":\"deadline name1\",\"due\":[1970,1,2,0,0],"
                                                    + "\"finished\":false}";
    public static final String TEST_DEADLINE2_JSON = "{\"name\":\"deadline name2\",\"due\":[1970,1,2,0,0],"
                                                    + "\"finished\":true}";

    static {
        try {
            TEST_DEADLINE1 = new DeadlineTask(new Name("deadline name1"), UNIX_EPOCH.plusDays(1));
            TEST_DEADLINE2 = new DeadlineTask(new Name("deadline name2"), UNIX_EPOCH.plusDays(1), true);
        } catch (IllegalValueException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void setupObjectMapper() {
        final SimpleModule module = new SimpleModule();
        module.setMixInAnnotation(Name.class, JsonNameMixin.class);
        module.setMixInAnnotation(DeadlineTask.class, JsonDeadlineTaskMixin.class);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules().registerModule(module);
    }

    @Test
    public void serialize() throws Exception {
        final String expected1 = TEST_DEADLINE1_JSON;
        final String expected2 = TEST_DEADLINE2_JSON;
        final String actual1 = objectMapper.writeValueAsString(TEST_DEADLINE1);
        final String actual2 = objectMapper.writeValueAsString(TEST_DEADLINE2);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void deserialize() throws Exception {
        final DeadlineTask expected1 = TEST_DEADLINE1;
        final DeadlineTask expected2 = TEST_DEADLINE2;
        final DeadlineTask actual1 = objectMapper.readValue(TEST_DEADLINE1_JSON, DeadlineTask.class);
        final DeadlineTask actual2 = objectMapper.readValue(TEST_DEADLINE2_JSON, DeadlineTask.class);
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

}
```
