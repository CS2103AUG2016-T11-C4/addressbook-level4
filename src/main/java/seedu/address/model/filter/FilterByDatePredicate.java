package seedu.address.model.filter;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import seedu.address.model.task.DeadlineTask;
import seedu.address.model.task.EventTask;
import seedu.address.model.task.FloatingTask;
/*
 * Filters the events and deadlines based on the time when they are scheduled.
 * The user inputs a start time and end time, and all events and tasks that occur between the time range
 * specified by the user, are displayed on the user interface.
 *
 *
 */
public class FilterByDatePredicate implements TaskPredicate{
	private final LocalDateTime startTime;
	private final LocalDateTime endTime;

	public FilterByDatePredicate (LocalDateTime startTime, LocalDateTime endTime){
		assert !startTime.equals(null);
		assert !endTime.equals(null);

		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public boolean test (DeadlineTask task){
		if((greaterThan(task) && lesserThan(task)) | equalToMin(task) | equalToMax(task))
			return true;
		else
			return false;
	}

	@Override
	public boolean test(EventTask event){
		if((greaterThan(event) && lesserThan(event)) | (equalToMin(event) && lesserThan(event))
				| (equalToMin(event) && equalToMax(event)) | (greaterThan(event) && equalToMax(event))
				| (equalToMin(event) && equalToMax(event)))
			return true;
		else
			return false;
	}

	@Override
	public boolean test(FloatingTask floatTask){
		return true;
	}

	@Override
	public String toHumanReadableString(){
		Stream<LocalDateTime> rules = Stream.of(startTime, endTime);
		return rules.toString();
	}

	private boolean greaterThan (DeadlineTask task){
		LocalDateTime due = task.getDue();
		return due.isAfter(startTime);
	}

	private boolean lesserThan (DeadlineTask task){
		LocalDateTime due = task.getDue();
		return due.isBefore(endTime);
	}

	private boolean equalToMin (DeadlineTask task){
		LocalDateTime due = task.getDue();
		return due.isEqual(startTime);
	}

	private boolean equalToMax (DeadlineTask task){
		LocalDateTime due = task.getDue();
		return due.isEqual(endTime);
	}


	private boolean greaterThan (EventTask event){
		LocalDateTime start = event.getStart();
		return start.isAfter(startTime);
	}

	private boolean lesserThan (EventTask event){
		LocalDateTime end = event.getEnd();
		return end.isBefore(endTime);
	}

	private boolean equalToMin (EventTask event){
		LocalDateTime start = event.getStart();
		return start.isEqual(startTime);
	}

	private boolean equalToMax (EventTask event){
		LocalDateTime end = event.getEnd();
		return end.isEqual(endTime);
	}




}
