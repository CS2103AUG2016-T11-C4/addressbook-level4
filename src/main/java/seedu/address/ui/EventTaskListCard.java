package seedu.address.ui;

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import seedu.address.commons.core.IndexPrefix;
import seedu.address.model.filter.EventNotInPastPredicate;
import seedu.address.model.task.EventTask;

public class EventTaskListCard extends UiPart<Pane> {

    private static final String FXML = "/view/EventTaskListCard.fxml";

    @FXML
    private Label indexLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label startLabel;

    @FXML
    private Label endLabel;

    /**
     * @param eventTask The event task to display. Can be null to not display anything.
     */
    public EventTaskListCard(EventTask eventTask, int index) {
        super(FXML);
        if (eventTask != null) {
            indexLabel.setText(IndexPrefix.EVENT.getPrefixString() + index + ". ");
            nameLabel.setText(eventTask.getName().toString());
            startLabel.setText(eventTask.getStart().toString());
            endLabel.setText(eventTask.getEnd().toString());
            if (!(new EventNotInPastPredicate()).test(eventTask)) {
                getRoot().getStyleClass().add("finished");
            } else if (eventTask.getStart().isBefore(LocalDateTime.now())) {
                getRoot().getStyleClass().add("inProgress");
            }
        } else {
            getRoot().setVisible(false);
        }
    }

}
