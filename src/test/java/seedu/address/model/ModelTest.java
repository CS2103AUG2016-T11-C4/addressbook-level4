package seedu.address.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.TypicalFloatingTasks;

public class ModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TypicalFloatingTasks tpflt = new TypicalFloatingTasks();

    private Model model;

    @Before
    public void setupModel() {
        model = new ModelManager();
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), model.getFilteredFloatingTaskList());
    }

    @Test
    public void addFloatingTask_appendsFloatingTask() throws Exception {
        model.addFloatingTask(tpflt.readABook);
        assertEquals(tpflt.readABook, model.getFloatingTask(0));
        model.addFloatingTask(tpflt.buyAHelicopter);
        assertEquals(tpflt.readABook, model.getFloatingTask(0));
        assertEquals(tpflt.buyAHelicopter, model.getFloatingTask(1));
        assertEquals(tpflt.readABook, model.getFloatingTask(0));
    }

    @Test
    public void getFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getFloatingTask(0);
    }

    @Test
    public void removeFloatingTask_removesIndexInFilteredList() throws Exception {
        model.addFloatingTask(tpflt.readABook);
        model.addFloatingTask(tpflt.buyAHelicopter);
        model.setFloatingTaskFilter(floatingTask -> floatingTask.equals(tpflt.buyAHelicopter));
        model.removeFloatingTask(0);
        model.setFloatingTaskFilter(null);
        assertEquals(Arrays.asList(tpflt.readABook), model.getFilteredFloatingTaskList());
    }

    @Test
    public void removeFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeFloatingTask(0);
    }

    @Test
    public void setFloatingTask_replacesIndexInFilteredList() throws Exception {
        model.addFloatingTask(tpflt.readABook);
        model.addFloatingTask(tpflt.buyAHelicopter);
        model.setFloatingTaskFilter(floatingTask -> floatingTask.equals(tpflt.buyAHelicopter));
        model.setFloatingTask(0, tpflt.readABook);
        model.setFloatingTaskFilter(null);
        assertEquals(Arrays.asList(tpflt.readABook, tpflt.readABook),
                    model.getFilteredFloatingTaskList());
    }

    @Test
    public void setFloatingTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setFloatingTask(0, tpflt.readABook);
    }

}