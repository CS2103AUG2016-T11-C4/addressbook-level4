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
components. Of these classes, the `LogsCenter` plays an important role in the
application architecture. It is a singleton class used by classes to write log
messages to the application log file.

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

The `Model` component:

* holds the task book data in memory. In particular, it stores the list of
  floating, deadline and event tasks.

* exposes three lists for the stored floating, deadline and event tasks
  respectively.  These lists can be "observed". For example, the Ui component
  registers event listeners on these lists so that the Ui automatically updates
  when the data in these lists change.

### Storage component

The `Storage` component:

* can save `TaskBook` objects to the hard disk and read it back.

* can save `Config` objects to the hard disk and read it back.

### Logic component

The `Logic` component:

* parses and executes user commands.

* filters the lists of floating, deadline and event tasks in the task book.

* writes the `Model` to the `Storage` if the `Model` has been modified by
  command execution, so that changes will be persisted to disk.

### Ui component

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

<figure>
<img src="images/devguide/comp-model.png">
<figcaption><div align="center">Figure 2.2: Model component class diagram</div></figcaption>
</figure>

The model component internally uses various classes to model the data of the
application.

#### The task classes

<figure>
<img src="images/devguide/classdiag-model-task.png">
<figcaption><div align="center">Figure 2.X: Task classes</div></figcaption>
</figure>

Task Tracker is able to store floating tasks, deadline tasks and event tasks.
These are modeled as separate `FloatingTask`, `DeadlineTask` and `EventTask`
classes respectively. Each class contains the fields specific to each type of
task. For instance, `DeadlineTask` has a `due` field which stores its due
datetime, while `EventTask` has both `start` and `end` fields which stores its
starting datetime and ending datetime respectively.

These classes inherit from a common `Task` abstract base class, which contains
their common fields.

The task classes are all guranteed to be immutable POJOs.

#### The `TaskBook` class and `ReadOnlyTaskBook` interface

<figure>
<img src="images/devguide/classdiag-model-taskbook.png">
<figcaption><div align="center">Figure 2.X: Taskbook classes</div></figcaption>
</figure>

The `TaskBook` class stores the lists of floating tasks, deadline tasks and
event tasks. It is an internal class of the Model component -- external
components can only access its data via the `ReadOnlyTaskBook` or `Model` interface.

Since `Tasks` are immutable, it only needs to implement the basic operations of
adding, removing and setting tasks.

The `TaskBook` class implements the `ReadOnlyTaskBook` interface, which
provides a read-only view of its data.

#### Task item indexing, sorting and filtering (`WorkingTaskBook`)

<figure>
<img src="images/devguide/classdiag-model-workingtaskbook.png">
<figcaption><div align="center">Figure 2.X: WorkingTaskBook</div></figcaption>
</figure>

The `TaskBook` class only stores the raw task data. However, the application
user cases dictates the following additional requirements on top:

1. We must be able to filter the lists of tasks, to view a subset of each list.
   However, this filtering must not affect the actual `TaskBook` itself.

2. We must be able to specify a sort ordering on the lists of
   floating/deadline/event tasks. This sorting must be automatic -- if the
   field's of a task changes, it's position in the sorted list should change as
   required as well.

3. We must be able to attach a unique index number to any task. This index
   number should be stick to the task, no matter if its fields changes or its
   position in the sorted list changes. Furthermore, this index number must be
   separate from the `TaskBook` -- For instance even if a task is in position
   `100` in the underlying `TaskBook` list, it should still be possible to
   refer to it as index `1` via a combination of filters and/or sort
   comparators.

These requirements are all implemented with the `WorkingTaskBook` class. The
prefix `Working` comes from the idea that it is some kind of "workspace" for
working with tasks. This workspace is a wrapper around a `TaskBook` and manages
its data.

For the 1st requirement, `WorkingTaskBook` keeps its own internal copy of a
`TaskBook`. It then uses a `TaskPredicate` to filter the lists of
floating/deadline/event tasks, which it then exposes through the
`workingFloatingTasks`, `workingDeadlineTasks` and `workingEventTasks` lists.

For the 2nd requirement, `WorkingTaskBook` also uses comparators to sort the
working floating/deadline/event tasks lists. This sorting is automatic -- if
the contents of the floating/deadline/event tasks lists change, they will
automatically be re-sorted as required. (Internally, we use a JavaFX
`SortedList`).

