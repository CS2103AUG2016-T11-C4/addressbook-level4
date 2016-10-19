package seedu.address.logic.commands;

import java.util.EmptyStackException;

public class UndoCommand extends Command{
	
	public static final String COMMAND_WORD = "undo";
	 
	 public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Undos previous action that modifies address book information.\n\t"
	            + "Example: " + COMMAND_WORD;


	@Override
	public CommandResult execute() {
		try{
			
			Command undoneAction = model.undo();
			return new CommandResult("Successfully undid previous "+undoneAction.getCommandWord());			
			} catch (EmptyStackException e){
				return new CommandResult("No actions to undo.");
			}
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
