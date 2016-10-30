package seedu.address.model.filter;

import java.util.function.Predicate;

import seedu.address.model.task.DeadlineTask;

/**
 * Filter out the deadlines that are finished already.
 * test() will return true if the task if not finished.
 */
public class DeadlineNotFinishedPredicate implements Predicate<DeadlineTask> {

    @Override
    public boolean test(DeadlineTask task) {
        return !task.isFinished();
    }

}
