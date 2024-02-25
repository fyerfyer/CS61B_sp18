package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Hallways extends RectangleHelper{
    private static Deque<Position> posList = new LinkedList<>();

    private static List<Position> checkPath(Position cur, TETile[][] world) {
        List<Position> suitPos = new LinkedList<>();
        Position[] around = aroundPosition(cur);
        Position[] des = aroundPosition(cur, 2);

        for (int i = 0; i < 4; i += 1) {
            if (!isOnEdge(around[i], world)
                    && !world[around[i].x][around[i].y].equals(Tileset.FLOOR)
                    && world[des[i].x][des[i].y].equals(Tileset.NOTHING)){
                    suitPos.add(des[i]);
                }
            }

        return suitPos;
    }

    // connect room by setting its midpoint to floor and do it recursively.
    // by putting its destination into deque to create another path.
    public static void connectPath(List<Position> SuitPath, Position cur, Random RANDOM, TETile[][] world) {
        int pathNum = RANDOM.nextInt(SuitPath.size());
        Position des = SuitPath.get(pathNum);
        Position mid = cur.midPos(des);
        world[cur.x][cur.y] = Tileset.FLOOR;
        world[mid.x][mid.y] = Tileset.FLOOR;
        world[des.x][des.y] = Tileset.FLOOR;

        posList.addLast(des);
    }

    // assume that the start place is at [][1]
    public static Position randomStart(TETile[][] world) {
        for (int i = 1; i < world.length - 1; i += 2) {
            if (world[i + 1][1] != Tileset.NOTHING || world[i - 1][1] == Tileset.NOTHING) {
                Position pos = new Position(i, 1);
                return pos;
            }
        }

        throw new RuntimeException("There is no suitable start position!");
    }

    // use the BFS-like algorithm to get the needed hallways
    public static void hallwaysGenerator(Random RANDOM, TETile[][] world) {
        Position start = randomStart(world);
        world[start.x][start.y] = Tileset.FLOOR;
        posList.addLast(start);

        while (!posList.isEmpty()) {
            Position cur = posList.getLast();
            List<Position> reach = new LinkedList<>();
            reach = checkPath(cur, world);
            if (!reach.isEmpty()) {
                connectPath(reach, cur, RANDOM, world);
            } else {
                posList.removeLast();
            }
        }
    }
}
