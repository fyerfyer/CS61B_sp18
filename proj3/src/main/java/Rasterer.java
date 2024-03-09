import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static double[] depthLonDPP = new double[8];
    private static final double initLRLON = MapServer.ROOT_LRLON;
    private static final double initLRLAT = MapServer.ROOT_LRLAT;
    private static final double initULLON = MapServer.ROOT_ULLON;
    private static final double initULLAT = MapServer.ROOT_ULLAT;

    static {
        depthLonDPP[0] = (initLRLON - initULLON) / MapServer.TILE_SIZE;
        for (int i = 1; i < 8; i += 1) {
            depthLonDPP[i] = depthLonDPP[i - 1] / 2;
        }
    }

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> result = new HashMap<>();
        double neededULLON = params.get("ullon");
        double neededULLAT = params.get("ullat");
        double neededLRLON = params.get("lrlon");
        double neededLRLAT = params.get("lrlat");

        if (neededULLON < initULLON || neededULLAT > initULLAT || neededLRLON > initLRLON || neededLRLAT < initLRLAT) {
            result.put("render_grid", null);
            result.put("raster_ul_lon", 0);
            result.put("raster_ul_lat", 0);
            result.put("raster_lr_lon", 0);
            result.put("raster_lr_lat", 0);
            result.put("depth", 0);
            result.put("query_success", false);

            return result;
        }

        double neededLonDPP = (neededLRLON - neededULLON) / params.get("w");
        int neededDepth = getDepth(neededLonDPP);
        double maxTILE = Math.pow(2, neededDepth);

        int xLeft = 0, xRight = 0, yLeft = 0, yRight = 0;
        double xDiff = (initLRLON - initULLON) / maxTILE;
        double yDiff = (initULLAT - initLRLAT) / maxTILE;

        for (double init = initULLON; init <= neededULLON; init += xDiff) {
            xLeft += 1;
        }

        for (double init = initULLON; init <= neededLRLON; init += xDiff) {
            if (xRight < maxTILE - 1) xRight += 1;
            else break;//has used up all the place
        }

        //from top to bottom!!!
        for (double init = initULLAT; init >= neededULLAT; init -= yDiff) {
            yLeft += 1;
        }

        for (double init = initULLAT; init >= neededLRLAT; init -= yDiff) {
            if (yRight < maxTILE - 1) yRight += 1;
            else break;
        }

        if (xLeft > 0) xLeft -= 1;
        if (xRight > 0) xRight -= 1;
        if (yLeft > 0) yLeft -= 1;
        if (yRight > 0) yRight -= 1;

//        System.out.println(xLeft);
//        System.out.println(xRight);
//        System.out.println(yLeft);
//        System.out.println(yRight);

        String[][] file = new String[yRight - yLeft + 1][xRight - xLeft + 1];
        for (int y = yLeft; y <= yRight; y++) {
            for (int x = xLeft; x <= xRight; x++) {
                file[y - yLeft][x - xLeft] = "d" + neededDepth + "_x" + x + "_y" + y + ".png";
            }
        }

        result.put("render_grid", file);
        result.put("raster_ul_lon", initULLON + xLeft * xDiff);
        result.put("raster_ul_lat", initULLAT - yLeft * yDiff);
        result.put("raster_lr_lon", initULLON + (xRight + 1) * xDiff);
        result.put("raster_lr_lat", initULLAT - (yRight + 1) * yDiff);
        result.put("depth", neededDepth);
        result.put("query_success", true);

        return result;
    }

    private int getDepth(double inputLonDPP) {
        int index = 0;
        while (depthLonDPP[index] > inputLonDPP) {
            index += 1;
            if (index == depthLonDPP.length - 1) break;
        }

        return index;
    }
}
