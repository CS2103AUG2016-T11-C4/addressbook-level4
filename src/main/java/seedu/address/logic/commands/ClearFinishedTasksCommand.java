package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;

public class ClearFinishedTasksCommand extends Command {

    private static final String MSG_SUCCESS = "Cleared all finished tasks.";

    @Override
    public CommandResult execute() {
        assert model != null;
        final List<Optional<FloatingTask>> floatingTaskList = model.getFilteredFloatingTaskList();
        for (int i = 0; i < floatingTaskList.size(); i++) {
            if (floatingTaskList.get(i).isPresent() && floatingTaskList.get(i).get().isFinished()) {
                try {
                    model.removeFloatingTask(i);
                } catch (IllegalValueException e) {
                    assert false;
                }
            }
        }

        final List<Optional<DeadlineTask>> deadlineTaskList = model.getFilteredDeadlineTaskList();
        for (int i = 0; i < deadlineTaskList.size(); i++) {
            if (deadlineTaskList.get(i).isPresent() && deadlineTaskList.get(i).get().isFinished()) {
                try {
                    model.removeDeadlineTask(i);
                } catch (IllegalValueException e) {
                    assert false;
                }
            }
        }

        final List<Optional<EventTask>> eventTaskList = model.getFilteredEventTaskList();
        for (int i = 0; i < eventTaskList.size(); i++) {
            if (eventTaskList.get(i).isPresent() && eventTaskList.get(i).get().getEnd().isBefore(LocalDateTime.now())) {
                try {
                    model.removeEventTask(i);
                } catch (IllegalValueException e) {
                    assert false;
                }
            }
        }

        return new CommandResult(MSG_SUCCESS);
    }

}
