package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskBook;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Task;

/**
 *
 */
public class TypicalTestTasks {

    public static TestTask alice;

    public static TestTask benson;

    public static TestTask carl;

    public static TestTask daniel;

    public static TestTask elle;

    public static TestTask fiona;

    public static TestTask george;

    public static TestTask hoon;

    public static TestTask ida;

    public TypicalTestTasks() {
        try {
            alice = new TaskBuilder().withName("Alice Pauline").build();
            benson = new TaskBuilder().withName("Benson Meier").build();
            carl = new TaskBuilder().withName("Carl Kurz").build();
            daniel = new TaskBuilder().withName("Daniel Meier").build();
            elle = new TaskBuilder().withName("Elle Meyer").build();
            fiona = new TaskBuilder().withName("Fiona Kunz").build();
            george = new TaskBuilder().withName("George Best").build();

            //Manually added
            hoon = new TaskBuilder().withName("Hoon Meier").build();
            ida = new TaskBuilder().withName("Ida Mueller").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(TaskBook ab) throws IllegalValueException {
        ab.addTask(new FloatingTask("alice"));
        ab.addTask(new FloatingTask("benson"));
        ab.addTask(new FloatingTask("carl"));
        ab.addTask(new FloatingTask("daniel"));
        ab.addTask(new FloatingTask("elle"));
        ab.addTask(new FloatingTask("fiona"));
        ab.addTask(new FloatingTask("george"));
    }

    public TestTask[] getTypicalPersons() {
        return new TestTask[]{alice, benson, carl, daniel, elle, fiona, george};
    }

    public TaskBook getTypicalAddressBook() {
        TaskBook ab = new TaskBook();
        try {
			loadAddressBookWithSampleData(ab);
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ab;
    }
}
