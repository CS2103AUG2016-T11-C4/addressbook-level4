package seedu.address.ui;

import java.time.LocalDateTime;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import seedu.address.model.IndexedItem;
import seedu.address.model.ReadOnlyModel;
import seedu.address.model.filter.TaskUnfinishedPredicate;
import seedu.address.model.task.FloatingTask;

public class FloatingTaskListPane extends UiPart<Pane> {

    private static final String FXML = "/view/FloatingTaskListPane.fxml";

    @FXML
    private ListView<IndexedItem<FloatingTask>> floatingTaskListView;

    @FXML
    private Label listedFloatingTaskCounter;

    @FXML
    private Label unfinishedFloatingTaskCounter;

    public FloatingTaskListPane(ReadOnlyModel model) {
        super(FXML);
        floatingTaskListView.setItems(model.getFloatingTaskList());
        floatingTaskListView.setCellFactory(listView -> new FloatingTaskListCell());
        listedFloatingTaskCounter.textProperty().bind(Bindings.size(model.getFloatingTaskList())
                                                        .asString("The number of listed floating task: %d"));
        unfinishedFloatingTaskCounter.textProperty().bind(Bindings.size(model.getFloatingTaskList(new TaskUnfinishedPredicate(LocalDateTime.now())))
                                                            .asString("Unfinished: %d"));
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
}
