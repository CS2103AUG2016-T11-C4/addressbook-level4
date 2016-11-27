# pyokagan@gmail.com

###### src/test/java/seedu/address/logic/parser/TimeParserTest.java

``` java
public class TimeParserTest {

    private static final LocalTime TEST_TIME = LocalTime.of(3, 14);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TimeParser parser;

    @Before
    public void setupParser() {
        parser = new TimeParser(TEST_TIME);
    }

    @Test
    public void constructor() {
        assertEquals(TEST_TIME, parser.getReferenceTime());
    }

    @Test
    public void parse() throws Exception {
        assertParse("  4am  ", LocalTime.of(4, 0));
        assertParse("1.23pm", LocalTime.of(13, 23));
        assertParse("2:45am", LocalTime.of(2, 45));
        assertParse("12am", LocalTime.of(0, 0));
        assertParse("12pm", LocalTime.of(12, 0));
        assertParseFail("13am");
        assertParseFail("4:99am");
        assertParseFail("5:am");
        assertParseFail("not a time at all");
    }

    private void assertParse(String str, LocalTime expected) throws Exception {
        final LocalTime actual = parser.parse(str);
        assertEquals(expected, actual);
    }

    private void assertParseFail(String str) throws Exception {
        thrown.expect(IllegalValueException.class);
        parser.parse(str);
    }

}
```
###### src/test/java/seedu/address/logic/parser/EditEventParserTest.java

``` java
public class EditEventParserTest {

    private EditEventParser parser;

    @Before
    public void setupParser() {
        parser = new EditEventParser(UNIX_EPOCH);
    }

    @Test
    public void parse() {
        // No modifications
        assertParse("1", 1, null, null, null, null);

        // startDate
        assertParse("2 sd-4/5/2016", 2, LocalDate.of(2016, 5, 4), null, null, null);

        // startTime
        assertParse("3 st-5:32am", 3, null, LocalTime.of(5, 32), null, null);

        // endDate
        assertParse("4 ed-7/8", 4, null, null, LocalDate.of(1970, 8, 7), null);

        // endTime
        assertParse("5 et-7pm", 5, null, null, null, LocalTime.of(19, 0));

        // Index cannot be negative
        assertIncorrect("-1");

        // Invalid flags
        assertIncorrect("8 invalid-flag");
    }

    private void assertParse(String args, int targetIndex, LocalDate newStartDate, LocalTime newStartTime,
                             LocalDate newEndDate, LocalTime newEndTime) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof EditEventCommand);
        final EditEventCommand editCommand = (EditEventCommand) command;
        assertEquals(targetIndex, editCommand.getTargetIndex());
        assertEquals(newStartDate, editCommand.getNewStartDate());
        assertEquals(newStartTime, editCommand.getNewStartTime());
        assertEquals(newEndDate, editCommand.getNewEndDate());
        assertEquals(newEndTime, editCommand.getNewEndTime());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}
```
###### src/test/java/seedu/address/logic/parser/DateParserTest.java

``` java
public class DateParserTest {

    private static final LocalDate TEST_DATE = LocalDate.of(2015, 3, 14);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DateParser parser;

    @Before
    public void setupParser() {
        parser = new DateParser(TEST_DATE);
    }

    @Test
    public void constructor() {
        assertEquals(TEST_DATE, parser.getReferenceDate());
    }

    @Test
    public void parse() throws Exception {
        assertParse("tdy", TEST_DATE);
        assertParse("tmr", TEST_DATE.plusDays(1));
        assertParse("yst", TEST_DATE.minusDays(1));
        assertParse(" 1 ", LocalDate.of(2015, 3, 1));
        assertParse("1/2", LocalDate.of(2015, 2, 1));
        assertParse("1/2/2014", LocalDate.of(2014, 2, 1));
        assertParseFail("0/1/2016"); // No such day
        assertParseFail("1/0/2015"); // No such month
        assertParseFail("1/13/2015"); // No such month
        assertParse("31/12/2014", LocalDate.of(2014, 12, 31)); // December has 31 days
        assertParseFail("31/9/2016"); // September only has 30 days
        assertParse("29/2/2016", LocalDate.of(2016, 2, 29)); // A leap year
        assertParseFail("29/2/2015"); // Not a leap year
        assertParseFail("1/2/14"); // We don't support 2-digit years
        assertParseFail("not a date at all");
    }

    private void assertParse(String str, LocalDate expected) throws Exception {
        final LocalDate actual = parser.parse(str);
        assertEquals(expected, actual);
    }

    private void assertParseFail(String str) throws Exception {
        thrown.expect(IllegalValueException.class);
        parser.parse(str);
    }

}
```
###### src/test/java/seedu/address/logic/parser/AddEventParserTest.java

