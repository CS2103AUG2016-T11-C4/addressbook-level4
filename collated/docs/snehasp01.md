# snehasp01@gmail.com

###### docs/AboutUs.md

    ### [Sneha Santha Prabakar](https://github.com/snehasp13)
    ![Sneha Santha Prabakar](images/team/Sneha.jpg)
    
    Role: Developer <br>
    Responsibilities: Testing
###### docs/DeveloperGuide.md

    ## Use case: Add priority tags
    
    **MSS**
    
    1. The user requests to add a priority tag for a specified event.
    
    2. TaskTracker prompts the user to select the priority level of the event -
       "Very Important"/ "Important"/ "Not important".
    
    3. The user selects the appropriate priority level for the event from these
       options.
    
    4. TaskManager updates the priority of the event with the specified priority, and it is displayed on the user's schedule.
    
    5. The user is notified that the update was successful.
    
       Use case ends.
    
    **Extensions**
    
    1a. The user places a request to add a priority tag, but does not add it.
    
    > 1a1. User presses "Esc". TaskTracker goes back to the normal view of the
    > schedule. Now even if the user clicks on any event, priority tag options will
    > not be displayed as the TaskTracker switched from Priority Tag view to Normal
    > view.
    
    > Use case ends.
    
    ## Use case: Add new event under a specific "group activity"
    
    **MSS**
    
    1. User requests to add a new event to a group activity.
    
    2. If the user wants to create a new group activity, he places a request to add new group activity. 
    
    3. The user then enters the details of the group activity (such as name of the
       group activity and email addresses of the people in the group).
    
    4. TaskTracker automatically adds the event to this new activity.
    
    5. TaskTracker notifies the user that the event has been added to the schedule.
    
       User case ends.
    
    **Extensions**
    
    1a. The user enters an invalid email address of a group member, while creating
    a new group activity.
    
    > 1a1. TaskTracker displays the error message - "Invalid email".
    
    > Use case ends.
    
    1b. The user enters a group activity name that already exists, while adding a
        new group activity.
    
    > 1b1. TaskTracker displays an error message "Name already exists".
    
    > Use case ends.
    
    ## Use case: Add event description
    
    **MSS**
    
    1. The user requests to add a new event.
    
    2. The user writes a small description about the event in the "Description"
       field, present in the "Add new event" window.
    
    3. The event is added and the user is notified about the new event added.
    
       User case ends.
    
    ## Use case: Set reminders
    
    **MSS**
    
    1. When adding a new event, the user adds the time at which he wants to set a
       reminder, before the event occurs. The time is entered in day/hours/minutes
       format.
    
    2. The new event is added to the schedule and the user is notified about the
       addition of the new event.
    
       User case ends.
    
    **Extensions**
    
    1a. The user enters the time in a wrong format.
    
    > 1a1. TaskTracker shows an error message "Invalid time format"
    
    > User case ends.
###### docs/DeveloperGuide.md

    ## Google Calendar
    
    <!-- BEGIN GITHUB -->
    
    Full product survey [here](https://docs.google.com/document/d/1ELun1gQUiVAxC6it-16jikFRwAePTXS5Xri7GdhUnL8/edit?usp=sharing)
    
    <!-- END GITHUB -->
    
    Jim's Requirement                                                | Google Calendar
    :--------------------------------------------------------------- | ---------------
    Summoned quickly from anywhere | Yes
    Keyboard-oriented. Jim can type commands in "one shot" | Yes
    Works offline | Partially
    Simple search | Yes
    CRUD support for floating tasks | No
    CRUD support for deadlines | Yes
    CRUD support for events | Partially
    Keep track of which items are done/not done | No
    Look for a suitable slot to schedule an item | Yes
    Ability to "block" multiple slots | Fully
    Flexibility in command line format | No
    Undo operations | Yes
    
    Google Calendar is essentially a time-management mobile and web application
    developed by Google. It is a good platform for office workers, such as Jimmy to
    organize their schedule. It has most of the features that an office worker like
    Jim would require; for example, Add/Edit/Delete tasks, automatically find free
    slots in the schedule to add new tasks, “quick add”, ability to block multiple
    tasks, etc. It can also support features such as import, export and sync with
    desktop programs such as Outlook  and also, with mobile devices. However, it
    also has a few drawbacks for workers like Jim - New tasks or modification of
    existing tasks cannot be performed when the user is offline, it does not
    display floating tasks, and also does not display the status of a task as
    "done" or "not done".
    
    We have taken some reference features from Google Calendar to implement in our
    own time management software. Some of these include having an feature to find
    empty time slots in the user’s schedule. While adding an event in Google
    Calendar, the users have an option called "Find Time" in which all the free
    slots in their schedule are displayed and they can choose any one for adding
    their task.
###### docs/UserGuide.md

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
