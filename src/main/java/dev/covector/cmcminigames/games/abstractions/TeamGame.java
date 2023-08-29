package dev.covector.cmcminigames.games.abstractions;

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

import dev.covector.cmcminigames.DebugLogger;

public abstract class TeamGame extends WinLostGame {
    protected List<Team> teams;
    private HashMap<UUID, Team> playerTeamMap = new HashMap<>();

    public void createTeams(List<Team> teams) {
        this.teams = teams;

        if (mapInfo.spawnLocations.size() < teams.size()) {
            Bukkit.getLogger().warning("Not enough spawn locations for all teams!");
            return;
        }
        
        // count team sizes and record players that arent in any team
        ArrayList<Integer> teamSizes = new ArrayList<>();
        ArrayList<Team> IndTeamMap = new ArrayList<>();
        ArrayList<UUID> noTeamPlayers = new ArrayList<>(playerUUIDs);
        int i = 0;
        for (Team team : teams) {
            for (UUID playerUUID : team.members) {
                noTeamPlayers.remove(playerUUID);
                playerTeamMap.put(playerUUID, team);
                if (DebugLogger.willLog(2))
                    DebugLogger.log("Player " + Bukkit.getOfflinePlayer(playerUUID).getName() + " is in team " + team.teamName, 2);
            }
            IndTeamMap.add(team);
            teamSizes.add(team.members.size());
            // assign spawn to team
            team.setSpawnLocation(mapInfo.spawnLocations.get(i));
        }
        if (DebugLogger.willLog(1)) {
            for (int j = 0; j < teams.size(); j++) {
                DebugLogger.log("Team " + teams.get(j).teamName + " has " + String.valueOf(teamSizes.get(j)) + " players", 1);
            }
            for (UUID playerUUID : noTeamPlayers) {
                DebugLogger.log("Player " + Bukkit.getOfflinePlayer(playerUUID).getName() + " is not in a team", 1);
            }
        }

        // distribute the remaining players to teams
        Collections.shuffle(noTeamPlayers);
        for (UUID playerUUID : noTeamPlayers) {
            int smallestTeam = 0;
            for (int j = 1; j < teamSizes.size(); j++) {
                if (teamSizes.get(j) < teamSizes.get(smallestTeam)) {
                    smallestTeam = j;
                }
            }
            IndTeamMap.get(smallestTeam).addMember(playerUUID);
            playerTeamMap.put(playerUUID, IndTeamMap.get(smallestTeam));
            if (DebugLogger.willLog(2))
                DebugLogger.log("Player " + Bukkit.getOfflinePlayer(playerUUID).getName() + " is in team " + IndTeamMap.get(smallestTeam).teamName, 2);
        }

        
        if (DebugLogger.willLog(1)) {
            DebugLogger.log("Final team allocation: ", 1);
            for (Team team : teams) {
                DebugLogger.log("Team " + team.teamName + " has " + String.valueOf(team.members.size()) + " players", 1);
                if (DebugLogger.willLog(2)) {
                    for (UUID playerUUID : team.members) {
                        DebugLogger.log("Player " + Bukkit.getOfflinePlayer(playerUUID).getName() + " is in team " + team.teamName, 2);
                    }
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