``` java
public class AddEventParserTest {

    private AddEventParser parser;

    @Before
    public void setupParser() {
        parser = new AddEventParser(PI_DAY);
    }

    @Test
    public void parse() {
        // All arguments provided
        assertParse("\"a\" 1/2/2000 4:30am to 2/3/2100 6:32pm", "a", LocalDateTime.of(2000, 2, 1, 4, 30),
                    LocalDateTime.of(2100, 3, 2, 18, 32));

        // If startDate is not given, then it is the current date
        assertParse("\"a\" 7:23am to 4/6/2654 8:24pm", "a", LocalDateTime.of(2653, 9, 15, 7, 23),
                LocalDateTime.of(2654, 6, 4, 20, 24));

        // If startTime is not given, then it is 12am
        assertParse("\"a\" 3/4/2015 to 4/5/2015 7pm", "a", LocalDateTime.of(2015, 4, 3, 0, 0),
                LocalDateTime.of(2015, 5, 4, 19, 0));

        // If endDate is not given, then it is the same as startDate
        assertParse("\"a\" 1/2/2000 5:41am to 6:32pm", "a", LocalDateTime.of(2000, 2, 1, 5, 41),
                LocalDateTime.of(2000, 2, 1, 18, 32));

        // If endTime is not given, then it is 11.59pm
        assertParse("\"a\" 8/7/2014 12:45am to 9/7/2014", "a",
                LocalDateTime.of(2014, 7, 8, 0, 45), LocalDateTime.of(2014, 7, 9, 23, 59));

        // Must have at least one of { startDate, startTime }, and at least one of { endDate, endTime }
        assertIncorrect("\"a\" 1/2/2000 4:30am");
        assertIncorrect("\"a\"");
        assertIncorrect("");

        // Must have keyword "to"
        assertIncorrect("\"a\" 4/5/2016 7:23am 6/7/2017 8:42pm");

        // Too many arguments
        assertIncorrect("\"a\" 4/5/2016 7:23am to 6/7/2017 8:42pm extraArg");
    }

    private void assertParse(String args, String name, LocalDateTime start, LocalDateTime end) {
        final EventTask expected;
        try {
            expected = new EventTask(name, start, end);
        } catch (IllegalValueException e) {
            throw new AssertionError("should not happen", e);
        }
        final Command command = parser.parse(args);
        assertTrue(command instanceof AddEventCommand);
        assertEquals(expected, ((AddEventCommand)command).getTask());
    }

    private void assertIncorrect(String args) {
        final Command command = parser.parse(args);
        assertTrue(command instanceof IncorrectCommand);
    }

}
```
###### src/test/java/seedu/address/MainAppTest.java

``` java
/**
 * Integration tests
 */
@Category({GuiTests.class})
public class MainAppTest extends FxRobot {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Application application;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        File configFile = setupConfig(new File(tempFolder.getRoot(), "taskbook.json"),
                                      new File(tempFolder.getRoot(), "userprefs.json"));
        application = FxToolkit.setupApplication(() -> new MainApp(configFile.getAbsolutePath()));
        FxToolkit.showStage();
    }

    private File setupConfig(File taskBookFile, File userPrefsFile) throws Exception {
        File configFile = tempFolder.newFile("config.json");
        JsonGenerator jgen = new JsonFactory().createGenerator(configFile, JsonEncoding.UTF8);
        jgen.writeStartObject();
        jgen.writeObjectField("taskBookFilePath", taskBookFile.getAbsolutePath());
        jgen.writeObjectField("userPrefsFilePath", userPrefsFile.getAbsolutePath());
        jgen.writeEndObject();
        jgen.close();
        return configFile;
    }

    @After
    public void teardown() throws Exception {
        FxToolkit.cleanupApplication(application);
    }

    @Test
    public void startStop() {
    }

}
```
###### src/test/java/seedu/address/testutil/TestUtil.java

``` java
    private static Task[] getSampleTaskData() {
        try {
            return new Task[]{
                new Task(new Name("Ali Muster")),
                new Task(new Name("Boris Mueller")),
                new Task(new Name("Carl Kurz")),
                new Task(new Name("Daniel Meier")),
                new Task(new Name("Elle Meyer")),
                new Task(new Name("Fiona Kunz")),
                new Task(new Name("George Best")),
                new Task(new Name("Hoon Meier")),
                new Task(new Name("Ida Mueller"))
            };
        } catch (IllegalValueException e) {
            assert false;
            //not possible
            return null;
        }
    }

    public static List<Task> generateSampleTaskData() {
        return Arrays.asList(sampleTaskData);
    }

```
###### src/test/java/seedu/address/testutil/TestUtil.java

``` java
    public static TaskBook generateEmptyAddressBook() {
        return new TaskBook();
    }

    public static TaskBook generateSampleStorageAddressBook() {
        return new TaskBook(generateEmptyAddressBook());
    }

```
###### src/test/java/seedu/address/testutil/TestUtil.java

``` java
    /**
     * Removes a subset from the list of tasks.
     * @param tasks The list of tasks
     * @param tasksToRemove The subset of tasks.
     * @return The modified tasks after removal of the subset from tasks.
     */
    public static TestTask[] removeTasksFromList(final TestTask[] tasks, TestTask... tasksToRemove) {
        List<TestTask> listOfTasks = asList(tasks);
        listOfTasks.removeAll(asList(tasksToRemove));
        return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
    }


```
###### src/test/java/seedu/address/testutil/TestUtil.java

