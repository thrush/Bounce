/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.logic;

import java.math.BigDecimal;

/**
 * Provides methods and constants for converting between various units.
 */
public class Converter
{
    /** The number of pixels per meter */
    public static final int SCALE_PIXELS_PER_METER = 50;

    /** The abbreviation for meter */
    public static final String ABBREV_METER = "m";

    /**
     * Converts the specified value from meters to pixels based on a preset
     * scale.  If there is a fraction remaining, it will be rounded to the
     * nearest pixel, with fractions >= 0.5 being rounded up.
     *
     * @param meters the value to be converted (in meters)
     * @return the number of pixels
     */
    public static int toPixels(double meters)
    {
        BigDecimal bd = new BigDecimal(meters * SCALE_PIXELS_PER_METER);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return bd.intValue();
    }

    /**
     * Converts the specified value from pixels to meters based on a preset
     * scale.
     *
     * @param pixels the value to be converted (in pixels)
     * @return the number of meters
     */
    public static double toMeters(int pixels)
    {
        return pixels / (SCALE_PIXELS_PER_METER * 1d);
    }

    /**
     * Gets the position on the Y axis at which an object is to be painted.
     * This is necessary since when thinking of an object's height in meters,
     * zero is the ground, whereas when the object is painted, zero is at the
     * top of the window.
     *
     * @param y the distance of the object from the ground in meters
     * @param diameter the diameter (or height, if not a circle) in meters
     * @param windowHeight the height of the drawing space in pixels
     * @return the Y position at which the object should be painted
     */
    public static int invertYValue(double y, double diameter, int windowHeight)
    {
        return windowHeight - toPixels(y + diameter);
    }
}