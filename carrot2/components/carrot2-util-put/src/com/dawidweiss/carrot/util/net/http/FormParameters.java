package com.dawidweiss.carrot.util.net.http;

import java.util.*;

import org.jdom.Element;
import com.dawidweiss.carrot.util.jdom.JDOMHelper;


/**
 * Objects of this class hold parameters and their values, eventually submitted to
 * some HTTP service.
 *
 * Parameter names should be Strings, but they don't necessarily need to be - all arguments
 * are converted to String type using <code>.toString()</code> method. Parameter values are
 * of three types:
 * <ul>
 * <li>Mapped values - the value of this type of parameter is a key, which is used to retrieve
 *     the actual value from some other Map object (when submitting).
 * <li>InputStreams and Readers - if the value of a parameter is either InputStream or a Reader, then
 *     the value of this parameter is read from that object upon submitting the form, no sooner.
 * <li>Constants - all other types are converted to strings using <code>.toString</code>
 * </ul>
 *
 * This class can be initialized using JDOM's XML Element. The XML must follow the structure below:
 *
 * @author Dawid Weiss
 */
public class FormParameters
{
    /** All parameters of this form in the order they were added. */
    protected List    parameters      = new LinkedList();


    /**
     * Use parameter constructors to acquire objects of this class.
     */
    public FormParameters()
    {
    }


    /**
     * Initialize using JDOM's XML fragment.
     * @param initFromXML JDOM's Element with initialization parameters.
     */
    public FormParameters(Element initFromXML)
    {
        initInstanceFromXML(initFromXML);
    }


    /**
     * Returns an iterator of parameter names.
     */
    public Iterator getParametersIterator()
    {
        return parameters.iterator();
    }


    /**
     * Adds a parameter to the set.
     */
    public void addParameter(Parameter param)
    {
        parameters.add( param );
    }


    // ------------------------------------------------------- protected section


    /**
     * Initializes this object using an XML Element.
     *
     * The DOM structure must contain at least the following elements (this is
     * an example of wrapping Google).
     *
     * <PRE>
     *    &lt;!-- Specify any other parameters which will be sent to the service
     *         If "value" attribute is defined, the parameter will have the
     *         value copied verbatim from this file. If "mapto" attribute
     *         is defined, the value will be mapped to the key given.
     *
     *    --&gt;
     *    &lt;parameters&gt;
     *        &lt;parameter name="sourceid" value="mozilla-search" /&gt;
     *        &lt;parameter name="q"        mapto="query.string" /&gt;
     *        &lt;parameter name="start"    mapto="query.startFrom" /&gt;
     *    &lt;/parameters&gt;
     * </PRE>
     *
     * @param qr JDOM's Element with initialization parameters.
     */
    protected void initInstanceFromXML(Element qr)
    {
        final String PARAMETER_NODE     = "/parameters/parameter";
        final String PARAMETER_NAME     = "name";
        final String PARAMETER_VALUE    = "value";
        final String PARAMETER_MAPTO    = "mapto";

        // retrieve query parameters
        List parameters = JDOMHelper.getElements(PARAMETER_NODE, qr);

        if (parameters != null)
        {
            for (ListIterator param = parameters.listIterator(); param.hasNext(); )
            {
                Element paramElement = (Element) param.next();

                // get the name of the parameter
                String pname = paramElement.getAttribute(PARAMETER_NAME).getValue();

                // check whether constant-valued
                if (paramElement.getAttribute(PARAMETER_VALUE) != null)
                {
                    addParameter(new Parameter( pname, paramElement.getAttribute(PARAMETER_VALUE).getValue(), false));
                }
                else
                if (paramElement.getAttribute(PARAMETER_MAPTO) != null)
                {
                    addParameter(new Parameter( pname, paramElement.getAttribute(PARAMETER_MAPTO).getValue(), true));
                }
                else
                {
                    throw new IllegalArgumentException("Illegal XML structure: argument type unknown.");
                }
            }
        }
    }
    
    /**
     * Returns a stringified version of this form parameters (for
     * debugging purposes mostly).
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (Iterator i = parameters.iterator(); i.hasNext() ;) {
            Parameter p = (Parameter) i.next();
            buf.append("[");
            buf.append(p);
            buf.append("]\n");
        }
        return buf.toString();
    }
}



