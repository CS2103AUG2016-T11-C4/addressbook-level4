package seedu.address.logic.commands;

import java.time.LocalDateTime;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;

/**
 * Adds an event task to the task book.
 */
public class AddTaskCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an task to the TaskTracker.\n"
            + "Parameters for adding event: \"NAME\" <STARTING_DATE> <STARTING_TIME> to <ENDING_DATE> <ENDING_TIME>\n"
            + "Parameters for adding deadline: \"NAME\" <DATE> <TIME> \n"
            + "Parameters for adding floating task: \"NAME\" [p-Priority] \n"
            + "Example: " + COMMAND_WORD + " \"Event Name\" 12/12/2016 12pm to 2pm"
            + "Example: " + COMMAND_WORD + " \"Deadline Name\" 12/12/2016 2pm"
            + "Example: " + COMMAND_WORD + " \"Floating Task Name\" p-3";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";

    private final Task task;

    // Constructor for adding event command
    public AddTaskCommand(String name, LocalDateTime start, LocalDateTime end) throws IllegalValueException {
        this.task = new EventTask(name, start, end);
    }

    // Constructor for adding deadline command
    public AddTaskCommand(String name, LocalDateTime due) throws IllegalValueException {
        this.task = new DeadlineTask(name, due);
    }

    // Constructor for adding floating task command
    public AddTaskCommand(String name, String priority) throws IllegalValueException {
        this.task = new FloatingTask(name, new Priority(priority));
    }

    public Task getTask() {
        return task;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        if (task instanceof EventTask) {
            model.addEventTask((EventTask)task);
        } else if (task instanceof DeadlineTask) {
            model.addDeadlineTask((DeadlineTask)task);
        } else if (task instanceof FloatingTask) {
            model.addFloatingTask((FloatingTask)task);
        } else {
            // should never reach this point
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, task));
    }

}
