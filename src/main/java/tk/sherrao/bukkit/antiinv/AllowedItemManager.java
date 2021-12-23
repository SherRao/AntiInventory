package tk.sherrao.bukkit.antiinv;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import tk.sherrao.bukkit.utils.plugin.SherPluginFeature;
import tk.sherrao.utils.strings.StringMultiJoiner;

public class AllowedItemManager extends SherPluginFeature {

	private final class Lock {}
	private final Object lock = new Lock();
	
	protected File itemsFile;
	protected YamlConfiguration items;
	
	public AllowedItemManager( AntiInventory pl ) {
		super(pl);
		
		this.itemsFile = pl.createFile( "items.yml" );
		this.items = YamlConfiguration.loadConfiguration( itemsFile );
		
	}
	
	public void addItem( ItemStack item ) {
		synchronized( lock ) {
			try {
				String itemInfo = new StringMultiJoiner( "," )
						.add( "type=" + item.getType().toString() )
						.add( "dura=" + item.getDurability() )
						.add( "name=\"" + item.getItemMeta().getDisplayName() + "\"" )
						.add( "lore=\"" + item.getItemMeta().getLore() + "\"" )
						.add( "ench=" + item.getEnchantments() )
						.toString();
				
				List<String> list = items.getStringList( "allowed-items" );
				list.add( itemInfo );
				items.set( "allowed-items", list );
				items.save( itemsFile );
				
			} catch( IOException e ) { log.severe( "Failed to save item: " + item.toString(), e ); }
		}
	}
	
	public boolean isAllowed( ItemStack item ) {
		synchronized( lock ) {
			try {
				String itemInfo = new StringMultiJoiner( "," )
						.add( "type=" + item.getType().toString() )
						.add( "dura=" + item.getDurability() )
						.add( "name=\"" + item.getItemMeta().getDisplayName() + "\"" )
						.add( "lore=\"" + item.getItemMeta().getLore() + "\"" )
						.add( "ench=" + item.getEnchantments() )
						.toString();
			
				for( String str : items.getStringList( "allowed-items" ) )
					if( str.equals( itemInfo ) )
						return true;
			
				return false;
			
			} catch( NullPointerException e ) {
				return false;
				
			}
		}
	}
	
}