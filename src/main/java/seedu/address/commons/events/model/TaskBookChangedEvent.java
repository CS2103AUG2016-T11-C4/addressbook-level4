package seedu.address.commons.events.model;

import com.google.common.base.MoreObjects;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyTaskBook;

/** Indicates the TaskBook in the model has changed*/
public class TaskBookChangedEvent extends BaseEvent {

    public final ReadOnlyTaskBook data;

    public TaskBookChangedEvent(ReadOnlyTaskBook data) {
        this.data = data;
    }

    public int dataSize() {
        return data.getFloatingTasks().size()
               + data.getDeadlineTasks().size()
               + data.getEventTasks().size();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("floatingTasks", data.getFloatingTasks().size())
                .add("deadlineTasks", data.getDeadlineTasks().size())
                .add("eventTasks", data.getEventTasks().size())
                .toString();
    }
}
