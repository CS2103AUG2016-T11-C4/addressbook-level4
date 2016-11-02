package seedu.address.model.filter;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import seedu.address.model.task.EventTask;

/**
 * Predicate if the given event task is finished or not.
 * test() will return true if finished.
 */
public class EventTaskFinishedPredicate implements Predicate<EventTask> {

    @Override
    public boolean test(EventTask task) {
        return task.getEnd().isBefore(LocalDateTime.now());
    }
}
