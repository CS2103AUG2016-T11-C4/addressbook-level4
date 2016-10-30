package seedu.address.ui;

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import seedu.address.commons.core.IndexPrefix;
import seedu.address.model.filter.DeadlineNotFinishedPredicate;
import seedu.address.model.task.DeadlineTask;

public class DeadlineTaskListCard extends UiPart<Pane> {

    private static final String FXML = "/view/DeadlineTaskListCard.fxml";

    @FXML
    private Label indexLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label dueLabel;

    /**
     * @param deadlineTask The deadline task to display. Can be null to not display anything.
     */
    public DeadlineTaskListCard(DeadlineTask deadlineTask, int index) {
        super(FXML);
        if (deadlineTask != null) {
            indexLabel.setWrapText(true);
            indexLabel.setText(IndexPrefix.DEADLINE.getPrefixString() + index + ". ");
            nameLabel.setText(deadlineTask.getName().toString());
            dueLabel.setText(deadlineTask.getDue().toString());
            if (!(new DeadlineNotFinishedPredicate()).test(deadlineTask)) {
                getRoot().getStyleClass().add("finished");
            } else if (deadlineTask.getDue().isBefore(LocalDateTime.now())) {
                getRoot().getStyleClass().add("overdue");
            }
        } else {
            getRoot().setVisible(false);
        }
    }

}
