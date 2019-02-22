package de.danielr1996.geojson;// License: GPL. For details, see LICENSE file.

import de.danielr1996.geojson.geojson.Coordinates;
import org.geojson.LngLatAlt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;


/**
 * Class HgtReader reads data from SRTM HGT files. Currently this class is restricted to a resolution of 3 arc seconds.
 * <p>
 * SRTM data files are available at the <a href="http://dds.cr.usgs.gov/srtm/version2_1/SRTM3">NASA SRTM site</a>
 *
 * @author Oliver Wieland &lt;oliver.wieland@online.de&gt;
 */
public class HgtReader {
    private static final int SECONDS_PER_MINUTE = 60;

    private static final String HGT_EXT = ".hgt";

    // alter these values for different SRTM resolutions
    private static final int HGT_RES = 3; // resolution in arc seconds
    private static final int HGT_ROW_LENGTH = 1201; // number of elevation values per line
    private static final int HGT_VOID = -32768; // magic number which indicates 'void data' in HGT file

    private final HashMap<String, ShortBuffer> cache = new HashMap<>();

    public LngLatAlt getElevationFromHgt(LngLatAlt coor1) {
        LngLatAlt coor = new LngLatAlt();
//        coor.setLongitude(coor1.getLatitude());
//        coor.setLatitude(coor1.getLongitude());
        coor = coor1;
//        System.out.println(coor);
        try {
            String file = getHgtFileName(coor);
            // given area in cache?
            if (!cache.containsKey(file)) {

                // fill initial cache value. If no file is found, then
                // we use it as a marker to indicate 'file has been searched
                // but is not there'
                cache.put(file, null);
                // Try all resource directories
//                for (String location : Main.pref.getAllPossiblePreferenceDirs()) {
                String fullPath = new File("C:\\Users\\Daniel\\Documents\\Entwicklung\\open-elevation\\data\\dds.cr.usgs.gov\\srtm\\version2_1\\SRTM3\\Eurasia", file).getPath();
                File f = new File(fullPath);
                if (f.exists()) {
                    // found something: read HGT file...
                    ShortBuffer data = readHgtFile(fullPath);
                    // ... and store result in cache
                    cache.put(file, data);
                }
//                }
            }

            // read elevation value
//            System.out.println(cache);
            coor.setAltitude(readElevation(coor));
            return coor;
        } catch (FileNotFoundException e) {
//            System.err.println("Get elevation from HGT " + coor + " failed: => " + e.getMessage());
            // no problem... file not there
            coor.setAltitude(Double.MIN_VALUE);
            return coor;
        } catch (Exception ioe) {
            // oops...
//            ioe.printStackTrace(System.err);
            // fallback
            coor.setAltitude(Double.MIN_VALUE);
            return coor1;
        }
    }

    @SuppressWarnings("resource")
    private ShortBuffer readHgtFile(String file) throws Exception {
        FileChannel fc = null;
        ShortBuffer sb = null;
        try {
            // Eclipse complains here about resource leak on 'fc' - even with 'finally' clause???
            fc = new FileInputStream(file).getChannel();
            // choose the right endianness

            ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());
            while (bb.remaining() > 0) fc.read(bb);

            bb.flip();
            //sb = bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
            sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        } finally {
            if (fc != null) fc.close();
        }

        return sb;
    }

    /**
     * Reads the elevation value for the given coordinate.
     * <p>
     * See also <a href="http://gis.stackexchange.com/questions/43743/how-to-extract-elevation-from-hgt-file">stackexchange.com</a>
     *
     * @param coor the coordinate to get the elevation data for
     * @return the elevation value or <code>Double.NaN</code>, if no value is present
     */
    private double readElevation(LngLatAlt coor) {
        String tag = getHgtFileName(coor);

        ShortBuffer sb = cache.get(tag);

        if (sb == null) {
//            System.err.println("ShortBuffer is null");
        }

        // see http://gis.stackexchange.com/questions/43743/how-to-extract-elevation-from-hgt-file
        double fLat = frac(coor.getLatitude()) * SECONDS_PER_MINUTE;
        double fLon = frac(coor.getLongitude()) * SECONDS_PER_MINUTE;

        // compute offset within HGT file
        int row = (int) Math.round(fLat * SECONDS_PER_MINUTE / HGT_RES);
        int col = (int) Math.round(fLon * SECONDS_PER_MINUTE / HGT_RES);

        row = HGT_ROW_LENGTH - row;
        int cell = (HGT_ROW_LENGTH * (row - 1)) + col;

        //System.out.println("Read SRTM elevation data from row/col/cell " + row + "," + col + ", " + cell + ", " + sb.limit());

        // valid position in buffer?
        if (cell < sb.limit()) {
            short ele = sb.get(cell);
            //System.out.println("==> Read SRTM elevation data from row/col/cell " + row + "," + col + ", " + cell + " = " + ele);
            // check for data voids
            if (ele == HGT_VOID) {
//                System.err.println("Height is void");
                return Double.MIN_VALUE;
            } else {
                return ele;
            }
        } else {
//            System.err.println("Else");
            return Double.MIN_VALUE;
        }
    }

    /**
     * Gets the associated HGT file name for the given way point. Usually the
     * format is <tt>[N|S]nn[W|E]mmm.hgt</tt> where <i>nn</i> is the integral latitude
     * without decimals and <i>mmm</i> is the longitude.
     *
     * @param latLon the coordinate to get the filename for
     * @return the file name of the HGT file
     */
    private String getHgtFileName(LngLatAlt latLon) {
        int lat = (int) latLon.getLatitude();
        int lon = (int) latLon.getLongitude();

        String latPref = "N";
        if (lat < 0) latPref = "S";

        String lonPref = "E";
        if (lon < 0) {
            lonPref = "W";
        }

        return String.format("%s%02d%s%03d%s", latPref, lat, lonPref, lon, HGT_EXT);
    }

    private static double frac(double d) {
        long iPart;
        double fPart;

        // Get user input
        iPart = (long) d;
        fPart = d - iPart;
        return fPart;
    }
}