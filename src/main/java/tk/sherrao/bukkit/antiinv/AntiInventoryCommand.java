package tk.sherrao.bukkit.antiinv;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tk.sherrao.bukkit.utils.command.CommandBundle;
import tk.sherrao.bukkit.utils.command.SherCommand;

public class AntiInventoryCommand extends SherCommand {

	protected AllowedItemManager aim;
	
	protected String invalidItemMsg, successMsg, alreadyAddedMsg, senderConsoleMsg;
	protected Sound invalidItemSound, successSound, alreadyAddedSound;
	
	public AntiInventoryCommand( AntiInventory pl ) {
		super( "antiinventory", pl );
		
		this.aim = pl.getAllowedItemManager();
	
		this.senderConsoleMsg = config.getString( "messages.cmd-sender-is-console" );
		this.invalidItemMsg = config.getString( "messages.cmd-invalid-item" );
		this.successMsg = config.getString( "messages.cmd-success" );
		
		this.invalidItemSound = config.getSound( "sounds.cmd-invalid-item" );
		this.successSound = config.getSound( "sounds.cmd-success" );
		
	}

	@Override
	protected void onExecute( CommandBundle bundle ) {
		if( bundle.isPlayer() ) {
			Player player = (Player) bundle.sender;
			ItemStack item = player.getItemInHand();
			if( player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand().getType() == Material.AIR )
				bundle.messageSound( invalidItemMsg, invalidItemSound );
			
			else if( aim.isAllowed( item ) )
				bundle.messageSound( alreadyAddedMsg, alreadyAddedSound );
			
			else {
				aim.addItem( item );
				bundle.messageSound( successMsg, successSound );
				
			}
			
		} else 
			bundle.message( senderConsoleMsg );
		
	}
	
}