For the 3rd requirement, we have the `IndexedItem<E>` interface, which
represents an item of the type `E` which has an index which we call the
"working index", attached to it. The `WorkingItem<E>` class implements this
interface, and, in addition to storing the `workingIndex` and `item`, also
stores the `sourceIndex`, which maps the `IndexedItem` back to the source item
of the internal `TaskBook`. By decoupling the source item index from the
working index, we can ensure that the working index remains the same even if
other tasks in the task book are deleted.

#### The `Config` class and `ReadOnlyConfig` interface

<figure>
<img src="images/devguide/classdiag-model-config.png">
<figcaption><div align="center">Figure 2.X: Config</div></figcaption>
</figure>

The `Config` class stores the various configuration settings as listed in the
[configuration](#configuration) section. It implements the `ReadOnlyConfig`
interface, which provides a read-only view to a `Config` object.

#### The `ModelManager` class

The `ModelManager` class implements the `Model` interface, and provides access
to the model data while hiding the internal complexity of its various classes.
All external components can only interact with the model data via this class.

### Storage implementation

<figure>
<img src="images/devguide/comp-storage.png">
<figcaption><div align="center">Figure 2.3: Storage component class diagram</div></figcaption>
</figure>

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

### Logic component implementation

<figure>
<img src="images/devguide/comp-logic.png">
<figcaption><div align="center">Figure 2.4: Logic component class diagram</div></figcaption>
</figure>

The `Logic` component accomplishes its parsing and execution of user commands in a few steps:

1. `Logic` uses its own internal `TaskTrackerParser` to parse the user command.

2. This results in a `Command` object which is executed by the `LogicManager`.

3. The command execution can affect the `Model` (e.g. adding a task, or
   changing a config setting.)

4. The result of the command execution is encapsulated as a `CommandResult`
   object which is passed back to the `Ui`.

5. If the `Model` has been modified as a result of the command, `Logic` will
   then write the updated `Model` back to disk using the `Storage` component.

6. If the `Model` has been modified as a result of the command, `Logic` will
   call `model.recordState()` to record the state of the model for undo/redo.

Given in Figure 2.5 below is the sequence diagram for interactions within the
`Logic` component for the `execute("delete 1")` API call.

<figure>
<img src="images/devguide/seq-deleteevent.png">
<figcaption><div align="center">Figure 2.5: Sequence diagram for event deletion</div></figcaption>
</figure>


#### The parser hierarchy

<figure>
<img src="images/devguide/classdiag-logic-parser.png">
<figcaption><div align="center">Figure 2.X:</div></figcaption>
</figure>

As shown in Figure 2.X, from the perspective of the top level `LogicManager`
class, the `TaskTrackerParser::parse()` method just needs to be called to parse
a command string into a `Command`. However, internally the `TaskTrackerParser`
actually consists of a hierarchy of parsers, each handling a specific part of
the parsing process (Single Responsibility Principle).

The first level of parsers are for parsing the subcommands. For example,
`AddTaskParser` will handle the `add` command, `ListCommandParser` will handle
the `list` command, etc.

These first level command parsers include:

* `AddTaskParser` (`add`)
* `EditCommandParser` (`edit`)
* `DeleteCommandParser` (`del`)
* `MarkFinishedCommandParser` (`fin`)
* `MarkTaskUnfinishedCommandParser` (`unfin`)
* `ClearCommandParser` (`clear`)
* `ExitCommandParser` (`exit`)
* `HelpCommandParser` (`help`)
* `SetDataDirectoryParser` (`setdatadir`)
* `ListCommandParser` (`list`)
* `UndoCommandParser` (`undo`)
* `RedoCommandParser` (`redo`)
* `FindCommandParser` (`find`)

A full list of these command parsers can be found in `TaskTrackerParser.java`.

The second level of parsers are usually for overloaded command parsing. For
example, the `AddTaskCommandParser` internally consists of
`AddFloatingTaskCommandParser`, `AddDeadlineTaskCommandParser` and
`AddEventTaskCommandParser` subparsers. Each of these subparsers handle a
variation of the `add` command -- the command format variation for adding
floating tasks, deadline tasks and event tasks respectively. Some commands,
such as the `list` command, do not have any variations in their command format
and thus bypass the second level.

These second level command parsers include:

* `AddEventParser` (Add an event)
* `AddDeadlineParser` (Add a deadline)
* `AddFloatingTaskParser` (Add a floating task)
* `EditEventParser` (Edit an event)
* `EditDeadlineParser` (Edit a deadline)
* `EditFloatingTaskParser` (Edit a floating task)
* `DeleteEventParser` (Delete an event)
* `DeleteDeadlineParser` (Delete a deadline)
* `DeleteFloatingTaskParser` (Delete a floating task)
* `MarkDeadlineFinishedParser` (Mark a deadline as finished)
* `MarkFloatingTaskFinishedParser` (Mark a floating task as finished)
* `MarkDeadlineUnfinishedCommandParser` (Mark a deadline task as unfinished)
* `MarkFloatingTaskUnfinishedCommandParser` (Mark a floating task as unfinished)

The third level of parsers are for parsers that parse the
individual arguments and flags. For instance, `IndexParser` implements the
parsing of test indexes (e.g. `e1`, `d1`, `f1` etc.) As such, it is logical
that `EditFloatingTaskParser`, `EditDeadlineParser`, `DeleteFloatingTaskParser`
etc. depends on them as the `edit` and `del` command formats include task
indexes to tell the command which task to edit or delete.

These third level parsers include:

* `DateParser` (parses a date and returns a `LocalDate`)
* `TimeParser` (parses a time and returns a `LocalTime`)
* `FileParser` (parses a file path and returns a `File`)
* `NameParser` (parses a task name and returns a `Name`)
* `PriorityParser` (parses a task priority and returns a `Priority`)
* `TaskPredicateParser` (parses a task predicate name and returns a `TaskPredicate`)
* `IndexParser` (parses a task index and returns an `Integer`)

The application of the Single Responsibility Principle in the parsing component
brings several benefits. Firstly, it is extremely easy to unit test each
individual parser. Secondly, this approach is also very DRY (Don't repeat
yourself) -- each piece of parsing logic has a single definitive source. For
example, if we wanted to add a new kind of `TaskPredicate`, all we need to do
is to modify `TaskPredicateParser` and all parsers will immediately recognize
the task predicate name.

#### The `Parser` interface

<figure>
<img src="images/devguide/classdiag-model-parser-interface.png">
<figcaption><div align="center">Figure 2.X:</div></figcaption>
</figure>

The whole parsing architecture makes use of the command pattern. All parsers
implement the `Parser<T>` interface, which is a functional interface whose
functional method is `parse(String)`. This method should take a string, parse
it, and return its result of type `T`.

If a parse error occurs, the `parse(String)` method should throw a
`ParseException`, with `ranges` denoting the `SubstringRange(s)` of the input
string which caused the parse to fail.

Parsers can also optionally implement an autocomplete method, to allow the
parser to complete an input string given a `ReadOnlyModel` and an integer caret
position. It should then return a list of possible candidates, with are strings
that could possibly be inserted at the caret's position in the string. The
default implementation returns an empty list (no candidates).

As such, a simple parser that returns a command could be implemented as follows:

```java
package seedu.address.logic.parser;

import seedu.address.commons.util.SubstringRange;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;

/**
 * A parser that returns a command that says "hello world!" if the input is "hello",
 * otherwise throws a ParseException.
 */
public class HelloWorldParser implements Parser<Command> {
    @Override
    public Command parse(String str) throws ParseException {
        if (str.equals("hello")) {
            return model -> new CommandResult("hello world!");
        } else {
            throw new ParseException("must say hello", SubstringRange.of(str));
        }
    }
}
```

This parser can now actually be used with `LogicManager` by constructing it
with:
```java
new LogicManager(model, storage, new HelloWorldParser());
```
where `model` and `storage` are instances of `ModelManager` and `StorageManager`.

However, a useful command-line parser requires much more effort and code than
that. This is because the parser also needs to keep track of things like
subcommands, overloaded commands, and arguments, flags, quoting etc.

This is where the command pattern used here truly shines as the `Parser`
interface allows us to easily mix and match parsers through the use of OCP
(open-closed principle) to generate new command formats. The
`seedu.address.logic.parser` package provides a few utility classes to do just
that. They include: the `CommandLineParser`, the `SubcommandParser` and the
`OverloadParser`. With this utility classes, it is extremely easy to implement
your own `Parser` with minimal effort.

#### The `CommandLineParser` utility class

<figure>
<img src="images/devguide/classdiag-logic-commandlineparser.png">
<figcaption><div align="center">Figure 2.X:</div></figcaption>
</figure>

The `CommandLineParser` class helps to piece together individual `Parser<T>`
parsers to parse a command line input.

For consistency, all commands in Task Tracker follow a standard command format
which consists of the following three parts in order:

* Arguments, which are single fields identified by their position on the
  command line. Arguments are separated by whitespace. If an argument value
  needs to contain whitespace, it can be `"quoted"` with quotes.

* An optional "rest argument", which extends from the last argument all the way
  to the start of the first flag.

* Flags, which are fields identified by starting with their "flag prefixes".

The `CommandLineParser`, along with its `ArgumentParsers` and `FlagParsers`
helps to parse such a command line input and break the arguments and flags into
their individual components for easy processing. In addition, it will help to
detect problems like missing arguments or repeated flags.

* `CommandLineParser.Argument<T>` will parse a single argument.

* `CommandLineParser.RestArgument<T>` will parse a "rest argument".

* `CommandLineParser.ListArgument<T>` will parse a "rest argument" as a list of
  arguments.

* `CommandLineParser.Flag<T>` will parse a flag. The `CommandLineParser` will
  error out (throw a `ParseException`) if the flag was not specified or was
  specified multiple times.

* `CommandLineParser.OptionalFlag<T>` will parse a flag. The
  `CommandLineParser` will *not* complain if the flag was not specified, but
  will still error out if the flag was specified multiple times.

Note that these classes all take a `Parser<? extends T>` in their constructors
-- these classes will call the specified parser to parse the argument/flag into
the type of object that you want.

For instance, let's say we want to parse the input format of the form:
```
NAME_ARG [p-PRIORITY]
```

An example input would be:
```
"Learn Task Tracker" p-4
```

We first break down the arguments and flags into their individual types:

* `NAME_ARG` is an argument that must be a valid task name. We can parse that
  with `NameParser` which will return a `Name`.

* `p-PRIORITY` is an optional flag, but if provided, it must be a valid
  priority. We can parse that with `PriorityParser` which will return a
  `Priority`.

We can thus piece together the `CommandLineParser.Argument<T>` and
`CommandLineParser.OptionalFlag<T>` like this:

```java
private final CommandLineParser.Argument<Name> nameArg =
        new CommandLineParser.Argument<>("NAME", new NameParser());
private final CommandLineParser.OptionalFlag<Priority> priorityFlag =
        new CommandLineParser.OptionalFlag<>("p-", "PRIORITY", new PriorityParser());
```

And then we can build our `CommandLineParser` like this:
```java
private final CommandLineParser cmdParser = new CommandLineParser()
                                                .addArgument(nameArg)
                                                .putFlag(priorityFlag);
```

Now, with the input string, all we need to do is to call:
```java
cmdParser.parse("\"Learn Task Tracker\" p-4");
```

And then the values of the argument and flag will be available in `nameArg` and
`priorityFlag`. You can get them by calling `getValue()`:
```java
System.out.println(nameArg.getValue()); // Learn Task Tracker
System.out.println(priorityFlag.getValue()); // Optional[4]
```

#### The `SubcommandParser` utility class

<figure>
<img src="images/devguide/classdiag-logic-subcommandparser.png">
<figcaption><div align="center">Figure 2.X: SubcommandParser class diagram</div></figcaption>
</figure>

#### The `OverloadParser` utility class

<figure>
<img src="images/devguide/classdiag-logic-overloadparser.png">
<figcaption><div align="center">Figure 2.X: OverloadParser class diagram</div></figcaption>
</figure>

#### The `Command` interface

<figure>
<img src="images/devguide/classdiag-logic-commandinterface.png">
<figcaption><div align="center">Figure 2.X: The command interface</div></figcaption>
</figure>

Other than parsing, the logic component also executes commands, represented by
the `Command` interface. Commands have hidden internal logic and the ability to
be executed. The `Command` interface is a functional interface whose functional
method is `execute(Model)`.

If an error occurs during execution, the command can throw a
`CommandException`. The caller (e.g. the `LogicManager`) is then expected to
handle the exception gracefully, such as reporting the error to the user and
rolling back any changes to the model.

All commands in the `seedu.address.logic.commands` package implement the
`Command` interface to allow them to be executed by the `LogicManager`.

### UI implementation

<figure>
<img src="images/devguide/comp-ui.png">
<figcaption><div align="center">Figure 2.6: Ui component class diagram</div></figcaption>
</figure>

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
`* * *`  | User | Mark a deadline as finished before the due time | distinguish finished tasks and unfinished tasks in UI.
`* * *`  | User | Mark a floating task as finished | remove it from my floating task list.
`* * *`  | User | Mark a finished deadline as unfinished before the due time |
`* * *`  | User | Mark a finished floating task as unfinished |
`* *`  | User | Set my database directory | 
`* *`  | User | Revise the due datetime for a certain deadline. | keep track of it and avoid creating a new deadline when the time has been revised.
`* *`  | User | Revise the timeslot for a certain event | keep track of it and avoid creating a new event when the time has been revised.
`* *`  | User | List all the tasks that I have finished | 
`* *`  | User | List all the tasks that I have not finished | 
`* *`  | User | Clear all finished tasks at one time | avoid deleting my finished tasks one by one.
`* *`  | User | Undo an action | restore tasks deleted by accident.
`* *`  | User | Redo an action | reverse an action done by undo. 
`* *`    | User | Have the app notify me of the error in my command, and suggest the right command when I make a typo/forget the format of the command | enter in the correct command immediately without having to open up the manual.
`*`      | Busy user | Set priority levels for my tasks | able to decide which task needs to be completed urgently
`*`      | Power user | Have shortcut keys to launch the app | launch the app quickly.
`* `     | Power user | Have shortcut keys to minimise the app | hide the app with only the keyboard.

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

> 1b1. TaskTracker will add this event task but mark it as finished automatically.

> Use case ends.

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

> 1a1. TaskTracker will add this deadline but mark it as overdue.

> Use case ends.

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

## Use case: View all finished/unfinished tasks

**MSS**

1. User requests to view all finished/unfinished tasks.

2. TaskTracker displays all finished/unfinished tasks in the database as a list;
   floating tasks will be ordered from highest to lowest priority;
   deadline tasks will be ordered from earliest due time to latest due time;
   event tasks will be order from earliest startign time to latest starting time. <br>
   Use case ends.

**Extensions**

1a. There are no tasks in the database.

> 1a1. TaskTracker will display an empty list.

> Use case ends.

## Use case: Revise the due time of a deadline task

**MSS**

1. User requests to update the due datetime of a certain deadline with new
   date/time information.

2. TaskTracker revises the deadline to the new time, and notifies the user that
   the due date/time was successfully revised.

   Use case ends.

**Extensions**

1a. The requested deadline task does not exist.

> 1a1. TaskTracker informs the user that the requested deadline does not exist.

> Use case ends.

1b. The new date/time occurs in the past.

> 1b1. TaskTracker will edit the date/time and then mark this task as overdue.

> Use case ends.

## Use case: Revise the time of an event

**MSS**

1. User requests to revise the the time of a certain event with new
   date/time information.

2. TaskTracker revises the event to a new time slot, and notifies the user that
   the time was successfully revised.

   Use case ends.

**Extensions**

1d. The requested event does not exist.

> 1a1. TaskTracker informs the user that the requested event does not exist.

> Use case ends.

1b. The range of time specified by the start datetime and end datetime occurs
    in the past.

> 1b1. TaskTracker will edit the date/time and then mark this task as finished.

> Use case end.

1c. The start datetime occurs after the end datetime.

> 1c1. TaskTracker notifies the user that the start datetime and end datetime
> are invalid.

> Use case ends.

## Use case: mark a floating task/deadline as finished/unfinished

**MSS**

1. User requests to mark a certain floating task/deadline as finished/unfinished.

2. TaskTracker marks the task as finished/unfinished and informs the user.
   Use case ends.

**Extensions**

1a. The floating task/deadline does not exist.

> 1a1. TaskTracker informs the user that the floating task does not exist.

> Use case ends.

## See the manual

**Use case: see the manual**

**MSS**

1. User requests to see the manual.

2. TaskTracker shows manual.

   Use case ends.

## Search the manual

**Use case: Auto-complete**

**MSS**

1. User request to auto complete the task name when editing one specific task.

2. TaskTracker will complete the precious task name automatically.

   Use case ends

**Extensions**

1a. The target task does not exist.

> 1a1. TaskTracker will show nothing.

> Use case ends

## Undo an action

**User case: Undo an action, such as restoring a deleted a task**

**MSS**

1. The user wants to undo a completed action

2. The previous action is undone. Task manager is restored to state before the
   completed action.

   User case ends.

**Extensions**

1a. There is no action to undo

> 1a1. TaskTracker shows an error message "no action to undo"

> User Case ends.

## Redo an action

**User case: Redo an action, such as deleting a task**

**MSS**

1. The user wants to redo an action that is undone previously.

2. The previous undone action is redone. Task manager is restored to state after the
   completed action.

   User case ends.

**Extensions**

1a. There is no action to redo

> 1a1. TaskTracker shows an error message "no action to redo"

> User Case ends.

# Appendix C: Non Functional Requirements

1. Should work on any mainstream OS as long as it has Java `1.8.0_60` or higher
   installed.

2. Should come with automated unit tests and open source code.

3. Have the app notify me of the error in my command, and suggest the right
   command when I make a typo/forget the format of the command.

4. Storage should not use relational databases, but an editable text file.

5. For a full list of constrains, see the handbook at http://www.comp.nus.edu.sg/~cs2103/AY1617S1/contents/handbook.html#handbook-project-constraints  

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

## Todoist

<!-- BEGIN GITHUB -->

Full product survey [here](productsurveys/Todoist.md)

<!-- END GITHUB -->

Jim’s Requirement                                        | Todoist support
:------------------------------------------------------- | ---------------
Summoned quickly from anywhere | No
Keyboard-oriented. Jim can type commands in “one shot”. | No
Keyboard-oriented. Jim can use keyboard shortcut to increase efficiency. | Yes
Works offline | Yes
Simple Search | Yes
CRUD support for floating tasks | Full
CRUD support for deadlines | Full
CRUD support for events | None
Keep track of which items are done/not done | Yes
Look for a suitable time schedule a task. | Tiny
Ability to “block” multiple slots. | No
Flexibility in command line format. | No
Undo operations | Partial

Todoist is a half keyboard oriented cross-platform applications, with plenty of
useful keyboard short-cut on both PC and Web clients. It can work both online
or offline on mobile phone or personal computer.

It fully supports creation, reading, updating and deleting (CRUD) function of
floating tasks and deadline-like task. The user interface is well designed and
user can perform their actions quite easily through it. Besides, user can also
conduct simple searching action among all task he or she has created. However,
Todoist also does not support the functionality to add event-like task at all.

Also for premium account, Todoist also gives the support of tracking the task
that has already been done or finished, which gives convenience for user to
record their working history.

Besides, Todoist can automatically suggest how many tasks have already existed
on a certain day when creating the tasks. But since Todoist does not support
event-like task, it also has no support at all for generating suggestion of
suitable time-slots or placing multiple time slots/blocks when creating the
tasks.

Moreover, Todoist does support undo-actions, but only for the last one actions
the user performs.

Furthermore, there are several features that we could learn from Todoist. The
first one is the "Quick-Add" function which enables the user to add either
floating task or deadline task in a single window. The second one is the "Undo"
command, which enables the user to undo the last command immediately when them
perform wrongly.

## Wunderlist

<!-- BEGIN GITHUB -->

Full product survey [here](productsurveys/Wunderlist.md)

<!-- END GITHUB -->

Wunderlist in brief: Written in Javascript and PHP, Wunderlist is a task
manager application with a simple, intuitive,and beautiful User Interface that
is installed locally on the hard disk. It has server functions to share todo
lists and is able to sync information using a Wunderlist account. Thus CRUD of
local tasks work offline, but with online, additional syncing, sharing and
collaborating features are accessible.

So, how well does Wunderlist satisfy Jim's requirements?

| Jim's Requirement                                        | Wunderlist support |
| :------------------------------------------------------- | ------------------ |
| Summoned quickly from anywhere | No |
| Keyboard-oriented. Jim can type commands in "one shot". | Partially |
| Works offline | Yes |
| Simple Search | Yes |
| CRUD support for floating tasks | Full |
| CRUD support for deadlines | Full |
| CRUD support for events | None |
| Keep track of which items are done/not done | Yes |
| Look for a suitable slot to schedule an item. | No |
| Ability to "block" multiple slots. | No |
| Flexibility in command line format. | No |
| Undo operations | Tiny |

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
