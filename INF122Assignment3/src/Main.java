public class Main {
    public static GuildQuest game = GuildQuest.getInstance();

    public static void main(String[] args) {
        System.out.println("Welcome to GuildQuest!");
        game.login();
    }
}
