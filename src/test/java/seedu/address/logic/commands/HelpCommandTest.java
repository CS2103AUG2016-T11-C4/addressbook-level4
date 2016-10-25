package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.testutil.EventsCollector;

public class HelpCommandTest {

    @Test
    public void execute() {
        final HelpCommand command = new HelpCommand();
        final EventsCollector eventsCollector = new EventsCollector();
        final CommandResult result = command.execute();
        assertEquals("Opened help window.", result.feedbackToUser);
        assertTrue(eventsCollector.get(0) instanceof ShowHelpRequestEvent);
    }

}