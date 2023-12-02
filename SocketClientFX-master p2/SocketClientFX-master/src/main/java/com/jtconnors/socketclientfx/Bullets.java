package com.jtconnors.socketclientfx;

import javafx.fxml.FXML;

import java.util.ArrayList;

public class Bullets {

    private Location bulletLocation;

    private ArrayList<Portal> allportals = new ArrayList<>();

    //private Player ownerplayer;

    private int bounces = 0;

    private int[][]grid = new int[50][80];



    private int finalxthing;
    private int finalything;
    private int startrow;
    private int startcol;

    private boolean pastgoal = false;


    private Location goal;

    public Bullets(Location givenlocation, Location goallocation){
        //this.ownerplayer = givenplayer;
        this.bulletLocation = givenlocation;
        this.goal = goallocation;
        this.startrow = givenlocation.getRow();
        this.startcol = givenlocation.getColumn();
    }

    public void setthegrid(int[][] given){
        for (int i = 0; i < given.length; i++) {
            for (int j = 0; j < given[0].length; j++) {
                grid[i][j] = given[i][j];
            }
        }
    }

    public void setPortallocations(ArrayList<Portal> given){
        this.allportals = given;
    }

    public void setBounces(int given){
        this.bounces=given;
    }


    public void movebullet(){

        if(!pastgoal){
            double needtruncatex = 0.0;
            double needtruncatey=0.0;
            int bulletdistancex = this.goal.getColumn()-this.bulletLocation.getColumn();
            int bulletdistancey = this.goal.getRow()-this.bulletLocation.getRow();
            int tinystep =0;
            int tinxstep=0;

            if(bulletdistancex==0){
                tinxstep=0;
            }
            if(bulletdistancey==0){
                tinystep=0;
            }
            if(bulletdistancey!=0 && bulletdistancex!=0){
                if(Math.abs(bulletdistancey)>Math.abs(bulletdistancex)){
                    tinystep = Reducenum(Math.abs(bulletdistancey), Math.abs(bulletdistancex), "Numerator");
                    tinxstep = Reducenum(Math.abs(bulletdistancey), Math.abs(bulletdistancex), "Denominator");
                    if(tinxstep!=1){
                        needtruncatey = ((double) tinystep)/((double) tinxstep);
                        tinystep = (int)needtruncatey;
                        tinxstep =1;
                    }
                }

                if(Math.abs(bulletdistancex)>Math.abs(bulletdistancey)){
                    tinxstep = Reducenum(Math.abs(bulletdistancex), Math.abs(bulletdistancey), "Numerator");
                    tinystep = Reducenum(Math.abs(bulletdistancex), Math.abs(bulletdistancey), "Denominator");
                    if(tinxstep!=1){
                        needtruncatex = ((double) tinxstep)/((double) tinystep);
                        tinxstep = (int)needtruncatex;
                        tinystep =1;
                    }
                }
            }

            if(bulletdistancex==0){
                tinystep = 1;
                tinxstep=0;
            }
            if(bulletdistancey==0){
                tinxstep = 1;
                tinystep = 0;
            }

            if(bulletdistancey<0){
                tinystep*=-1;
            }
            if(bulletdistancex<0){
                tinxstep*=-1;
            }


            if(this.bulletLocation.getRow()+tinystep<0||this.bulletLocation.getRow()+tinystep>49){
                //bounces++;
                tinystep*=-1;
            }
            if(this.bulletLocation.getColumn()+tinxstep<0||this.bulletLocation.getColumn()+tinxstep>79){
                //bounces++;
                tinxstep*=-1;
            }

            if(grid[this.bulletLocation.getRow()+tinystep][this.bulletLocation.getColumn()]==2){
                bounces++;
                tinystep*=-1;
            }
            if(grid[this.bulletLocation.getRow()][this.bulletLocation.getColumn()+tinxstep]==2){
                bounces++;
                tinxstep*=-1;
            }

            if(this.bulletLocation.getRow()+tinystep>49 || this.bulletLocation.getRow()+tinystep<0){
                tinystep = tinystep/(Math.abs(tinystep));
            }
            if(this.bulletLocation.getColumn()+tinxstep>79 || this.bulletLocation.getColumn()+tinxstep<0){
                tinxstep = tinxstep/(Math.abs(tinxstep));
            }





            this.bulletLocation.changerow(tinystep);
            this.bulletLocation.changecol(tinxstep);

            if(this.startrow==this.bulletLocation.getRow()&&this.startcol==this.bulletLocation.getColumn()){
                this.bounces = 20;
            }

            if(this.bulletLocation.getRow()==this.goal.getRow()&&this.bulletLocation.getColumn()==this.goal.getColumn()){
                finalxthing = tinxstep;
                finalything = tinystep;
                pastgoal = true;
            }
        }else{


            if(this.bulletLocation.getRow()+finalything<0||this.bulletLocation.getRow()+finalything>49){

                Portal theportalithit = null;
                for (int i = 0; i < allportals.size(); i++) {
                    if(allportals.get(i).getTypeofportal().equals("Horizontal")){
                        if(this.bulletLocation.getColumn()>=allportals.get(i).getCollist().get(0)&&this.bulletLocation.getColumn()<=allportals.get(i).getCollist().get(2)){
                            System.out.println("HIT THE HORIZONTAL PORTAL");
                            if(this.bulletLocation.getRow()+finalything<0&&allportals.get(i).getRowsame()==0){
                                theportalithit=allportals.get(i);
                            }else if(this.bulletLocation.getRow()+finalything>49&&allportals.get(i).getRowsame()==49){
                                theportalithit=allportals.get(i);
                            }
                        }
                    }
                }

                if(theportalithit!=null){
                    for (int i = 0; i < allportals.size(); i++) {
                        if(theportalithit.getPortalid()==allportals.get(i).getPortalid()){
                            if(theportalithit.getIndividual()!=allportals.get(i).getIndividual()){

                                //DO STUFF HERE SO BULLET TRAVELS THROUGH PORTALS

                                if(theportalithit.getTypeofportal().equals("Horizontal")){

                                    System.out.println("HORIZONTAL PORTAL HIT");

                                    if(allportals.get(i).getTypeofportal().equals("Horizontal")){

                                        System.out.println("HORIZONTAL TO HORIZONTAL PORTAL");

                                        if(theportalithit.getRowsame()==0){


                                            if(allportals.get(i).getRowsame()==0){
                                                System.out.println("TOP TO TOP");
                                                finalything*=-1;
                                                this.bulletLocation.setRow(0);
                                                this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));

                                            }
                                            if(allportals.get(i).getRowsame()==49){
                                                System.out.println("TOP TO BOTTOM");
                                                this.bulletLocation.setRow(49);
                                                this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));

                                            }


                                        }else if(theportalithit.getRowsame()==49){

                                            if(allportals.get(i).getRowsame()==0){
                                                System.out.println("BOTTOM TO TOP");

                                                this.bulletLocation.setRow(0);
                                                this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));

                                            }
                                            if(allportals.get(i).getRowsame()==49){
                                                System.out.println("BOTTOM TO BOTTOM");
                                                finalything*=-1;
                                                this.bulletLocation.setRow(49);
                                                this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));

                                            }

                                        }

                                    }else{
                                        //if allportals is vertical

                                        if(allportals.get(i).getColsame()==0){
                                            System.out.println("TOP TO LEFT");
                                            int temp = finalxthing;
                                            finalxthing =finalything;
                                            finalything = temp;

                                            finalxthing = Math.abs(finalxthing);

                                            this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            this.bulletLocation.setColumn(0);

                                        }
                                        if(allportals.get(i).getColsame()==79){
                                            System.out.println("TOP TO RIGHT");

                                            int temp = finalxthing;
                                            finalxthing =finalything;
                                            finalything = temp;
                                            finalxthing = Math.abs(finalxthing) *-1;

                                            this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            this.bulletLocation.setColumn(79);

                                        }


                                    }


                                }


                                if(theportalithit.getTypeofportal().equals("Vertical")){


                                    if(allportals.get(i).getTypeofportal().equals("Vertical")){

                                        if(theportalithit.getColsame()==0){

                                            if(allportals.get(i).getColsame()==0){
                                                finalxthing*=-1;
                                                this.bulletLocation.setColumn(0);
                                                this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            }
                                            if(allportals.get(i).getColsame()==79){
                                                this.bulletLocation.setColumn(79);
                                                this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            }




                                        } else if (theportalithit.getColsame()==79) {

                                            if(allportals.get(i).getColsame()==0){

                                                this.bulletLocation.setColumn(0);
                                                this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            }
                                            if(allportals.get(i).getColsame()==79){
                                                finalxthing*=-1;
                                                this.bulletLocation.setColumn(79);
                                                this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            }

                                        }

                                    }else{


                                        if(allportals.get(i).getRowsame()==0){
                                            int temp = finalxthing;
                                            finalxthing =finalything;
                                            finalything = temp;

                                            finalything = Math.abs(finalything);
                                            this.bulletLocation.setRow(0);
                                            this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));



                                        }
                                        if(allportals.get(i).getRowsame()==49){
                                            int temp = finalxthing;
                                            finalxthing =finalything;
                                            finalything = temp;

                                            finalything = Math.abs(finalything) * -1;
                                            this.bulletLocation.setRow(0);
                                            this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));


                                        }


                                    }



                                }

                            }

                        }
                    }


                }else{
                    bounces++;
                    finalything*=-1;
                }




            }
            if(this.bulletLocation.getColumn()+finalxthing<0||this.bulletLocation.getColumn()+finalxthing>79){


                Portal theportalithit = null;
                for (int i = 0; i < allportals.size(); i++) {
                    if(allportals.get(i).getTypeofportal().equals("Vertical")){
                        if(this.bulletLocation.getRow()>=allportals.get(i).getRowlist().get(0)&&this.bulletLocation.getRow()<=allportals.get(i).getRowlist().get(2)){
                            System.out.println("HIT THE  VERTICAL PORTAL");
                            if(this.bulletLocation.getColumn()+finalxthing<0&&allportals.get(i).getColsame()==0){
                                theportalithit=allportals.get(i);
                            }else if(this.bulletLocation.getColumn()+finalxthing>79&&allportals.get(i).getColsame()==79){
                                theportalithit=allportals.get(i);
                            }
                        }
                    }
                }


                if(theportalithit!=null){
                    for (int i = 0; i < allportals.size(); i++) {
                        if(theportalithit.getPortalid()==allportals.get(i).getPortalid()){
                            if(theportalithit.getIndividual()!=allportals.get(i).getIndividual()){

                                //DO STUFF HERE SO BULLET TRAVELS THROUGH PORTALS

                                if(theportalithit.getTypeofportal().equals("Horizontal")){

                                    if(allportals.get(i).getTypeofportal().equals("Horizontal")){

                                        if(theportalithit.getRowsame()==0){


                                            if(allportals.get(i).getRowsame()==0){
                                                finalything*=-1;
                                                this.bulletLocation.setRow(0);
                                                this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));

                                            }
                                            if(allportals.get(i).getRowsame()==49){
                                                this.bulletLocation.setRow(49);
                                                this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));

                                            }


                                        }else if(theportalithit.getRowsame()==49){

                                            if(allportals.get(i).getRowsame()==0){

                                                this.bulletLocation.setRow(0);
                                                this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));

                                            }
                                            if(allportals.get(i).getRowsame()==49){
                                                finalything*=-1;
                                                this.bulletLocation.setRow(49);
                                                this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));

                                            }

                                        }

                                    }else{

                                        int temp = finalxthing;
                                        finalxthing =finalything;
                                        finalything = temp;
                                        this.bulletLocation.setColumn(allportals.get(i).getColsame());
                                        this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));


                                    }


                                }
                                if(theportalithit.getTypeofportal().equals("Vertical")){


                                    if(allportals.get(i).getTypeofportal().equals("Vertical")){

                                        if(theportalithit.getColsame()==0){

                                            if(allportals.get(i).getColsame()==0){
                                                finalxthing*=-1;
                                                this.bulletLocation.setColumn(0);
                                                this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            }
                                            if(allportals.get(i).getColsame()==79){
                                                this.bulletLocation.setColumn(79);
                                                this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            }




                                        } else if (theportalithit.getColsame()==79) {

                                            if(allportals.get(i).getColsame()==0){

                                                this.bulletLocation.setColumn(0);
                                                this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            }
                                            if(allportals.get(i).getColsame()==79){
                                                finalxthing*=-1;
                                                this.bulletLocation.setColumn(79);
                                                this.bulletLocation.setRow(allportals.get(i).getRowlist().get(1));
                                            }

                                        }

                                    }else{

                                        int temp = finalxthing;
                                        finalxthing =finalything;
                                        finalything = temp;
                                        this.bulletLocation.setRow(allportals.get(i).getRowsame());
                                        this.bulletLocation.setColumn(allportals.get(i).getCollist().get(1));


                                    }



                                }


                            }

                        }
                    }


                }else{
                    bounces++;
                    finalxthing*=-1;
                }










            }

            if(this.bulletLocation.getRow()+finalything>-1&&this.bulletLocation.getColumn()+finalxthing>-1&&this.bulletLocation.getRow()+finalything<50&&this.bulletLocation.getColumn()<80){
                if(grid[this.bulletLocation.getRow()+finalything][this.bulletLocation.getColumn()]==2){
                    bounces++;
                    finalything*=-1;
                }
                if(grid[this.bulletLocation.getRow()][this.bulletLocation.getColumn()+finalxthing]==2){
                    bounces++;
                    finalxthing*=-1;
                }
            }


            System.out.println("BULLET NEW LOCATION : "+this.bulletLocation.getRow()+", "+this.bulletLocation.getColumn());
            System.out.println("MOVE FINAL Y"+finalything);
            System.out.println("MOVE FINAL X"+finalxthing);

            if(this.bulletLocation.getColumn()+finalxthing<0||this.bulletLocation.getColumn()+finalxthing>79){
                finalxthing*=-1;
            }
            if(this.bulletLocation.getRow()+finalything<0||this.bulletLocation.getRow()+finalything>49){
                finalything*=-1;
            }


            this.bulletLocation.changerow(finalything);
            this.bulletLocation.changecol(finalxthing);

            if(this.startrow==this.bulletLocation.getRow()&&this.startcol==this.bulletLocation.getColumn()){
                this.bounces = 20;
            }


        }





    }










    @FXML
    public int Reducenum(int nume, int deno, String which){
        int numerator = nume;
        int denominator = deno;
        //lblreducefac.setText("");
        int smallernum = 0;
        if(numerator>denominator){
            smallernum = numerator;
        }else if(denominator>numerator){
            smallernum = denominator;
        }else{
            smallernum = 1;
        }
        for(int r = 1; r<smallernum;r++){
            if(numerator%r == 0 && denominator%r == 0){
                numerator = numerator/r;
                denominator = denominator/r;
                r = 1;
            }
        }
        System.out.println(numerator+"/"+denominator);
//        lblreducefac.setText(numerator+"/"+denominator);

        if(which.equals("Numerator")){
            return numerator;
        }else {
            return denominator;
        }


    }



    public Location getBulletLocation() {
        return bulletLocation;
    }

    public int getBounces() {
        return bounces;
    }

//    public Player getOwnerplayer() {
//        return ownerplayer;
//    }

    public Location getGoal() {
        return goal;
    }



}