``` java
    /**
     * Replaces tasks[i] with a task.
     * @param tasks The array of tasks.
     * @param task The replacement task
     * @param index The index of the task to be replaced.
     * @return
     */
    public static TestTask[] removeTaskFromList(TestTask[] tasks, TestTask task, int index) {
        tasks[index] = task;
        return tasks;
    }

    /**
     * Appends persons to the array of persons.
     * @param tasks A array of persons.
     * @param tasksToAdd The persons that are to be appended behind the original array.
     * @return The modified array of persons.
     */
    public static TestTask[] addPersonsToList(final TestTask[] tasks, TestTask... tasksToAdd) {
        List<TestTask> listOfTasks = asList(tasks);
        listOfTasks.addAll(asList(tasksToAdd));
        return listOfTasks.toArray(new TestTask[listOfTasks.size()]);
    }

```
###### src/test/java/seedu/address/testutil/TaskBuilder.java

``` java
    public TestTask build() {
        return new TestTask(name);
    }

}
```
###### src/test/java/seedu/address/testutil/TestTask.java

``` java
/**
 * A mutable person object. For testing only.
 */
public class TestTask extends Task {

    public TestTask(Name name) {
        super(name);
    }

```
###### src/test/java/seedu/address/testutil/TypicalTestTasks.java

``` java
/**
 *
 */
public class TypicalTestTasks {

    public static TestTask alice;

    public static TestTask benson;

    public static TestTask carl;

    public static TestTask daniel;

    public static TestTask elle;

    public static TestTask fiona;

    public static TestTask george;

    public static TestTask hoon;

    public static TestTask ida;

    public TypicalTestTasks() {
        try {
            alice = new TaskBuilder().withName("Alice Pauline").build();
            benson = new TaskBuilder().withName("Benson Meier").build();
            carl = new TaskBuilder().withName("Carl Kurz").build();
            daniel = new TaskBuilder().withName("Daniel Meier").build();
            elle = new TaskBuilder().withName("Elle Meyer").build();
            fiona = new TaskBuilder().withName("Fiona Kunz").build();
            george = new TaskBuilder().withName("George Best").build();

            //Manually added
            hoon = new TaskBuilder().withName("Hoon Meier").build();
            ida = new TaskBuilder().withName("Ida Mueller").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(TaskBook ab) {
        ab.addTask(new Task(alice));
        ab.addTask(new Task(benson));
        ab.addTask(new Task(carl));
        ab.addTask(new Task(daniel));
        ab.addTask(new Task(elle));
        ab.addTask(new Task(fiona));
        ab.addTask(new Task(george));
    }

    public TestTask[] getTypicalPersons() {
        return new TestTask[]{alice, benson, carl, daniel, elle, fiona, george};
    }

```
###### src/test/java/seedu/address/model/ModelTest.java

``` java
    @Test
    public void getEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getEventTask(0);
    }

    @Test
    public void removeEventTask_removesIndexInFilteredList() throws Exception {
        model.addEventTask(tpent.lunchWithBillGates);
        model.addEventTask(tpent.launchNuclearWeapons);
        model.setEventTaskFilter(eventTask -> eventTask.equals(tpent.launchNuclearWeapons));
        model.removeEventTask(0);
        model.setEventTaskFilter(null);
        assertEquals(Arrays.asList(tpent.lunchWithBillGates), model.getFilteredEventTaskList());
    }

    @Test
    public void removeEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeEventTask(0);
    }

```
###### src/test/java/seedu/address/model/TaskBookTest.java

``` java
    @Test
    public void setEventTasks() {
        taskBook.setEventTasks(typicalEventTasks.getEventTasks());
        assertEquals(typicalEventTasks.getEventTasks(), taskBook.getEventTasks());
    }

    @Test
    public void addEventTask_appendsToList() {
        final ArrayList<EventTask> expected = new ArrayList<>();
        taskBook.addEventTask(typicalEventTasks.lunchWithBillGates);
        expected.add(typicalEventTasks.lunchWithBillGates);
        assertEquals(expected, taskBook.getEventTasks());
        taskBook.addEventTask(typicalEventTasks.launchNuclearWeapons);
        expected.add(typicalEventTasks.launchNuclearWeapons);
        assertEquals(expected, taskBook.getEventTasks());
    }

    @Test
    public void removeEventTask_removesFromList() {
        final ArrayList<EventTask> expected = new ArrayList<>(typicalEventTasks.getEventTasks());
        expected.remove(0);
        taskBook.setEventTasks(typicalEventTasks.getEventTasks());
        taskBook.removeEventTask(0);
        assertEquals(expected, taskBook.getEventTasks());
    }

```
###### src/test/java/seedu/address/model/task/EventTaskTest.java

