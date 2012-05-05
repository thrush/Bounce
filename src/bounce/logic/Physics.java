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
 * Provides methods and constants for performing calculations with standard
 * physics formulas.
 */
public class Physics
{
    /**
     * The direction in which an object is moving along a vertical axis.
     */
    public static enum Direction
    {
        UP, DOWN, NONE;
    }

    /** Earth's average gravity in meters per second squared */
    public static final double GRAVITY = 9.8;

    /**
     * Calculates the total displacement that an object should have moved based
     * on the specified time and velocity.  The value is returned in meters
     * rounded to the nearest millimeter (3 decimal places).  The value is
     * calculated using the formula: <br />
     * <br />
     * d = v<sub>i</sub><sup>2</sup>t + &frac12; at<sup>2</sup> <br />
     * <br />
     * where: <br />
     * <br />
     * d = displacement in meters <br />
     * v<sub>i</sub> = the initial velocity <br />
     * a = acceleration (in this case, Earth's average gravity:
     * 9.8 m/s<sup>2</sup>) <br />
     * t = time in seconds <br />
     *
     * @param direction the direction in which the object is travelling
     * @param time the time in seconds
     * @param velocity the initial velocity
     * @return the displacement of the object in meters
     */
    public static double calcDisplacement(
            Direction direction, double time, double velocity)
    {
        double acceleration = 0;

        switch(direction)
        {
            case UP:
            {
                acceleration = -GRAVITY;
                break;
            }
            case DOWN:
            {
                acceleration = GRAVITY;
                break;
            }
            default:
            {
                break;
            }
        }
        
        BigDecimal bd = new BigDecimal(
                (velocity * time) + (0.5d * acceleration * Math.pow(time, 2)));
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);

        return bd.doubleValue();
    }

    /**
     * Calculates the velocity of an object that has fallen from a specified
     * height.  The value is returned in meters per second rounded to the
     * nearest millimeter (3 decimal places).  The value is calculated using the
     * formula: <br />
     * <br />
     * v = &radic;(2ad) <br />
     * <br />
     * where: <br />
     * <br />
     * v = velocity in meters per second <br />
     * a = acceleration (in this case, Earth's average gravity:
     * 9.8 m/s<sup>2</sup>) <br />
     * d = the distance the object has travelled (in this case, the distance
     * fallen) <br />
     *
     * @param height the height in meters
     * @return the velocity of the object in meters per second
     */
    public static double calcVelocity(double height)
    {
        BigDecimal bd = new BigDecimal(Math.sqrt(2d * GRAVITY * height));
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);

        return bd.doubleValue();
    }

    /**
     * Calculates the maximum height of a bounce using acceleration, and initial
     * and final velocities.  The value is returned in meters rounded to the
     * nearest millimeter (3 decimal places).  The value is calculated using the
     * formula: <br />
     * <br />
     * &plusmn;d = (v<sub>f</sub><sup>2</sup> - v<sub>i</sub><sup>2</sup>) /
     * 2g <br />
     * <br />
     * where: <br />
     * <br />
     * d = the distance in meters <br />
     * v<sub>f</sub> = the final velocity in meters per second <br />
     * v<sub>i</sub> = the initial velocity in meters per second <br />
     * g = Earth's average gravity (9.8 m/s<sup>2</sup>) <br />
     *
     * @param velocity the initial velocity in meters per second
     * @return the distance in meters
     */
    public static double calcBounceApex(double velocity)
    {
        /* We can always use 0 in place of the final velocity because at its
         * apex, an object is not moving.
         */
        BigDecimal bd = new BigDecimal(
                (0 - Math.pow(velocity, 2)) / (2d * -GRAVITY));
        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);

        return bd.doubleValue();
    }

    /**
     * Calculates the time in seconds needed for an object to reach an apex of a
     * specified height.  The value is returned in seconds rounded to the
     * nearest millisecond (3 decimal places).  The value is calculated using
     * the formula: <br />
     * <br />
     * t = d / v<sub>avg</sub> <br />
     * <br />
     * where: <br />
     * <br />
     * t = the time in seconds <br />
     * d = the distance in meters <br />
     * v<sub>avg</sub> = the average velocity travelled <br />
     *
     * @param displacement the height of the apex in meters
     * @param velocity the initial velocity of the object in meters per second
     * @return the time in seconds
     */
    public static double calcTimeToApex(
            double displacement, double velocity)
    {
        BigDecimal bd;

        if(velocity > 0)
        {
            /* The average velocity is simply the initial velocity / 2, since at
             * the apex, the object will have a final velocity of zero.
             */
            bd = new BigDecimal(displacement / (velocity / 2));
        }
        else
        {
            bd = new BigDecimal(0);
        }

        bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);

        return bd.doubleValue();
    }
}