package seedu.address.model.filter;

import java.util.function.Predicate;

import seedu.address.model.task.FloatingTask;

/**
 * Filter out the floating tasks that are finished already.
 * test() will return true if the task if not finished.
 */
public class FloatingTaskNotFinishedPredicate implements Predicate<FloatingTask> {

    @Override
    public boolean test(FloatingTask task) {
        return !task.isFinished();
    }
}