``` java
public class EventTaskTest {

    private EventTask eventTask;

    @Before
    public void setupEventTask() throws Exception {
        final Name name = new Name("Event Task Name");
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.plusHours(1);
        final LocalDateTimeDuration duration = new LocalDateTimeDuration(start, end);
        eventTask = new EventTask(name, duration);
    }

    @Test
    public void getDuration_returnsDuration() throws Exception {
        final LocalDateTimeDuration expected = new LocalDateTimeDuration(UNIX_EPOCH, UNIX_EPOCH.plusHours(1));
        final LocalDateTimeDuration actual = eventTask.getDuration();
        assertEquals(expected, actual);
    }

    @Test
    public void getStart_returnsStart() throws Exception {
        final LocalDateTime expected = UNIX_EPOCH;
        final LocalDateTime actual = eventTask.getStart();
        assertEquals(expected, actual);
    }

    @Test
    public void getEnd_returnsEnd() throws Exception {
        final LocalDateTime expected = UNIX_EPOCH.plusHours(1);
        final LocalDateTime actual = eventTask.getEnd();
        assertEquals(expected, actual);
    }

    @Test
    public void equals_isEqual_returnsTrue() throws Exception {
        final EventTask other = new EventTask("Event Task Name", UNIX_EPOCH, UNIX_EPOCH.plusHours(1));
        assertTrue(eventTask.equals(other));
        assertTrue(eventTask.hashCode() == other.hashCode());
    }

    @Test
    public void equals_notEqual_returnsFalse() throws Exception {
        final EventTask other = new EventTask(new Name("Event Task Name"), UNIX_EPOCH, UNIX_EPOCH.plusHours(2));
        assertFalse(eventTask.equals(other));
        assertFalse(eventTask.hashCode() == other.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        final String expected = "EventTask[name=Event Task Name, "
                                + "duration=LocalDateTimeDuration[start=1970-01-01T00:00, "
                                + "end=1970-01-01T01:00]]";
        final String actual = eventTask.toString();
        assertEquals(expected, actual);
    }

}
```
###### src/test/java/seedu/address/model/task/TypicalEventTasks.java

``` java
public class TypicalEventTasks {

    public final EventTask lunchWithBillGates;

    public final EventTask launchNuclearWeapons;

    public TypicalEventTasks() {
        try {
            lunchWithBillGates = new EventTask("Lunch with Bill Gates", UNIX_EPOCH, UNIX_EPOCH.plusHours(1));
            launchNuclearWeapons = new EventTask("Launch nuclear weapons", UNIX_EPOCH.plusHours(1),
                                                 UNIX_EPOCH.plusHours(2));
        } catch (IllegalValueException e) {
            throw new AssertionError("this should not happen", e);
        }
    }

    public List<EventTask> getEventTasks() {
        final EventTask[] tasks = {lunchWithBillGates, launchNuclearWeapons};
        return Arrays.asList(tasks);
    }

}
```
###### src/test/java/seedu/address/ui/UiManagerTest.java

``` java
@Category({GuiTests.class})
public class UiManagerTest extends FxRobot {

    private Config config;

    private ObservableList<FloatingTask> floatingTaskList;

    private ObservableList<EventTask> eventTaskList;

    private ObservableList<DeadlineTask> deadlineTaskList;

    @Mock
    private Logic logic;

    private UiManager uiManager;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        floatingTaskList = FXCollections.observableArrayList();
        eventTaskList = FXCollections.observableArrayList();
        deadlineTaskList = FXCollections.observableArrayList();
        Mockito.when(logic.getFilteredFloatingTaskList())
               .thenReturn(FXCollections.unmodifiableObservableList(floatingTaskList));
        Mockito.when(logic.getFilteredDeadlineTaskList())
               .thenReturn(FXCollections.unmodifiableObservableList(deadlineTaskList));
        Mockito.when(logic.getFilteredEventTaskList())
               .thenReturn(FXCollections.unmodifiableObservableList(eventTaskList));
        config = new Config();
        uiManager = new UiManager(logic, config);
        FxToolkit.setupStage(stage -> {
            uiManager.start(stage);
        });
        FxToolkit.showStage();
    }

    @After
    public void teardown() throws Exception {
        interact(() -> uiManager.stop());
        FxToolkit.cleanupStages();
    }

    @Test
    public void startStop_works() {
    }

    @Test
    public void onShowHelpRequestEvent_showsHelp() throws Exception {
        Platform.runLater((() -> EventsCenter.getInstance().post(new ShowHelpRequestEvent())));
        WaitForAsyncUtils.waitFor(3, TimeUnit.SECONDS, () -> {
            try {
                Window w = window(window -> from(rootNode(window)).lookup("#webView").tryQuery().isPresent());
                interact(() -> w.hide());
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }
        });
    }

}
```
###### src/test/java/seedu/address/ui/EventTaskListCardTest.java

