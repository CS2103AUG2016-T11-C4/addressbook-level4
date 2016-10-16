package seedu.address.logic.commands;

import java.util.Set;

import seedu.address.model.filter.NameContainsKeywordsPredicate;

/**
 * Finds and lists all tasks in task book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {
        model.setFilter(new NameContainsKeywordsPredicate(keywords));
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
    }

	@Override
	public boolean modifiesData() {
		return false;
	}
	
	@Override
	public String getCommandWord() {
		return COMMAND_WORD;
	}

}
