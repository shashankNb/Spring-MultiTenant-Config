package com.ea.crm.dataprovider.constants;

public enum TextWrap {

    /**
     * WRAP: when no text wrap is selected
     */
    NOWRAP,
    /**
     * WRAP: when wrap text is selected
     */
    WRAP;


    public static TextWrap valueOf(int textWrap) {
        for (TextWrap tw : TextWrap.values()) {
            if (tw.ordinal() == textWrap) {
                return tw;
            }
        }
        return NOWRAP;
    }

}
