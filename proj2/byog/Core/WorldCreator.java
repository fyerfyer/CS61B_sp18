package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;
import java.util.Random;


public class WorldCreator extends RectangleHelper {

    /** Start by filling world with walls like this:
     * 0 0 0 0 0
     * 0 1 0 1 0
     * 0 0 0 0 0
     * 0 1 0 1 0
     * 0 0 0 0 0
     */
    private static void fillWithWalls(TETile[][] world) {

        // Print rows with walls and none.
        for (int i = 1; i < world[0].length; i += 2) {
            for (int j = 0; j < world.length; j += 2) {
                world[j][i] = Tileset.WALL;
            }
        }

        // Print rows full of walls
        for (int i = 0; i < world[0].length; i += 2) {
            for (int j = 0; j < world.length; j += 1) {
                world[j][i] = Tileset.WALL;
            }

        }
    }

    /** Remove all the dead ends. */
    private static void removeDeadEnds(TETile[][] world) {
        boolean done = false;

        while (!done) {
            done = true;
            for (int i = 0; i < world[0].length; i++) {
                for (int j = 0; j < world.length; j++) {
                    if (world[j][i] != Tileset.FLOOR) {
                        continue;
                    }
                    if (!isOnDeadEnd(new Position(j, i), world)) {
                        continue;
                    }
                    done = false;
                    world[j][i] = Tileset.WALL;
                }
            }
        }
    }

    /** Remove all the inner walls.
     *  When all four corner positions of a wall aren't floor
     *  it's called a inner wall.
     */
    private static void removeInnerWalls(TETile[][] world) {
        for (int i = 1; i < world[0].length - 1; i++) {
            for (int j = 1; j < world.length - 1; j++) {
                if (world[j][i] != Tileset.WALL) {
                    continue;
                }
                if (!isInnerWall(new Position(j, i), world)) {
                    continue;
                }
                world[j][i] = Tileset.NOTHING;
            }
        }
    }

    public static TETile[][] worldGenerator(Random RANDOM, TETile[][] world) {
        fillWithWalls(world);
        List<Room> rooms = Room.roomGenerator(RANDOM, world);
        Hallways.hallwaysGenerator(RANDOM, world);
        for (Room room : rooms) {
            room.convertWall(RANDOM, world);
        }
        removeDeadEnds(world);
        removeInnerWalls(world);

        // Add a door at right edge.
        for (int i = world[0].length - 1; i > 0; i--) {
            if (world[world.length - 2][i].equals(Tileset.FLOOR)) {
                world[world.length - 2][i] = Tileset.LOCKED_DOOR;
                break;
            }
        }
        return world;
    }

    public static void main(String[] args) {
        int width = 81;
        int height = 31;
        int seed = 23456758;
        Random RANDOM = new Random(seed);
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TETile[][] world = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        worldGenerator(RANDOM, world);
        ter.renderFrame(world);
    }
}