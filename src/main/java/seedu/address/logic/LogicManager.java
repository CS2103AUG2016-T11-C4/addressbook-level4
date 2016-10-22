package seedu.address.logic;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.parser.Parser;
import seedu.address.model.Model;
import seedu.address.model.TaskBookChangeListener;
import seedu.address.model.config.Config;
import seedu.address.model.config.ReadOnlyConfig;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Task;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final Parser parser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.parser = new Parser();
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
        commitNewModelState(commandText);
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

    /**
     * Commit any changes to the model.
     */
    private void commitNewModelState(String commandText) {
        if (model.hasUncommittedChanges()) {
            model.commit(commandText);
        }
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    @Override
    public ObservableList<FloatingTask> getFilteredFloatingTaskList() {
        return model.getFilteredFloatingTaskList();
    }

    @Override
    public ObservableList<EventTask> getFilteredEventTaskList() {
        return model.getFilteredEventTaskList();
    }

    @Override
    public ObservableList<DeadlineTask> getFilteredDeadlineTaskList() {
        return model.getFilteredDeadlineTaskList();
    }

}
