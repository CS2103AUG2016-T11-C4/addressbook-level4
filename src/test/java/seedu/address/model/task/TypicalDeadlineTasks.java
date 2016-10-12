package seedu.address.model.task;

import static seedu.address.commons.time.TypicalLocalDateTimes.UNIX_EPOCH;

import java.util.Arrays;
import java.util.List;

import seedu.address.commons.exceptions.IllegalValueException;

public class TypicalDeadlineTasks {

    public final DeadlineTask speechTranscript;

    public final DeadlineTask assembleTheMissiles;

    public TypicalDeadlineTasks() {
        try {
            speechTranscript = new DeadlineTask("Speech Transcript", UNIX_EPOCH.plusHours(1));
            assembleTheMissiles = new DeadlineTask("Assemble The Missiles", UNIX_EPOCH.plusHours(2));
        } catch (IllegalValueException e) {
            throw new AssertionError("this should not happen", e);
        }
    }

    public List<DeadlineTask> getDeadlineTasks() {
        final DeadlineTask[] tasks = {speechTranscript, assembleTheMissiles};
        return Arrays.asList(tasks);
    }

}
