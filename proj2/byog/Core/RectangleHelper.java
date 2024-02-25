package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class RectangleHelper {
    /*
    func1:
    0.....1
    .     .
    .     .
    .     .
    2.....3
     */
    public static Position[] cornerPositions(int width, int height, Position pos) {
        Position[] pArray = new Position[4];
        pArray[0] = new Position(pos.x, pos.y);
        pArray[1] = new Position(pos.x, pos.y + height);
        pArray[2] = new Position(pos.x + width, pos.y);
        pArray[3] = new Position(pos.x + width, pos.y + height);
        return pArray;
    }

    /*
    func2:
      1
    0 * 2
      3
     */
    public static Position[] aroundPosition(Position pos) {
       Position[] pArray = new Position[4];
       pArray[0] = new Position(pos.x, pos.y - 1);
       pArray[1] = new Position(pos.x - 1, pos.y);
       pArray[2] = new Position(pos.x, pos.y + 1);
       pArray[3] = new Position(pos.x + 1, pos.y);

       return pArray;
    }

    public static Position[] aroundPosition(Position pos, int dis) {
        Position[] pArray = new Position[4];
        pArray[0] = new Position(pos.x, pos.y - dis);
        pArray[1] = new Position(pos.x - dis, pos.y);
        pArray[2] = new Position(pos.x, pos.y + dis);
        pArray[3] = new Position(pos.x + dis, pos.y);

        return pArray;
    }

    // judge if a tile is located on the edge of the world map
    public static boolean isOnEdge(Position pos, TETile[][] world) {
        if (pos.x == 0) {
            return true;
        } else if (pos.y == 0) {
            return true;
        } else if (pos.x == world.length - 1) {
            return true;
        } else if (pos.y == world[0].length - 1) {
            return true;
        }

        return false;
    }

    // judge if a place is a DeadEnd, particularly for the remove of the room's wall
    // ensure that the room is exactly a rectangle
    public static boolean isOnDeadEnd(Position pos, TETile[][] world) {
        Position[] pArray = new Position[4];
        pArray = aroundPosition(pos);
        int cnt = 0;
        for (int i = 0; i < 4; i += 1) {
            if (world[pArray[i].x][pArray[i].y] == Tileset.FLOOR) {
                cnt += 1;
            }
        }

        return cnt == 1;
    }

    public static boolean isInnerWall(Position pos, TETile[][] world) {
        Position[] pArray = new Position[4];
        pArray = aroundPosition(pos);
        for (int i = 0; i < 4; i += 1) {
            if (world[pArray[i].x][pArray[i].y] == Tileset.FLOOR) {
                return false;
            }
        }

        return true;
    }
}