``` java
@Category({GuiTests.class})
public class EventTaskListCardTest extends FxRobot {

    private TypicalEventTasks tet = new TypicalEventTasks();

    private EventTaskListCard eventTaskListCard;

    private Label indexLabel;

    private Label nameLabel;

    private Label startLabel;

    private Label endLabel;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    private void setupNodes() throws Exception {
        FxToolkit.showStage();
        indexLabel = lookup("#indexLabel").query();
        nameLabel = lookup("#nameLabel").query();
        startLabel = lookup("#startLabel").query();
        endLabel = lookup("#endLabel").query();
    }

    @Test
    public void withEventTask_showsInfoInLabels() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            eventTaskListCard = new EventTaskListCard(tet.lunchWithBillGates, 42);
            return eventTaskListCard.getRoot();
        });
        setupNodes();
        assertEquals("42. ", indexLabel.getText());
        assertEquals("Lunch with Bill Gates", nameLabel.getText());
        assertEquals("1970-01-01T00:00", startLabel.getText());
        assertEquals("1970-01-01T01:00", endLabel.getText());
    }

    @Test
    public void nullEventTask_becomesInvisible() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            eventTaskListCard = new EventTaskListCard(null, 34);
            return eventTaskListCard.getRoot();
        });
        setupNodes();
        assertFalse(eventTaskListCard.getRoot().isVisible());
    }

}
```
###### src/test/java/seedu/address/ui/ResultDisplayTest.java

``` java
@Category({GuiTests.class})
public class ResultDisplayTest extends GuiTest {

    private ResultDisplay resultDisplay;

    private TextArea textArea;

    @Override
    protected Parent getRootNode() {
        resultDisplay = new ResultDisplay();
        return resultDisplay.getRoot();
    }

    @Before
    public void setupNodes() {
        textArea = find("#resultDisplay");
    }

    @Test
    public void constructor() {
        assertEquals("", textArea.getText());
    }

    @Test
    public void postMessage_setsMessage() {
        resultDisplay.postMessage("test message");
        assertEquals("test message", textArea.getText());
    }

}
```
###### src/test/java/seedu/address/ui/StatusBarFooterTest.java

``` java
@Category({GuiTests.class})
public class StatusBarFooterTest extends GuiTest {

    private StatusBarFooter statusBarFooter;

    private StatusBar syncStatus;

    private StatusBar saveLocationStatus;

    @Override
    protected Parent getRootNode() {
        statusBarFooter = new StatusBarFooter("save/location");
        return statusBarFooter.getRoot();
    }

    @Before
    public void setupNodes() {
        syncStatus = find("#syncStatus");
        saveLocationStatus = find("#saveLocationStatus");
    }

    @Test
    public void constructor() {
        assertEquals("Not updated yet in this session", syncStatus.getText());
        assertEquals("./save/location", saveLocationStatus.getText());
    }

}
```
###### src/test/java/seedu/address/ui/UiRegionTest.java

``` java
@Category({GuiTests.class})
public class UiRegionTest extends GuiTest {

    private UiRegion uiRegion;

    @Override
    protected Parent getRootNode() {
        uiRegion = new UiRegion();
        return uiRegion;
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), uiRegion.getChildrenUnmodifiable());
    }

    @Test
    public void setNode_addsOrReplacesChild() {
        final Region node1 = new Region();
        interact(() -> uiRegion.setNode(node1));
        assertEquals(node1, uiRegion.getNode());
        assertEquals(Arrays.asList(node1), uiRegion.getChildrenUnmodifiable());
        final Region node2 = new Region();
        interact(() -> uiRegion.setNode(node2));
        assertEquals(node2, uiRegion.getNode());
        assertEquals(Arrays.asList(node2), uiRegion.getChildrenUnmodifiable());
    }

}
```
###### src/test/java/seedu/address/ui/CommandBoxTest.java

``` java
/**
 * Unit Tests for CommandBox.
 */
@Category({GuiTests.class})
public class CommandBoxTest extends GuiTest {

    @Mock
    private Logic logic;

    @Mock
    private ResultDisplay resultDisplay;

    private CommandBox commandBox;

    private TextField textField;

    @Override
    protected Parent getRootNode() {
        MockitoAnnotations.initMocks(this);
        commandBox = new CommandBox(resultDisplay, logic);
        return commandBox.getRoot();
    }

    @Before
    public void setupNodes() {
        textField = find("#commandTextField");
    }

    @Test
    public void commandInputChanged_callsLogicExecute() {
        final String inputCommand = "some command";
        final CommandResult result = new CommandResult("some result");
        Mockito.when(logic.execute(inputCommand)).thenReturn(result);

        textField.setText(inputCommand);
        clickOn(textField).push(KeyCode.ENTER);

        Mockito.verify(logic).execute(inputCommand);
        Mockito.verify(resultDisplay).postMessage(result.feedbackToUser);
        assertEquals("", textField.getText());
    }

    @Test
    public void incorrectCommand_restoresCommandText() {
        final String inputCommand = "some command";
        final CommandResult result = new CommandResult("some result");
        Mockito.when(logic.execute(inputCommand)).thenReturn(result);

        textField.setText(inputCommand);
        clickOn(textField).push(KeyCode.ENTER);
        assertEquals("", textField.getText());

        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(null));
        assertEquals(inputCommand, textField.getText());
    }

}
```
###### src/test/java/seedu/address/ui/HelpWindowTest.java

