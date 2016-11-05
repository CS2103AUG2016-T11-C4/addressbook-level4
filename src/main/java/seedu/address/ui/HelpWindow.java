package seedu.address.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seedu.address.commons.util.TableBuilder;

/**
 * Controller for a help page
 */
public class HelpWindow {

    private static final String HELPBOX_CONTENT =
              "Add Floating Task             | add \"FLOATING_TASK_NAME\"  [p-PRIORITY]\n"
            + "Add Deadline Task             | add \"DEADLINE_NAME\" <DATE> <TIME>\n"
            + "Add Events                    | add \"EVENT_NAME\" <STARTING_DATE> <STARTING_TIME> to <ENDING_DATE> <ENDING_TIME>\n"
            + "Delete                          | del <INDEX>\n"
            + "Edit Floating Tasks           | edit <INDEX> [n-NEW_NAME] [p-PRIORITY]\n"
            + "Edit Deadline                | edit <INDEX> [dd-DUE_DATE] [dt-DUE_TIME] [n-NEW_NAME]\n"
            + "Mark task as finished      | fin <INDEX>\n"
            + "Mark task as unfinished | unfin <INDEX>\n"
            + "Search for keywords         | search KEYWORD\n"
            + "Undo                             | undo\n"
            + "Redo                              | redo\n"
            + "Clear                              | clear\n"
            + "Exit                                 | exit\n"
            + "Hide and show Task Tracker  | Ctrl + SPACE\n";

    private static final String ICON = "/images/help_icon.png";
    private static final String FXML = "/view/HelpWindow.fxml";
    private static final String USERGUIDE_URL =
            "https://github.com/se-edu/addressbook-level4/blob/master/docs/UserGuide.md";

    @FXML
    private WebView webView;

    public HelpWindow() {

        Alert helpBox = new Alert(javafx.scene.control.Alert.AlertType.NONE);
        helpBox.initModality(Modality.NONE);

        //add icon
        Stage stage = (Stage) helpBox.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(ICON));

        //window settings
        stage.setAlwaysOnTop(true);
        helpBox.initModality(Modality.NONE);
        helpBox.setResizable(false);
        helpBox.getDialogPane().setPrefWidth(700.0);
        helpBox.getDialogPane().setPrefHeight(320.0);

        //set contents
        helpBox.setTitle("HELP");
        helpBox.setHeaderText("Press <ESC> to close");
        helpBox.setContentText(generateHelpTable());

        //press escape to close
        stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                    stage.close();
                    keyEvent.consume();
                }
            }
        });

        helpBox.showAndWait();
    }

    public String generateHelpTable() {
        TableBuilder content = new TableBuilder();
        content.addRow("Action", "Command");
        content.addRow("----------------------------", "---------------------------------------------------------------");
        content.addRow("Add Floating Task", "add \"FLOATING_TASK_NAME\"  [p-PRIORITY]");
        content.addRow("Add Deadline Task", "add \"DEADLINE_NAME\" <DATE> <TIME>");
        content.addRow("Add Events", "add \"EVENT_NAME\" <STARTING_DATE> <STARTING_TIME> to <ENDING_DATE> <ENDING_TIME>");
        content.addRow("Delete", "del <INDEX>");
        content.addRow("Edit Floating Tasks", "edit <INDEX> [n-NEW_NAME] [p-PRIORITY]");
        content.addRow("Edit Deadline", "edit <INDEX> [dd-DUE_DATE] [dt-DUE_TIME] [n-NEW_NAME]");
        content.addRow("Mark task as finished", "fin <INDEX>");
        content.addRow("Mark task as unfinished", "unfin <INDEX>");
        content.addRow("Search for keywords", "search KEYWORD");
        content.addRow("Undo", "undo");
        content.addRow("Redo", "redo");
        content.addRow("Clear", "clear");
        content.addRow("Exit", "exit");
        content.addRow("Hide and show Task Tracker", "Ctrl + SPACE");
        return content.toString();
    }

}
