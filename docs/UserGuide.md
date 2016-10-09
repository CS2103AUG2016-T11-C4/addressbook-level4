# User Guide

* [Quick Start](#quick-start)
* [Features](#features)
* [Command Summary](#command-summary)

## Quick Start

1. Ensure you have Java version `1.8.0_60` or later installed in your computer.

  > Having any Java 8 version is not enough.
  > This app will not work with earlier versions of Java 8.

2. Download the latest `TaskTracker.jar` from the 'releases' tab.

3. Copy the file to the folder you want to use as the home folder for your task
   database.

4. Double-click the file to start the app. The GUI should appear in a few
   seconds.

5. Set up: enter your name and verify the curent date and time.

6. You're good to go! Try adding your first Task. press ';' to launch/hide the
   command bar. Press Enter to enter a command. For floating task, `add
   [taskname]`!

   ![alt text](./images/userguide/command_bar.png "command bar")

7. Add task with due date/time, example commands are:

	* `add "bake cookies" 31/12 1500`<br>
      bake cookies by 31 December of this year, 3pm.

	* `add "bake cookies" 1500`<br>
      bake cookies by 3pm today.

   ![alt text](./images/userguide/task.png "")

8. Add event. An event is a task with a start time and end time, example
   commands are:

	* `add "CS2103t summer camp" 30/8 1300 3/1/2017 1800`

      Event CS2103t summer camp starts on 30 Aug 2016, 1pm, and ends on 3
      January 2017,6pm.

	* `add "potato peeling" 1500 1900`

      Event potato peeling starts today 3pm and ends today 7pm.

	* `add "cupcake festival" 30/08 800 1200`

      Event cupcake festival starts on 8am and ends on 12pm of 24 Oct of this
      year.

	* `add "Trick or treat" 800 31/10 900`

      Event Trick or treat starts today 8pm and ends on 31 Oct 9pm.

   The events for current selected day will show up under schedule.

   ![alt text](./images/userguide/event.png "")

9. To see the schedule of a different day, press 'C' to toggle calender mode on
   and off. Use arrow keys and enter to select the day to view. The schedule
   for the day will show up in the schedule column.

   ![alt text](./images/userguide/calender.png "")

   ![alt text](./images/userguide/toggle_calender.png "")

   Alternatively, use the command line to view schedules. For example: `view 30/08/2016`

10. To delete a task/event, try:

	    del "cupcake festival"

11. To edit a deadline, try:

	* `edit "bake potato" dd-12/10 dt-1500`

      The following fields of task are modified: due date, due time. (`dd`
      refers to `due date`, `dt` refers to `due time`)

	* `edit "bake potato" dt-1500`

      Due time of bake potato modified.

11. To edit an event, try:

	* `edit "cupcake festival" loc-NUS`

      Location of cupcake festival set to NUS.

	* `edit "cupcake festival" st-1500 sd-12/10 et-1700 -ed 13/10 loc-Yishun`

      All fields modified. (`st` : `starting time`, `sd` : `starting date`,
      `et` : `ending time`, `ed` : `ending date`)

    * `edit "cupcake festival" st-1500`

      start time of cupcake festival modified.

12. To edit a floating task, try:

	* `edit "bake potato" p-1"`

      cupcake festival is given a priority of 1.

13. To exit the program, try:

	* `exit`

      Close the Task-tracker.

14. Refer to the [Features](#features) section below for details of each
    command.

## Features

**Command Format**

* Words in `UPPER_CASE` are the required parameters.

* Words in `lower_case` are the reserved keywords.

* Items in `[SQUARE_BRACKETS]` are optional parameters.

* Items in `<ANGLE_BRACKETS>` are partially optional parameters (a minimum
  number of partial optional parameters are required for certain command).

* Items seperated by `|` are in parallel relation, only one of them should be
  use in each command.

* Items with `...` after them can have multiple instances.

### Viewing help : `help`

    help

Help is also shown if you enter an incorrect command e.g. `abcd`

### Adding a floating task : `task`

Adds a floating task to TaskTracker.

    task "FLOATING_TASK_NAME" [PRIORITY]

* Task name should be in a pair of quotation marks.

* Task name could be a single word or a phrase, white space are allowed.

* The floating task will be shown according to their `PRIORITY`.

* Two floating tasks with the same `PRIORITY` will be shown according to
  the order of the time they created.

* The `PRIORITY` attribute is an integer which ranges from `0` to `5`.

* The default piority of the floating task is 0.

#### Examples

* `task "EE2020 lab report" 5`

To create a task called `EE2020 lab report` with `PRIORITY` of 5.

* `task ProgressReflection`

To create a task called `ProgressReflection` with default `PRIORITY` of 0.

### Adding a event-like task : `event`

Adds an event with specific starting and ending date or time to TaskTracker.

    event “EVENT_NAME” <STARTING_DATE> <STARTING_TIME> <ENDING_DATE> <ENDING_TIME> [loc-LOCATION]

* Event name should be in a pair of quotation marks.

* The four parameters `STARTING_DATE`, `STARTING_TIME`, `ENDING_DATE`,
  `ENDING_TIME` are not all required for adding event command.

  However, at least 2 of them are required from the command. Namely, at least
  one of `STARTING_DATE` and `STARTING_TIME` is required, and at least one of
  `ENDING_DATE` and `ENDING_TIME` is required.

* `STARTING_DATE` and `ENDING_DATE` must follow fixed formats : `dd/mm/yyyy` or
  `dd/mm` or `dd`, where `yyyy` is the numerical notation for years, `mm` is
  the numerical notation for months, and `dd` is the numerical notation for the
  date.

  The default year of `dd/mm` is the current year and the default month for
  `dd` is the current month.

* `STARTING_DATE` and `STARTING_TIME` must follow fixed formats : `ABCDam` or `ABCDpm`
  where each letter represents a number. `AB` represent the hour
  in 12 hour system, while `CD` represent the minute. Letter `A` can only be omitted
  if it is `0`, and 'CD' can be only omitted (together) when both of them are `0`.

* The default starting date will be the current date.

* The default ending date will be the same as the starting date.

* The default starting time will be `0000am` of starting date.

* The default ending time will be `1159pm` of the ending date.

* `[LOCATION]` is a String which could contain any characters.

#### Examples

* `event "CS2103 week8 lecture" 7/10 2pm 4pm`

To create an event `CS2103 week8 lecture` with starting time at 7th October 2pm
and end at 4pm.

* `event "programming workshop" 10am 5pm loc-LT15`

To create an event `programming workshop` with starting time at 10am of current date
and end at 5pm. The location will be LT15.

* `event "sports training camp" 1/12/2016 10/12/2016`

To create an event `sports training camp` with starting time at 2016 1st December 0am 
and end at 10th December 11:59pm.

* `event "job interview" 17/1/2017 915am 1045am loc-Block 71`

To create an event `job interview` with starting time at 2017 17th January 9:15 am and
ending time at 10:45 am.


### Adding a deadline-like task: `due`

Adds a deadline with specific due date or time to TaskTracker.

    due “DEADLINE_NAME” <DATE> <TIME>

* Dealine name should be in a pair of quotation marks.

* Either `<DATE>` or `<TIME>` or both of them are required here.

* Formats of `DATE` and `TIME` are the same as what we use to create event-like
  tasks.

* The default value of `DATE` will be the current date.

* There is no default value for `TIME` as long as the `DATE` passed in is
  valid.

#### Examples

* `due "CS2103 V1.1" 16/12`

To create a deadline named `CS2103 V1.1` with due date of 16th December.

* `due "event proposal" 6pm`

To create a deadline named `event proposal` with due time at today's 6 pm.

* `due "EE2024 homework 1" 1/11/2016 6am`

To create a deadline named `EE2024 homework 1` at 2016 1st October 6 am.

### List tasks: `list`

List certain type of task stored in the database

    list task|event|due
    
* `list task` will show all floating tasks according to the order of their priority.

* `list event` will show all events according to the order of their starting time.

* 'list due' will show all deadline according to their due time and due date. (when sorting
the deadline without due time, the sorting will use 11:59 pm of the due date as deafault)

* Every tasks (floating tasks / events / deadlines) will have a unique index to differentiate
their uniqueness. And their index will also be listed besides them when `list` command is
excuted.

* The overdue/past/finished tasks will not be listed.

### Deleting a floating task/event/deadline: `del`

Delete a useless floating task/event/deadline on TaskTracker.

    del TASK_UNIQUE_INDEX

* `TASK_UNIQUE_INDEX` will be different from each to each single task.

* A task's `TASK_UNIQUE_INDEX` will be the same as what is shown when `list` command is excuted.
The user can refer `list` cammand to look for the index of a certain task.

* `TASK_UNIQUE_INDEX` will never be changed once a index is assigned to a task when created.

#### Examples

* `list event`

List all the events stored in the database with their unique index number. 

`del 00123` <br>

Delete the task with the unique index of `00123`.

### Edit a floating task/event/dealine: `edit`

* Edit command can only edit the parameters of each commands but cannot transform a task to another
type. For example, `eidt` cannot transform a floating task to a event task.

#### Edit an floating task:

Edit a floating task to revise its name or priority.

    edit TASK_UNIQUE_INDEX [n-NEW_NAME | p-PRIORITY]...

* Quotation marks are not necessary for `NEW_NAME`.

* `PRIORITY` should only be the interger ranges from `0` to `5`.

#### Examples

* `edit 00124 p-0`

Edit task with unique index of `00124`'s priority to 0.

* `edit 00124 n-buy stationary`

Edit task with unique index of `00124`'s name to `buy stationary`.

* `edit 00125 n-"go to Nanyang Mart" p-1`

Edit task with unique index of `00125`'s name to `go to Nanyang Mart` and priority to 1.

#### Edit an event :

Edit an event to revise its name, starting/ending date/time and location.

    edit TASK_UNIQUE_INDEX [sd-NEW_START_TIME | st-NEW_START_DATE | ed-NEW_END_DATE | et-NEW_END_TIME | n-NEW_NAME | loc-NEW_LOCATION]...

* Quotation marks are not necessary for `NEW_NAME`.

* `[sd-NEW_START_TIME | st-NEW_START_DATE | ed-NEW_END_DATE | et-NEW_END_TIME]`
  are of the same format when creating event-like task. Please refer `event` command for reference.

#### Examples

* `edit 00126 loc-LT6`

Edit event with unique index of `00126`'s location to `LT6`.

* `edit 00126 st-4pm et-6pm`

Edit event with unique index of `00126`'s starting time to 4pm and ending time to 6pm.

* `edit 00127 n-proposal meeting st-7pm`

Edit event with unique index of `00127`'s starting time to 7pm and name to `proposal meeting`.


#### Edit a dealine :

Edit a deadline to revise its name and due date/time.

    edit TASK_UNIQUE_INDEX [dd-DUE_DATE | dt-DUE_TIME | n-NEW_NAME]...

* Quotation marks are not necessary for `NEW_NAME`.

* `[dd-DUE_DATE | dt-DUE_TIME]` are of the same formate when creating
  event-like task. Please refer `event` command for reference.

#### Examples

* `edit 00128 dt-5pm`

Edit deadline with unique index of `00128`'s due time to 5 pm.

* `edit 00128 dd-23/11/2016`

Edit deadline with unique index of `00128`'s due date to 2016 23th November.

### Mark a floating task/deadline as done/finished: `fin`

Mark a floating task/event/deadline as done on TaskTracker, the marked tasks
will be archived.

    fin TASK_UNIQUE_INDEX

* Events that have already passed it `DUE_TIME` will be marked as done
  automatically.

* Deadlines that have already passed it `DUE_TIME` will not be marked as done,
  but will be marked as "overdue" automatically.

#### Examples

* `fin 00123`

Mark task `00123` as finished.

### Show empty time slots : `slot`

Show all empty time slots in a given time period with a given duration.

    slot <STARTING_DATE> <STARTING_TIME> <ENDING_DATE> <ENDING_TIME> <h-HOUR> <m-MINUTE>

* At least one of `<STARTING_DATE> <STARTING_TIME>` are required.

* At least one of `<ENDING_DATE> <ENDING_TIME>` are required.

* At least one of `<h-HOUR> <m-MINUTE>` are required.

#### Examples

* `slot 1/11/2016 3/11/2016 h-4`

* `slot 5/11/2016 1400 2300 m-45`

### View a date: `view`

View all the task of a specific date in the Calander View of TaskTracker.

    view DATE

### Search by keywords: `srch`
Search task that contains specific keywords.

    srch KEY_WORDS

* The `KEY_WORDS` are CASE-SENSITIVE

#### Examples

* `srch lecture`

  Search for all the tasks that contain keyword `lecture`, TaskTracker will
  generate a list for view.

* `srch training SESSION`

  Search for all the tasks that contain keyword `training SESSION`, TaskTracker
  will generate a list for view.

### Undo an action : `undo`

Undo the last one action that the user performs wrongly.

    undo

### Clearing all entries : `clear`

Clears all entries from TaskTracker.

    clear

### Exiting the program : `exit`

Exits the program.

    exit

## Command Summary

Command | Format
------------ | :--------
Add Floating Task | `task "FLOATING_TASK_NAME" [PRIORITY]`
Add Event |`add “EVENT_NAME” <STARTING_DATE> <STARTING_TIME> <ENDING_DATE> <ENDING_TIME> [loc/LOCATION]`
Add Deadline |`add “DEADLINE_NAME” <DATE> <TIME>`
Delte a task | `del "TASK_NAME"`
Edit Floating Tasks | `edit "TASK_NAME" [n-NEW_NAME | p-PRIORITY]... `
Edit Event |`edit "TASK_NAME" [sd-NEW_START_TIME | st-NEW_START_DATE | ed-NEW_END_DATE | et-NEW_END_TIME | n-NEW_NAME | loc-NEW_LOCATION]... `
Edit Deadline |`edit "TASK_NAME" [dd-DUE_DATE | dt-DUE_TIME | n-NEW_NAME]... `
Generate recomanded time slots | `slot <STARTING_DATE> <STARTING_TIME> <ENDING_DATE> <ENDING_TIME> <h-HOUR> <m-MINUTE>`
Mark a task as finished | `fin "TASK_NAME"`
View a date in calander| `view DATE`
Search for keywords | `srch KEY_WORDS`
Help | `help`
Unod | `undo`
Clear | `clear`
Exit | `exit`
