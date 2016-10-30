package seedu.address.model.filter;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import seedu.address.model.task.EventTask;

/**
 * Filter out the events that are in the past.
 * test() will return true if the task if not past.
 */
public class EventNotInPastPredicate implements Predicate<EventTask> {

    @Override
    public boolean test(EventTask task) {
        return !task.getEnd().isBefore(LocalDateTime.now());
    }
}
