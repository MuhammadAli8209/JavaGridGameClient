package com.jtconnors.socketclientfx;

import com.jtconnors.socket.DebugFlags;
import com.jtconnors.socket.SocketListener;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import com.jtconnors.socketfx.FxSocketClient;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author jtconnor
 */
public class FXMLDocumentController implements Initializable {

//    @FXML
//    private ListView<String> rcvdMsgsListView;
//    @FXML
//    private ListView<String> sentMsgsListView;
//    @FXML
//    private Button sendButton;
//    @FXML
//    private TextField sendTextField;
//    @FXML
//    private TextField selectedTextField;
    @FXML
    private Button connectButton, btnrestart;
    @FXML
    private Button disconnectButton;

    @FXML
    private Button buybutton;
    @FXML
    private TextField hostTextField, txtnamebox;

    @FXML
    private long starttime, timed;
    @FXML
    private int[][] environmentgrid = new int[50][80];

    @FXML
    private ListView weaponslist, armorlist, itemlist, factoryupglist, playerstatslist, playerinventorylist;
    @FXML
    private TextField portTextField;
    @FXML
    private ArrayList<Integer> thisrow = new ArrayList<>();

    @FXML
    private ArrayList<Bullets> allbullets = new ArrayList<>();

    @FXML
    private ArrayList<Drop> alldrops = new ArrayList<>();

    @FXML
    private String selectedfrominventory;

    @FXML
    private ArrayList<Portal> allportals = new ArrayList<>();


    @FXML
    private ArrayList<Integer> thiscol = new ArrayList<>();
    @FXML
    private CheckBox autoConnectCheckBox;
    @FXML
    private TextField retryIntervalTextField;
    @FXML
    private GridPane gPane;
    @FXML
    private Button[][] buttongrid = new Button[50][80];


    @FXML
    private Label connectedLabel;

    private final static Logger LOGGER
            = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

//    private ObservableList<String> rcvdMsgsData;
//    private ObservableList<String> sentMsgsData;
//    private ListView<String> lastSelectedListView;
    private Tooltip portTooltip;
    private Tooltip hostTooltip;
    @FXML
    private String playername;

    private boolean connected;
    private volatile boolean isAutoConnected;

    private static final int DEFAULT_RETRY_INTERVAL = 2000; // in milliseconds

//    @FXML
//    public void buysting(ActionEvent event) {
//    }

    @FXML
    public void buysting(){

        if(selected=="ITEMSELECTED"){
            buyitem();
        }
        if(selected=="ARMORSELECTED"){
            System.out.println("DO STHUF");
        }
        if(selected=="FACTORYSELECTED"){
            buyfromfactorylist();
        }
        if(selected=="WEAPONSELECTED"){
            buyweapon();
        }

    }

    @FXML
    public void restartgame(){
        socket.sendMessage("RestartGame");
    }

    @FXML
    public void buyfromfactorylist(){
        if(factoryupglist.getSelectionModel().getSelectedItem().toString().equals("Factory")){
            socket.sendMessage("BuyFactory:"+playername+","+chosenrow+","+chosencol);
        }
        factoryupglist.getSelectionModel().clearSelection();
    }


    @FXML
    public void showdrops(){
        System.out.println("SHOWING DROPS NOWWWWW");
        for (int i = 0; i < alldrops.size(); i++) {
            buttongrid[alldrops.get(i).getDroplocation().getRow()][alldrops.get(i).getDroplocation().getColumn()].setStyle("-fx-background-color:"+alldrops.get(i).getColorofresource()+"; ");
        }
    }

    @FXML
    public void buyweapon(){
        playerinventorylist.getItems().clear();
        String weaponchosen  = weaponslist.getSelectionModel().getSelectedItem().toString();
        String weaponname = weaponchosen.substring(0,weaponchosen.indexOf(":"));
        socket.sendMessage("Buy:"+playername+","+weaponname);
    }

