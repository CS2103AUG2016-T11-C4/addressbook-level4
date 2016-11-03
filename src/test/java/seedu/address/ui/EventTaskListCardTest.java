package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import javafx.scene.control.Label;
import seedu.address.model.task.TypicalEventTasks;
import seedu.address.testutil.GuiTests;

@Category({GuiTests.class})
public class EventTaskListCardTest extends FxRobot {

    private TypicalEventTasks tet = new TypicalEventTasks();

    private EventTaskListCard eventTaskListCard;

    private Label indexLabel;

    private Label nameLabel;

    private Label startLabel;

    private Label endLabel;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    private void setupNodes() throws Exception {
        FxToolkit.showStage();
        indexLabel = lookup("#indexLabel").query();
        nameLabel = lookup("#nameLabel").query();
        startLabel = lookup("#startLabel").query();
        endLabel = lookup("#endLabel").query();
    }

    @Test
    public void withEventTask_showsInfoInLabels() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            eventTaskListCard = new EventTaskListCard(tet.lunchWithBillGates, 42);
            return eventTaskListCard.getRoot();
        });
        setupNodes();
        assertEquals("e42. ", indexLabel.getText());
        assertEquals("Lunch with Bill Gates", nameLabel.getText());
        assertEquals("01/01/1970 Time: 00:00", startLabel.getText());
        assertEquals("01/01/1970 Time: 01:00", endLabel.getText());
    }

    @Test
    public void nullEventTask_becomesInvisible() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            eventTaskListCard = new EventTaskListCard(null, 34);
            return eventTaskListCard.getRoot();
        });
        setupNodes();
        assertFalse(eventTaskListCard.getRoot().isVisible());
    }

}
