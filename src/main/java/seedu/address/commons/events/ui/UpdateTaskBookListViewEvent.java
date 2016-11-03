package seedu.address.commons.events.ui;

import com.google.common.base.MoreObjects;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyModel;

/**
 * Indicate that the List view of tasks in the UI has changed.
 */
public class UpdateTaskBookListViewEvent extends BaseEvent {

    private final ReadOnlyModel model;

    public UpdateTaskBookListViewEvent(ReadOnlyModel model) {
        this.model = model;
    }

    public ReadOnlyModel getModel() {
        return this.model;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("floatingTasks", model.getFloatingTaskList().size())
                .add("deadlineTasks", model.getDeadlineTaskList().size())
                .add("eventTasks", model.getEventTaskList().size())
                .toString();
    }

}
