package seedu.address;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyTaskBook;
import seedu.address.model.TaskBook;
import seedu.address.model.config.Config;
import seedu.address.model.config.ReadOnlyConfig;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.config.ConfigStorage;
import seedu.address.storage.config.JsonConfigStorage;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    public static final Version VERSION = new Version(0, 4, 0, true);

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

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing TaskBook ]===========================");
        super.init();

        final ConfigStorage configStorage = initConfigStorage(configPath);
        final Config config = initConfig(configStorage);

        storage = new StorageManager(configStorage, config.getTaskBookFilePath());

        initLogging(config);

        model = initModelManager(storage, config);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic, config);

        initEventsCenter();
    }

    private String getApplicationParameter(String parameterName) {
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

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

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

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

    private Config initConfig(ConfigStorage configStorage) throws IOException {
        Config config = new Config();

        try {
            config.resetData(configStorage.readConfig().orElse(new Config()));
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configStorage.getConfigFilePath() + " is not in the correct format. "
                           + "Using default config properties");
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            configStorage.saveConfig(config);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return config;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting TaskBook " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        ui.stop();
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
        // The application uses AWT components, but sometimes they do not clean up their threads properly
        // (operating-system dependent). Ensure the process actually terminates by calling System.exit()
        System.exit(0);
    }
}