``` java
@Category({GuiTests.class})
public class HelpWindowTest extends FxRobot {

    private HelpWindow helpWindow;

    private WebView webView;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setupHelpWindow() throws Exception {
        FxToolkit.registerStage(() -> {
            helpWindow = new HelpWindow();
            return helpWindow.getRoot();
        });
        FxToolkit.showStage();
        webView = lookup("#webView").query();
    }

    @After
    public void teardownHelpWindow() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Test
    public void constructor() {
        assertEquals("https://github.com/se-edu/addressbook-level4/blob/master/docs/UserGuide.md",
                     webView.getEngine().getLocation());
    }

}
```
###### src/test/java/seedu/address/ui/DeadlineTaskListCardTest.java

``` java
@Category({GuiTests.class})
public class DeadlineTaskListCardTest extends FxRobot {

    private TypicalDeadlineTasks tdt = new TypicalDeadlineTasks();

    private DeadlineTaskListCard deadlineTaskListCard;

    private Label indexLabel;

    private Label nameLabel;

    private Label dueLabel;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    private void setupNodes() throws Exception {
        FxToolkit.showStage();
        indexLabel = lookup("#indexLabel").query();
        nameLabel = lookup("#nameLabel").query();
        dueLabel = lookup("#dueLabel").query();
    }

    @Test
    public void withDeadlineTask_showsInfoInLabels() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            deadlineTaskListCard = new DeadlineTaskListCard(tdt.speechTranscript, 42);
            return deadlineTaskListCard.getRoot();
        });
        setupNodes();
        assertEquals("42. ", indexLabel.getText());
        assertEquals("Speech Transcript", nameLabel.getText());
        assertEquals("1970-01-01T01:00", dueLabel.getText());
    }

    @Test
    public void nullDeadlineTask_becomesInvisible() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            deadlineTaskListCard = new DeadlineTaskListCard(null, 34);
            return deadlineTaskListCard.getRoot();
        });
        setupNodes();
        assertFalse(deadlineTaskListCard.getRoot().isVisible());
    }

}
```
###### src/test/java/seedu/address/ui/FloatingTaskListCardTest.java

``` java
@Category({GuiTests.class})
public class FloatingTaskListCardTest extends FxRobot {

    private TypicalFloatingTasks tft = new TypicalFloatingTasks();

    private FloatingTaskListCard floatingTaskListCard;

    private Label indexLabel;

    private Label nameLabel;

    private Label priorityLabel;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    private void setupNodes() throws Exception {
        FxToolkit.showStage();
        indexLabel = lookup("#indexLabel").query();
        nameLabel = lookup("#nameLabel").query();
        priorityLabel = lookup("#priorityLabel").query();
    }

    @Test
    public void withFloatingTask_displaysInfoInLabels() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            floatingTaskListCard = new FloatingTaskListCard(tft.buyAHelicopter, 42);
            return floatingTaskListCard.getRoot();
        });
        setupNodes();
        assertEquals("42. ", indexLabel.getText());
        assertEquals("buy A Helicopter", nameLabel.getText());
        assertEquals("4", priorityLabel.getText());
    }

    @Test
    public void nullFloatingTask_becomesInvisible() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            floatingTaskListCard = new FloatingTaskListCard(null, 31);
            return floatingTaskListCard.getRoot();
        });
        assertFalse(floatingTaskListCard.getRoot().isVisible());
    }

}
```
###### src/test/java/seedu/address/storage/StorageManagerTest.java

``` java
    @Test
    public void addressBookReadSave() throws Exception {
        TaskBook original = new TypicalTestTasks().getTypicalAddressBook();
        storageManager.saveTaskBook(original);
        ReadOnlyTaskBook retrieved = storageManager.readTaskBook().get();
        assertEquals(original, new TaskBook(retrieved));
        //More extensive testing of TaskBook saving/reading is done in XmlTaskBookStorageTest
    }

```
###### src/test/java/seedu/address/storage/config/JsonConfigStorageTest.java

