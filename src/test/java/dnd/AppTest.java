package dnd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import dnd.Backend.*;
import dnd.Backend.Enemies.*;
import dnd.Backend.Player.StatPerLevel;

import dnd.Backend.PlayerCharacters.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppTest {

    private CharacterFactory characterFactory;

    @Before
    public void setUp() {
        characterFactory = new CharacterFactory();
    }

    @Test
    public void testPlayerCharacterCreation() {
        Player player = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.JON_SNOW);
        assertNotNull(player);
        assertEquals("Jon Snow", player.getName());
        assertEquals(300, player.getMaxHealth());
        assertEquals(30, player.getAttack());
        assertEquals(4, player.getDefense());
    }

    @Test
    public void testEnemyCreation() {
        Enemy enemy = characterFactory.createEnemy(CharacterFactory.EnemyType.WRIGHT.getValue(), new Position2D(0, 0));
        assertNotNull(enemy);
        assertEquals("Wright", enemy.getName());
        assertEquals(600, enemy.getMaxHealth());
        assertEquals(30, enemy.getAttack());
        assertEquals(15, enemy.getDefense());
    }

    @Test
    public void testUnitTakingDamage() {
        Unit unit = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.JON_SNOW);
        int initialHealth = unit.getCurrentHealth();
        int damage = 20;

        int actualDamage = unit.takeDamage(damage);
        int actualHealth = unit.getCurrentHealth();

        assertEquals(actualDamage <= 20, true);
        assertEquals(initialHealth > actualHealth, true);
    }

    @Test
    public void testPlayerLevelUp() {
        Player testWarrior = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.JON_SNOW);
        Player testMage = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.MELISANDRE);
        Player testRogue = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.ARYA_STARK);
        Player testHunter = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.YGRITTE);
        Player[] testPlayers = { testWarrior, testMage, testRogue, testHunter };
        for (Player player : testPlayers) {
            int initialLevel = player.getLevel();
            int initialAttack = player.getAttack();
            int initialDefense = player.getDefense();
            int initialMaxHealth = player.getMaxHealth();
            int initialMaxMana = 0;
            int initialPower = 0;
            int initialArrows = 0;

            if (player.getType() == Player.Type.MAGE) {
                initialMaxMana = ((Mage) player).getMaxMana();
                initialPower = ((Mage) player).getPower();
            }

            if (player.getType() == Player.Type.HUNTER) {
                initialArrows = ((Hunter) player).getArrows();
            }

            HashMap<StatPerLevel, Integer> statsPerLevel = player.getStatsPerLevel();

            player.gainExp(50);
            int newLevel = player.getLevel();

            assertEquals(initialLevel + 1, newLevel);
            assertEquals(
                    initialMaxHealth + newLevel * statsPerLevel.get(StatPerLevel.HEALTH)
                            + newLevel * statsPerLevel.get(StatPerLevel.EXTRA_HEALTH),
                    player.getMaxHealth());
            assertEquals(
                    initialAttack + newLevel * statsPerLevel.get(StatPerLevel.ATTACK)
                            + newLevel * statsPerLevel.get(StatPerLevel.EXTRA_ATK),
                    player.getAttack());
            assertEquals(
                    initialDefense + newLevel * statsPerLevel.get(StatPerLevel.DEFENSE)
                            + newLevel * statsPerLevel.get(StatPerLevel.EXTRA_DEF),
                    player.getDefense());

            if (player.getType() == Player.Type.MAGE) {
                assertEquals(initialMaxMana + newLevel * statsPerLevel.get(StatPerLevel.MANA),
                        ((Mage) player).getMaxMana());
                assertEquals(
                        initialPower + newLevel * statsPerLevel.get(StatPerLevel.POWER),
                        ((Mage) player).getPower());
            }

            if (player.getType() == Player.Type.HUNTER) {
                assertEquals(initialArrows + newLevel * statsPerLevel.get(StatPerLevel.ARROWS),
                        ((Hunter) player).getArrows());
                assertEquals(newLevel * statsPerLevel.get(StatPerLevel.ARROW_REGEN),
                        ((Hunter) player).getArrowRegen());
            }

        }
    }

    @Test
    public void testMageCastAbility() {
        Mage mage = (Mage) characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.MELISANDRE);
        List<Unit> targets = new ArrayList<>();
        targets.add(characterFactory.createEnemy(CharacterFactory.EnemyType.WRIGHT.getValue(), new Position2D(0, 1)));

        Report report = mage.castAbility(targets);

        assertNotNull(report);
        assertEquals(1, report.units.size());
        assertTrue(report.units.contains(targets.get(0)));
    }

    @Test
    public void testRogueCastAbility() {
        Rogue rogue = (Rogue) characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.ARYA_STARK);
        List<Unit> targets = new ArrayList<>();
        targets.add(characterFactory.createEnemy(CharacterFactory.EnemyType.LANNISTER_SOLDIER.getValue(),
                new Position2D(0, 1)));
        targets.add(characterFactory.createEnemy(CharacterFactory.EnemyType.WRIGHT.getValue(), new Position2D(1, 0)));

        Report report = rogue.castAbility(targets);

        assertNotNull(report);
        assertEquals(2, report.units.size());
        assertTrue(report.units.contains(targets.get(0)));
        assertTrue(report.units.contains(targets.get(1)));
    }

    @Test
    public void testBossDie() {
        Boss boss = (Boss) characterFactory.createEnemy(CharacterFactory.EnemyType.NIGHTS_KING.getValue(),
                new Position2D(0, 0));

        boss.takeDamage(boss.getMaxHealth());
        boss.takeDamage(boss.getMaxHealth());
        boolean isDead = boss.isDead();
        assertTrue(isDead);
        assertEquals(0, boss.getCurrentHealth());
    }

    @Test
    public void testGameTileOccupied() {
        Player player = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.JON_SNOW);

        GameTile occupiedTile = new GameTile(GameTile.TileType.PLAYER, player);
        GameTile unoccupiedTile = new GameTile(GameTile.TileType.WALL, null);

        assertEquals(player, occupiedTile.getUnit());
        assertEquals(player.getPosition(), occupiedTile.getPosition());

        assertNull(unoccupiedTile.getUnit());
        assertNull(unoccupiedTile.getPosition());
    }

    @Test
    public void testReportUnits() {
        List<Unit> units = new ArrayList<>();
        units.add(characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.JON_SNOW));
        units.add(characterFactory.createEnemy(CharacterFactory.EnemyType.WRIGHT.getValue(), new Position2D(0, 0)));

        Report report = new Report(units);

        assertEquals(units.size(), report.units.size());
        assertEquals(units.get(0), report.units.get(0));
        assertEquals(units.get(1), report.units.get(1));
    }

    @Test
    public void testEnemyCreationInvalidTile() {
        char invalidTile = 'X';
        Position2D position = new Position2D(0, 0);

        try {
            Enemy enemy = characterFactory.createEnemy(invalidTile, position);
            assertNull(enemy);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid character tile: " + invalidTile, e.getMessage());
        }
    }

    @Test
    public void testUnitTakeDamageInvalidDamage() {
        Unit unit = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.JON_SNOW);
        int initialHealth = unit.getCurrentHealth();

        int invalidDamage = -10;
        int actualDamage = unit.takeDamage(invalidDamage);
        int actualHealth = unit.getCurrentHealth();

        assertEquals(0, actualDamage);
        assertEquals(initialHealth, actualHealth);
    }

    @Test
    public void testMoveIntoBlock() {
        Player player = characterFactory.choosePlayer(CharacterFactory.PlayerCharacter.JON_SNOW);
        GameBoard gameBoard = new GameBoard(characterFactory, player);
        gameBoard.parseLevelTest("@#...\n.....\n.....\n.....\n.....\n");

        // Set up the game tile with a block at position (1, 0)
        GameTile blockTile = new GameTile(GameTile.TileType.WALL, null);
        gameBoard.setTileAt(blockTile, new Position2D(1, 0));

        // Move player into the block
        boolean moveSuccessful = gameBoard.move(player.getGameTile(), new Position2D(1, 0));

        assertFalse(moveSuccessful);
        assertEquals(new Position2D(0, 0), player.getPosition());
    }

}
