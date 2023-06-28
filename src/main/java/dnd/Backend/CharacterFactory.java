package dnd.Backend;

import dnd.Backend.PlayerCharacters.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import dnd.Backend.Enemies.*;

public class CharacterFactory {

    public enum PlayerCharacter {
        JON_SNOW(1),
        THE_HOUND(2),
        MELISANDRE(3),
        THOROS_OF_MYR(4),
        ARYA_STARK(5),
        BRONN(6),
        YGRITTE(7);

        private final int value;

        private PlayerCharacter(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum EnemyType {
        BONUS_TRAP('B'),
        QUEENS_TRAP('Q'),
        DEATH_TRAP('D'),
        LANNISTER_SOLDIER('s'),
        LANNISTER_KNIGHT('k'),
        QUEENS_GUARD('q'),
        WRIGHT('z'),
        BEAR_WRIGHT('b'),
        GIANT_WRIGHT('g'),
        WHITE_WALKER('w'),
        THE_MOUNTAIN('M'),
        QUEEN_CERSEI('C'),
        NIGHTS_KING('K');

        private final char value;

        private EnemyType(char value) {
            this.value = value;
        }

        public char getValue() {
            return value;
        }
    }

    private LinkedHashMap<PlayerCharacter, Player> playerCharacters;

    // Enemies char bindings
    private final char BONUS_TRAP = 'B';
    private final char QUEENS_TRAP = 'Q';
    private final char DEATH_TRAP = 'D';
    private final char LANNISTER_SOLDIER = 's';
    private final char LANNISTER_KNIGHT = 'k';
    private final char QUEENS_GUARD = 'q';
    private final char WRIGHT = 'z';
    private final char BEAR_WRIGHT = 'b';
    private final char GIANT_WRIGHT = 'g';
    private final char WHITE_WALKER = 'w';
    private final char THE_MOUNTAIN = 'M';
    private final char QUEEN_CERSEI = 'C';
    private final char NIGHTS_KING = 'K';

    public CharacterFactory() {
        playerCharacters = new LinkedHashMap<PlayerCharacter, Player>() {
            {
                put(PlayerCharacter.JON_SNOW, new Warrior("Jon Snow", 300, 30, 4, 3, new Position2D(0, 0)));
                put(PlayerCharacter.THE_HOUND, new Warrior("The Hound", 400, 20, 6, 5, new Position2D(0, 0)));
                put(PlayerCharacter.MELISANDRE,
                        new Mage("Melisandre", 100, 5, 1, 300, 30, 40, 5, 6, new Position2D(0, 0)));
                put(PlayerCharacter.THOROS_OF_MYR,
                        new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4, new Position2D(0, 0)));
                put(PlayerCharacter.ARYA_STARK, new Rogue("Arya Stark", 150, 40, 2, 20, new Position2D(0, 0)));
                put(PlayerCharacter.BRONN, new Rogue("Bronn    ", 250, 35, 3, 50, new Position2D(0, 0)));
                put(PlayerCharacter.YGRITTE, new Hunter("Ygritte ", 220, 30, 2, 6, new Position2D(0, 0)));
            }
        };
    }

    public Player choosePlayer(PlayerCharacter character) {
        return playerCharacters.get(character);
    }

    public ArrayList<Player> getCharacters() {
        return new ArrayList<Player>(playerCharacters.values());
    }

    public Enemy createEnemy(char tile, Position2D position) {
        switch (tile) {
            case BONUS_TRAP:
                return new Trap("Bonus Trap", BONUS_TRAP, 1, 1, 1, 250, 1, 5, position);
            case QUEENS_TRAP:
                return new Trap("Queen's Trap", QUEENS_TRAP, 250, 50, 10, 1000, 3, 7, position);
            case DEATH_TRAP:
                return new Trap("Death Trap", DEATH_TRAP, 500, 100, 20, 2000, 5, 10, position);
            case LANNISTER_SOLDIER:
                return new Monster("Lannister Soldier", LANNISTER_SOLDIER, 80, 8, 3, 3, 25, position);
            case LANNISTER_KNIGHT:
                return new Monster("Lannister Knight", LANNISTER_KNIGHT, 200, 14, 8, 3, 50, position);
            case QUEENS_GUARD:
                return new Monster("Queen's Guard", QUEENS_GUARD, 400, 20, 15, 5, 100, position);
            case WRIGHT:
                return new Monster("Wright", WRIGHT, 600, 30, 15, 3, 100, position);
            case BEAR_WRIGHT:
                return new Monster("Bear-Wright", BEAR_WRIGHT, 1000, 75, 30, 4, 250, position);
            case GIANT_WRIGHT:
                return new Monster("Giant-Wright", GIANT_WRIGHT, 1500, 100, 40, 4, 500, position);
            case WHITE_WALKER:
                return new Monster("White Walker", WHITE_WALKER, 2000, 150, 50, 6, 1000, position);
            case THE_MOUNTAIN:
                return new Boss("The Mountain", THE_MOUNTAIN, 1000, 60, 25, 500, 6, 5, position);
            case QUEEN_CERSEI:
                return new Boss("Queen Cersei", QUEEN_CERSEI, 100, 10, 10, 1000, 1, 8, position);
            case NIGHTS_KING:
                return new Boss("Night's King", NIGHTS_KING, 5000, 300, 150, 5000, 8, 3, position);
            default:
                throw new IllegalArgumentException("Invalid character tile: " + tile);
        }
    }

}
