package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.List;
import java.util.Random;


public class WorldCreator extends RectangleHelper {
    /*
        fill the world like this:
        w w w w w w w
        w 0 w 0 w 0 w
        w w w w w w w
        w 0 w 0 w 0 w
        w w w w w w w
         */
    //width : world.length
    //height : world[0].length
    private static void filledWithWalls(TETile[][] world) {
        for (int i = 0; i < world.length; i += 2) {
            for (int j = 0; j < world[0].length; j += 1) {
                world[i][j] = Tileset.WALL;
            }
        }

        for (int i = 1; i < world.length - 1; i += 2) {
            for (int j = 0; j < world[0].length; j += 2) {
                world[i][j] = Tileset.WALL;
            }
        }
    }

    /** Remove all the dead ends. */
    private static void removeDeadEnds(TETile[][] world) {
        boolean flag = false;

        while (!flag) {
            flag = true;
            for (int i = 0; i < world.length; i += 1) {
                for (int j = 0; j < world[0].length; j += 1) {
                    if (world[i][j] != Tileset.FLOOR) {
                        continue;
                    } else {
                        Position pos = new Position(i,j);
                        if (!isOnDeadEnd(pos, world)) {
                            continue;
                        }
                    }

                    world[i][j] = Tileset.NOTHING;
                    flag = false;
                }
            }
        }
    }

    private static void removeInnerWall(TETile[][] world) {
        for (int i = 1; i < world.length - 1; i += 1) {
            for (int j = 1; j < world[0].length - 1; j += 1) {
                if (world[i][j] != Tileset.WALL) {
                    continue;
                }
                Position pos = new Position(i,j);
                if (!isInnerWall(pos, world)) {
                    continue;
                }
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static TETile[][] worldGenerator(Random RANDOM, TETile[][] world) {
        filledWithWalls(world);
        List<Room> rooms = Room.roomGenerator(RANDOM, world);
        Hallways.hallwaysGenerator(RANDOM, world);
        for (Room room : rooms) {
            room.convertWall(RANDOM, world);
        }
        removeDeadEnds(world);
        removeInnerWall(world);

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