package seedu.address.model.filter;

import java.util.function.Predicate;

import seedu.address.model.task.DeadlineTask;

/**
 * Predicate if the given deadline task is finished or not.
 * test() will return true if finished.
 */
public class DeadlineTaskFinishedPredicate implements Predicate<DeadlineTask> {

    @Override
    public boolean test(DeadlineTask task) {
        return task.isFinished();
    }

}
