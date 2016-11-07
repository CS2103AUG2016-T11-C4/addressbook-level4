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
import seedu.address.model.filter.TaskOverduePredicate;
import seedu.address.model.filter.TaskUnfinishedPredicate;
import seedu.address.model.filter.TaskWillHappenTodayPredicate;
import seedu.address.model.task.DeadlineTask;

public class DeadlineTaskListPane extends UiPart<Pane> {

    private static final String FXML = "/view/DeadlineTaskListPane.fxml";

    private static final Logger logger = LogsCenter.getLogger(DeadlineTaskListPane.class);

    private final ObservableList<IndexedItem<DeadlineTask>> listedDeadlineTasks;
    private ObservableList<IndexedItem<DeadlineTask>> todayDeadlineTasks;
    private ObservableList<IndexedItem<DeadlineTask>> overdueDeadlineTasks;
    private ObservableList<IndexedItem<DeadlineTask>> unfinishedDeadlineTasks;

    @FXML
    private ListView<IndexedItem<DeadlineTask>> deadlineTaskListView;

    @FXML
    private Label listedDeadlineCounter;
    @FXML
    private Label todayUnfinishedDeadlineCounter;
    @FXML
    private Label overdueDeadlineCounter;
    @FXML
    private Label unfinishedDeadlineCounter;

    public DeadlineTaskListPane(ReadOnlyModel model) {
        super(FXML);

        // Initialize task lists
        this.listedDeadlineTasks = model.getDeadlineTaskList();
        this.todayDeadlineTasks = model.getDeadlineTaskList(new TaskWillHappenTodayPredicate(LocalDateTime.now()));
        this.overdueDeadlineTasks = model.getDeadlineTaskList(new TaskOverduePredicate(LocalDateTime.now()));
        this.unfinishedDeadlineTasks = model.getDeadlineTaskList(new TaskUnfinishedPredicate(LocalDateTime.now()));
        handleListedDeadlineTasksChanges(model);

        // Initialize Floating Task List View
        deadlineTaskListView.setItems(model.getDeadlineTaskList());
        deadlineTaskListView.setCellFactory(listView -> new DeadlineTaskListCell());

        // Initialize Task Counter
        listedDeadlineCounter.textProperty().bind(Bindings.size(model.getDeadlineTaskList())
                                                  .asString("Number of Deadlines listed: %d"));
        todayUnfinishedDeadlineCounter.textProperty().bind(Bindings.size(model.getDeadlineTaskList(new TaskWillHappenTodayPredicate(LocalDateTime.now())))
                                                           .asString("Today remaining: %d"));
        overdueDeadlineCounter.textProperty().bind(Bindings.size(model.getDeadlineTaskList(new TaskOverduePredicate(LocalDateTime.now())))
                                                   .asString("Overdue: %d"));
        unfinishedDeadlineCounter.textProperty().bind(Bindings.size(model.getDeadlineTaskList(new TaskUnfinishedPredicate(LocalDateTime.now())))
                                                      .asString("Total unfinished: %d"));
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

    /**
     * Handle listedDeadlineTasks change
     */
    public void handleListedDeadlineTasksChanges(ReadOnlyModel model) {
        this.listedDeadlineTasks.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                // update the different with updated filters created at current time point
                todayDeadlineTasks = model.getDeadlineTaskList(new TaskWillHappenTodayPredicate(LocalDateTime.now()));
                overdueDeadlineTasks = model.getDeadlineTaskList(new TaskOverduePredicate(LocalDateTime.now()));
                unfinishedDeadlineTasks = model.getDeadlineTaskList(new TaskUnfinishedPredicate(LocalDateTime.now()));

                // update the task counter with updated task lists
                todayUnfinishedDeadlineCounter.textProperty().bind(Bindings.size(todayDeadlineTasks)
                                                 .asString("Today remaining: %d"));
                overdueDeadlineCounter.textProperty().bind(Bindings.size(overdueDeadlineTasks)
                                         .asString("Overdue: %d"));
                unfinishedDeadlineCounter.textProperty().bind(Bindings.size(unfinishedDeadlineTasks)
                                            .asString("Total unfinished: %d"));

                // logging information
                logger.info("DeadlineTask counter updated with current time.");
            }
        });
    }

    private static class DeadlineTaskListCell extends ListCell<IndexedItem<DeadlineTask>> {
        @Override
        protected void updateItem(IndexedItem<DeadlineTask> deadlineTask, boolean empty) {
            super.updateItem(deadlineTask, empty);
            final DeadlineTaskListCard card = new DeadlineTaskListCard(deadlineTask != null ? deadlineTask.getItem() : null, deadlineTask != null ? deadlineTask.getWorkingIndex() : 0);
            setGraphic(card.getRoot());
        }
    }

}
