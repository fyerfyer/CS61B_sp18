package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.LinkedList;
import java.util.List;

public class Room extends RectangleHelper {
    private int width;
    private int height;
    private final Position position;

    public Room(int wid, int hei,Position pos) {
        width = wid;
        height = hei;
        position = pos;
    }

    public boolean containPos(Position other) {
        int xx = other.x;
        int yy = other.y;
        return (xx >= position.x && yy >= position.y)
                && (xx <= position.x + width -1 && yy <= position.y + height - 1);
    }

    public boolean isOverlap(Room other) {
        Position[] pArray = new Position[4];
        pArray = cornerPositions(other.width, other.height, other.position);
        for (int i = 0; i< 4; i += 1) {
            if (containPos(pArray[i])) {
                return true;
            }
        }

        return false;
    }

    // width - world.length
    // height - world[0].length
    public void printRoom(TETile[][] world) {
        //print wall of the height
        for (int i = 0; i < height; i += 1) {
            if (position.y + i >= world[0].length - 1) {
                height = i;
                break;
            }
            world[position.x][position.y + i] = Tileset.WALL;

            if (position.x + width - 1 <= world.length - 1) {
                world[position.x + width - 1][position.y + i] = Tileset.WALL;
            } else {
                width = world.length - position.x;
            }
        }

        //print wall of the width
        for (int i = 0; i < width; i += 1) {
            world[position.x + i][position.y] = Tileset.WALL;
            world[position.x + i][position.y + height - 1] = Tileset.WALL;
        }

        for (int i = 1; i < width - 1; i += 1) {
            for (int j = 1; j < height - 1; j += 1) {
                world[position.x + i][position.y + j] = Tileset.FLOOR;
            }
        }
    }

    private static Room randomRoom(Random RANDOM, TETile[][] world) {
        // limit the wid and hei to 7-9
        int wid = (3 + RANDOM.nextInt(2)) * 2 - 1;
        int hei = (3 + RANDOM.nextInt(2)) * 2 - 1;

        // xx - world's width, world length
        int xx = RANDOM.nextInt(world.length - 2);
        int yy = RANDOM.nextInt(world[0].length - 2);
        Position pos = new Position(xx, yy);
        Room newRoom = new Room(wid, hei, pos);
        return newRoom;
    }

    public static void removeOverlap(List<Room> rooms) {
        for (int i = 0; i < rooms.size(); i += 1) {
            for (int j = i + 1; j < rooms.size(); j += 1) {
                Room pre = rooms.get(i);
                Room las = rooms.get(j);

                if (pre.isOverlap(las)) {
                    rooms.remove(j);
                    j -= 1;
                }
            }
        }
    }

    public static List<Room> roomGenerator(Random RANDOM, TETile[][] world) {
        int roomNum = 50 + RANDOM.nextInt(50);
        List<Room> rooms = new LinkedList<>();
        for (int i = 0; i < roomNum; i += 1) {
            Room room = randomRoom(RANDOM, world);
            rooms.add(room);
        }

        removeOverlap(rooms);
        for (Room room : rooms) {
            room.printRoom(world);
        }
        return rooms;
    }

    // create position on each edge of the given room
    private Position[] randomPosition(Random RANDOM) {
        int widpos = RANDOM.nextInt(width - 3) + 1;
        int heipos = RANDOM.nextInt(height - 3) + 1;
        int widneg = - (RANDOM.nextInt(width - 3) + 1);
        int heineg = - (RANDOM.nextInt(height - 3) + 1);

        Position[] pArray = new Position[4];
        pArray[0] = new Position(position.x + widpos, position.y);
        pArray[1] = new Position(position.x, position.y + heipos);
        pArray[2] = new Position(position.x + width - 1 + widneg, position.y + height - 1);
        pArray[3] = new Position(position.x + width - 1, position.y + height - 1 + heineg);

        return pArray;
    }

    // convert some specific wall into floor, to create corridor in the future.
    public void convertWall(Random RANDOM, TETile[][] world) {
        Position[] pArray = new Position[4];
        pArray = randomPosition(RANDOM);
        for (int i = 0; i < 4; i += 1) {
            int roomNum = RANDOM.nextInt(4);
            if (!isOnEdge(pArray[roomNum], world) && !isOnDeadEnd(pArray[roomNum], world)) {
                Position pos = pArray[roomNum];
                world[pos.x][pos.y] = Tileset.FLOOR;
            }
        }
    }
}
