package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import seedu.address.commons.core.IndexPrefix;
import seedu.address.model.task.FloatingTask;

public class FloatingTaskListCard extends UiPart<Pane> {

    private static final String FXML = "/view/FloatingTaskListCard.fxml";

    @FXML
    private Label indexLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priorityLabel;

    /**
     * @param floatingTask: The floating task to display. Can be null to not display anything.
     */
    public FloatingTaskListCard(FloatingTask floatingTask, int index) {
        super(FXML);
        if (floatingTask != null) {
            indexLabel.setText(IndexPrefix.FLOAT.getPrefixString() + index + ". ");
            nameLabel.setText(floatingTask.getName().toString());
            priorityLabel.setText(floatingTask.getPriority().toString());
            if (floatingTask.isFinished()) {
                getRoot().getStyleClass().add("finished");
            } else {
                getRoot().getStyleClass().add("priority" + floatingTask.getPriority().toString());
            }
        } else {
            getRoot().setVisible(false);
        }
    }

}
