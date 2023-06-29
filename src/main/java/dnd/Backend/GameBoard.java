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

    public boolean parseLevel() {
        String map = readMap(level);
        if (map == null)
            return false;

        parseLevel(map);
        return true;
    }

    public void parseLevel(String map) {
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

    private String readMap(int level) {
        StringBuilder text = new StringBuilder();
        String path = System.getProperty("user.dir") + "\\src\\main\\java\\dnd\\levels_dir\\level" + level + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
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
        return parseLevel();
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
