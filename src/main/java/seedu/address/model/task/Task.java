package seedu.address.model.task;

import seedu.address.commons.util.CollectionUtil;

/**
 * Represents a Task in the address book.
 * Guarantees: Is a POJO. Details are present and not null. Field values are validated and immutable.
 */
public abstract class Task {

    protected final Name name;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name) {
        assert !CollectionUtil.isAnyNull(name);
        this.name = name;
    }

    public Name getName() {
        return name;
    }
}
