package seedu.address.ui;

import java.util.Date;
import java.util.logging.Logger;

import org.controlsfx.control.StatusBar;

import com.google.common.eventbus.Subscribe;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.model.ReadOnlyModel;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Pane> {

    private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);

    private static final String FXML = "/view/StatusBarFooter.fxml";

    @FXML
    private StatusBar taskCounter;

    @FXML
    private StatusBar syncStatus;

    @FXML
    private StatusBar saveLocationStatus;

    public StatusBarFooter(String saveLocation, ReadOnlyModel model) {
        super(FXML);
        connectTaskCounter(model);
        setSyncStatus("Not updated yet in this session");
        setSaveLocation("./" + saveLocation);
    }

    private void connectTaskCounter(ReadOnlyModel model) {
        ObservableValue<String> totalTasks = Bindings.size(model.getFloatingTaskList())
                                                .add(Bindings.size(model.getDeadlineTaskList()))
                                                .add(Bindings.size(model.getEventTaskList()))
                                                .asString("Total number of tasks in Task Tracker: %d");
        taskCounter.textProperty().bind(totalTasks);
    }

    private void setTaskCounter(String counterValue) {
        taskCounter.setText("Total number of tasks in Task Tracker: " + counterValue);
    }

    private void setSaveLocation(String location) {
        this.saveLocationStatus.setText(location);
    }

    private void setSyncStatus(String status) {
        this.syncStatus.setText(status);
    }

    @Subscribe
    public void handleAddressBookChangedEvent(TaskBookChangedEvent abce) {
        String lastUpdated = (new Date()).toString();
        logger.info(LogsCenter.getEventHandlingLogMessage(abce, "Setting last updated status to " + lastUpdated));
        setSyncStatus("Last Updated: " + lastUpdated);
    }
}
