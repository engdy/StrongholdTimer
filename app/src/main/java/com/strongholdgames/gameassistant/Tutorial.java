package com.strongholdgames.timer;

public class Tutorial {
    public String name;
    public int resid;
    public int stringid;
    public String youtube;

    public Tutorial(String n, int r, int s, String y) {
        name = n;
        resid = r;
        stringid = s;
        youtube = y;
    }

    @Override
    public String toString() {
        return name;
    }
}
