package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.time.TypicalLocalDateTimes.UNIX_EPOCH;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.Name;

public class JsonDeadlineTaskMixinTest {

    private static ObjectMapper objectMapper;

    public static final DeadlineTask TEST_DEADLINE;
    public static final String TEST_DEADLINE_JSON = "{\"name\":\"deadline name\",\"due\":[1970,1,2,0,0]}";

    static {
        try {
            TEST_DEADLINE = new DeadlineTask(new Name("deadline name"), UNIX_EPOCH.plusDays(1));
        } catch (IllegalValueException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void setupObjectMapper() {
        final SimpleModule module = new SimpleModule();
        module.setMixInAnnotation(Name.class, JsonNameMixin.class);
        module.setMixInAnnotation(DeadlineTask.class, JsonDeadlineTaskMixin.class);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules().registerModule(module);
    }

    @Test
    public void serialize() throws Exception {
        final String expected = TEST_DEADLINE_JSON;
        final String actual = objectMapper.writeValueAsString(TEST_DEADLINE);
        assertEquals(expected, actual);
    }

    @Test
    public void deserialize() throws Exception {
        final DeadlineTask expected = TEST_DEADLINE;
        final DeadlineTask actual = objectMapper.readValue(TEST_DEADLINE_JSON, DeadlineTask.class);
        assertEquals(expected, actual);
    }

}
