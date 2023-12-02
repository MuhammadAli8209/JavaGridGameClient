package com.jtconnors.socketclientfx;

public class Location {

    private int row;
    private int column;
    public Location(int rowgiven, int columngiven){
        this.row = rowgiven;
        this.column = columngiven;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void changerow(int givenint){
        this.row+=givenint;
    }
    public void changecol(int givenint){
        this.column+=givenint;
    }




}
