package tk.sherrao.bukkit.antiinv;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import tk.sherrao.bukkit.utils.plugin.SherEventListener;

public class InventoryInteractListener extends SherEventListener {

	protected AllowedItemManager aim;
	protected List<World> blockedWorlds;

	protected String blockedMsg;
	protected Sound blockedSound;
	
	public InventoryInteractListener( AntiInventory pl ) {
		super(pl);
		
		this.aim = pl.getAllowedItemManager();
		this.blockedWorlds = new ArrayList<>();
		for( String name : config.getStringList( "blocked-worlds" ) ) {
			World world = Bukkit.getWorld( name );
			if( world != null )
				blockedWorlds.add( world );
			
			else
				log.warning( "Ignoring world with name '" + world + "', world doesn't exist!" );
			
		}

		this.blockedMsg = config.getString( "messages.blocked" );
		this.blockedSound = config.getSound( "sounds.blocked" );
		
	}

	@EventHandler
	public void onInventoryInteract( InventoryClickEvent event ) {
		if( !(blockedWorlds.contains( event.getWhoClicked().getWorld() ) ))
			return;
		
		if( event.getClick() == ClickType.NUMBER_KEY ) {
			event.setCancelled( true );
			return;
			
		} else {
		
			if( ( event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || aim.isAllowed( event.getCurrentItem() ) ) && 
					( event.getCursor() == null || event.getCursor().getType() == Material.AIR || aim.isAllowed( event.getCursor() ) ) )
				return;
		
			if( event.getWhoClicked().getGameMode() != GameMode.SURVIVAL )
				return;
		
			if( event.getWhoClicked().isOp() || event.getWhoClicked().hasPermission( "antiinv.bypass" ) )
				return;

			HumanEntity player = event.getWhoClicked();
			a( event, player );
			
			
		}
	}
	
	private void a( Cancellable event, HumanEntity player ) {
		event.setCancelled( true );
		player.sendMessage( blockedMsg );
		pl.playSound( (Player) player, blockedSound );
		
	}
	
}