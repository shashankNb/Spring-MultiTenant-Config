package com.ea.crm.dataprovider.constants;

public enum AlignTypes {

    /**
     *NONE: when no align type is selected
     */
    NONE,
    /**
     *LEFT: when left alignment is preferred
     */
    LEFT,
    /**
     *RIGHT: when right alignment is preferred
     */
    RIGHT,
    /**
     *CENTER: when center alignment is preferred
     */
    CENTER;

    public static AlignTypes valueOf(int alignment) {
        for (AlignTypes ca : AlignTypes.values()) {
            if (ca.ordinal() == alignment) {
                return ca;
            }
        }
        return NONE;
    }
}