package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.UpdateTaskBookListViewEvent;
import seedu.address.model.IndexedItem;
import seedu.address.model.task.FloatingTask;

public class FloatingTaskListPane extends UiPart<Pane> {

    private static final Logger logger = LogsCenter.getLogger(DeadlineTaskListPane.class);

    private static final String FXML = "/view/FloatingTaskListPane.fxml";

    @FXML
    private ListView<IndexedItem<FloatingTask>> floatingTaskListView;

    @FXML
    private Label listedFloatingTaskCounter;

    public FloatingTaskListPane(ObservableList<IndexedItem<FloatingTask>> floatingTaskList,
                                int listedFloatingTaskNumber) {
        super(FXML);
        floatingTaskListView.setItems(floatingTaskList);
        floatingTaskListView.setCellFactory(listView -> new FloatingTaskListCell());
        setFloatingTaskCounter(listedFloatingTaskNumber);
    }

    /**
     * Selects a floating task as specified by its working index.
     */
    public void select(int workingIndex) {
        final List<IndexedItem<FloatingTask>> floatingTaskList = floatingTaskListView.getItems();
        for (int i = 0; i < floatingTaskList.size(); i++) {
            if (floatingTaskList.get(i).getWorkingIndex() == workingIndex) {
                floatingTaskListView.scrollTo(i);
                floatingTaskListView.getSelectionModel().select(i);
                return;
            }
        }
    }

    /**
     * Clears any floating task selection.
     */
    public void clearSelect() {
        floatingTaskListView.getSelectionModel().clearSelection();
    }

    private static class FloatingTaskListCell extends ListCell<IndexedItem<FloatingTask>> {
        @Override
        protected void updateItem(IndexedItem<FloatingTask> floatingTask, boolean empty) {
            super.updateItem(floatingTask, empty);
            final FloatingTaskListCard card = new FloatingTaskListCard(floatingTask != null ? floatingTask.getItem() : null, floatingTask != null ? floatingTask.getWorkingIndex() : 0);
            setGraphic(card.getRoot());
        }
    }

    private void setFloatingTaskCounter(int counterValue) {
        this.listedFloatingTaskCounter.setText("Number of Floating tasks listed: " + counterValue);
    }

    @Subscribe
    public void handleUIChangedEvent(UpdateTaskBookListViewEvent listViewChanged) {
        int newNumberOfFloatingTaskListed = listViewChanged.getModel().getFloatingTaskList().size();
        logger.info(LogsCenter.getEventHandlingLogMessage(listViewChanged, "The number of listed floating task: "
                                                          + newNumberOfFloatingTaskListed));
        setFloatingTaskCounter(newNumberOfFloatingTaskListed);
    }

}
