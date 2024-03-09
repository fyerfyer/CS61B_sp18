import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat, double destlon, double destlat) {
        // FIXME
        long stNode = g.closest(stlon, stlat);
        long destNode = g.closest(destlon, destlat);
        Map<Long, Long> edgeTo = new HashMap<>();
        Set<Long> isVisit = new HashSet<>();

        PriorityQueue<Long> pq = new PriorityQueue<>(g.getNodeComparator());
        for (long node : g.vertices()) {
            g.changeDistTo(node, Double.POSITIVE_INFINITY);
        }

        g.changeDistTo(stNode, 0);
        pq.add(stNode);

        while (!pq.isEmpty()) {
            long v =pq.poll();
            if (isVisit.contains(v)) continue;
            if (v == destNode) break;
            isVisit.add(v);
            for (long w : g.adjacent(v)) {
                //dijkstra
                if (g.getDistTo(v) + g.distance(v, w) < g.getDistTo(w)) {
                    g.changeDistTo(w, g.getDistTo(v) + g.distance(v, w));
                }

                g.changePriority(w, g.getDistTo(w) + g.distance(v, w));
                pq.add(w);
                edgeTo.put(v, w);
            }
        }

        //record the path
        List<Long> path = new ArrayList<>();
        path.add(destNode);
        while (destNode != stNode) {
            if (edgeTo.get(destNode) == null) {
                return new ArrayList<>();
            }

            path.add(0, edgeTo.get(destNode));
            destNode = edgeTo.get(destNode);
        }

        //clean
        for (Long node : g.vertices()) {
            g.changePriority(node, 0);
        }

        return path;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
       List<NavigationDirection> dir = new ArrayList<>();
       NavigationDirection cur = new NavigationDirection();
       cur.direction = NavigationDirection.START;
       cur.way = getWayName(g, route.get(0), route.get(1));
       cur.distance = g.distance(route.get(0), route.get(1));

       for (int i = 1, j = 2; j < route.size(); i += 1, j += 1) {
           //if two paths have different name, then turning is needed.
           if (!getWayName(g, route.get(i), route.get(j)).equals(cur.way)) {
               dir.add(cur);
               cur = new NavigationDirection();
               cur.way = getWayName(g, route.get(i), route.get(j));

               double prevBearing = g.bearing(route.get(i - 1), route.get(i));
               double nowBearing = g.bearing(route.get(i), route.get(j));
               cur.direction = convertBearingToDirection(prevBearing, nowBearing);

               cur.distance = g.distance(route.get(i), route.get(j));
               continue;
           }

           cur.distance = g.distance(route.get(i), route.get(j));
       }
       dir.add(cur);
       return dir;
    }

    private static String getWayName(GraphDB g, long node1, long node2) {
        String noName = "";

        List<Long> ways1 = g.getWay(node1);
        List<Long> ways2 = g.getWay(node2);

        // intersection
        List<Long> intersection =
                ways1.stream().filter(ways2::contains).collect(Collectors.toList());

        if (!intersection.isEmpty()) {
            if (g.getWayName(intersection.get(0)) == null) {
                return noName;
            } else {
                return g.getWayName(intersection.get(0));
            }
        }

        return noName;
    }

    private static int convertBearingToDirection(double prevBearing, double curBearing) {
        double relativeBearing = curBearing - prevBearing;
        if (relativeBearing > 180) {
            relativeBearing -= 360;
        } else if (relativeBearing < -180) {
            relativeBearing += 360;
        }

        if (relativeBearing < -100) {
            return NavigationDirection.SHARP_LEFT;
        } else if (relativeBearing < -30) {
            return NavigationDirection.LEFT;
        } else if (relativeBearing < -15) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (relativeBearing < 15) {
            return NavigationDirection.STRAIGHT;
        } else if (relativeBearing < 30) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (relativeBearing < 100) {
            return NavigationDirection.RIGHT;
        } else {
            return NavigationDirection.SHARP_RIGHT;
        }
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
