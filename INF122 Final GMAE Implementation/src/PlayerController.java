import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerController {
    private List<Player> players;

    public PlayerController(){
        this.players = new ArrayList<Player>();
    }

    public void createPlayers(){
        Scanner scanner = new Scanner(System.in);
        System.out.printf("""
                Creating player 1...
                Please enter your name: 
                """);
        addPlayer(scanner.nextLine());
        System.out.printf("""
                Creating player 2...
                Please enter your name: """);
        addPlayer(scanner.nextLine());

    }

    public void addPlayer(String name){
        players.add(new Player(name));
    }

    //find player based on name
    public Player findPlayer(String name){
        for  (Player player : players) {
            if(player.getName().equals(name)) { return player; }
        }
        return null;
    }

    //find player based on player number (ie player 1 or player 2)
    public Player findPlayer(int number){
        return players.get(number - 1);
    }
}
