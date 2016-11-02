package seedu.address.model.filter;

import java.util.function.Predicate;

import seedu.address.model.task.FloatingTask;

/**
 * Predicate if the given floating task is finished or not.
 * test() will return true if finished.
 */
public class FloatingTaskFinishedPredicate implements Predicate<FloatingTask> {

    @Override
    public boolean test(FloatingTask task) {
        return task.isFinished();
    }
}
