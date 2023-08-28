package dev.covector.cmcminigames.abs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.UUID;

import dev.covector.cmcminigames.Game;

public abstract class TeamGame extends WinLostGame {
    protected List<Team> teams;
    private HashMap<UUID, Team> playerTeamMap = new HashMap<>();

    public void createTeams(List<Team> teams) {
        this.teams = teams;

        if (gameMeta.spawnLocations.size() < teams.size()) {
            Bukkit.getLogger().warning("Not enough spawn locations for all teams!");
            return;
        }
        
        // count team sizes and record players that arent in any team
        ArrayList<Integer> teamSizes = new ArrayList<>();
        ArrayList<UUID> noTeamPlayers = new ArrayList<>(playerUUIDs);
        int i = 0;
        for (Team team : teams) {
            for (UUID playerUUID : team.members) {
                noTeamPlayers.remove(playerUUID);
                playerTeamMap.put(playerUUID, team);
            }
            teamSizes.add(team.members.size());
            // assign spawn to team
            team.setSpawnLocation(gameMeta.spawnLocations.get(i));
        }

        // distribute the remaining players to teams
        Collections.shuffle(noTeamPlayers);
        for (UUID playerUUID : noTeamPlayers) {
            Team smallestTeam = null;
            for (Team team : teams) {
                if (smallestTeam == null || team.members.size() < smallestTeam.members.size()) {
                    smallestTeam.addMember(playerUUID);
                    playerTeamMap.put(playerUUID, team);
                    break;
                }
            }
        }
    }

    public Team getTeam(Player player) {
        return playerTeamMap.get(player.getUniqueId());
    }

    public Team getTeam(UUID playerUUID) {
        return playerTeamMap.get(playerUUID);
    }

    public class Team {
        public final List<UUID> members = new ArrayList<>();
        public final Color teamColor;
        public final ChatColor teamChatColor;
        public final String teamName;
        public Location spawnLocation;

        public Team(Color teamColor, ChatColor teamChatColor, String teamName) {
            this.teamColor = teamColor;
            this.teamChatColor = teamChatColor;
            this.teamName = teamName;
        }

        public Team(Color teamColor, ChatColor teamChatColor, String teamName, List<UUID> members) {
            this.teamColor = teamColor;
            this.teamChatColor = teamChatColor;
            this.teamName = teamName;
            for (UUID playerUUID : members) {
                addMember(playerUUID);
            }
        }

        public void setSpawnLocation(Location spawnLocation) {
            this.spawnLocation = spawnLocation;
        }

        public void addMember(UUID playerUUID) {
            members.add(playerUUID);
        }

        public boolean isInTeam(UUID playerUUID) {
            return members.contains(playerUUID);
        }

        public boolean isInTeam(Player player) {
            return members.contains(player.getUniqueId());
        }

        public void teleportToSpawn(Player player) {
            player.teleport(spawnLocation);
        }

        public void teleportAllToSpawn() {
            for (UUID playerUUID : members) {
                Player player = Bukkit.getPlayer(playerUUID);
                if (player != null) {
                    player.teleport(spawnLocation);
                }
            }
        }
    }
}