package seedu.address.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.IndexedItem;
import seedu.address.model.ReadOnlyModel;
import seedu.address.model.filter.TaskUnfinishedPredicate;
import seedu.address.model.filter.TaskWillHappenTodayPredicate;
import seedu.address.model.task.EventTask;

public class EventTaskListPane extends UiPart<Pane> {

    private static final String FXML = "/view/EventTaskListPane.fxml";

    private static final Logger logger = LogsCenter.getLogger(EventTaskListPane.class);

    private final ObservableList<IndexedItem<EventTask>> listedEventTasks;
    private ObservableList<IndexedItem<EventTask>> todayEventTasks;
    private ObservableList<IndexedItem<EventTask>> unfinishedEventTasks;

    @FXML
    private ListView<IndexedItem<EventTask>> eventTaskListView;

    @FXML
    private Label listedEventCounter;

    @FXML
    private Label todayUnfinishedEventCounter;

    @FXML
    private Label unfinishedEventCounter;

    public EventTaskListPane(ReadOnlyModel model) {
        super(FXML);

        // Initialize task lists
        this.listedEventTasks = model.getEventTaskList();
        this.todayEventTasks = model.getEventTaskList(new TaskWillHappenTodayPredicate(LocalDateTime.now()));
        this.unfinishedEventTasks = model.getEventTaskList(new TaskUnfinishedPredicate(LocalDateTime.now()));
        handleListedEventTasksChanges(model);

        // Initialize Event Task List View
        eventTaskListView.setItems(this.listedEventTasks);
        eventTaskListView.setCellFactory(listView -> new EventTaskListCell());

        // Initialize Task Counter
        listedEventCounter.textProperty().bind(Bindings.size(this.listedEventTasks)
                                               .asString("Number of Events listed: %d"));
        unfinishedEventCounter.textProperty().bind(Bindings.size(this.unfinishedEventTasks)
                                                   .asString("Total upcoming: %d"));
        todayUnfinishedEventCounter.textProperty().bind(Bindings.size(this.todayEventTasks)
                                                        .asString("Today upcoming: %d"));
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

    /**
     * Handle listedEventTasks change
     */
    public void handleListedEventTasksChanges(ReadOnlyModel model) {
        this.listedEventTasks.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                // update the different with updated filters created at current time point
                todayEventTasks = model.getEventTaskList(new TaskWillHappenTodayPredicate(LocalDateTime.now()));
                unfinishedEventTasks = model.getEventTaskList(new TaskUnfinishedPredicate(LocalDateTime.now()));

                // update the task counter with updated task lists
                unfinishedEventCounter.textProperty().bind(Bindings.size(unfinishedEventTasks)
                        .asString("Total upcoming: %d"));
                todayUnfinishedEventCounter.textProperty().bind(Bindings.size(todayEventTasks)
                             .asString("Today upcoming: %d"));

                // logging information
                logger.info("EventTask counter updated with current time.");
            }
        });
    }

    private static class EventTaskListCell extends ListCell<IndexedItem<EventTask>> {
        @Override
        protected void updateItem(IndexedItem<EventTask> eventTask, boolean empty) {
            super.updateItem(eventTask, empty);
            final EventTaskListCard card = new EventTaskListCard(eventTask != null ? eventTask.getItem() : null, eventTask != null ? eventTask.getWorkingIndex() : 0);
            setGraphic(card.getRoot());
        }
    }

}
