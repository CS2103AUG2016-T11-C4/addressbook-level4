package seedu.address.model.task;

import java.time.LocalDateTime;
import java.util.Objects;

import seedu.address.commons.exceptions.IllegalValueException;

public class DeadlineTask extends Task {

    private static final String FMT_STRING = "DeadlineTask[name=%s, dueDate=%s, dueTime=%s]";

    private final LocalDateTime due;

    public DeadlineTask(Name name, LocalDateTime due) {
        super(name);
        assert due != null;
        this.due = due;
    }

    public DeadlineTask(String name, LocalDateTime due) throws IllegalValueException {
        this(new Name(name), due);
    }

    public LocalDateTime getDue() {
        return due;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
               || (other instanceof DeadlineTask
               && name.equals(((DeadlineTask)other).name)
               && due.equals(((DeadlineTask)other).due));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, due);
    }

    @Override
    public String toString() {
        return String.format(FMT_STRING, name, due);
    }

}
