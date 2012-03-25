package com.github.nerdshark.factions;

// bukkit API imports
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

// Massivecraft Factions API imports
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class EnemyKillBonus extends JavaPlugin implements Listener {	
	private int multiplier;
	
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		this.multiplier = this.getConfig().getInt("config.experience_multiplier");
		pm.registerEvents(this, this);		
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {		
		Player player = event.getEntity();
		Player killer = player.getKiller();
		
		if (!(killer instanceof Player)) return;
		
		FPlayer fkiller = FPlayers.i.get(killer);
		FPlayer fplayer = FPlayers.i.get(player);
						
		if (fplayer.hasFaction() && fkiller.hasFaction() &&
				fplayer.getFaction().getRelationTo(fkiller).isEnemy()) {
			if(this.getConfig().getBoolean("config.on_kill.multiply_experience")) {
				event.setDroppedExp(event.getDroppedExp() * this.multiplier);
			}
			if(this.getConfig().getBoolean("config.on_kill.drop_gold")) {
				event.getDrops().add(new ItemStack(Material.GOLD_INGOT, this.getConfig().getInt("config.gold_amount")));
			}			
		}		
		return;
	}	
}
