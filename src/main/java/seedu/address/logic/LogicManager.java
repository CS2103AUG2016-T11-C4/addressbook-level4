package seedu.address.logic;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.TaskTrackerParser;
import seedu.address.model.Model;
import seedu.address.model.TaskBookChangeListener;
import seedu.address.model.config.Config;
import seedu.address.model.config.ReadOnlyConfig;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final TaskTrackerParser parser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.parser = new TaskTrackerParser();
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        final TaskBookChangeListener taskBookListener = new TaskBookChangeListener(model.getTaskBook());
        final Config oldConfig = new Config(model.getConfig());
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        final CommandResult result = command.execute();
        updateConfigStorage(oldConfig);
        updateTaskBookStorage(taskBookListener);

        if (model.hasUncommittedChanges() && !(command instanceof UndoCommand) && !(command instanceof RedoCommand) ) {
            model.recordState(command);
        }
        return result;
    }

    private void updateTaskBookStorage(TaskBookChangeListener listener) {
        try {
            if (listener.getHasChanged()) {
                logger.info("Task book data changed, saving to file");
                storage.saveTaskBook(model.getTaskBook());
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

    @Override
    public ObservableList<FloatingTask> getFilteredFloatingTaskList() {
        return model.getFilteredFloatingTaskList();
    }

    @Override
    public ObservableList<DeadlineTask> getFilteredDeadlineTaskList() {
        return model.getFilteredDeadlineTaskList();
    }

    @Override
    public ObservableList<EventTask> getFilteredEventTaskList() {
        return model.getFilteredEventTaskList();
    }

}
