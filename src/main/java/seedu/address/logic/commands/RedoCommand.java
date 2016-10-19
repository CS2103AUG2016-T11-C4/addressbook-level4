package seedu.address.logic.commands;

import java.util.EmptyStackException;

public class RedoCommand extends Command{
	
	public static final String COMMAND_WORD = "redo";
	 
	 public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Redo the previous undo.\n\t"
	            + "Example: " + COMMAND_WORD;

	@Override
	public CommandResult execute() {
		try{
			Command action = model.redo();
			return new CommandResult("Redid "+action.getCommandWord());
			
		}catch (EmptyStackException e){
			return new CommandResult("No undos to redo.");
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
