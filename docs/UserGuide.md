---
title: Task Tracker
papersize: A4
documentclass: report
numbersections: true
geometry: margin=2.4cm
mainfont: Myriad Pro
monofont: Inconsolata
fontsize: 11pt
toc: true
---

# User Guide

<!-- BEGIN GITHUB -->

* [About] (#about)
* [Quick Start](#quick-start)
	* [Quick Start Summary](#qss)
* [Features](#features)
	* [Data Models](#dm)
    * [Command Format](#cf)
    * [Parameter Keywords](#pk)
    * [Date Format](#DateFormat)
    * [Time Format](#TimeFormat)
    * [Viewing Help](#help)
    * [Add Floating Task](#addft)
    * [Add Deadline Task](#adddt)
    * [Add Event](#adde)
    * [Delete](#del)
    * [Edit Floating Task](#editft)
    * [Edit Deadline Task](#editdt)
    * [Edit Event](#adde)
    * [Mark Finish](#fin)
    * [Show Empty Timeslots](#slot)
    * [Search](#search)
    * [Undo](#undo)
    * [Redo](#redo)
    * [Clear](#clear)
    * [Exit](#exit)
* [Command Summary](#command-summary)

<!-- END GITHUB -->

## About

Unable to keep track of all your deadlines and other commitments?  Finding it
difficult to remember tasks which do not have a specific date and time, but
nevertheless needs to be done?

Fret not, TaskTracker is here to your rescue!

To be simply defined, TaskTracker is a digital assistant that keeps your
schedule neat and organized. It displays all your events lined up for the day,
upcoming deadlines and also tasks that do not have a specific time/ date, on
the same window.

You can perform various operations such as add/ delete/ edit/ prioritize, on
your events by simply typing a one-line command,
as TaskTracker has a flexible command line interface suited to your needs and comfort.
You don’t need to waste any
more time navigating through multiple options or filling multiple fields every
time you want to modify your schedule.

TaskTracker makes time-management smart and simple for you!

## Quick Start
Oh busy human, office worker by day and super hero by night, lets quick start the end to your disorganised tasks!

1. Ensure you have Java version `1.8.0_60` or later installed in your computer.

    > Note: Having any Java 8 version is not enough.
    > This app will not work with earlier versions of Java 8.

2. Download the latest `TaskTracker.jar` from the 'releases' tab.

3. Copy the file to the folder you want to use as the home folder for your task database.

4. Double-click the file to start the app. The GUI should appear in a few seconds.

5. Set up: enter your name and verify the current date and time.

6. You're good to go! Try adding your first Task. Press Enter to enter a
   command. For adding a floating task, try:

	* `add "Learn Task Tracker"`.

    ![alt text](./images/userguide/1.png "command bar")

    Oh shoot, here comes BOSS. "Busy human! I need these stack of papers signed!". Let's do another one:

	* `add "sign papers" p-2`

    ![alt text](./images/userguide/2.png "")

    Notice how `sign papers` is above `Learn Task Tracker`. That's because
    `sign papers` has higher priority of 2, while `Learn Task Tracker` with an
    undefined priority takes the default priority number of 0.

7. Suddenly, cubicle neighbour Sharon's head pops up from behind those grey office dividers. "Psst, thats alot of papers to sign. Let me help you with that." Aww, how nice of Sharon! Lets bake some cookies for her before she quits this dingy office year end.  Let's add a deadline tasks with due date and time.

	* `add "bake cookies" 31/12 3pm`

        `bake cookies` by 31 December of this year, 3pm. Undeclared year in date
        field will be taken as the current year.
        
 Also, remeber to collect the signed papers by today, 3pm!

	* `add "collect papers from Sharon" tdy 3pm`

        Cllect those papers from Sharon by today, 3pm.

    ![alt text](./images/userguide/3.png "")

8. Your phone bleeps. An invitation to attend the Avenger's convention organised by Dr Strange. Next up, events. An event is a task with a start date, start time, end date
   and end time. 

	* `add "Super hero's convention" 30/8 1pm to 3/1/2017 6pm`

        Event `super hero's convenrtion` starts from 1pm of the 30th August 2016, and ends at 6pm of the 3rd January 2017.

	* `add "potato peeling" tdy 3pm to 7pm`

        Event `potato peeling` starts from today's 3pm and ends at today's 7pm.

	* `add "cupcake festival" tdy 8pm tmr 12pm`

        Event `cupcake festival` starts from today's 8pm and ends at tomorrow's 12 pm.

	* `add "Trick or treat" 31/10 8pm to 9pm`

        Event `Trick or treat` starts from 8pm of 31st October this year and ends on the same day's 9pm.

    ![alttext](./images/userguide/4.png "")

9. To delete a task, try:

    ```
    del-float 2
    ```

    Delete floating task `bake potatoes`.

    ```
    del-deadline 1
    ```

    Delete deadline task `bake cookies`.

    ```
    del-event 4
    ```

    Delete Event `Trick or treat`.

    ![alttext](./images/userguide/5.png "")

10. To edit a deadline, try:

	* `edit-deadline 1 dd-29/12 dt-2pm`

        The following properties of task are modified: 'due date' and 'due time'. (`dd` refers to due date, `dt` refers to due time)

	* `edit-deadline 1 dt-3pm`

        Only the due time of `bake cookies` is modified.

    ![alttext](./images/userguide/6.png "")

11. To edit an event, try:

	* `edit-event 2 loc-NUS`

        Location of `potato peeling` set to NUS.

	* `edit-event 3 st-3pm sd-12oct et-5pm ed-13oct loc-Yishun`

        All fields modified. (`st` : `starting time`, `sd` : `starting date`,
        `et` : `ending time`, `ed` : `ending date`)

        ![alttext](./images/userguide/7.png "")

    * `edit-event 2 st-3pm`

        Start time of `potato peeling` modified.

        ![alttext](./images/userguide/8.png "")

12. To edit a floating task, try:

	* `edit-float 1 p-1`

        Floating Task `Learn Task Tracker` is given a priority of `1`.

        ![alttext](./images/userguide/9.png "")

13. If you've finished a task. Congrations! Let's mark that task as finshed.

     * `fin-float 1`

        Floating task `Learn Task Tracker` will be marked as finished.

     * `fin-deadline 1`

        Deadline task `bake cookies` will be marked as finished.

14. To exit the program, please enter:

	 * `exit`

        Close the Task-tracker.

15. For more details of each
     command.
please refer to the [Features](#features) section below.


## Features

### <a name="dm">Data models</a>

All tasks stored in TaskTracker will be automatically categorized into three different types: `floating task`, `deadline task`, and `event`, depending on the types and number of parameters entered when created.

|A/An... | has...|
|----| :-------------------------------------|
|Floating Task | only a task name|
|Deadline task |  end time and date |
|Event | start time and date,  end time and date|

### <a name="cf">Command Format</a>

* Words in `UPPER_CASE` are parameters to be defined by the user.

* Words in `lower_case` are the reserved keywords. All keywords can not be changed in each command line.

* Items in `[SQUARE_BRACKETS]` are optional parameters. All paramters in square backets can be omitted.

* Items in `<ANGLE_BRACKETS>` are optional parameters. However, a certain number of these kind of parameters will be required in different commands.

* All square brackets and angle brackets should not be included in the real commands.

* Items separated by `|` are in parallel relation, only one of them should be
  use in each command.

* Items with `...` after them can have multiple instances.

* The parameters have to follow the order in which they are presented, unless otherwise specified.

### <a name="pk">Keywords for Special Time Representation</a>

|Keyword | Definition     |
|----| :------------------|
|`tdy` | today|
|`tmr` | tommorow|
|`yst` | yesteday|

### <a name="DateFormat">Date format</a>

Valid input examples:

* dd/mm/yy

    ```
    31/12/2016
    ```

* dd/mm

    ```
    3/12
    ```

    If the year is not given, the default year will be the current year as logged by the local machine.

* dd

    ```
    31
    ```

    If the month is not defined, the default month will be the current as logged by the local machine.

* Today

    ```
    tdy
    ```

    Means today, the current date as logged by the local machine.

* Tommorow

    ```
    tmr
    ```

    Means tommorow, the day after date as logged by the local machine.


### <a name="TimeFormat">Time Format</a>

The 12-hour clock is used. `hh:mm` + `am/pm`

Valid input examples:

* `8:30am`

    8:30am in the morning

* `11:45pm`

    11:45pm at night

* `8pm` or `8:00pm`

    8:00pm at night

If the minute field is `00`, it may be ommitted from the command.

### <a name="help">Viewing help: `help`</a>

View the all the command formats of TaskTracker.

```
help
```

* Help is also shown if you enter an incorrect command e.g. `abcd`

![alt text](./images/userguide/help.png "")

### <a name="addft">Adding a floating task: `add`</a>

Adds a floating task to TaskTracker.

Format:
```
add "FLOATING_TASK_NAME" [p-PRIORITY]
```

* Task name should be in a pair of quotation marks. However, quotations marks are not allowed in task name.

* Task name can be a single word or a phrase, white space are allowed.

* All floating tasks will be shown according to their `PRIORITY`s.

* Two floating tasks with the same `PRIORITY` will be shown according to the order of the time they are created.

* The `PRIORITY` attribute is an integer which ranges from `0` to `5`, with `0` being the lowest pririoty and `5` the higest.

* The default priority of a floating task is `0`.

#### Examples

* `add "EE2020 lab report" p-5`

  To create a floating task called `EE2020 lab report` with `PRIORITY` of 5.

* `add "Progress Reflection"`

  To create a floating task called `Progress Reflection` with default `PRIORITY` of 0.

### <a name="adddt">Adding a deadline task: `add`</a>

Adds a deadline with specific due date and time to TaskTracker.

Format:
```
add "DEADLINE_NAME" <DATE> <TIME>
```

* Deadline name should be in a pair of quotation marks. And quotations marks are not allowed in deadline name.

* Keywords like `tdy`, `tmr`, `yst`, can be used in the `DATE` field.

* Formats of `DATE` and `TIME` should follow those stated above in this user guide. See [Date Format](#DateFormat) and [Time Format](#TimeFormat)

#### Examples

* `add "CS2103 milestone 1" 16/12/2016 2pm`

    To create a deadline task named `CS2103 milestone 1` with deadline of 16th December 2016, 2 pm.

* `add "spend pizza vouchers" 20/11/2018 2pm`

    To create a deadline task named `spend pizza vouchers` with deadline of 20 November 2018, 6 pm.

* `add "event proposal" tdy 6pm`

    To create a deadline named `event proposal` with due date of today, and due time of 6 pm.

* `add "EE2024 homework 1" tmr 6am`

    To create a deadline named `EE2024 homework 1` with deadline of tommorow, 6 am.

### <a name="adde">Adding an event: `add`</a>

Adds an event with specific starting date, starting time and ending date, ending time to TaskTracker.

Format:
```
add "EVENT_NAME" <START_DATE> <START_TIME> to <END_DATE> <END_TIME>
```

* Event name should be within a pair of quotation marks. Quotations marks are not allowed in event name.

* The four parameters `START_DATE`, `START_TIME`, `END_DATE`, `END_TIME` are not all required for adding an event. Only one of `START_DATE` and `START_TIME` is required and only one of `START_TIME` and `END_DATE` is required.

* Formats of `START_DATE`, `START_TIME`, `END_DATE`, `END_TIME` should follow those stated above in this user guide. See [Date Format](#DateFormat) and [Time Format](#TimeFormat).

#### Examples

* <a name="to">`add "CS2103 week8 lecture" 7/10 2pm to 4pm`</a>

    To create an event `CS2103 week8 lecture` with starting date 7 October 2016, starting time 2pm, ending date 7 Oct 2016, ending time 4pm.

* `add "programming workshop" tdy 10am to 5pm`

    To create an event `programming workshop` that starts today, 10am, and last till 5pm.

* `add "sports training camp" 1/12/2016 7pm to 10/1/2017 1pm`

    To create an event `sports training camp` with starting date 1 December 2016, starting time 7pm, ending date 10 January 2017 and ending time 1pm.

### <a name="del">Deleting a floating task/event/deadline: `del`</a>

Delete a useless floating task/event/deadline on TaskTracker.

Format:
```
del-float|-deadline|-event <INDEX>
```

* A task's `INDEX` is the number displayed beside the task name in the user interface.

#### Examples

* `del-event 2`

    Delete the event with the index of `2` in the task list shown in user interface.

### Edit a floating task/deadline/event: `edit`

* Edit command can only edit the parameters of each commands but cannot change
  the type of task.  For example, `edit` cannot transform a floating task to a
  event task.

* To edit an task, key in the index of the event followed by the properties to
  be modified. Label the new properties with their respective field references.

* The `[PARAMETERS]` need not follow the order shown in the command format.

#### <a name="editft">Edit a floating task: `edit`</a>

Edit a floating task to revise its name or priority.

|Field reference | Definition                    |
|:----------------|:-----------------------------|
| n- | name |
| p- | priority |

Format:
```
edit-float <INDEX> [n-NEW_NAME] [p-PRIORITY]
```

* Quotation marks are not required for `NEW_NAME`.

* `PRIORITY` should only be the integer ranges from `0` to `5`.

##### Examples

* `edit-float 2 p-0`

    Edit floating task with index of `2`'s priority to 0.

* `edit-float 2 n-buy stationary`

    Edit floating task with index of `2`'s name to `buy stationary`.

* `edit-float 5 n-go to Nanyang Mart p-1`

    Edit floating task with index of `5`'s name to `go to Nanyang
    Mart` and priority to 1.

#### <a name="editdl">Edit a deadline : `edit`</a>

Edit a deadline to revise its name and due date/time.

|Field reference | Definition                        |
|:----------------|:---------------------------------|
| n- | name |
| dd- | due date |
| dt- | due time |

Format:
```
edit-deadline <INDEX> [dd-DUE_DATE] [dt-DUE_TIME] [n-NEW_NAME]
```

* Quotation marks are not required for `NEW_NAME`.

* Formats of `[dd-DUE_DATE]` and `[dt-DUE_TIME]` should follow those stated above in this user guide. See [Date Format](#DateFormat) and [Time Format](#TimeFormat).

##### Examples

* `edit-deadline 1 dt-5pm`

    Edit deadline with index of `1`'s due time to 5 pm.

* `edit-deadline 2 dd-23/11/2016`

    Edit deadline with index of `2`'s due date to 2016 23th November.

#### <a name="edite">Edit an event : `edit`</a>

Edit an event to revise its name, starting/ending date/time and location.

|Field reference | Definition                  |
|----------------|:--------------------------- |
| n- | name |
| sd- | start date |
| st- | start time |
| ed- | end date |
| et- | end time |
| loc-| location |

Format: 
```
edit-event <INDEX> [ n-NEW_NAME] [sd-NEW_START_TIME] [st-NEW_START_DATE] [ed-NEW_END_DATE] [et-NEW_END_TIME] [n-NEW_NAME]
```

* Quotation marks are not required for `NEW_NAME`.

* Formates of `[sd-NEW_START_TIME]` `[st-NEW_START_DATE]` `[ed-NEW_END_DATE]` `[ed-NEW_END_DATE]` should follow those stated above in this user guide. See [Date Format](#DateFormat) and [Time Format](#TimeFormat).

##### Examples

* `edit-event 2 st-4pm et-6pm`

    Edit event with index of `2`'s starting time to 4pm and ending time to 6pm.

* `edit-event 7 n-"proposal meeting" st-7pm`

    Edit event with index of `7`'s starting time to 7pm and name to
    `proposal meeting`.

### <a name="fin">Mark a floating task/deadline as done/finished: `fin`</a>

Mark a floating task/event/deadline as done on TaskTracker, the marked tasks
will be archived.

Format: 
```
fin-float|-deadline|-event <INDEX>
```

* Events that have already passed it `DUE_TIME` will be marked as done automatically.

* Deadlines that have already passed it `DUE_TIME` will not be marked as done, but will be marked as "overdue" automatically.

#### Examples

* `fin-float 1`

    Marked floating task `1` as finished.

### <a name="slot">Show empty time slots: `slot`</a>

Show all empty time slots in a given time period with a given duration.

Format: 
```
slot <STARTING_DATE> <STARTING_TIME> <ENDING_DATE> <ENDING_TIME> <h-HOUR> <m-MINUTE>
```

* At least one of `<STARTING_DATE> <STARTING_TIME>` is required.

* At least one of `<ENDING_DATE> <ENDING_TIME>` is required.

* At least one of `<h-HOUR> <m-MINUTE>` is required.

#### Examples

* `slot 1/11/2016 3/11/2016 h-4`

    The TaskTracker will generate all empty time slots that are equal or
    greater than 4 hours between 2016 1st November 12am to 3rd 11:59pm.

* `slot 5/11/2016 2pm 11pm m-45`

    The TaskTracker will generate all empty time slots that are equal or
    greater than 45 minutes between 2016 5st November 2pm to 3rd 11:00pm.

* `slot 5/11/2016 2pm m-45`

    The TaskTracker will generate all empty time slots that are equal or
    greater than 45 minutes between 2016 5st November 12am to 2pm.

### <a name="view">Toggle views: `view`</a>

#### View all events that start on and all deadline tasks due on a specific date.

Format: 
```
view [DATE]
```

* `DATE` will follow the format shown in [Date Format](#DateFormat)

* The deadlines and events will be listed according to the order of the deadline's due time and the event's starting time, with the earlier time displayed at the top of the list.

#### View all time events and deadline task

Format: 
```
view
```

* All the task in the database will be displayed.

#### Examples

* `view 1/12`

  View all the tasks that are of the date of 1st December of the current year.

### <a name="search">Search by keywords: `search`</a>

Search task that contains specific keywords.

Format: 
```
search KEY_WORDS
```

* The `KEY_WORDS` are CASE-SENSITIVE

* Quotations marks are not allowed in `KEY_WORDS`

* The results will be listed according to their `INDEX`

#### Examples

* `search lecture`

    Search for all the tasks that contain keyword `lecture`, TaskTracker will
    generate a list for view.

* `search training SESSION`

    Search for all the tasks that contain keyword `training SESSION`,
    TaskTracker will generate a list for view.

### <a name="undo">Undo an action: `undo`</a>

Undo the previous action that modifies data. Undo can be performed many times until the first action since the app was launched has been undone.

Format: 
```
undo
```

View the stack of actions that undo will perform: `undo stack`

Format: 
```
undo stack
```

### <a name="redo">Redo an action: `redo`</a>

Redo the previous action that was undone by undo. The amount of consecutive redos doable is equal to the number of consecutive undos performed right before redo is entered.

Format: 
```
redo
```

View the stack of actions that redo will perform: `redo stack`

Format: 
```
redo stack
```

### <a name="clear">Clearing all entries: `clear`</a>

Clears all entries from TaskTracker.

Format: 
```
clear
```

### <a name="exit">Exiting the program: `exit`</a>

Exits the program.

Format: 
```
exit
```

## Command Summary

Command | Format
------------ | :---------------------------------------------------------------
Add Floating Task | `add "FLOATING_TASK_NAME" [p-PRIORITY]`
Add Event |`add “EVENT_NAME” <STARTING_DATE> <STARTING_TIME>  to <ENDING_DATE> <ENDING_TIME>`
Add Deadline |`add “DEADLINE_NAME” <DATE> <TIME>`
Delete a task | `del-float|-deadline|-event] <INDEX>`
Edit Floating Tasks | `edit-float <INDEX> [n-NEW_NAME] [p-PRIORITY]`
Edit Event |`edit-event <INDEX> [sd-NEW_START_DATE] [st-NEW_START_TIME] [ed-NEW_END_DATE] [et-NEW_END_TIME] [n-NEW_NAME]`
Edit Deadline |`edit-deadline <INDEX> [dd-DUE_DATE] [dt-DUE_TIME] [n-NEW_NAME]`
Generate recommended time slots | `slot <STARTING_DATE> <STARTING_TIME> <ENDING_DATE> <ENDING_TIME> <h-HOUR> <m-MINUTE>`
Mark a task as finished | `fin-float|-deadline|-event <INDEX>`
View a date in calendar | `view [DATE]`
Search for keywords | `search KEY_WORDS`
Help | `help`
Undo | `undo`
Redo | `redo`
Clear | `clear`
Exit | `exit`
