package com.jtconnors.socketclientfx;

import java.util.ArrayList;

public class Portal {


    private String typeofportal;
    private int rowsame;
    private int colsame;
    private int individual;

    private int portalid;//have to be the same
    private String color;//have to be the same color

    private ArrayList<Integer> rowlist = new ArrayList<>();
    private ArrayList<Integer> collist = new ArrayList<>();

    public Portal(String typeofportal, int samething, ArrayList<Integer> differentthing, int idgiven, String colorgiven, int seperate){
        this.typeofportal = typeofportal;
        if(typeofportal.equals("Vertical")){
            this.colsame = samething;
            rowlist = differentthing;
        }
        if(typeofportal.equals("Horizontal")){
            this.rowsame = samething;
            collist = differentthing;
        }
        this.portalid = idgiven;
        this.color = colorgiven;
        this.individual = seperate;
    }

    public ArrayList<Integer> getCollist() {
        return collist;
    }

    public ArrayList<Integer> getRowlist() {
        return rowlist;
    }

    public int getColsame() {
        return colsame;
    }

    public int getRowsame() {
        return rowsame;
    }

    public String getTypeofportal() {
        return typeofportal;
    }

    public String getColor() {
        return color;
    }

    public int getPortalid() {
        return portalid;
    }

    public int getIndividual() {
        return individual;
    }



}
