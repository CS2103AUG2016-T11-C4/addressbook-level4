package seedu.address.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.TypicalDeadlineTasks;
import seedu.address.model.task.TypicalEventTasks;
import seedu.address.model.task.TypicalFloatingTasks;

public class ModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TypicalFloatingTasks tpflt = new TypicalFloatingTasks();

    private TypicalEventTasks tpent = new TypicalEventTasks();

    private TypicalDeadlineTasks tpdue = new TypicalDeadlineTasks();

    private Model model;

    @Before
    public void setupModel() {
        model = new ModelManager();
    }

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), model.getFilteredFloatingTaskList());
        assertEquals(Collections.emptyList(), model.getFilteredEventTaskList());
        assertEquals(Collections.emptyList(), model.getFilteredDeadlineTaskList());
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

    @Test
    public void addEventTask_appendsEventTask() throws Exception {
        model.addEventTask(tpent.lunchWithBillGates);
        assertEquals(tpent.lunchWithBillGates, model.getEventTask(0));
        model.addEventTask(tpent.launchNuclearWeapons);
        assertEquals(tpent.lunchWithBillGates, model.getEventTask(0));
        assertEquals(tpent.launchNuclearWeapons, model.getEventTask(1));
    }

    @Test
    public void getEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getEventTask(0);
    }

    @Test
    public void removeEventTask_removesIndexInFilteredList() throws Exception {
        model.addEventTask(tpent.lunchWithBillGates);
        model.addEventTask(tpent.launchNuclearWeapons);
        model.setEventTaskFilter(eventTask -> eventTask.equals(tpent.launchNuclearWeapons));
        model.removeEventTask(0);
        model.setEventTaskFilter(null);
        assertEquals(Arrays.asList(tpent.lunchWithBillGates), model.getFilteredEventTaskList());
    }

    @Test
    public void removeEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeEventTask(0);
    }

    @Test
    public void setEventTask_replacesIndexInFilteredList() throws Exception {
        model.addEventTask(tpent.lunchWithBillGates);
        model.addEventTask(tpent.launchNuclearWeapons);
        model.setEventTaskFilter(eventTask -> eventTask.equals(tpent.launchNuclearWeapons));
        model.setEventTask(0, tpent.lunchWithBillGates);
        model.setEventTaskFilter(null);
        assertEquals(Arrays.asList(tpent.lunchWithBillGates, tpent.lunchWithBillGates),
                    model.getFilteredEventTaskList());
    }

    @Test
    public void setEventTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setEventTask(0, tpent.lunchWithBillGates);
    }

    ///

    @Test
    public void addDeadlineTask_appendsDeadlineTask() throws Exception {
        model.addDeadlineTask(tpdue.speechTranscript);
        assertEquals(tpdue.speechTranscript, model.getDeadlineTask(0));
        model.addDeadlineTask(tpdue.assembleTheMissiles);
        assertEquals(tpdue.speechTranscript, model.getDeadlineTask(0));
        assertEquals(tpdue.assembleTheMissiles, model.getDeadlineTask(1));
    }

    @Test
    public void getDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.getDeadlineTask(0);
    }

    @Test
    public void removeDeadlineTask_removesIndexInFilteredList() throws Exception {
        model.addDeadlineTask(tpdue.speechTranscript);
        model.addDeadlineTask(tpdue.assembleTheMissiles);
        model.setDeadlineTaskFilter(deadlineTask -> deadlineTask.equals(tpdue.assembleTheMissiles));
        model.removeDeadlineTask(0);
        model.setDeadlineTaskFilter(null);
        assertEquals(Arrays.asList(tpdue.speechTranscript), model.getFilteredDeadlineTaskList());
    }

    @Test
    public void removeDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.removeDeadlineTask(0);
    }

    @Test
    public void setDeadlineTask_replacesIndexInFilteredList() throws Exception {
        model.addDeadlineTask(tpdue.speechTranscript);
        model.addDeadlineTask(tpdue.assembleTheMissiles);
        model.setDeadlineTaskFilter(deadlineTask -> deadlineTask.equals(tpdue.assembleTheMissiles));
        model.setDeadlineTask(0, tpdue.speechTranscript);
        model.setDeadlineTaskFilter(null);
        assertEquals(Arrays.asList(tpdue.speechTranscript, tpdue.speechTranscript),
                    model.getFilteredDeadlineTaskList());
    }

    @Test
    public void setDeadlineTask_invalidIndex_throwsException() throws Exception {
        thrown.expect(IllegalValueException.class);
        model.setDeadlineTask(0, tpdue.speechTranscript);
    }

}
