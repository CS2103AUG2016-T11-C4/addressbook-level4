package seedu.address.model.task;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Floating Task's priority in the TaskTracker.
 * Guarantees: is valid as declared in {@link #isValidPriority(String)}
 */

public class Priority {
    
    public static final int UPPER_BOUND = 5;
    public static final int LOWER_BOUND = 0;
    
    public static final String MESSAGE_PRIORITY_CONSTRAINTS = 
            "priority should be an integer ranges from " + LOWER_BOUND + " to " + UPPER_BOUND + ".";
    public static final String PRIORITY_VALIDATION_REGEX = "\\p{Digit}";

    private String priority;

    /**
     * Validates given priority.
     *
     * @throws IllegalValueException if given priority string is invalid.
     */
    public Priority (String priority) throws IllegalValueException {
        assert priority != null;
        priority = priority.trim();
        if (!isValidPriority(priority)) {
            throw new IllegalValueException(MESSAGE_PRIORITY_CONSTRAINTS);
        }
        this.priority = priority;
    }

    /**
     * Returns true if a given string is a valid priority.
     */
    public static boolean isValidPriority(String test) {
        if(test.matches(PRIORITY_VALIDATION_REGEX)) {
            return (Integer.parseInt(test) >= LOWER_BOUND) 
                    && (Integer.parseInt(test) <= UPPER_BOUND);
            
        } else {
            return false;
        }
    }
    
    public int toInteger() {
        return Integer.parseInt(this.priority);
    }

    @Override
    public String toString() {
        return priority;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                && this.priority.equals(((Priority) other).priority)); // state check
    }

    @Override
    public int hashCode() {
        return priority.hashCode();
    }

}
