package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import javafx.scene.control.Label;
import seedu.address.model.task.TypicalDeadlineTasks;
import seedu.address.testutil.GuiTests;

@Category({GuiTests.class})
public class DeadlineTaskListCardTest extends FxRobot {

    private TypicalDeadlineTasks tdt = new TypicalDeadlineTasks();

    private DeadlineTaskListCard deadlineTaskListCard;

    private Label indexLabel;

    private Label nameLabel;

    private Label dueLabel;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    private void setupNodes() throws Exception {
        FxToolkit.showStage();
        indexLabel = lookup("#indexLabel").query();
        nameLabel = lookup("#nameLabel").query();
        dueLabel = lookup("#dueLabel").query();
    }

    @Test
    public void withDeadlineTask_showsInfoInLabels() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            deadlineTaskListCard = new DeadlineTaskListCard(tdt.speechTranscript, 42);
            return deadlineTaskListCard.getRoot();
        });
        setupNodes();
        assertEquals("d42. ", indexLabel.getText());
        assertEquals("Speech Transcript", nameLabel.getText());
        assertEquals("01/01/1970 Time: 01:00", dueLabel.getText());
    }

    @Test
    public void nullDeadlineTask_becomesInvisible() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            deadlineTaskListCard = new DeadlineTaskListCard(null, 34);
            return deadlineTaskListCard.getRoot();
        });
        setupNodes();
        assertFalse(deadlineTaskListCard.getRoot().isVisible());
    }

}
