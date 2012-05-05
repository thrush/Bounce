/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.logic;

import java.awt.Color;
import java.awt.Graphics2D;

import bounce.logic.Physics.Direction;

/**
 * Represents a ball and stores the information needed to paint it.  It is also
 * capable of painting itself.
 */
public class Ball
{
    /** The default diameter of a ball in meters. */
    public static final double DEFAULT_DIAMETER = 0.15;

    /** The apex of the ball in meters. */
    private double apex;

    /** The diameter of the ball in meters. */
    private double diameter;

    /** The velocity of the ball in meters per second. */
    private double velocity;

    /** The x position of the ball in meters. */
    private double x;

    /** The y position (height) of the ball in meters. */
    private double y;

    /** The material the ball is made from. */
    private Material material;

    /** The direction in which the ball is moving. */
    private Direction direction;

    /**
     * Creates a ball with the specified diameter.
     *
     * @param diameter the diameter of the ball in meters
     */
    public Ball(double diameter)
    {
        this(diameter, 0, 0);
    }

    /**
     * Creates a ball with the specified diameter, x position, and y position.
     *
     * @param diameter the diameter of the ball in meters
     * @param x the x position of the ball in meters
     * @param y the y position of the ball in meters
     */
    public Ball(double diameter, double x, double y)
    {
        this(diameter, x, y, new Material(Material.ID_RUBBER));
    }

    /**
     * Creates a ball with the specified diameter, x position, y position, and
     * material.
     *
     * @param diameter the diameter of the ball in meters
     * @param x the x position of the ball in meters
     * @param y the y position of the ball in meters
     * @param material the material the ball is made of
     * @see bounce.logic.Material
     */
    public Ball(double diameter, double x, double y, Material material)
    {
        this.diameter = diameter;
        this.x = x;
        this.y = y;
        this.material = material;
    }

    // <editor-fold defaultstate="collapsed" desc="Accessors / Mutators">
    /**
     * Gets the apex of the ball.
     * @return the apex of the ball in meters
     */
    public double getApex() {
        return apex;
    }

    /**
     * Sets the apex of the ball.
     * @param apex the apex of the ball in meters
     */
    public void setApex(double apex) {
        this.apex = apex;
    }

    /**
     * Gets the diameter of the ball.
     * @return the diameter of the ball in meters
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Sets the diameter of the ball.
     * @param diameter the diameter of the ball in meters
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * Gets the material the ball is made of.
     * @return the material the ball is made of
     * @see bounce.logic.Material
     */
    public Material getMaterial() {

        return material;
    }

    /**
     * Sets the material the ball is made of.
     * @param material the new material
     * @see bounce.logic.Material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Gets the direction in which the ball is travelling.
     * @return the direction in which the ball is travelling
     * @see bounce.logic.Physics.Direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction in which the ball is travelling.
     * @param direction the direction in which the ball is travelling
     * @see bounce.logic.Physics.Direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets the velocity of the ball.
     * @return the velocity of the ball in meters per second
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Sets the velocity of the ball.
     * @param velocity the velocity of the ball in meters per second
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets the "x" position of the ball&mdash;that is, the horizontal position
     * relative to the left edge of the drawing area.
     * @return the "x" position of the ball in meters
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the "x" position of the ball&mdash;that is, the horizontal position
     * relative to the left edge of the drawing area.
     * @param x the "x" position of the ball in meters
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the "y" position of the ball&mdash;that is, the height of the ball
     * from the ground.  This value is measured from the bottom of the ball.
     * @return the "y" position of the ball in meters
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the "y" position of the ball&mdash;that is, the height of the ball
     * from the ground.  This value is measured from the bottom of the ball.
     * @param y the "y" position of the ball in meters
     */
    public void setY(double y) {
        this.y = y;
    }
    // </editor-fold>

    /**
     * Paints the ball using its stored values and the specified
     * {@code Graphics2D} object.
     *
     * @param g the {@code Graphics2D} object that will paint the ball
     * @param drawingHeight the height of the drawing area in pixels
     */
    public void paint(Graphics2D g, int drawingHeight)
    {
        Color oldColor = g.getColor();

        g.setColor(getMaterial().getColor());
        g.fillOval(Converter.toPixels(getX()),
                Converter.invertYValue(getY(), getDiameter(), drawingHeight),
                Converter.toPixels(getDiameter()),
                Converter.toPixels(getDiameter()));

        g.setColor(oldColor);

        // Traces
        /*
        System.out.println("\n\n\npainting ball...");
        System.out.println("x       : " + getX() + " m");
        System.out.println("y       : " + getY() + " m");
        System.out.println("diameter: " + getDiameter() + " m");
        System.out.println("\nx       : " + Converter.toPixels(getX()) + " px");
        System.out.println("y       : " + Converter.invertYValue(
                getY(), getDiameter(), windowHeight) + " px");
        System.out.println("diameter: " + 
                Converter.toPixels(getDiameter()) + " px");
        */
    }
}