``` java
public class JsonConfigStorageTest {

    private Config typicalConfig;

    private JsonFactory jsonFactory;

    private File configFile;

    private JsonConfigStorage configStorage;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setup() {
        typicalConfig = new Config();
        typicalConfig.setLogLevel(Level.INFO);
        typicalConfig.setTaskBookFilePath("taskbook.json");
        jsonFactory = new JsonFactory();
        configFile = new File(testFolder.getRoot(), "config.json");
        configStorage = new JsonConfigStorage(configFile.getAbsolutePath());
    }

    @Test
    public void constructor_nullPath_assertionError() {
        thrown.expect(AssertionError.class);
        new JsonConfigStorage(null);
    }

    @Test
    public void getConfigFilePath_returnsConfigFilePath() {
        assertEquals(configFile.getAbsolutePath(), configStorage.getConfigFilePath());
    }

    @Test
    public void readConfig_null_assertionError() throws Exception {
        thrown.expect(AssertionError.class);
        configStorage.readConfig(null);
    }

    @Test
    public void readConfig_missingFile_emptyResult() throws Exception {
        assertFalse(configStorage.readConfig().isPresent());
    }

    @Test
    public void readConfig_notJsonFormat_exceptionThrown() throws Exception {
        FileUtil.writeToFile(configFile, "not json format");
        thrown.expect(DataConversionException.class);
        configStorage.readConfig();
    }

    @Test
    public void readConfig_fileInOrder_successfullyRead() throws Exception {
        final JsonGenerator jgen = jsonFactory.createGenerator(configFile, JsonEncoding.UTF8);
        jgen.writeStartObject();
        jgen.writeObjectField("logLevel", "INFO");
        jgen.writeObjectField("taskBookFilePath", "taskbook.json");
        jgen.writeEndObject();
        jgen.close();
        final ReadOnlyConfig actual = configStorage.readConfig().get();
        assertEquals(typicalConfig, actual);
    }

    @Test
    public void readConfig_valuesMissingFromFile_defaultValuesUsed() throws Exception {
        final JsonGenerator jgen = jsonFactory.createGenerator(configFile, JsonEncoding.UTF8);
        jgen.writeStartObject();
        jgen.writeEndObject();
        jgen.close();
        final ReadOnlyConfig actual = configStorage.readConfig().get();
        assertEquals(new Config(), actual);
    }

    @Test
    public void readConfig_extraValuesInFile_extraValuesIgnored() throws Exception {
        final JsonGenerator jgen = jsonFactory.createGenerator(configFile, JsonEncoding.UTF8);
        jgen.writeStartObject();
        jgen.writeObjectField("logLevel", "INFO");
        jgen.writeObjectField("taskBookFilePath", "taskbook.json");
        jgen.writeObjectField("extraField", "extraValue");
        jgen.writeEndObject();
        jgen.close();
        final ReadOnlyConfig actual = configStorage.readConfig().get();
        assertEquals(typicalConfig, actual);
    }

    @Test
    public void saveConfig_nullConfig_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        configStorage.saveConfig(null);
    }

    @Test
    public void saveConfig_nullFile_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        configStorage.saveConfig(typicalConfig, null);
    }

    @Test
    public void saveConfig_allInOrder_success() throws Exception {
        configFile = new File(testFolder.getRoot(), "a/config.json");
        configStorage = new JsonConfigStorage(configFile.getAbsolutePath());

        // Try saving when the file and parent directories do not exist
        configStorage.saveConfig(typicalConfig);
        ReadOnlyConfig readBack = configStorage.readConfig().get();
        assertEquals(typicalConfig, readBack);

        // Try saving when the file exists
        typicalConfig.setLogLevel(Level.FINE);
        configStorage.saveConfig(typicalConfig);
        readBack = configStorage.readConfig().get();
        assertEquals(typicalConfig, readBack);
    }

}
```
###### src/test/java/seedu/address/storage/JsonTaskBookStorageTest.java

``` java
public class JsonTaskBookStorageTest {

    private static String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/JsonTaskBookStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private Optional<ReadOnlyTaskBook> readTaskBook(String filePath) throws Exception {
        final JsonTaskBookStorage storage = new JsonTaskBookStorage(filePath);
        return storage.readTaskBook(filePath == null ? null : TEST_DATA_FOLDER + filePath);
    }

    @Test
    public void readTaskBook_nullFilePath_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readTaskBook(null);
    }

    @Test
    public void readTaskBook_missingFile_emptyResult() throws Exception {
        assertFalse(readTaskBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void readTaskBook_notJsonFormat_exceptionThrown() throws Exception {
        thrown.expect(DataConversionException.class);
        readTaskBook("NotJsonFormatTaskBook.json");
    }

    @Test
    public void readAndSaveTaskBook_allInOrder_success() throws Exception {
        final String filePath = testFolder.getRoot().getPath() + "TempTaskBook.json";
        final TypicalTestTasks tt = new TypicalTestTasks();
        final TaskBook original = tt.getTypicalAddressBook();
        final JsonTaskBookStorage storage = new JsonTaskBookStorage(filePath);

        // Save in new file and read back
        storage.saveTaskBook(original);
        final ReadOnlyTaskBook readBack = storage.readTaskBook().get();
        assertEquals(original, readBack);
    }
}
```
###### src/test/java/seedu/address/storage/JsonLocalDateTimeDurationMixinTest.java

``` java
public class JsonLocalDateTimeDurationMixinTest {

    public static final LocalDateTimeDuration UNIX_EPOCH_1HR;
    public static final String UNIX_EPOCH_1HR_JSON = "{\"start\":[1970,1,1,0,0],\"end\":[1970,1,1,1,0]}";

    private static ObjectMapper objectMapper;

    static {
        try {
            UNIX_EPOCH_1HR = new LocalDateTimeDuration(UNIX_EPOCH, UNIX_EPOCH.plusHours(1));
        } catch (IllegalValueException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void setupObjectMapper() {
        final SimpleModule module = new SimpleModule();
        module.setMixInAnnotation(LocalDateTimeDuration.class, JsonLocalDateTimeDurationMixin.class);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules().registerModule(module);
    }

    @Test
    public void serialize() throws Exception {
        final String expected = UNIX_EPOCH_1HR_JSON;
        final String actual = objectMapper.writeValueAsString(UNIX_EPOCH_1HR);
        assertEquals(expected, actual);
    }

    @Test
    public void deserialize() throws Exception {
        final LocalDateTimeDuration expected = UNIX_EPOCH_1HR;
        final LocalDateTimeDuration actual = objectMapper.readValue(UNIX_EPOCH_1HR_JSON,
                                                                    LocalDateTimeDuration.class);
        assertEquals(expected, actual);
    }

}
```
###### src/test/java/seedu/address/storage/JsonEventTaskMixinTest.java