    @FXML
    public void buyitem(){
        playerinventorylist.getItems().clear();
        String itemchosen  = itemlist.getSelectionModel().getSelectedItem().toString();
        String itemname = itemchosen.substring(0,itemchosen.indexOf(":"));
        System.out.println("CLIENTBOUGHT"+itemname);
        socket.sendMessage("Buy:"+playername+","+itemname);
        itemlist.getItems().clear();
    }


    @FXML
    private String selected = "";


    @FXML
    public void itemlistselected(){
        selected = "ITEMSELECTED";
    }
    @FXML
    public void weaponslistselected(){
        selected = "WEAPONSELECTED";
    }
    @FXML
    public void armorlistselected(){
        selected = "ARMORSELECTED";
    }
    @FXML
    public void factorylistselected(){
        selected = "FACTORYSELECTED";
    }



    @FXML
    public void start(){
        new AnimationTimer(){
            @Override
            public void handle(long now){
                int time = (int) (now/1000000000 - starttime/1000000000);

                //if(now-timed>10000000){
                    if(allbullets.size()>0){
                        System.out.println("SIze of Allbullets array: "+allbullets.size());
                        System.out.println("MOVEBULLETS");
                        movebullets();

                    }

                    //showpeopleonscreen();
                    timed = System.nanoTime();

                //}

            }
        }.start();
    }


    @FXML
    public void movebullets(){

        for (int i = 0; i < allbullets.size(); i++) {
            buttongrid[allbullets.get(i).getBulletLocation().getRow()][allbullets.get(i).getBulletLocation().getColumn()].setStyle("-fx-background-color: #c2d9ff; ");
        }

        for (int i = 0; i < allbullets.size(); i++) {

            if(i<0 && allbullets.size()<1){
                System.out.println("no bullets to move");
            }
            if(i <0 && allbullets.size()>0){
                i=0;
            }
            if(allbullets.get(i).getBounces()>6){
                allbullets.remove(i);
                i--;
            }
            if(i>=0){
                allbullets.get(i).movebullet();
            }

        }

        for (int i = 0; i < allbullets.size(); i++) {
            buttongrid[allbullets.get(i).getBulletLocation().getRow()][allbullets.get(i).getBulletLocation().getColumn()].setStyle("-fx-background-color: #0a0000; ");
            //allbulletcoordinates+=allbullets.get(i).getBulletLocation().getRow()+","+allbullets.get(i).getBulletLocation().getColumn()+",";
        }

        for (int i = 0; i < environmentgrid.length; i++) {
            for (int j = 0; j < environmentgrid[0].length; j++) {
                if(environmentgrid[i][j]==2){
                    buttongrid[i][j].setStyle("-fx-background-color:#05f525; ");
                }
            }
        }

        for (int j = 0; j < allportals.size(); j++) {
            if(allportals.get(j).getTypeofportal().equals("Vertical")){
                for (int k = 0; k < allportals.get(j).getRowlist().size(); k++) {
                    buttongrid[allportals.get(j).getRowlist().get(k)][allportals.get(j).getColsame()].setStyle("-fx-background-color:"+allportals.get(j).getColor()+"; ");
                }
            }else{
                for (int k = 0; k < allportals.get(j).getCollist().size(); k++) {
                    buttongrid[allportals.get(j).getRowsame()][allportals.get(j).getCollist().get(k)].setStyle("-fx-background-color:"+allportals.get(j).getColor()+"; ");
                }
            }
        }



    }







