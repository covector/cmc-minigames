package dev.covector.cmcminigames.abs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Color;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import dev.covector.cmcminigames.Game;

public abstract class TeamGame extends WinLostGame {
    protected List<Team> teams;
    private HashMap<Player, Team> playerTeamMap = new HashMap<>();

    public void createTeams(List<Team> teams) {
        this.teams = teams;
        
        // count team sizes and record players that arent in any team
        ArrayList<Integer> teamSizes = new ArrayList<>();
        ArrayList<Player> noTeamPlayers = new ArrayList<>(players);
        for (Team team : teams) {
            for (Player player : team.members) {
                noTeamPlayers.remove(player);
                playerTeamMap.put(player, team);
            }
            teamSizes.add(team.members.size());
        }

        // distribute the remaining players to teams
        for (Player player : noTeamPlayers) {
            Team smallestTeam = null;
            for (Team team : teams) {
                if (smallestTeam == null || team.members.size() < smallestTeam.members.size()) {
                    smallestTeam.addPlayer(player);
                    playerTeamMap.put(player, team);
                    break;
                }
            }
        }
    }

    public Team getTeam(Player player) {
        return playerTeamMap.get(player);
    }

    public class Team {
        public final List<Player> members = new ArrayList<>();
        public final Color teamColor;
        public final ChatColor teamChatColor;
        public final String teamName;

        public Team(Color teamColor, ChatColor teamChatColor, String teamName) {
            this.teamColor = teamColor;
            this.teamChatColor = teamChatColor;
            this.teamName = teamName;
        }

        public Team(Color teamColor, ChatColor teamChatColor, String teamName, List<Player> members) {
            this.teamColor = teamColor;
            this.teamChatColor = teamChatColor;
            this.teamName = teamName;
            for (Player player : members) {
                addPlayer(player);
            }
        }

        public void addPlayer(Player player) {
            members.add(player);
        }

        public boolean isInTeam(Player player) {
            return members.contains(player);
        }
    }
}