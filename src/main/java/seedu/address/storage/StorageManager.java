package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyTaskBook;
import seedu.address.model.config.ReadOnlyConfig;
import seedu.address.storage.config.ConfigStorage;
import seedu.address.storage.config.JsonConfigStorage;

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

    @Override
    public String getTaskBookFilePath() {
        return taskBookStorage.getTaskBookFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook() throws DataConversionException, IOException {
        return readTaskBook(taskBookStorage.getTaskBookFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskBook(String filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskBookStorage.readTaskBook(filePath);
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook) throws IOException {
        saveTaskBook(taskBook, taskBookStorage.getTaskBookFilePath());
    }

    @Override
    public void saveTaskBook(ReadOnlyTaskBook taskBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskBookStorage.saveTaskBook(taskBook, filePath);
    }

    @Override
    @Subscribe
    public void handleTaskBookChangedEvent(TaskBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