    @FXML
    public void createboard(){
        gPane.setGridLinesVisible(true);
        gPane.setVisible(true);
        for (int i = 0; i < buttongrid.length; i++) {
            for (int j = 0; j < buttongrid[0].length; j++) {
                buttongrid[i][j] = new Button();
                buttongrid[i][j].setPrefSize(15,15);
                buttongrid[i][j].setMinSize(15,15);
                buttongrid[i][j].setStyle("-fx-background-color: #c2d9ff; ");
                //buttongrid[i][j].setPrefSize(50, 25);
                gPane.add(buttongrid[i][j], j, i);
            }
        }

        EventHandler z = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                chosenrow = GridPane.getRowIndex(((Button) event.getSource()));
                chosencol = GridPane.getColumnIndex(((Button) event.getSource()));

                System.out.println(chosenrow);
                System.out.println(chosencol);

                if(factoryupglist.getSelectionModel().getSelectedItem()==null){
                    socket.sendMessage("BulletFromClient:"+playername+","+chosenrow+","+chosencol+","+selectedfrominventory.substring(0,selectedfrominventory.indexOf(":")));
                }

                if(factoryupglist.getSelectionModel().getSelectedItem()!=null){
                    if(factoryupglist.getSelectionModel().getSelectedItem().toString().equals("Factory")){
                        if(environmentgrid[chosenrow][chosencol]==15){
                            socket.sendMessage("ShowFactoryStats:"+playername+","+chosenrow+","+chosencol);
                        }
                    }

                    if(factoryupglist.getSelectionModel().getSelectedItem().toString().equals("ShowFactoryStats")){
                        if(environmentgrid[chosenrow][chosencol]==15){
                            socket.sendMessage("ShowRealFactoryStats:"+playername+","+chosenrow+","+chosencol);
                        }
                    }
                    if(factoryupglist.getSelectionModel().getSelectedItem().toString().equals("Upgrade Factory")){
                        if(environmentgrid[chosenrow][chosencol]==15){
                            socket.sendMessage("UpgradeFactory:"+playername+","+chosenrow+","+chosencol);
                        }
                    }
                    if(factoryupglist.getSelectionModel().getSelectedItem().toString().equals("Exit")){
                        if(environmentgrid[chosenrow][chosencol]==15){
                            factoryupglist.getItems().clear();
                            factoryupglist.getItems().add("Factory");
                        }
                    }

                }






                ((Button) event.getSource()).setStyle("-fx-background-color: #000000;");

            }


        };

        for (int i = 0; i < buttongrid.length; i++) {
            for (int j = 0; j < buttongrid[0].length; j++) {
                buttongrid[i][j].setOnAction(z);
            }

        }

    }

    @FXML
    public void makeselectedthingy(){
        selectedfrominventory = playerinventorylist.getSelectionModel().getSelectedItem().toString();
    }





    public enum ConnectionDisplayState {

        DISCONNECTED, ATTEMPTING, CONNECTED, AUTOCONNECTED, AUTOATTEMPTING
    }

    private FxSocketClient socket;

    /*
     * Synchronized method set up to wait until there is no socket connection.
     * When notifyDisconnected() is called, waiting will cease.
     */
    private synchronized void waitForDisconnect() {
        while (connected) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /*
     * Synchronized method responsible for notifying waitForDisconnect()
     * method that it's OK to stop waiting.
     */
    private synchronized void notifyDisconnected() {
        connected = false;
        notifyAll();
    }

    /*
     * Synchronized method to set isConnected boolean
     */
    private synchronized void setIsConnected(boolean connected) {
        this.connected = connected;
    }

    /*
     * Synchronized method to check for value of connected boolean
     */
    private synchronized boolean isConnected() {
        return (connected);
    }

    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                hostTextField.getText(),
                Integer.valueOf(portTextField.getText()),
                DebugFlags.instance().DEBUG_NONE);
        socket.connect();

    }

    private void autoConnect() {
        new Thread() {
            @Override
            public void run() {
                while (isAutoConnected) {
                    if (!isConnected()) {
                        socket = new FxSocketClient(new FxSocketListener(),
                                hostTextField.getText(),
                                Integer.valueOf(portTextField.getText()),
                                DebugFlags.instance().DEBUG_NONE);
                        socket.connect();
                    }
                    waitForDisconnect();
                    try {
                        Thread.sleep(Integer.valueOf(
                                retryIntervalTextField.getText()) * 1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();
    }

    private void displayState(ConnectionDisplayState state) {
        switch (state) {
            case DISCONNECTED:
                connectButton.setDisable(false);
                disconnectButton.setDisable(true);
                //sendButton.setDisable(true);
                //sendTextField.setDisable(true);
                connectedLabel.setText("Not connected");
                break;
            case ATTEMPTING:
            case AUTOATTEMPTING:
                connectButton.setDisable(true);
                disconnectButton.setDisable(true);
                //sendButton.setDisable(true);
                //sendTextField.setDisable(true);
                connectedLabel.setText("Attempting connection");
                break;
            case CONNECTED:
                connectButton.setDisable(true);
                disconnectButton.setDisable(false);
                //sendButton.setDisable(false);
                //sendTextField.setDisable(false);
                connectedLabel.setText("Connected");
                break;
            case AUTOCONNECTED:
                connectButton.setDisable(true);
                disconnectButton.setDisable(true);
                //sendButton.setDisable(false);
                //sendTextField.setDisable(false);
                connectedLabel.setText("Connected");
                break;
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIsConnected(false);
        isAutoConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);

//        sentMsgsData = FXCollections.observableArrayList();
//        sentMsgsListView.setItems(sentMsgsData);
//        sentMsgsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        sentMsgsListView.setOnMouseClicked((Event event) -> {
//            String selectedItem
//                    = sentMsgsListView.getSelectionModel().getSelectedItem();
//            if (selectedItem != null && !selectedItem.equals("null")) {
//                selectedTextField.setText("Sent: " + selectedItem);
//                lastSelectedListView = sentMsgsListView;
//            }
//        });
//
//        rcvdMsgsData = FXCollections.observableArrayList();
//        rcvdMsgsListView.setItems(rcvdMsgsData);
//        rcvdMsgsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        rcvdMsgsListView.setOnMouseClicked((Event event) -> {
//            String selectedItem
//                    = rcvdMsgsListView.getSelectionModel().getSelectedItem();
//            if (selectedItem != null && !selectedItem.equals("null")) {
//                selectedTextField.setText("Received: " + selectedItem);
//                lastSelectedListView = rcvdMsgsListView;
//            }
//        });

        portTooltip = new Tooltip("Port number cannot be modified once\n" +
        "the first connection attempt is initiated.\n" +
        "Restart application in order to change.");

        portTextField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                portTextField.setText(oldText);
            }
        });

        hostTooltip = new Tooltip("Host cannot be modified once\n" +
        "the first connection attempt is initiated.\n" +
        "Restart application to change.");

        retryIntervalTextField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                retryIntervalTextField.setText(oldText);
            }
        });

        Runtime.getRuntime().addShutdownHook(new ShutDownThread());
    }

    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(DebugFlags.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            if (line != null && !line.equals("")) {

//                if(line.contains(":")){
//                    System.out.println("MESSAGE WITH :");
//                }

                if(line.equals("CREATEBOARD")){
                    createboard();
                    starttime = System.nanoTime();
                    timed = System.nanoTime();
                    start();
                }else if (line.contains(":")) {

                    if(line.substring(0,line.indexOf(":")).equals("BulletToAdd")){

                        System.out.println("MESSAGE RECIEVED");

                        String[] splitcor = line.substring(line.indexOf(":")+1).split(",");

                        int row = Integer.parseInt(splitcor[0]);
                        int col=Integer.parseInt(splitcor[1]);
                        int goalrow=Integer.parseInt(splitcor[2]);
                        int goalcol=Integer.parseInt(splitcor[3]);

                        allbullets.add(new Bullets(new Location(row,col),new Location(goalrow,goalcol)));
                        allbullets.get(allbullets.size()-1).setthegrid(environmentgrid);
                        allbullets.get(allbullets.size()-1).setPortallocations(allportals);


                    }

                    if(line.substring(0,line.indexOf(":")).equals("StatsLine")){
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        if(numbestaken.equals(playername)){
                            factoryupglist.getItems().add("ShowFactoryStats");
                        }
                    }
                    if(line.substring(0,line.indexOf(":")).equals("ClearPlayerFactoryList")){
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        if(numbestaken.equals(playername)){
                            factoryupglist.getItems().clear();
                        }
                    }
                    if(line.substring(0,line.indexOf(":")).equals("Level") || line.substring(0,line.indexOf(":")).equals("MoneyMultiplier") || line.substring(0,line.indexOf(":")).equals("Health") || line.substring(0,line.indexOf(":")).equals("Upgrade Factory") || line.substring(0,line.indexOf(":")).equals("ResourceAmount")){
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        String[] newnums = numbestaken.split(",");
                        if(newnums[0].equals(playername)){

                            System.out.println(line);

//                            if(line.substring(0,line.indexOf(":")).equals("ResourceAmount")){
//                                factoryupglist.getItems().add("ResourceAmount:"+line.substring(line.indexOf(":")+1));
//                            }

                            if(line.substring(0,line.indexOf(":")).equals("Upgrade Factory")){
                                factoryupglist.getItems().add("Upgrade Factory");
                            }else{
                                factoryupglist.getItems().add(line.substring(0,line.indexOf(":")+1)+newnums[1]);
                            }


                        }
                    }
                    if(line.substring(0,line.indexOf(":")).equals("Exit")){
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        if(numbestaken.equals(playername)){
                            factoryupglist.getItems().add("Exit");
                        }
                    }

                    if(line.substring(0,line.indexOf(":")).equals("ShowDrops")){
                        showdrops();
                        System.out.println("Showing Drops");
                    }

                    if(line.substring(0,line.indexOf(":")).equals("NewDrop")){
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        System.out.println("GOTTEN DROP MESSAGE");
                        String[] newnums = numbestaken.split(",");
                        alldrops.add(new Drop(newnums[0], new Location(Integer.parseInt(newnums[1]),Integer.parseInt(newnums[2]))));
                        showdrops();
                    }


                    if(line.substring(0,line.indexOf(":")).equals("SetEnvironmentToThis")){
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        String[] newnums = numbestaken.split(",");
                        environmentgrid[Integer.parseInt(newnums[0])][Integer.parseInt(newnums[1])] = Integer.parseInt(newnums[2]);
                    }

                    if(line.substring(0,line.indexOf(":")).equals("RemoveBullet")){
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        allbullets.remove(Integer.parseInt(numbestaken));
                    }





                    if(line.substring(0,line.indexOf(":")).equals("EnvironmentNew")){
                        ArrayList<Integer> rows = new ArrayList<>();
                        ArrayList<Integer> cols = new ArrayList<>();
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        System.out.println(numbestaken);
                        String[] newnums = numbestaken.split(",");
                        for (int i = 0; i < newnums.length; i++) {
                            if(i%2==0){
                                rows.add(Integer.parseInt(newnums[i]));
                            }else {
                                cols.add(Integer.parseInt(newnums[i]));
                            }
                        }


                        for (int i = 0; i < environmentgrid.length; i++) {
                            for (int j = 0; j < environmentgrid[0].length; j++) {
                                environmentgrid[i][j] = 0;
                            }
                        }

                        for (int i = 0; i < rows.size(); i++) {
                            environmentgrid[rows.get(i)][cols.get(i)] = 2;
                            buttongrid[rows.get(i)][cols.get(i)].setStyle("-fx-background-color:#05f525; ");
                        }

                    }



                    if(line.substring(0,line.indexOf(":")).equals("NewFactory")){

                        String numbestaken = line.substring(line.indexOf(":")+1);
                        System.out.println(numbestaken);
                        String[] newnums = numbestaken.split(",");
                        environmentgrid[Integer.parseInt(newnums[0])][Integer.parseInt(newnums[1])] = 15;

                        for (int i = 0; i < environmentgrid.length; i++) {
                            for (int j = 0; j < environmentgrid[0].length; j++) {
                                if(environmentgrid[i][j]==15){
                                    buttongrid[i][j].setStyle("-fx-background-color:#e811f0; ");
                                }
                            }
                        }


                    }

                    if(line.substring(0,line.indexOf(":")).equals("EnvironmentShow")){
                        for (int i = 0; i < environmentgrid.length; i++) {
                            for (int j = 0; j < environmentgrid[0].length; j++) {
                                if(environmentgrid[i][j]==2){
                                    buttongrid[i][j].setStyle("-fx-background-color:#05f525; ");
                                }
                            }
                        }

                        for (int j = 0; j < allportals.size(); j++) {
                            if(allportals.get(j).getTypeofportal().equals("Vertical")){
                                for (int k = 0; k < allportals.get(j).getRowlist().size(); k++) {
                                    buttongrid[allportals.get(j).getRowlist().get(k)][allportals.get(j).getColsame()].setStyle("-fx-background-color:"+allportals.get(j).getColor()+"; ");
                                }
                            }else{
                                for (int k = 0; k < allportals.get(j).getCollist().size(); k++) {
                                    buttongrid[allportals.get(j).getRowsame()][allportals.get(j).getCollist().get(k)].setStyle("-fx-background-color:"+allportals.get(j).getColor()+"; ");
                                }
                            }
                        }

                        showdrops();


                    }






                    if(line.substring(0,line.indexOf(":")).equals("PortalNew")){
                        System.out.println(line);
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        String[] things = numbestaken.split(",");
                        String type = things[0];
                        int whatsingle = Integer.parseInt(things[1]);
                        ArrayList<Integer> manylist = new ArrayList<>();
                        String[] manysplit = things[2].split("\\.");
                        System.out.println("MANYSPLIT "+manysplit[0]+" "+manysplit[1]+" "+manysplit[2]);
                        for (int i = 0; i < manysplit.length; i++) {
                            manylist.add(Integer.parseInt(manysplit[i]));
                        }
                        int idnumber = Integer.parseInt(things[3]);
                        String color = things[4];
                        int seperate = Integer.parseInt(things[5]);

                        allportals.add(new Portal(type,whatsingle,manylist,idnumber,color,seperate));


                        for (int j = 0; j < allportals.size(); j++) {
                            if(allportals.get(j).getTypeofportal().equals("Vertical")){
                                for (int k = 0; k < allportals.get(j).getRowlist().size(); k++) {
                                    environmentgrid[allportals.get(j).getRowlist().get(k)][allportals.get(j).getColsame()] = 4;
                                }
                            }else{
                                for (int k = 0; k < allportals.get(j).getCollist().size(); k++) {
                                    environmentgrid[allportals.get(j).getRowsame()][allportals.get(j).getCollist().get(k)] = 4;
                                }
                            }
                        }

                        for (int j = 0; j < allportals.size(); j++) {
                            if(allportals.get(j).getTypeofportal().equals("Vertical")){
                                for (int k = 0; k < allportals.get(j).getRowlist().size(); k++) {
                                    buttongrid[allportals.get(j).getRowlist().get(k)][allportals.get(j).getColsame()].setStyle("-fx-background-color:"+allportals.get(j).getColor()+"; ");
                                }
                            }else{
                                for (int k = 0; k < allportals.get(j).getCollist().size(); k++) {
                                    buttongrid[allportals.get(j).getRowsame()][allportals.get(j).getCollist().get(k)].setStyle("-fx-background-color:"+allportals.get(j).getColor()+"; ");
                                }
                            }
                        }

                    }





                    if(line.substring(0,line.indexOf(":")).equals("Color")){
                        ArrayList<Integer> rows = new ArrayList<>();
                        ArrayList<Integer> cols = new ArrayList<>();
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        if (numbestaken != "") {
                            System.out.println(numbestaken);
                            String[] newnums = numbestaken.split(",");
                            for (int i = 0; i < newnums.length; i++) {
                                if(i%2==0){
                                    rows.add(Integer.parseInt(newnums[i]));
                                }else {
                                    cols.add(Integer.parseInt(newnums[i]));
                                }
                            }



                        }
                        for (int i = 0; i < buttongrid.length; i++) {
                            for (int j = 0; j < buttongrid[0].length; j++) {
                                buttongrid[i][j].setStyle("-fx-background-color:#c2d9ff; ");
                            }
                        }

                        for (int i = 0; i < rows.size(); i++) {
                            buttongrid[rows.get(i)][cols.get(i)].setStyle("-fx-background-color:#fc1303; ");
                        }





                    }


                    if(line.substring(0,line.indexOf(":")).equals("RemoveDrop")){
                        String usethis = line.substring(line.indexOf(":")+1);
                        String[] splitter = usethis.split(",");
                        System.out.println("Row: "+splitter[0]);
                        System.out.println("Col: "+splitter[1]);
                        for (int i = 0; i < alldrops.size(); i++) {
                            if(Integer.parseInt(splitter[0])==alldrops.get(i).getDroplocation().getRow()&&Integer.parseInt(splitter[1])==alldrops.get(i).getDroplocation().getColumn()){
                                alldrops.remove(i);
                            }
                        }


                    }

                    if(line.substring(0,line.indexOf(":")).equals("ItemList")){
                        itemlist.getItems().add(line.substring(line.indexOf(":")+1));
                    }
                    if(line.substring(0,line.indexOf(":")).equals("PlayerInventory")){
                        String usethis = line.substring(line.indexOf(":")+1);
                        String[] splitter = usethis.split(",");
                        if(splitter[0].equals(playername)){
                            playerinventorylist.getItems().add(splitter[1]);
                        }
                    }
                    if(line.substring(0,line.indexOf(":")).equals("MoneyForClient")){
                        String numbestaken = line.substring(line.indexOf(":")+1);
                        String[] things = numbestaken.split(",");
                        if(things[0].equals(playername)){
                            playerinventorylist.getItems().add("Money: "+things[1]);
                        }
                    }

                    if(line.substring(0,line.indexOf(":")).equals("WeaponList")){
                        weaponslist.getItems().add(line.substring(line.indexOf(":")+1));
                    }
                    if(line.substring(0,line.indexOf(":")).equals("Stats")){
                        playerstatslist.getItems().clear();
                        String usethis = line.substring(line.indexOf(":")+1);
                        String[] splitter = usethis.split(",");
                        if(splitter[0].equals(playername)){
                            playerstatslist.getItems().add("Name: "+playername);
                            playerstatslist.getItems().add("Health: "+splitter[1]);
                            //playerstatslist.getItems().add("Money: "+splitter[2]);

                        }
                    }

                    if(line.substring(0,line.indexOf(":")).equals("Resources")){
                        String usethis = line.substring(line.indexOf(":")+1);
                        String[] splitter = usethis.split(",");
                        if(splitter[0].equals(playername)){
                            playerinventorylist.getItems().add("Resources: "+ splitter[1]);
                        }
                    }


                } else if (line.equals("ClearItemList")) {
                    itemlist.getItems().clear();
                } else if (line.equals("ClearWeaponsList")) {
                    weaponslist.getItems().clear();
                } else if (line.equals("StartFactory")) {
                    factoryupglist.getItems().clear();
                    factoryupglist.getItems().add("Factory");
                }else if(line.equals("ClearInventory")){
                    playerinventorylist.getItems().clear();
                } else if (line.equals("ShowFactories")) {
                    for (int i = 0; i < environmentgrid.length; i++) {
                        for (int j = 0; j < environmentgrid[0].length; j++) {
                            if(environmentgrid[i][j]==15){
                                buttongrid[i][j].setStyle("-fx-background-color:#e811f0; ");
                            }
                        }
                    }
                } else if (line.equals("ShowRestart")) {
                    btnrestart.setVisible(true);
                    for (int i = 0; i < buttongrid.length; i++) {
                        for (int j = 0; j < buttongrid[0].length; j++) {
                            buttongrid[i][j].setStyle("-fx-background-color:#FFFFFF; ");
                        }
                    }
                } else if (line.equals("Restart")) {
                    alldrops.clear();
                    allportals.clear();
                    allbullets.clear();
                    for (int i = 0; i < environmentgrid.length; i++) {
                        for (int j = 0; j < environmentgrid[0].length; j++) {
                            environmentgrid[i][j] = 0;
                        }
                    }
                    playerinventorylist.getItems().clear();
                    playerstatslist.getItems().clear();
                    itemlist.getItems().clear();
                    weaponslist.getItems().clear();
                    createboard();
                }


            }
        }






        @Override
        public void onClosedStatus(boolean isClosed) {
            if (isClosed) {
                notifyDisconnected();
                if (isAutoConnected) {
                    displayState(ConnectionDisplayState.AUTOATTEMPTING);
                } else {
                    displayState(ConnectionDisplayState.DISCONNECTED);
                }
            } else {
                setIsConnected(true);
                if (isAutoConnected) {
                    displayState(ConnectionDisplayState.AUTOCONNECTED);
                } else {
                    displayState(ConnectionDisplayState.CONNECTED);
                }
            }
        }
    }

