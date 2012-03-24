package com.github.nerdshark.factions;

// bukkit API imports
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.configuration.file.FileConfiguration;

// Massivecraft Factions API imports
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class EnemyKillBonusPlugin extends JavaPlugin implements Listener {	
	private int multiplier;
	
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		this.multiplier = this.getConfig().getInt("config.enemy_kill_experience_multiplier");
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
			event.setDroppedExp(event.getDroppedExp() * this.multiplier);			
			killer.sendMessage(new String[]{"You killed a member of an enemy faction, experience dropped is multiplied by ", Integer.toString(this.multiplier)});
		}		
		return;
	}	
}