``` java
public class JsonEventTaskMixinTest {

    private static ObjectMapper objectMapper;

    public static final EventTask TEST_EVENT;
    public static final String TEST_EVENT_JSON = "{\"name\":\"event name\",\"start\":[1970,1,1,0,0],"
                                                 + "\"end\":[1970,1,1,1,0]}";

    static {
        try {
            TEST_EVENT = new EventTask(new Name("event name"), UNIX_EPOCH, UNIX_EPOCH.plusHours(1));
        } catch (IllegalValueException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void setupObjectMapper() {
        final SimpleModule module = new SimpleModule();
        module.setMixInAnnotation(Name.class, JsonNameMixin.class);
        module.setMixInAnnotation(EventTask.class, JsonEventTaskMixin.class);
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
        final EventTask expected = TEST_EVENT;
        final EventTask actual = objectMapper.readValue(TEST_EVENT_JSON, EventTask.class);
        assertEquals(expected, actual);
    }

}
```
###### src/test/java/seedu/address/commons/time/LocalDateTimeDurationTest.java

``` java
public class LocalDateTimeDurationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_validArguments_works() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.plusDays(1);
        final LocalDateTimeDuration zonedDateTimeDuration = new LocalDateTimeDuration(start, end);
        assertEquals(start, zonedDateTimeDuration.getStart());
        assertEquals(end, zonedDateTimeDuration.getEnd());
    }

    @Test
    public void constructor_zeroDuration_works() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH;
        final LocalDateTimeDuration zonedDateTimeDuration = new LocalDateTimeDuration(start, end);
        assertEquals(start, zonedDateTimeDuration.getStart());
        assertEquals(end, zonedDateTimeDuration.getEnd());
    }

    @Test
    public void constructor_endBeforeBegin_throwsException() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.minusDays(1);
        thrown.expect(IllegalValueException.class);
        new LocalDateTimeDuration(start, end);
    }

    @Test
    public void addTo_ZonedDateTime_addsDuration() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.plusHours(1);
        final LocalDateTimeDuration duration = new LocalDateTimeDuration(start, end);

        final LocalDateTime expected = PI_DAY.plusHours(1);
        // LocalDateTime.plus() calls LocalDateTimeDuration.addTo() internally
        final LocalDateTime actual = PI_DAY.plus(duration);
        assertEquals(expected, actual);
    }

    @Test
    public void get_seconds_returnsCorrectResult() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.plusHours(1).plusMinutes(32);
        final LocalDateTimeDuration duration = new LocalDateTimeDuration(start, end);

        assertEquals(5520, duration.get(ChronoUnit.SECONDS));
    }

    @Test
    public void getUnits_returnsAtLeastOneUnit() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.plusHours(1);
        final LocalDateTimeDuration duration = new LocalDateTimeDuration(start, end);

        final List<TemporalUnit> units = duration.getUnits();
        assertTrue(units.size() > 0);
    }

    @Test
    public void subtractFrom_ZonedDateTime_subtractsDuration() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.plusHours(1);
        final LocalDateTimeDuration duration = new LocalDateTimeDuration(start, end);

        final LocalDateTime expected = PI_DAY.minusHours(1);
        // LocalDateTime.minus() calls LocalDateTimeDuration.subtractFrom() internally
        final LocalDateTime actual = PI_DAY.minus(duration);
        assertEquals(expected, actual);
    }

    @Test
    public void equals_isEquals_returnsTrue() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.plusHours(1);
        final LocalDateTimeDuration duration = new LocalDateTimeDuration(start, end);
        final LocalDateTimeDuration other = new LocalDateTimeDuration(start, end);
        assertTrue(duration.equals(other));
    }

    @Test
    public void equals_notEquals_returnsFalse() throws Exception {
        final LocalDateTime start = UNIX_EPOCH;
        final LocalDateTime end = UNIX_EPOCH.plusHours(1);
        final LocalDateTimeDuration duration = new LocalDateTimeDuration(start, end);
        final LocalDateTime otherEnd = UNIX_EPOCH.plusHours(2);
        final LocalDateTime otherStart = UNIX_EPOCH.minusHours(1);
        assertFalse(duration.equals(new LocalDateTimeDuration(start, otherEnd)));
        assertFalse(duration.equals(new LocalDateTimeDuration(otherStart, end)));
    }

}
```
