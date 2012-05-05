/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 * Provides members and methods that allow listeners to be supported.  A class
 * that inherits from {@code PresentationModel} should store information that is
 * related strictly to a view.
 */
public abstract class PresentationModel
{
    /**
     * The listeners that are registered to receive updates.
     */
    private ArrayList<PropertyChangeListener> listeners =
            new ArrayList<PropertyChangeListener>();

    /**
     * Adds a listener from the list of listeners that are notified of field
     * changes.
     *
     * @param listener the listener to be added
     *
     * @see PropertyChangeListener
     */
    public void addListener(PropertyChangeListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the list of listeners that are notified of field
     * changes.
     *
     * @param listener the listener to be removed
     *
     * @see PropertyChangeListener
     */
    public void removeListener(PropertyChangeListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Fires a {@code PropertyChangeEvent} that notifies all registered
     * listeners of a field update.
     *
     * @param field the field that was updated
     * @param oldValue the previous value
     * @param newValue the new value
     *
     * @see PropertyChangeListener
     * @see PropertyChangeEvent
     */
    public void firePropertyChange(
            String field, Object oldValue, Object newValue)
    {
        PropertyChangeEvent evt = 
                new PropertyChangeEvent(this, field, oldValue, newValue);

        for(PropertyChangeListener listener : listeners)
        {
            listener.propertyChange(evt);
        }
    }
}