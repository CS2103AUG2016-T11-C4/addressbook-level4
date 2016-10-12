package seedu.address.storage;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

import seedu.address.commons.time.LocalDateTimeDuration;
import seedu.address.model.TaskBook;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;

public class JsonStorageModule extends Module {

    @Override
    public String getModuleName() {
        return "JsonStorageModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(LocalDateTimeDuration.class, JsonLocalDateTimeDurationMixin.class);
        context.setMixInAnnotations(Name.class, JsonNameMixin.class);
        context.setMixInAnnotations(Priority.class, JsonPriorityMixin.class);
        context.setMixInAnnotations(Task.class, JsonTaskMixin.class);
        context.setMixInAnnotations(FloatingTask.class, JsonFloatingTaskMixin.class);
        context.setMixInAnnotations(EventTask.class, JsonEventTaskMixin.class);
        context.setMixInAnnotations(DeadlineTask.class, JsonDeadlineTaskMixin.class);
        context.setMixInAnnotations(TaskBook.class, JsonTaskBookMixin.class);
    }

}
