package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;

public class JsonFloatingTaskMixinTest {

    private static ObjectMapper objectMapper;

    public static final FloatingTask TEST_EVENT;
    public static final String TEST_EVENT_JSON = "{\"name\":\"event name\","
                                                 + "\"priority\":\"3\"}";

    static {
        try {
            TEST_EVENT = new FloatingTask(new Name("event name"), new Priority("3"));
        } catch (IllegalValueException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void setupObjectMapper() {
        final SimpleModule module = new SimpleModule();
        module.setMixInAnnotation(Name.class, JsonNameMixin.class);
        module.setMixInAnnotation(Priority.class, JsonPriorityMixin.class);
        module.setMixInAnnotation(FloatingTask.class, JsonFloatingTaskMixin.class);
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules().registerModule(module);
    }

    @Test
    public void serialize() throws Exception {
        final String expected = TEST_EVENT_JSON;
        final String actual = objectMapper.writeValueAsString(TEST_EVENT);
        assertEquals(expected, actual);
    }

    @Test
    public void deserialize() throws Exception {
        final FloatingTask expected = TEST_EVENT;
        final FloatingTask actual = objectMapper.readValue(TEST_EVENT_JSON, FloatingTask.class);
        assertEquals(expected, actual);
    }

}