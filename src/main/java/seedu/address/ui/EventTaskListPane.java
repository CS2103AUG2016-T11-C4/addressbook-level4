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
import seedu.address.model.task.EventTask;

public class EventTaskListPane extends UiPart<Pane> {

    private static final Logger logger = LogsCenter.getLogger(DeadlineTaskListPane.class);

    private static final String FXML = "/view/EventTaskListPane.fxml";

    @FXML
    private ListView<IndexedItem<EventTask>> eventTaskListView;

    @FXML
    private Label listedEventCounter;

    public EventTaskListPane(ObservableList<IndexedItem<EventTask>> eventTaskList,
                             int listedEventNumber) {
        super(FXML);
        eventTaskListView.setItems(eventTaskList);
        eventTaskListView.setCellFactory(listView -> new EventTaskListCell());
        setEventCounter(listedEventNumber);
    }

    /**
     * Selects an event task as specified by its working index.
     */
    public void select(int workingIndex) {
        final List<IndexedItem<EventTask>> eventTaskList = eventTaskListView.getItems();
        for (int i = 0; i < eventTaskList.size(); i++) {
            if (eventTaskList.get(i).getWorkingIndex() == workingIndex) {
                eventTaskListView.scrollTo(i);
                eventTaskListView.getSelectionModel().select(i);
                return;
            }
        }
    }

    /**
     * Clears any event task selection.
     */
    public void clearSelect() {
        eventTaskListView.getSelectionModel().clearSelection();
    }

    private static class EventTaskListCell extends ListCell<IndexedItem<EventTask>> {
        @Override
        protected void updateItem(IndexedItem<EventTask> eventTask, boolean empty) {
            super.updateItem(eventTask, empty);
            final EventTaskListCard card = new EventTaskListCard(eventTask != null ? eventTask.getItem() : null, eventTask != null ? eventTask.getWorkingIndex() : 0);
            setGraphic(card.getRoot());
        }
    }

    private void setEventCounter(int counterValue) {
        this.listedEventCounter.setText("Number of Events listed: " + counterValue);
    }

    @Subscribe
    public void handleTaskBookChangedEvent(UpdateTaskBookListViewEvent listViewChanged) {
        int newNumberOfEventTaskListed = listViewChanged.getModel().getEventTaskList().size();
        logger.info(LogsCenter.getEventHandlingLogMessage(listViewChanged, "The number of listed event task: "
                                                          + newNumberOfEventTaskListed));
        setEventCounter(newNumberOfEventTaskListed);
    }

}
