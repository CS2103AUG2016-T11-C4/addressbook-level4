package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import seedu.address.model.task.FloatingTask;

public class FloatingTaskListPane extends UiPart<Pane> {
    
    private static final String FXML = "/view/FloatingTaskListPane.fxml";
    
    @FXML
    private ListView<FloatingTask> floatingTaskListView;

    public FloatingTaskListPane(ObservableList<FloatingTask> floatingTaskList) {
        super(FXML);
        floatingTaskListView.setItems(floatingTaskList);
        floatingTaskListView.setCellFactory(listView -> new FloatingTaskListCell());
    }
    
    private static class FloatingTaskListCell extends ListCell<FloatingTask> {
        @Override
        protected void updateItem(FloatingTask floatingTask, boolean empty) {
            super.updateItem(floatingTask, empty);
            final FloatingTaskListCard card = new FloatingTaskListCard(floatingTask, getIndex() + 1);
            setGraphic(card.getRoot());
        }
    }
}