//    @FXML
//    private void handleClearRcvdMsgsButton(ActionEvent event) {
//        rcvdMsgsData.clear();
//        if (lastSelectedListView == rcvdMsgsListView) {
//            selectedTextField.clear();
//        }
//    }
//
//    @FXML
//    private void handleClearSentMsgsButton(ActionEvent event) {
//        sentMsgsData.clear();
//        if (lastSelectedListView == sentMsgsListView) {
//            selectedTextField.clear();
//        }
//    }

    @FXML
    private void sendmessage(ActionEvent event) {
//        if (!sendTextField.getText().equals("")) {
//            socket.sendMessage(sendTextField.getText());
//            sentMsgsData.add(sendTextField.getText());
//        }
        System.out.println("Hello");
    }

    @FXML
    private void handleConnectButton(ActionEvent event) {
        displayState(ConnectionDisplayState.ATTEMPTING);
        hostTextField.setEditable(false);
        hostTextField.setTooltip(hostTooltip);
        portTextField.setEditable(false);
        portTextField.setTooltip(portTooltip);
        connect();
    }

    @FXML
    private int chosenrow;
    private int chosencol;
    @FXML
    public void readybutton(ActionEvent event){
        socket.sendMessage("Ready");
        String name = txtnamebox.getText();
        playername = name;
        socket.sendMessage("CreatePlayer:"+name);


//        EventHandler z = new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//
//                chosenrow = GridPane.getRowIndex(((Button) event.getSource()));
//                chosencol = GridPane.getColumnIndex(((Button) event.getSource()));
//
//                System.out.println(chosenrow);
//                System.out.println(chosencol);
//
//                if(factoryupglist.getSelectionModel().getSelectedItem()==null){
//                    socket.sendMessage("BulletFromClient:"+playername+","+chosenrow+","+chosencol);
//                }
//
//                ((Button) event.getSource()).setStyle("-fx-background-color: #000000;");
//
//            }
//
//
//        };
//
//        for (int i = 0; i < buttongrid.length; i++) {
//            for (int j = 0; j < buttongrid[0].length; j++) {
//                buttongrid[i][j].setOnAction(z);
//            }
//
//        }


    }


    @FXML
    public void move(KeyEvent o){

        //up down left and right only, no diagonal, can add later
        if(o.getCode().equals(KeyCode.W)){
            socket.sendMessage("Move:"+playername+",up");
        } else if (o.getCode().equals(KeyCode.A)) {
            socket.sendMessage("Move:"+playername+",left");
        } else if (o.getCode().equals(KeyCode.S)) {
            socket.sendMessage("Move:"+playername+",down");
        }else if (o.getCode().equals(KeyCode.D)){
            socket.sendMessage("Move:"+playername+",right");
        }




    }


    @FXML
    private void handleDisconnectButton(ActionEvent event) {
        socket.shutdown();
    }

    @FXML
    private void handleAutoConnectCheckBox(ActionEvent event) {
        if (autoConnectCheckBox.isSelected()) {
            isAutoConnected = true;
            hostTextField.setEditable(false);
            hostTextField.setTooltip(hostTooltip);
            portTextField.setEditable(false);
            portTextField.setTooltip(portTooltip);
            if (isConnected()) {
                displayState(ConnectionDisplayState.AUTOCONNECTED);
            } else {
                displayState(ConnectionDisplayState.AUTOATTEMPTING);
                autoConnect();
            }
        } else {
            isAutoConnected = false;
            if (isConnected()) {
                displayState(ConnectionDisplayState.CONNECTED);
            } else {
                displayState(ConnectionDisplayState.DISCONNECTED);
            }
        }
    }
}
