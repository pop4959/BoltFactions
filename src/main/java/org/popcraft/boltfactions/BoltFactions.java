package org.popcraft.boltfactions;

import dev.kitteh.factions.FPlayer;
import dev.kitteh.factions.FPlayers;
import dev.kitteh.factions.Faction;
import dev.kitteh.factions.Factions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.bolt.BoltAPI;
import org.popcraft.bolt.source.SourceTypes;

public final class BoltFactions extends JavaPlugin implements Listener {
    private BoltAPI bolt;

    @Override
    public void onEnable() {
        this.bolt = getServer().getServicesManager().load(BoltAPI.class);
        if (bolt == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!getServer().getPluginManager().isPluginEnabled("FactionsUUID")) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        bolt.registerPlayerSourceResolver((source, uuid) -> {
            if (!SourceTypes.FACTION.equals(source.getType())) {
                return false;
            }
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                return false;
            }
            final FPlayer fPlayer = FPlayers.fPlayers().get(player);
            final String factionName = source.getIdentifier();
            final Faction faction = Factions.factions().get(factionName);
            return fPlayer.faction() == faction;
        });
    }

    @Override
    public void onDisable() {
        this.bolt = null;
    }
}
