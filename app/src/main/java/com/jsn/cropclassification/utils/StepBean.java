package com.jsn.cropclassification.utils;

import java.io.Serializable;

public class StepBean implements Serializable {
    public static final int STEP_UNDO = -1;//undo step
    public static final int STEP_CURRENT = 0;// current step
    public static final int STEP_COMPLETED = 1;// completed step

    private String name;

    private int state;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public StepBean()
    {
    }

    public StepBean(String name, int state)
    {
        this.name = name;
        this.state = state;
    }
}
