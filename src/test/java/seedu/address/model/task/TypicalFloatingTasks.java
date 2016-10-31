package seedu.address.model.task;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.exceptions.IllegalValueException;

public class TypicalFloatingTasks {

    public final FloatingTask readABook;
    public final FloatingTask buyAHelicopter;
    public final FloatingTask eatAnApple;

    public TypicalFloatingTasks() {
        try {
            readABook = new FloatingTask("read A Book");
            buyAHelicopter = new FloatingTask("buy A Helicopter", new Priority("4"));
            eatAnApple = new FloatingTask("eat an apple");
        } catch (IllegalValueException e) {
            throw new AssertionError("this should not happen", e);
        }
    }

    public List<FloatingTask> getFloatingTasks() {
        final FloatingTask[] tasks = {readABook, buyAHelicopter, eatAnApple};
        return Arrays.asList(tasks);
    }

}
