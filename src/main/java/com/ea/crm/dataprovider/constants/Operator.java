package com.ea.crm.dataprovider.constants;
/**
 * Created by Shashank Bhattarai on 5/2/2023.
 */
public enum Operator {
    /**
     *EQ: when Operator is 'Equals'
     */
    EQ,
    /**
     *GT: when Operator is 'Greater Than'
     */
    GT,
    /**
     *GTE: when Operator is 'Greater Than or Equals'
     */
    GTE,
    /**
     *LT: when Operator is 'Less Than'
     */
    LT,
    /**
     *LTE: when Operator is 'Less Than or Equals'
     */
    LTE,
    /**
     *BTWN: when Operator is 'Between'
     */
    BTWN,
    /**
     *NEQ: when Operator is 'Not Equals'
     */
    NEQ,
    /**
     *BGNWTH: when Operator is 'Begins With'
     */
    BGNWTH,
    /**
     *ENDWTH: when Operator is 'Ends With'
     */
    ENDWTH,
    /**
     *LIKE: when Operator is 'Like'
     */
    LIKE,
    /**
     *NOTLIKE: when Operator is 'Not Like'
     */
    NOTLIKE,
    /**
     *AND: To indicate filter applies in AND clause
     */
    AND,
    /**
     *OR: To indicate filter applies in AND clause
     */
    OR;

    public static Operator valueOf(int operator) {
        for (Operator op : Operator.values()) {
            if (op.ordinal() == operator) {
                return op;
            }
        }
        return null;
    }
}
