/*
 * This code is copyright.  All rights reserved.
 *
 * The presence of this code on GitHub or any other code hosting service does
 * not imply that it is open source.
 *
 * The author can be found at github.com/thrush.
 */
package bounce.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

import bounce.logic.BounceArea;

/**
 * Manages the communication between various GUI and logic components.
 *
 * @see PropertyChangeListener
 */
public class Controller implements PropertyChangeListener
{
    private ArrayList models = new ArrayList();
    private ArrayList<PropertyChangeListener> views =
            new ArrayList<PropertyChangeListener>();

    /**
     * Registers the specified model to be notified of updates coming from
     * various GUI components.
     *
     * @param area the model to be registered
     */
    public void registerModel(BounceArea area)
    {
        models.add(area);
    }

    /**
     * Registers the specified view to receive updates from any registered
     * models.
     *
     * @param listener the view to be registered
     * @see PropertyChangeListener
     */
    public void registerView(PropertyChangeListener listener)
    {
        views.add(listener);
    }

    /**
     * Searches the list of registered models until an instance of the specified
     * class is found.  The specified field in that model is then set using the
     * supplied value.
     *
     * @param c the class of model to set
     * @param field the field to be set
     * @param newValue the value to which the field will be set
     */
    public void setModelProperty(Class c, String field, Object newValue)
    {
        for (Object model : models)
        {
            if(model.getClass().equals(c))
            {
                try
                {
                    Method method = model.getClass().getMethod(
                            "set" + field, new Class[] {newValue.getClass()});

                    method.invoke(model, newValue);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Initialize the model of the specified class.  This typically means that
     * the fields in the model will be reset to their default values.
     *
     * @param c the class of model to initialize
     */
    public void initModel(Class c)
    {
        for (Object model : models)
        {
            if(model.getClass().equals(c))
            {
                try
                {
                    Method method = model.getClass().getMethod("init");
                    method.invoke(model);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Propagates the specified {@code PropertyChangeEvent} to all views that
     * have been registered to receive events.
     *
     * @param evt the event to be propagated
     * @see PropertyChangeListener
     * @see PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        for(PropertyChangeListener view : views)
        {
            view.propertyChange(evt);
        }
    }
}