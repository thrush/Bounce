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

/**
 * Stores data related to a specific material.
 */
/* TODO Make this class more scalable to avoid usage as in BounceControlPanel */
public class Material
{
    /** The ID for rubber */
    public static final int ID_RUBBER = 0;

    /** The ID for iron */
    public static final int ID_IRON = 1;

    /** The ID for stone */
    public static final int ID_STONE = 2;

    /** The coefficient of restitution of rubber */
    public static final double COR_RUBBER = 0.8;

    /** The coefficient of restitution of metal */
    public static final double COR_IRON = 0.15;

    /** The coefficient of restitution of stone */
    public static final double COR_STONE = 0;

    /** The ID of the material */
    private int id;

    /**
     * Creates an instance of {@code Material}.
     *
     * @param id the ID of the material
     */
    public Material(int id)
    {
        this.id = id;
    }

    /**
     * Gets the ID of the material.
     * @return the ID of the material
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the material.
     * @param id the ID of the material
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the color of the material.
     * @return the color of the material
     */
    public Color getColor()
    {
        switch(id)
        {
            case ID_RUBBER:
            {
                return new Color(160, 130, 40);
            }
            case ID_IRON:
            {
                return new Color(120, 55, 25);
            }
            case ID_STONE:
            {
                return Color.GRAY;
            }
            default:
            {
                return Color.BLACK;
            }
        }
    }

    /**
     * Gets the coefficient of restitution (COR) of the material.
     * @return the COR of the material
     */
    public double getCor() {
        switch(id)
        {
            case ID_RUBBER:
            {
                return COR_RUBBER;
            }
            case ID_IRON:
            {
                return COR_IRON;
            }
            case ID_STONE:
            {
                return COR_STONE;
            }
            default:
            {
                return 1.0;
            }
        }
    }

    /**
     * Gets a string representation of the material.
     * @return a string representation of the material
     */
    @Override
    public String toString()
    {
        switch(id)
        {
            case ID_RUBBER:
            {
                return "Rubber";
            }
            case ID_IRON:
            {
                return "Iron";
            }
            case ID_STONE:
            {
                return "Stone";
            }
            default:
            {
                return "Perfectly elastic";
            }
        }
    }
}