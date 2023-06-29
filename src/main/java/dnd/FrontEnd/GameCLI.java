package dnd.FrontEnd;

import java.util.ArrayList;
import java.util.List;

import dnd.Backend.GameBoard;
import dnd.Backend.Player;
import dnd.Backend.Unit;

public class GameCLI {
    private List<String> messages;

    public GameCLI() {
        messages = new ArrayList<>();
    }

    public void renderPlayerBar(Player player) {
        System.out.println(player.getDescription());
    }

    public void renderEnemyBar(Unit enemy) {
        if (enemy != null)
            System.out.println(enemy.getDescription());
        else
            System.out.println();
    }

    public void renderBoard(GameBoard board) {
        System.out.println("\n" + board.toString());
    }

    public void render(String output) {
        System.out.print(output);
    }

    public void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // For Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For Linux and macOS
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
        }
    }

    public void printEndScreen() {
        clearConsole();
        System.out.println("\r\n" + //
                "   _____                            _         _       _   _                 _ \r\n" + //
                "  / ____|                          | |       | |     | | (_)               | |\r\n" + //
                " | |     ___  _ __   __ _ _ __ __ _| |_ _   _| | __ _| |_ _  ___  _ __  ___| |\r\n" + //
                " | |    / _ \\| '_ \\ / _` | '__/ _` | __| | | | |/ _` | __| |/ _ \\| '_ \\/ __| |\r\n" + //
                " | |___| (_) | | | | (_| | | | (_| | |_| |_| | | (_| | |_| | (_) | | | \\__ \\_|\r\n" + //
                "  \\_____\\___/|_| |_|\\__, |_|  \\__,_|\\__|\\__,_|_|\\__,_|\\__|_|\\___/|_| |_|___(_)\r\n" + //
                "                     __/ |                                                    \r\n" + //
                "                    |___/                                                     \r\n" + //
                "");
        System.out.println("\nYou have reached the end of the dungeon!");
    }

    public void printOpeningScreen(List<Player> characters) {
        int index = 1;
        StringBuilder selectionString = new StringBuilder("Welcome to Dungeons & Dragons!\n");
        for (Player c : characters) {
            selectionString.append(index + "\t" + c.getInfo() + '\n');
            index++;
        }
        selectionString.append("\n");
        selectionString.append(popMessages());
        selectionString.append("Choose your character: ");
        System.out.print(selectionString.toString());
    }

    public void printMessages() {
        for (String message : messages) {
            System.out.println(message);
        }
        messages.clear();
    }

    private String popMessages() {
        StringBuilder messageString = new StringBuilder();
        for (String message : messages) {
            messageString.append(message + '\n');
        }
        messages.clear();
        return messageString.toString();
    }

    public void addMessage(String message) {
        messages.add(message);
    }
}
