# pyokagan@gmail.com

###### docs/UsingGradle.md

    ## Running the Application
    
    * **`run`** <br>
      Builds and runs the application.
    
    * **`runShadow`** <br>
      Builds the shadow JAR, and then runs it.
    
    ## Running Tests
    
    The following project properties control test execution:
    
    * `headless` -- Run tests in headless mode (default: `false`)
    
    * `guiTests` -- Run GUI tests (default: `true`)
    
    Here are some examples:
    
    * `./gradlew -Pheadless=true test` -- Runs all tests in headless mode.
    
    * `./gradlew -PguiTests=false clean test` -- Clean the project and runs only
      non-GUI tests.
###### docs/productsurveys/todo.txt.md

    # Product survey on Todo.txt/Todo.sh
    
    ## Introduction
    
    Todo.txt, as its core, is just a
    [file format](https://github.com/ginatrapani/todo.txt-cli/wiki/The-Todo.txt-Format).
    Due to the simplicity of its file format, an ecosystem of
    [alternative applications](https://github.com/ginatrapani/todo.txt-cli/wiki/Other-Todo.txt-Projects)
    have sprung up around it. In this document, we will only be focussing on the
    [reference CLI implementation](https://github.com/ginatrapani/todo.txt-cli) of
    Todo.txt, called todo.sh.
    
    ## Data model
    Reference: [The todo.txt file format](https://github.com/ginatrapani/todo.txt-cli/wiki/The-Todo.txt-Format)
    
    At its core, the todo.txt file format has support for the following pieces of
    information for each task:
    
    * Name `<String>` -- The name of the task. It cannot contain newlines. To
      prevent the task from being misinterpreted as a completed task, it should not
      start with the character ‘x’. Other than that, all characters are allowed.
    
    * Priority `<Enum>` (optional) -- An uppercase character from A-Z (26
      priorities)
    
    * Creation date `<Date>` (optional)
    
    * Complete `<Boolean>` -- Whether the task is completed or not.
    
    Additionally, words that start with `+` in a task name are treated as projects
    which the task belongs to. Words that start with `@` in a task name are treated
    as contexts which the task belongs to. As such, project and context names
    cannot contain any whitespace.
    
    There are 2 task lists: `todo.txt` and `done.txt`. `todo.txt` is used to store
    the active tasks, while `done.txt` is used to store tasks that are completed
    and have been archived.
    
    ## Keyboard-oriented
    
    **(Positive)** Todo.sh is a fully keyboard-oriented terminal command-line
    application. It accepts input as command-line arguments or from stdin, outputs
    to stdout/stderr and then exits.
    
    As such, commands can be entered in and executed in "one shot", fulfilling
    Jim’s requirements.
    
    ## Works offline
    
    **(Positive)** Todo.sh only operates on plain files on the local filesystem. It
    does not connect to the Internet at all, and thus Jim can work offline with it.
    
    ## Cannot be summoned quickly
    
    Todo.sh is a terminal command-line application that accepts input directly from
    its command-line arguments. As such, it could be argued that it can be summoned
    quickly, provided that Jim is in the terminal.
    
    **(Negative)** Jim, unfortunately, is a Windows user. Terminal command line
    applications cannot be summoned quickly on Windows, so Todo.sh fails to satisfy
    this requirement.
    
    ## CRUD for Floating Tasks
    
    Todo.txt/todo.sh has full support for the create-read-update-delete(CRUD) of
    floating tasks.
    
    ### Creating Floating tasks
    
    #### Adding a floating task
    
    Floating tasks can be created with the `add/a` command.
    
    Add a simple task:
    
    	$ todo add Make peace between Cylons and humans
    	1 Make peace between Cylons and humans
    	TODO: 1 added.
    
    Note that for multiple whitespace to be preserved, the task name must be quoted:
    
    	$ todo add ‘Make peace between     Cylons and humans’
    	1 Make peace between     Cylons and humans
    	TODO: 1 added
    
    Leading and trailing whitespace is preserved. All embedded newlines are
    automatically and silently stripped. Other than that, all characters are
    allowed. In terms of allowable characters in task names, the todo.txt format is
    very flexible.
    
    #### Adding a floating task that belongs to a `+project`
    
    A project is just a word that starts with the `+` character. It can appear
    anywhere, in any order, in the task name.
    
    Add a task with a project name:
    
    	$ todo a "Upgrade jump drives with Cylon technology +GalacticaRepairs"
    	2 Upgrade jump drives with Cylon technology +GalacticaRepairs
    	TODO: 2 added.
    
    #### Adding a floating task that belongs to a `@context`
    
    A context is just a word that starts with the `@` character. It can appear
    anywhere, in any order, in the task name.
    
    Add a task with a context:
    
    	$ todo add "Check for DRADIS contact @CIC"
    	3 Check for DRADIS contact @CIC
    	TODO: 3 added.
    
    #### Adding a floating task with a priority
    
    Adding a task with priority `A`:
    
    	$ todo add "(A) My Task"
    	2 (A) My Task
    	TODO: 2 added.
    
    However, the priority specifier `(A)` must be FIRST. So the add command is not
    that flexible.
    
    #### Adding multiple tasks at once
    
    Bringing things one step further, todo.sh allows Jim to add multiple tasks in
    one shot:
    
    	$ todo addm ‘Task 1
    	Task 2’
    	1 Task 1
    	TODO: 1 added.
    	2 Task 2
    	TODO: 2 added.
    
    Tasks are separated by newlines. Shell quoting is required, and thus this
    command format is not really user friendly.
    
    ### Read floating tasks / simple search
    
    #### Listing and filtering tasks (`list|ls`)
    
    List all tasks:
    
    	$ todo list
    	3 Check for DRADIS contact @CIC
    	1 Make peace between Cylons and humans
    	2 Upgrade jump drives with Cylon technology +GalacticaRepairs
    	--
    	TODO: 3 of 3 tasks shown
    
    By default, tasks are sorted by their name. This behaviour can be used by
    enterprising users for e.g. sorting dates. We'll see a use of that later.
    
    TERMs specified will list tasks that contain all of the terms. This is
    case-insensitive:
    
    	$ todo list cylons peace
    	1 Make peace between Cylons and humans
    	--
    	TODO: 1 of 3 tasks shown
    	Furthermore, TERMs will also match substrings of words:
    	$ todo list lo ce
    	1 Make peace between Cylons and humans
    	--
    	TODO: 1 of 3 tasks shown
    
    The TERMs filtering takes into account the whole task name, including all
    embedded `+projects` and `@contexts`.
    
    All tasks with a priority will be shown first, in A-Z order. Tasks with the
    same priority are sorted by name.
    
    	$ todo ls
    	3 (A) A
    	2 (A) B
    	1 (D) A
    	4 A
    
    ### Updating Floating Tasks
    
    #### Set the priority of a single task (`pri|p`)
    
    	$ todo pri 2 A
    	2 (A) Upgrade jump drives with Cylon technology +GalaticaRepairs
    	TODO: 2 prioritized (A).
    
    The priority will override the previous task priority:
    
    	$ todo pri 2 B
    	2 (B) Upgrade jump drives with Cylon technology +GalacticaRepairs
    	TODO: 2 re-prioritized from (A) to (B).
    
    #### Remove priorities from task(s) (`depri|dp`)
    
    	$ todo depri 2
    	2: Upgrade jump drives with Cylon technology +GalaticaRepairs
    	TODO: 2 deprioritized
    
    **(Positive)** Pretty straightforward and simple. Multiple tasks can be
    deprioritised at the same time.
    
    #### Updating task name/projects/contexts of a single task (`replace`)
    
    The replace command allows Jim to completely replace a single task. This gives
    the effect of allowing Jim to update a task’s name, projects, contexts,
    priority or completion all at once.
    
    For instance, Jim could replace the task name of a task:
    
    	$ todo add a
    	1 a
    	TODO: 1 added
    	$ todo replace 1 b
    	1 a
    	TODO: Replaced task with:
    	1 b
    
    **(Negative)** One major problem with this feature is that if Jim only wants to
    update a certain field of a task, while keeping other fields intact, he would
    have to repeat the data:
    
    	$ todo add ‘(A) a +MyProject @MyContext’
    	1 (A) a +MyProject @MyContext
    	TODO: 1 added.
    	$ todo replace 1 ‘b +MyProject @MyContext’
    	1 (A) a +MyProject @MyContext
    	TODO: Replaced task with:
    	1 (A) b +MyProject @MyContext
    
    In this case, although Jim only wanted to update the task name from `a` to `b`,
    he still needed to repeat `+MyProject @MyContext` to preserve them. Note,
    however, that todo.sh was nice enough to preserve the task priority.
    
    #### Appending task `+project`(s)/`@context`(s) to a single task (`append|app`)
    
    Using the append action, Jim can add an existing task to project(s) and
    context(s):
    
    	$ todo add My Task +MyProject
    	1 My Task +MyProject
    	TODO: 1 added.
    	$ todo append 1 +MySecondProject +MyThirdProject @MyFirstContext @MySecondContext
    	1 My Task +MyProject +MySecondProject +MyThirdProject @MyFirstContext @MySecondContext
    
    **(Positive)** The append/app action can add a single task to multiple projects
    and contexts at once.
    
    **(Negative)** The append/app action only works on one task at a time.
    
    ### Deleting floating tasks
    
    Tasks can be deleted with the del/rm command:
    
    	$ todo add My Task
    	1 My Task
    	TODO: 1 added.
    	$ todo rm 1
    	Delete ‘My Task’? (y/n)
    	1 My Task
    	TODO: 1 deleted
    	$ todo add A new task
    	2 A new task
    	TODO: 2 added.
    
    **(Negative)** Only one task can be deleted at a time.
    
    **(Positive)** Todo.sh asks for confirmation before deleting the task to reduce
    user error. Also, note that the deleted task number is still reserved, new
    tasks will not take that number. This is a good thing because it prevents
    mistakes arising from users having to keep track of what task takes the deleted
    task’s index.
    
    ## CRUD for Deadlines
    
    The todo.txt file format does not have any explicit support for deadlines.
    However, as the todo.txt format specification suggests, Jim could use the
    behavioural properties of the todo.sh app to create tasks with deadlines.
    
    ### Creating deadlines
    
    Jim must firstly agree on a personal rule that tasks with deadline must contain
    the word `due:YYYY-MM-DD-HH:MM`, and that it must be the first word in the task
    name. E.g.:
    
    	$ todo add 'due:2016-09-24-13:00 Do homework'
    	1 due:2016-09-24-13:00 Do homework
    	TODO: 1 added.
    	$ todo add 'due:2016-09-23-09:00 Wash house'
    	2 due:2016-09-23-09:00 Wash house
    	TODO: 2 added.
    
    **(Negative)** However, as this is just a personal rule, todo.sh provides no
    error-checking on the formatting at all. Jim could accidentally enter a totally
    bogus due date and todo.sh would not bat an eye. Furthermore, the underlying
    todo.txt could be corrupted with a bogus date and todo.sh will not detect it as
    well. All Jim will get would be confusing incorrect results.
    
    ### Reading deadlines
    
    By using the `ls` action and filtering by `due:`, all deadlines can be listed.
    
    **(Positive)** Thanks to the personal rules on task names, deadlines will be
    sorted by due date when they are printed out.
    
    	$ todo ls due:
    	2 due:2016-09-23-09:00 Wash house
    	1 due:2016-09-24-13:00 Do homework
    	--
    	TODO: 2 of 2 tasks shown
    
    ### Updating/postponing deadlines
    
    Deadlines can be updated by using the `replace` command:
    
    	$ todo replace 2 ‘due:2016-09-25-09:00 Wash house’
    	2 due:2016-09-23-09:00 Wash house
    	TODO: Replaced task with:
    	2 due:2016-09-25-09:00 Wash house
    
    **(Negative)** However, the same criticisms of the `replace` command applies:
    needless retyping. Also, no error checking for the `due:` format.
    
    ### Deleting deadlines
    
    Since deadlines are just tasks, Jim could just use the usual `del/rm` action to
    delete deadlines.
    
    ## CRUD for events
    
    The todo.txt file format does not have any explicit support for events.
    However, similarly to how we stored deadlines, Jim could set up a few personal
    rules to store events.
    
    **(Negative)** The same criticisms for deadlines, however, apply. Namely, no
    error checking of the format.
    
    ## Looking for a suitable slot to schedule an event
    No support at all.
    
    ## Ability to “block” multiple slots.
    No support at all.
    
    ## Flexibility in command line format
    
    The command line format when adding tasks has the following constraints:
    
    * If the task is completed, it must begin with an `x `.
    
    * If the task has a priority `(A)`, `(B)`, `(C)` etc., it must be before the
      task name. If the task is completed, the priority must be specified directly
      after the `x `.
    
    **(Negative)** As we can see, the command line format is not very flexible when
    adding tasks.
    
    ## Undo operations
    
    Todo.sh has no support at all for undoing operations.
    
    ## Summary
    
    Jim's Requirement | Todo.txt Support
    :---------------- | ----------------
    Summoned quickly from anywhere | No
    Keyboard-oriented. Jim can type commands in "one shot" | Yes
    Works offline | Yes
    Simple search | Yes
    CRUD support for floating tasks | Full
    CRUD support for deadlines | Partial
    CRUD support for events | Partial
    Keep track of which items are done/not done | Yes
    Look for a suitable slot to schedule an item | No
    Ability to "block" multiple slots | No
    Flexibility in command line format | Tiny
    Undo operations | No
    
    Todo.sh, as a command-line application, is fully keyboard oriented and thus
    suitable for power users who prefer entering commands in one shot. As it only
    operates on local text files, it works fully offline.  Furthermore, its data
    model and user interface is specially geared towards the creation, reading,
    updating and deleting (CRUD) of floating tasks, which it does quite well. One
    exception is the interface to edit the name of a task, which is very clunky as
    it requires the user to repeat existing information.
    
    While the simplicity and flexibility of its data model means that CRUD
    operations on deadlines and events are technically feasible, the lack of
    explicit support from the application and data model means that the user
    interface is poor and error checking is lacking. Users who store their
    deadlines and events in todo.txt, without understanding how it works under the
    hood, would thus be in for some nasty surprises.
    
    Furthermore, the lack of explicit support for deadlines and events means that
    the application has no support at all for looking for a slot to schedule an
    item and the ability to "block" multiple time slots, as it has no concept of
    time at all.
    
    Finally, it has no support for undoing operations. While this is alleviated
    somewhat as it asks for confirmation before deleting tasks, all other
    operations are done without user confirmation.
###### docs/AboutUs.md

    ### [Chua Ka Yi Ong](https://github.com/kychua)
    
    ![Chua Ka Yi Ong](images/team/ChuaKaYiOng.jpg)
    
    Role: Project Advisor
    
    ### [Paul Tan](https://github.com/pyokagan)
    
    ![Paul Tan](images/team/PaulTan.jpg)
    
    Role: Team lead <br>
    Responsibilities: Integration
###### docs/AboutUs.md

    # Original Authors
    
    Task Tracker is based on [code](https://github.com/se-edu/addressbook-level4) by the following authors:
    
    * [Damith C. Rajapakse](http://www.comp.nus.edu.sg/~damithch)
    * [Joshua Lee](http://github.com/lejolly)
    * [Leow Yijin](http://github.com/yijinl)
    * [Martin Choo](http://github.com/m133225)
    * [Thien Nguyen](https://github.com/ndt93)
    * [You Liang](http://github.com/yl-coder)
    * [Akshay Narayan](https://github.com/se-edu/addressbook-level4/pulls?q=is%3Apr+author%3Aokkhoy)
    * [Sam Yong](https://github.com/se-edu/addressbook-level4/pulls?q=is%3Apr+author%3Amauris)
###### docs/DeveloperGuide.md

    # Developer Guide
    
    <!-- BEGIN GITHUB -->
    
    * [Setting Up](#setting-up)
    * [Design](#design)
    * [Implementation](#implementation)
    * [Testing](#testing)
    * [Appendix A: User Stories](#appendix-a-user-stories)
    * [Appendix B: Use Cases](#appendix-b-use-cases)
    * [Appendix C: Non Functional Requirements](#appendix-c-non-functional-requirements)
    * [Appendix D: Glossary](#appendix-d-glossary)
    * [Appendix E: Product Survey](#appendix-e-product-survey)
    
    <!-- END GITHUB -->
    
    ## Introduction
    
    Welcome to the TaskTracker development team!
    
    TaskTracker is a digital assistant that keeps track of your tasks, events and
    deadlines. It allows you to manage them efficiently with a keyboard-oriented
    command line interface.
    
    This development guide aims to quickly familiarise you with the TaskTracker
    code base. It will give you an overview of the code architecture, as well as
    its various components and how they all interact with each other. This guide
    will also show you how to accomplish common development tasks, so you can fully
    integrate with our development workflow. By the end of this document, you will
    be ready to make your first awesome change to the code.
    
    Ready to dive in? Let's get started!
    
    ## Setting up
    
    ### Prerequisites
    
    1. **JDK `1.8.0_60`** or later
    
        > Note: Having any Java 8 version is not enough.
        > This app will not work with earlier versions of Java 8.
    
    2. [**Eclipse**](https://eclipse.org/) IDE
    
    3. **e(fx)clipse** plugin for Eclipse (Do the steps 2 onwards given in
       [this page](http://www.eclipse.org/efxclipse/install.html#for-the-ambitious) )
    
    4. **Buildship Gradle Integration** plugin from the
       [Eclipse Marketplace](https://marketplace.eclipse.org/content/buildship-gradle-integration)
    
    5. A [**Github**](https://github.com/) account
    
    6. A [**Git**](https://git-scm.com/) client such as
       [SourceTree](https://www.sourcetreeapp.com/) or
       [Github Desktop](https://desktop.github.com/)
    
    ### Importing the project into Eclipse
    
    Our central development repository lives on
    [Github](https://github.com/CS2103AUG2016-T11-C4/main).
    
    1. Start by
       [forking our central development repository](https://help.github.com/articles/fork-a-repo/)
       on [Github](https://github.com/CS2103AUG2016-T11-C4/main)
    
    2. [Clone](https://help.github.com/articles/cloning-a-repository/) your
       personal fork to your computer.
    
    3. Open Eclipse (Note: Ensure you have installed the **e(fx)clipse** and
       **buildship** plugins as given in the prerequisites above)
    
    4. Click `File` > `Import`
    
    5. Click `Gradle` > `Gradle Project` > `Next` > `Next`
    
    6. Click `Browse`, then locate the directory where you cloned the project to in
       Step 2.
    
    7. Click `Finish`. The project should be successfully imported, and you can now
       start working on it.
    
    > Note:
    >
    > Depending on your connection speed and server load, it can take up to 30
    > minutes from the set up to finish. This is because Gradle downloads library
    > files from servers during the project's set up process.
    
    ### Troubleshooting project setup
    
    * **Problem: Eclipse reports compile errors after new commits are pulled from Git**
    
        * Reason: Eclipse fails to recognise new files that appeared due to the Git
          pull.
    
        * Solution: Refresh the project in Eclipse:
    
            Right click on the project (in Eclipse package explorer), choose `Gradle` ->
            `Refresh Gradle Project`.
    
    * **Problem: Eclipse reports some required libraries missing**
    
        * Reason: Required libraries may not have been downloaded during the project
          import.
    
        * Solution: [Run tests using Gradle](#testing-with-gradle) once to download all
          required libraries.
    
    ## Design
    
    ### Architecture
    
    <figure>
    <img src="images/devguide/architecture.png">
    <figcaption><div align="center">Figure 2.1: Architecture diagram of TaskTracker</div></figcaption>
    </figure>
    
    The **_Architecture Diagram_** (Figure 2.1) explains the high-level
    design of the application.
    
    Given below is a quick overview of each component.
    
    `Main` has only one class called [`MainApp`][mainapp-src]. It is responsible
    for:
    
    * At app launch: Initializes the components in the correct sequence, and
      connect them up with each other.
    
    * At shut down: Shuts down the components and invokes their cleanup method
      where necessary.
    
    [mainapp-src]: ../src/main/java/seedu/address/MainApp.java
    
    **`Commons`** represents a collection of classes used by multiple other
    components. Of these classes, two of them play important roles in the
    application architecture:
    
    * `EventsCentre`: This singleton class is used by components to communicate
      with other components using events.
    
    * `LogsCenter`: This singleton class is used by classes to write log messages
      to the application log file.
    
    The rest of the App consists of four components:
    
    * [**`Model`**](#model-component): Holds the data of the application in-memory.
    
    * [**`Storage`**](#storage-component): Reads data from, and writes data to,
      the hard disk.
    
    * [**`Logic`**](#logic-component): Parses and executes commands.
    
    * [**`Ui`**](#ui-component): The user interface of the application.
    
    Each of the four components:
    
    * Defines its _API_ in an `interface` with the same name as the Component.
    
    * Exposes its functionality using a `{Component Name}Manager` class.
    
    For example, the `Logic` component defines it's API in the `Logic.java`
    interface and exposes its functionality using the `LogicManager.java` class.
    
    The sections below give more details of each component.
    
    ### Model component
    
    <figure>
    <img src="images/devguide/comp-model.png">
    <figcaption><div align="center">Figure 2.2: Model component class diagram</div></figcaption>
    </figure>
    
    The `Model` component:
    
    * holds the task book data in memory. In particular, it stores the list of
      floating, deadline and event tasks.
    
    * exposes three lists for the stored floating, deadline and event tasks
      respectively.  These lists can be "observed". For example, the Ui component
      registers event listeners on these lists so that the Ui automatically updates
      when the data in these lists change.
    
    ### Storage component
    
    <figure>
    <img src="images/devguide/comp-storage.png">
    <figcaption><div align="center">Figure 2.3: Storage component class diagram</div></figcaption>
    </figure>
    
    The `Storage` component:
    
    * can save `TaskBook` objects to the hard disk and read it back.
    
    * can save `Config` objects to the hard disk and read it back.
    
    ### Logic component
    
    <figure>
    <img src="images/devguide/comp-logic.png">
    <figcaption><div align="center">Figure 2.4: Logic component class diagram</div></figcaption>
    </figure>
    
    The `Logic` component:
    
    * parses and executes user commands.
    
    * filters the lists of floating, deadline and event tasks in the task book.
    
    * writes the `Model` to the `Storage` if the `Model` has been modified by
      command execution, so that changes will be persisted to disk.
    
    It accomplishes its parsing and execution of user commands in a few steps:
    
    1. `Logic` uses its own internal `Parser` to parse the user command.
    
    2. This results in a `Command` object which is executed by the `LogicManager`.
    
    3. The command execution can affect the `Model` (e.g. adding a task, or
       changing a config setting.)
    
    4. The result of the command execution is encapsulated as a `CommandResult`
       object which is passed back to the `Ui`.
    
    5. If the `Model` has been modified as a result of the command, `Logic` will
       then write the updated `Model` back to disk using the `Storage` component.
    
    Given in Figure 2.5 below is the sequence diagram for interactions within the
    `Logic` component for the `execute("delete 1")` API call.
    
    <figure>
    <img src="images/devguide/seq-deleteevent.png">
    <figcaption><div align="center">Figure 2.5: Sequence diagram for event deletion</div></figcaption>
    </figure>
    
    ### Ui component
    
    <figure>
    <img src="images/devguide/comp-ui.png">
    <figcaption><div align="center">Figure 2.6: Ui component class diagram</div></figcaption>
    </figure>
    
    The `Ui` component,
    
    * Executes user commands using the `Logic` component.
    
    * Binds itself to some data in the `Model` so that the UI can auto-update when
      data in the `Model` change.
    
    * Responds to events raised from various parts of the App and updates the UI
      accordingly.
    
    The Ui consists of a `MainWindow` that is made up of parts e.g.`CommandBox`,
    `ResultDisplay`, `EventTaskListPane`, `StatusBarFooter` etc. All these,
    including the `MainWindow`, inherit from the abstract `UiPart` class.
    
    The `Ui` component uses JavaFx UI framework. The layout of these UI parts are
    defined in matching `.fxml` files that are in the `src/main/resources/view`
    folder. For example, the layout of `MainWindow` is specified in
    `src/main/resources/view/MainWindow.fxml`.
    
    ## Implementation
    
    ### Logging
    
    We are using `java.util.logging` package for logging. The `LogsCenter` class is
    used to manage the logging levels and logging destinations.
    
    * The logging level can be controlled using the `logLevel` setting in the
      configuration file (See [Configuration](#configuration))
    
    * The `Logger` for a class can be obtained using `LogsCenter.getLogger(Class)`
      which will log messages according to the specified logging level.
    
    * Currently log messages are output through: `Console` and to a `.log` file.
    
    **Logging Levels**
    
    * `SEVERE`: Critical problem detected which may possibly cause the termination
      of the application
    
    * `WARNING`: Can continue, but with caution
    
    * `INFO`: Information showing the noteworthy actions by the App.
    
    * `FINE`: Details that are not usually noteworthy but may be useful in
      debugging e.g. print the actual list instead of just its size
    
    ### Model implementation
    
    The model component internally uses various classes to model the data of the
    application.
    
    #### The task classes
    
    Task Tracker is able to store floating tasks, deadline tasks and event tasks.
    These are modeled as separate `FloatingTask`, `DeadlineTask` and `EventTask`
    classes respectively. Each class contains the fields specific to each type of
    task. For instance, `DeadlineTask` has a `due` field which stores its due
    datetime, while `EventTask` has both `start` and `end` fields which stores its
    starting datetime and ending datetime respectively.
    
    These classes inherit from a common `Task` abstract base class, which contains
    their common fields.
    
    The task classes are all guranteed to be immutable POJOs.
    
    #### The `TaskBook` class
    
    The `TaskBook` class stores the lists of floating tasks, deadline tasks and
    event tasks. It is an internal class of the Model component -- external
    components can only access its data via the `ReadOnlyTaskBook` or `Model` interface.
    
    #### The `ReadOnlyTaskBook` interface
    
    The `ReadOnlyTaskBook` interface provides a read-only view to a `TaskBook`
    object.
    
    #### The `Config` class
    
    The `Config` class stores various configuration settings. It is an internal
    class of the Model component -- external components can only access its data
    via the `ReadOnlyConfig` or `Model` interface.
    
    #### The `ReadOnlyConfig` interface
    
    The `ReadOnlyConfig` interface provides a read-only view to a `Config` object.
    
    #### The `ModelManager` class
    
    The `ModelManager` class implements the `Model` interface, and provides access
    to the model data while hiding the internal complexity of its various classes.
    All external components can only interact with the model data via this class.
    
    ### Storage implementation
    
    The storage component uses [Jackson](https://github.com/FasterXML/jackson) to
    serialize/deserialize model data to/from JSON files.
    
    #### Jackson Mixin classes
    
    Using Jackson's ability to [Mix-in annotations](http://wiki.fasterxml.com/JacksonMixInAnnotations),
    we are able to implement proper serialization/deserialization support for all of our model classes with very little code.
    
    In the `seedu.address.storage` package, these mixin classes have the name
    `Json{Model class name}Mixin`. For example, `JsonEventTaskMixin` is the mixin
    class for the `EventTask` model class. Its contents are as follows:
    ```java
    package seedu.address.storage;
    
    import java.time.LocalDateTime;
    
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import com.fasterxml.jackson.annotation.JsonPropertyOrder;
    
    import seedu.address.commons.time.LocalDateTimeDuration;
    import seedu.address.model.task.Name;
    
    @JsonPropertyOrder({"name", "start", "end"})
    public abstract class JsonEventTaskMixin {
    
        JsonEventTaskMixin(@JsonProperty("name") Name name, @JsonProperty("start") LocalDateTime start,
                           @JsonProperty("end") LocalDateTime end) {
        }
    
        @JsonIgnore
        abstract LocalDateTimeDuration getDuration();
    
    }
    ```
    As you can see, Jackson mixin annotations allow us to directly specify
    Jackson-specific annotations without needing to touch the actual class. This
    allows us to cleanly separate the storage implementation details from the model
    component.
    
    #### Jackson Module classes
    
    [Jackson modules](http://wiki.fasterxml.com/JacksonFeatureModules) allow us to
    bundle together related serialisation/deserialisation implementation classes
    into a single class, so that the serialisation/deserialisation logic is fully
    encapsulated.
    
    The storage package contains two module classes, `JsonConfigModule` and
    `JsonStorageModule` which bundle together the logic for
    serialising/deserialising `Config` and `TaskBook` classes respectively.
    
    #### The Storage interfaces
    
    The storage package defines two storage interfaces, `ConfigStorage` and
    `TaskBookStorage`. These interfaces contain methods for saving/loading
    `ReadOnlyConfig` and `ReadOnlyTaskBook` objects respectively.
    
    The storage package also defines a facade `Storage` interface, which combines
    together the aforementioned `ConfigStorage` and `TaskBookStorage` interfaces
    into a single interface.
    
    #### The JsonStorage classes
    
    The `JsonConfigStorage` class implements the `ConfigStorage` interface. It
    saves/loads `ReadOnlyConfig`s using Jackson's serialisation/deserialisation
    functionality, supplemented with our own Jackson modules and mixins.
    
    Likewise, the `JsonTaskBookStorage` class implements the `TaskBookStorage`
    interface and saves/loads `ReadOnlyTaskBook`s.
    
    #### The StorageManager class
    
    The `StorageManager` class wraps a `ConfigStorage` and `TaskBookStorage` and
    provides a single unified interface to them.
    
    ### UI implementation
    
    As mentioned in the [UI component architecture overview](#ui-component), the UI
    component is made up of "UI Parts". Each UI Part inherits from the abstract
    class `UiPart` and models a distinct part of the user interface. For example,
    the `MainWindow` class, which implements the main application window, is a UI
    Part.
    
    UI Parts themselves can contain multiple child UI Parts as well. For example,
    the `MainWindow` UI Part itself contains a few child UI Parts such as the
    `CommandBox`, `ResultDisplay`, `EventTaskListPane` etc.
    
    The use of UI parts aids in encapsulation of the different components of the
    Task Tracker user interface.
    
    #### Implementing a new UI Part
    
    Internally, a UI Part consists of two things:
    
    * A scene graph constructed using the
      [FXML Markup Language](https://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html),
      called the *view*.
    
    * Java code that implements the logic of the scene graph, called the
      *controller*.
    
    The view and controller are defined in matching `*.fxml` and `*.java` files.
    For example, the `ResultDisplay` UI Part has its view defined in
    `src/main/resources/view/ResultDisplay.fxml` and its controller defined in
    `src/main/java/seedu/address/ui/ResultDisplay.java`.
    
    A simple view (`HelloWorldUiPart.fxml`) that contains a single "Hello World!"
    label could be implemented as follows:
    
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    
    <?import javafx.scene.layout.VBox?>
    <?import javafx.scene.control.Label?>
    
    <VBox xmlns:fx="http://javafx.com/fxml/1">
        <Label>Hello World!</Label>
    </VBox>
    ```
    
    Its corresponding controller (`HelloWorldUiPart.java`) could be implemented as follows:
    ```java
    package seedu.address.ui;
    
    import javafx.fxml.FXML;
    import javafx.scene.layout.Pane;
    
    public class HelloWorldUiPart extends UiPart<Pane> {
    
        private static final String FXML = "/view/HelloWorldUiPart.fxml";
    
        public HelloWorldUiPart() {
            super(FXML);
        }
    }
    ```
    Notice how we need to pass the location of the `HelloWorldUiPart.fxml` to the
    superclass constructor. This tells the `UiPart` which FXML file to load.
    
    #### Initialising a UI Part
    
    UI Parts can be directly constructed. For instance, we could construct a new
    `HelloWorldUiPart` with:
    ```java
    HelloWorldUiPart helloWorldUiPart = new HelloWorldUiPart();
    ```
    
    The root node of its scene graph can then be accessed with its `getRoot()` getter:
    ```java
    Pane helloWorldPane = helloWorldUiPart.getRoot();
    ```
    
    This root node can then be added as a child to other scene graphs to compose
    the JavaFX Scene using multiple UI Parts.
    
    ## Configuration
    
    By default, the application stores its configuration in the `config.json` file.
    This file can be modified to change the configuration of the application.
    
    * `logLevel`: Sets the minimum required level for log messages to be
      output. See [Logging Levels](#logging-levels) for the list of available
      levels. (Default: `INFO`)
    
    * `taskBookFilePath`: The path to the user's task book file. (Default:
      `data/taskbook.json`)
    
    ## Testing
    
    Tests can be found in the `./src/test/java` folder.
    
    We have two types of tests:
    
    1. **GUI Tests** - These are _System Tests_ that test the entire App by
       simulating user actions on the GUI. These tests are in the
       `seedu.address.testutil.GuiTests` category.
    
    2. **Non-GUI Tests** - These are tests not involving the GUI. They include:
    
        1. _Unit tests_ targeting the lowest level methods/classes.
    
            e.g. `seedu.address.commons.utils.StringUtilTest`
    
        2. _Integration tests_ that are checking the integration of multiple code
           units (those code units are assumed to be working).
    
            e.g. `seedu.address.storage.StorageManagerTest`
    
        3. Hybrids of unit and integration tests. These test are checking multiple
           code units as well as how the are connected together.
    
            e.g. `seedu.address.logic.LogicManagerTest`
    
    ### Testing with Eclipse
    
    * To run all tests, right-click on the `src/test/java` folder and choose
      `Run as` > `JUnit Test`
    
    * To run a subset of tests, you can right-click on a test package, test class,
      or a test and choose to run as a JUnit test.
    
    ### Testing with Gradle
    
    * To run all tests, execute the following in the project work
      directory:
    
        ```
        ./gradle test
        ```
    
    * To only run non-GUI tests, execute the following in the project work
      directory:
    
        ```
        ./gradle -PguiTests=false test
        ```
    
    ### Troubleshooting tests
    
    * **Problem: Tests fail because a NullPointException was thrown when
    AssertionError is expected**
    
        * Reason: Assertions are not enabled for JUnit tests.
          This can happen if you are not using a recent Eclipse version (i.e. _Neon_ or later)
    
        * Solution: Enable assertions in JUnit tests as described
          [here](http://stackoverflow.com/questions/2522897/eclipse-junit-ea-vm-option).
          Delete run configurations created when you ran tests earlier.
    
    <!-- BEGIN LATEX
    \appendix
    END LATEX -->
    
    # Appendix A: User Stories
    
    Priorities:
    
    * `* * *` -- High (must have)
    * `* *` -- Medium (nice to have)
    * `*` -- Low (unlikely to have)
    
    User categories:
    
    * User -- Any user
    * New user -- User that has used Task Tracker for less than 100 hours
    * Busy user -- User that has a high density of deadlines and/or events over the
      next 1 month
    * Power user -- A user that likes to do things with as few interactions
      (keystrokes, mouseclicks, confirmations) as possible.
    * Group user -- User that has other people working on the same task
    
    Priority | As a ... | I want to ...             | So that I can...
    -------- | :------- | :------------------------ | :---------------------------
    `* * *`  | New user | See the manual | refer to manual when I forget how to use the App
    `* * *`  | User | Add an event to the task manager | keep track of it and be notified when it is approaching.
    `* * *`  | User | Add a deadline to the task manager | keep track of it and be notified when it is approaching.
    `* * *`  | User | Add a floating task to the task manager | Keep track of it and remember to do it when I'm free
    `* * *`  | User | View my floating tasks | keep track of them.
    `* * *`  | User | Search specific tasks by keywords |
    `*`  | Power user | Have shortcut keys to launch the app | Launch the app quickly
    `* `  | Power user | Have shortcut keys to minimise the app | Hide the app with only the keyboard
    `* * *`    | User | Mark a deadline as finished before the due time | Remove it from the notification list and archive it.
    `* * *`    | User | Mark a floating task as finished | Remove it from my floating task list.
    `* *`    | User who has events taking place at multiple locations | Add the location of an event | Be reminded of where to go.
    `* *`    | User | Have the app notify me of the error in my command, and suggest the right command when I make a typo/forget the format of the command | Enter in the correct command immediately without having to open up the manual.
    `* *`    | Busy user | View what events or deadlines are scheduled over a range of time | Ensure that the event does not clash with other events or deadlines.
    `* * *`    | User | Revise the due datetime for a certain deadline. | Keep track of it and avoid creating a new deadline when the time has been revised.
    `* * *`    | User | Revise the timeslot for a certain event | Keep track of it and avoid creating a new event when the time has been revised.
    `* *`    | Busy user | Generate a list of all empty time slots in a given period | Choose a free time slot to create new events or tasks.
    `* * *`    | User | Undo an action | Restore tasks deleted by accident.
    `* * *` | User | Redo an action | Reverse an action done by undo. 
    `*`      | User who is unable to remember details of each task | Add a short description under the name of each task in my schedule | Know how to do the task and where, even if I forget these details by any chance.
    `*`      | Busy user | Set priority levels for my tasks | Able to decide which task needs to be completed urgently
    `*`      | Group user | Option to categorize my task as a "Group activity" and automatically send notifications (through mail or other social networking platforms) to all other users who are in my team, whenever I make any changes to our work schedule for the group activity; and send them reminders about upcoming deadlines for the tasks. Every time I add a new task, I should have also an option to either include it to an existing group activity or add it to a new group activity. | Improve my work efficiency, and make sure everyone in my team are aware of the work schedule of our project.
    `* `     | User who needs to be reminded of the task before the deadline date. | Set reminders at customized times before the deadline. | Have enough time to complete the task before deadline, even if I forgot to do it.
    
    # Appendix B: Use Cases
    
    **Software System**: TaskTracker
    
    **Actor**: User
    
    ## Use case: Add an event
    
    **MSS**
    
    1. User requests to add an event with the specified name, start date/time and
       end date/time.
    
    2. TaskTracker adds the event to the database, and notifies the user that the
       event was successfully added.
    
       Use case ends.
    
    **Extensions**
    
    1a. The start datetime occurs after the end datetime.
    
    > 1a1. TaskTracker notifies the user that the start datetime and end datetime
    >      are invalid.
    
    > Use case ends.
    
    1b. The range of time specified by the start datetime and end datetime occurs
        in the past.
    
    > 1b1. TaskTracker warns the user that the event is in the past.
    
    > Use case resumes from step 2.
    
    1c. The name of the event contains invalid characters.
    
    > 1c1. TaskTracker notifies the user that the event name contains which invalid
    >      character(s).
    
    > Use case ends.
    
    1d. The name of the event contains leading/trailing whitespace.
    
    > 1d1. TaskTracker silently strips the leading/trailing whitespace from the
    >      name.
    
    > Use case resumes from step 2.
    
    ## Use case: Add a deadline
    
    **MSS**
    
    1. User requests to add a deadline with the specified name and end date/time.
    
    2. TaskTracker adds the deadline to the database, and notifies the user that
       the deadline was successfully added.
    
       Use case ends.
    
    **Extensions**
    
    1a. The end datetime occurs in the past.
    
    > 1a1. TaskTracker warns the user that the end datetime is in the past.
    
    > Use case resumes from step 2.
    
    1b. The name of the deadline contains invalid characters.
    
    > 1b1. TaskTracker notifies the user that the deadline name contains which
    >      invalid character(s).
    
    > Use case ends.
    
    1c. The name of the deadline contains leading/trailing whitespace.
    
    > 1c1. TaskTracker silently strips the whitespace from the name.
    
    > Use case resumes from step 2.
    
    ## Use case: Add a floating task
    
    **MSS**
    
    1. Use requests to add a floating task with the specified name.
    
    2. TaskTracker adds the floating task to the database at the lowest priority,
       and notifies the user that the floating task was successfully added.
    
       Use case ends.
    
    **Extensions**
    
    1a. The name of the floating task contains invalid character(s).
    
    > 1a1. TaskTracker notifies the user that the floating task name contains which
    >      invalid character(s).
    
    > Use case ends.
    
    1b. The name of the floating task contains leading/trailing whitespace.
    
    > 1b1. TaskTracker silently strips the leading/trailing whitespace from the
    >      name.
    
    > Use case resumes from step 2.
    
    ## Use case: View all floating tasks
    
    **MSS**
    
    1. User requests to view all floating tasks.
    
    2. TaskTracker displays all floating tasks in the database as a list, ordered
       from highest to lowest priority. <br>
       Use case ends.
    
    **Extensions**
    
    1a. There are no floating tasks in the database.
    
    > 1a1. TaskTracker notifies the user that there are no floating tasks in the
    >      database.
    
    > Use case ends.
###### docs/DeveloperGuide.md

    # Appendix D: Glossary
    
    <dl>
      <dt>Task book</dt>
      <dd>The database where events, deadlines and floating tasks are stored.</dd>
    
      <dt>Datetime</dt>
      <dd>Date and Time</dd>
    
      <dt>Task</dt>
      <dd>A unit of information in the task book database. Each task has a name.</dd>
    
      <dt>Event</dt>
      <dd>Task that has a start datetime and end datetime</dd>
    
      <dt>Deadline</dt>
      <dd>Task that has an end datetime only.</dd>
    
      <dt>Floating task</dt>
      <dd>A task that has neither a start datetime not end datetime.</dd>
    
      <dt>Time slot</dt>
      <dd>A time slot is referring to a period of time</dd>
    </dl>
    
    <!-- BEGIN LATEX
    
    \begin{description}
    
    \item[Task book] \hfill \\
        The database where events, deadlines and floating tasks are stored.
    
    \item[Datetime] \hfill \\
        Date and time
    
    \item[Task] \hfill \\
        A unit of information in the task book database. Each task has a name.
    
    \item[Event] \hfill \\
        Task that has a start datetime and end datetime.
    
    \item[Deadline] \hfill \\
        Task that has an end datetime only.
    
    \item[Floating task] \hfill \\
        A task that has neither a start datetime nor end datetime.
    
    \item[Time slot] \hfill \\
        A time slot is referring to a period of time.
    
    \end{description}
    
    END LATEX -->
    
    # Appendix E: Product Survey
    
    ## Todo.txt
    
    <!-- BEGIN GITHUB -->
    
    Full product survey [here](productsurveys/todo.txt.md)
    
    <!-- END GITHUB -->
    
    Jim's Requirement                                        | Todo.txt Support
    :------------------------------------------------------- | ---------------
    Summoned quickly from anywhere | No
    Keyboard-oriented. Jim can type commands in "one shot" | Yes
    Works offline | Yes
    Simple search | Yes
    CRUD support for floating tasks | Full
    CRUD support for deadlines | Partial
    CRUD support for events | Partial
    Keep track of which items are done/not done | Yes
    Look for a suitable slot to schedule an item | No
    Ability to "block" multiple slots | No
    Flexibility in command line format | Tiny
    Undo operations | No
    
    Todo.sh, as a command-line application, is fully keyboard oriented and thus
    suitable for power users who prefer entering commands in one shot. As it only
    operates on local text files, it works fully offline.
    
    Furthermore, its data model and user interface is specially geared towards the
    creation, reading, updating and deleting (CRUD) of floating tasks, which it
    does quite well. One exception is the interface to edit the name of a task,
    which is very clunky as it requires the user to repeat existing
    information.
    
    While the simplicity and flexibility of its data model means that CRUD
    operations on deadlines and events are technically feasible, the lack of
    explicit support from the application and data model means that the user
    interface is poor and error checking is lacking. Users who store their
    deadlines and events in todo.txt, without understanding how it works under the
    hood, would thus be in for some nasty surprises.
    
    Furthermore, the lack of explicit support for deadlines and events means that
    the application has no support at all for looking for a slot to schedule an
    item and the ability to “block” multiple time slots, as it has no concept of
    time at all.
    
    Finally, it has no support for undoing operations. While this is alleviated
    somewhat as it asks for confirmation before deleting tasks, all other
    operations are done without user confirmation.
###### docs/UserGuide.md

    ### <a name="undo">Undo an action: `undo`</a>
    
    Undo the previous action that modifies data. Undo can be performed many times until the first action since the app was launched has been undone.
    
    ```
    undo
    ```
    
    View the stack of actions that undo will perform: `undo stack`
    
    ```
    undo stack
    ```
    
    ### <a name="redo">Redo an action: `redo`</a>
    
    Redo the previous action that was undone by undo. The amount of consecutive redos doable is equal to the number of consecutive undos performed right before redo is entered.
    
    ```
    redo
    ```
    
    View the stack of actions that redo will perform: `redo stack`
    
    ```
    redo stack
    ```
    
    ### <a name="clear">Clearing all entries: `clear`</a>
    
    Clears all entries from TaskTracker.
    
    ```
    clear
    ```
