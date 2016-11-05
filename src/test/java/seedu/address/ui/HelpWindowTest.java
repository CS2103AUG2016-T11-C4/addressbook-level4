package seedu.address.ui;

import static org.junit.Assert.assertEquals;

//import org.junit.After;
//import org.junit.Before;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import javafx.scene.web.WebView;
import seedu.address.testutil.GuiTests;

@Category({GuiTests.class})
public class HelpWindowTest extends FxRobot {

    private HelpWindow helpWindow;

    private WebView webView;

    @BeforeClass
    public static void setupFxToolkit() throws Exception {
        FxToolkit.registerPrimaryStage();
    }

    /*@Before
    public void setupHelpWindow() throws Exception {
        FxToolkit.registerStage(() -> {
            return helpWindow = new HelpWindow();
            //return helpWindow.getRoot();
        });
        FxToolkit.showStage();
        webView = lookup("#webView").query();
    }

    @After
    public void teardownHelpWindow() throws Exception {
        FxToolkit.cleanupStages();
    }**/

    @Test
    public void constructor() {
        assertEquals("https://github.com/se-edu/addressbook-level4/blob/master/docs/UserGuide.md",
                     webView.getEngine().getLocation());
    }

}
