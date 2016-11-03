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
import seedu.address.model.task.DeadlineTask;

public class DeadlineTaskListPane extends UiPart<Pane> {

    private static final Logger logger = LogsCenter.getLogger(DeadlineTaskListPane.class);

    private static final String FXML = "/view/DeadlineTaskListPane.fxml";

    @FXML
    private ListView<IndexedItem<DeadlineTask>> deadlineTaskListView;

    @FXML
    private Label listedDeadlineCounter;

    public DeadlineTaskListPane(ObservableList<IndexedItem<DeadlineTask>> deadlineTaskList,
                                int listedDeadlineNumber) {
        super(FXML);
        deadlineTaskListView.setItems(deadlineTaskList);
        deadlineTaskListView.setCellFactory(listView -> new DeadlineTaskListCell());
        setDeadlineCounter(listedDeadlineNumber);
    }

    /**
     * Selects a deadline task as specified by its working index.
     */
    public void select(int workingIndex) {
        final List<IndexedItem<DeadlineTask>> deadlineTaskList = deadlineTaskListView.getItems();
        for (int i = 0; i < deadlineTaskList.size(); i++) {
            if (deadlineTaskList.get(i).getWorkingIndex() == workingIndex) {
                deadlineTaskListView.scrollTo(i);
                deadlineTaskListView.getSelectionModel().select(i);
                return;
            }
        }
    }

    /**
     * Clears any deadline task selection.
     */
    public void clearSelect() {
        deadlineTaskListView.getSelectionModel().clearSelection();
    }

    private static class DeadlineTaskListCell extends ListCell<IndexedItem<DeadlineTask>> {
        @Override
        protected void updateItem(IndexedItem<DeadlineTask> deadlineTask, boolean empty) {
            super.updateItem(deadlineTask, empty);
            final DeadlineTaskListCard card = new DeadlineTaskListCard(deadlineTask != null ? deadlineTask.getItem() : null, deadlineTask != null ? deadlineTask.getWorkingIndex() : 0);
            setGraphic(card.getRoot());
        }
    }

    private void setDeadlineCounter(int counterValue) {
        this.listedDeadlineCounter.setText("Number of Deadlines listed: " + counterValue);
    }

    @Subscribe
    public void handleUIChangedEvent(UpdateTaskBookListViewEvent listViewChanged) {
        int newNumberOfDeadlineTaskListed = listViewChanged.getModel().getDeadlineTaskList().size();
        logger.info(LogsCenter.getEventHandlingLogMessage(listViewChanged, "The number of listed deadline task: "
                                                          + newNumberOfDeadlineTaskListed));
        setDeadlineCounter(newNumberOfDeadlineTaskListed);
    }

}
