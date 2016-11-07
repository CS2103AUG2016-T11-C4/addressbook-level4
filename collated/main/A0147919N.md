# A0147919N

Unused codes:

###### src/main/java/seedu/address/logic/parser/FilterByDatePredicateParser.java

``` java
package seedu.address.logic.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import seedu.address.logic.commands.ListCommand;
import seedu.address.model.filter.FilterByDatePredicate;
import seedu.address.model.filter.TaskPredicate;

public class FilterByDatePredicateParser implements Parser<ListCommand>{

	private final Optional<LocalDateTime> referenceDateTime;
  private final DateTimeArgument dateTimeArg = new DateTimeArgument("DATE", "TIME");

    public FilterByDatePredicateParser() {
        this(Optional.empty());
    }

    public FilterByDatePredicateParser(LocalDateTime referenceDateTime) {
        this(Optional.of(referenceDateTime));
    }

    public FilterByDatePredicateParser(Optional<LocalDateTime> referenceDateTime) {
        this.referenceDateTime = referenceDateTime;
    }

	private final CommandLineParser cmdParser = new CommandLineParser().addArgument(dateTimeArg);
	@Override
	public ListCommand parse(String str) throws ParseException{
		cmdParser.parse(str);
    final LocalDateTime now = referenceDateTime.orElse(LocalDateTime.now());

		 final LocalDate startDate = dateTimeArg.getDate().orElse(now.toLocalDate());
	     final LocalTime startTime = dateTimeArg.getTime().orElse(LocalTime.of(23, 59));
	     final LocalDate endDate = dateTimeArg.getDate().orElse(now.toLocalDate());
	     final LocalTime endTime = dateTimeArg.getTime().orElse(LocalTime.of(23, 59));

		final TaskPredicate predicate = new FilterByDatePredicate(LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime) );
		return new ListCommand(predicate);
	}

}
```
###### src/main/java/seedu/address/model/filter/FilterByDatePredicate.java

``` java
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

```
######  src/main/java/seedu/address/logic/parser/TaskTrackerParser.java

``` java
 .putSubcommand("filter", new FilterByDateCommandParser())
```
