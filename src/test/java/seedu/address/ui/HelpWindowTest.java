package seedu.address.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;

//import org.junit.After;
//import org.junit.Before;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import javafx.stage.Stage;
import seedu.address.testutil.GuiTests;

@Category({GuiTests.class})
public class HelpWindowTest extends FxRobot {

    private HelpWindow helpWindow;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    @Before
    public void setupHelpWindow() throws Exception {
        FxToolkit.registerStage(() -> {
            helpWindow = new HelpWindow();
            return helpWindow.getStage();
        });
        FxToolkit.showStage();
    }

    @After
    public void teardownHelpWindow() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Test
    public void constructor() {
        assertTrue(helpWindow.getStage() instanceof Stage);
    }

}
