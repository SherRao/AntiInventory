package tk.sherrao.bukkit.antiinv;

import tk.sherrao.bukkit.utils.plugin.SherPlugin;

public class AntiInventory extends SherPlugin {
	
	protected AllowedItemManager aim;
	
	@Override
	public void onLoad() {
		super.onLoad();
		
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		aim = new AllowedItemManager( this );
		
		super.registerEventListener( new InventoryInteractListener( this ) );
		super.registerCommand( "antiinventory", new AntiInventoryCommand( this ) );
		super.complete();
		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
	}
	
	public AllowedItemManager getAllowedItemManager() {
		return aim;
		
	}
	
}