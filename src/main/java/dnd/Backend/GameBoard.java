package dnd.Backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import dnd.Backend.GameTile.TileType;

public class GameBoard {

    private GameTile[][] currentBoard;
    private static GameTile[][] nextBoard;

    private Player player;
    private CharacterFactory cf;
    private GameTile playerTile;
    private int level;

    private final char EMPTY = '.';
    private final char WALL = '#';
    private final char PLAYER = '@';

    public GameBoard(CharacterFactory _cf, Player _player) {
        cf = _cf;
        player = _player;
        level = 1;
    }

    public double getRange(Position2D pos1, Position2D pos2) {
        assert pos1 != null && pos2 != null : "Cannot calculate range with a null: " + pos1 + ", " + pos2;
        return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
    }

    public void parseLevel() {
        String[] levelString = readMap(level).split("\n");
        int rows = levelString.length;
        int cols = levelString[0].length();
        nextBoard = new GameTile[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                char tileChar = levelString[y].charAt(x);
                Position2D pos = new Position2D(x, y);
                setTileAt(createGameTile(tileChar, pos), pos);
            }
        }
        currentBoard = nextBoard;
    }

    public void parseLevelTest(String map) {
        String[] levelString = map.split("\n");
        int rows = levelString.length;
        int cols = levelString[0].length();
        nextBoard = new GameTile[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                char tileChar = levelString[y].charAt(x);
                Position2D pos = new Position2D(x, y);
                setTileAt(createGameTile(tileChar, pos), pos);
            }
        }
        currentBoard = nextBoard;
    }

    public void tick() {
        for (GameTile[] row : currentBoard) {
            for (GameTile tile : row) {
                if (tile != null && tile.getUnit() != null)
                    tile.getUnit().tick();
            }
        }
        currentBoard = nextBoard;
    }

    private GameTile createGameTile(char tileChar, Position2D pos) {
        switch (tileChar) {
            case EMPTY:
                return null;
            case WALL:
                return new GameTile(TileType.WALL, null);
            case PLAYER:
                player.setPosition(pos);
                playerTile = player.getGameTile();
                return playerTile;
            default:
                Enemy e = cf.createEnemy(tileChar, pos);
                GameManager.addEnemy(e);
                return e.getGameTile();
        }
    }

    private String readMap(int level) {
        StringBuilder text = new StringBuilder();
        String path = System.getProperty("user.dir") + "\\levels_dir\\level" + level + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            text.append("An error occurred while uploading the text: " + e.getMessage());
        }
        return text.toString();
    }

    public boolean move(GameTile tile, Position2D pos) {
        if (getTileAt(pos) != null)
            return false;

        Position2D oldPos = tile.getPosition();
        setTileAt(tile, pos);
        setTileAt(null, oldPos);
        tile.setPosition(pos);
        return true;
    }

    public boolean advanceLevel() {
        level++;
        try {
            parseLevel();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public GameTile getTileAt(Position2D pos) {
        return nextBoard[pos.y][pos.x];
    }

    public void setTileAt(GameTile tile, Position2D pos) {
        nextBoard[pos.y][pos.x] = tile;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (GameTile[] row : currentBoard) {
            for (GameTile tile : row) {
                s.append(tile == null ? EMPTY : tile.getUnit() == null ? WALL : tile.toString());
            }
            s.append("\n");
        }
        return s.toString();
    }
}
