/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.logic;

import java.awt.Dimension;

/**
 * Represents an area in which a bounce animation is painted.
 */
public class BounceArea extends PresentationModel
{
    /** The default height of a "bounce area" in pixels */
    public static final int DEFAULT_HEIGHT = 250;

    /** The default width of a "bounce area" in pixels */
    public static final int DEFAULT_WIDTH = 200;

    /** String identifying the Size field */
    public static final String FIELD_SIZE = "Size";

    /** The size of a bounce area in pixels */
    private Dimension size;

    /**
     * Creates an instance of {@code BounceArea}.
     */
    public BounceArea(){}

    /**
     * Initializes the bounce area to a preset default size.
     */
    public void init()
    {
        setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
    }

    /**
     * Gets the size of the bounce area.
     *
     * @return the size of the bounce area in pixels
     */
    public Dimension getSize()
    {
        return size;
    }

    /**
     * Sets the size of the bounce area.
     *
     * @param size the size of the bounce area in pixels
     */
    public void setSize(Dimension size)
    {
        if(!size.equals(getSize()))
        {
            firePropertyChange(FIELD_SIZE, getSize(), size);
            this.size = size;
        }
    }
}