package seedu.address.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.ReadOnlyTaskBook;
import seedu.address.model.TaskBook;
import seedu.address.storage.config.JsonConfigStorage;
import seedu.address.testutil.EventsCollector;
import seedu.address.testutil.TypicalTestTasks;

public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setup() {
        storageManager = new StorageManager(getTempFilePath("config"), getTempFilePath("ab"));
    }

    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }

    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void addressBookReadSave() throws Exception {
        TaskBook original = new TypicalTestTasks().getTypicalAddressBook();
        storageManager.saveTaskBook(original);
        ReadOnlyTaskBook retrieved = storageManager.readTaskBook().get();
        assertEquals(original, new TaskBook(retrieved));
        //More extensive testing of TaskBook saving/reading is done in XmlTaskBookStorageTest
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getTaskBookFilePath());
    }

    @Test
    public void handleAddressBookChangedEvent_exceptionThrown_eventRaised() throws IOException {
        //Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new JsonConfigStorage("dummy"), new JsonTaskBookStorageExceptionThrowingStub("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleTaskBookChangedEvent(new TaskBookChangedEvent(new TaskBook()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class JsonTaskBookStorageExceptionThrowingStub extends JsonTaskBookStorage {

        JsonTaskBookStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskBook(ReadOnlyTaskBook addressBook, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }

}
