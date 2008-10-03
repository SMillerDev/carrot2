package org.carrot2.workbench.editors.impl.numeric;

import org.carrot2.util.RangeUtils;
import org.carrot2.util.attribute.constraint.IntRange;
import org.carrot2.workbench.editors.AttributeEditorInfo;

/**
 * Attribute editor for non-negative integer values with {@link IntRange} annotations.
 */
final class IntegerRangeEditor extends NumericRangeEditorBase
{
    /**
     * Range constraint or <code>null</code> if not present.
     */
    private IntRange constraint;

    /*
     * 
     */
    public IntegerRangeEditor()
    {
        super(/* precision digits */0);
    }

    /*
     * 
     */
    @Override
    public AttributeEditorInfo init()
    {
        constraint = NumberUtils.getIntRange(descriptor);

        final int min = constraint.min();
        final int max = constraint.max();
        final int increment = RangeUtils.getIntMinorTicks(min, max);
        final int pageIncrement = RangeUtils.getIntMajorTicks(min, max);

        setRanges(min, max, increment, pageIncrement);

        return super.init();
    }

    /*
     * 
     */
    @Override
    public void setValue(Object value)
    {
        if (!(value instanceof Number))
        {
            return;
        }

        super.propagateNewValue(((Number) value).intValue());
    }
}
