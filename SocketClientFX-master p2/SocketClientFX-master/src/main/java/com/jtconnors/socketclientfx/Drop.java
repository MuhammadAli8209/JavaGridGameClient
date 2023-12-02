package com.jtconnors.socketclientfx;

public class Drop {

    private Location droplocation;

    private int amountresource;

    private String typeofdrop;

    private String colorofresource;

    private long thisgiventime;

    private boolean ispickedup;

    public Drop(String whichcolor, Location givenlocation){
        this.droplocation = givenlocation;
        this.colorofresource = whichcolor;
        thisgiventime = System.nanoTime();
        ispickedup = false;
        this.typeofdrop = "Ite";
    }

    public int getAmountresource() {
        return amountresource;
    }

    public Location getDroplocation() {
        return droplocation;
    }

    public String getColorofresource() {
        return colorofresource;
    }




}
