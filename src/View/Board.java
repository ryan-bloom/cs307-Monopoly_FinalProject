package View;

import Controller.Controller;
import Model.*;
import Model.spaces.AbstractSpace;
import Model.spaces.ActionCardSpace;
import Model.spaces.PropSpace;
import Model.spaces.TaxSpace;
import View.SpaceDisplay.*;
import View.SpaceDisplay.CornerDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board {
    //Todo: needs to be refactored but wanted to make it work, all data is read in from file

    private static final String BOARD_PATH = "classic.jpg";

    private Controller myController;
    private Pane myBoardPane;
    private GridPane myGridPane;
    private ImageView boardLogo;
    private Map<Point2D.Double, AbstractSpace> indexToName;
    private Map<String,String> nameToColor;
    private Map<String,Integer> nameToPrice;
    private List<ImageView> imagesOnBoard = new ArrayList<>();

    public Board(Pane board, Controller controller) {
        this.myController = controller;
        this.myBoardPane = board;
        myGridPane = new GridPane();
        myGridPane.setGridLinesVisible(true);
        setUpGridConstraints();
        setUpBoardConfig();
        createSpaces();
        addTokensToGo();
    }

    public void addTokenToIndex(int i, ImageView image){
        int[] coord = indexToCoord(i);
        myGridPane.add(image,coord[0],coord[1]);
        imagesOnBoard.add(image);
    }

    public void renderPlayers(){
        for (ImageView i : imagesOnBoard){
            myGridPane.getChildren().remove(i);
        }
        int playerLocation = 0;
        Popup myPopup;
        for (AbstractPlayer pl : myController.getPlayers()){
            playerLocation = pl.getCurrentLocation();
            addTokenToIndex(pl.getCurrentLocation(),myController.getPlayerImageView(pl));
            System.out.println(playerLocation);
        }
        if (playerLocation==2 || playerLocation==7 || playerLocation==17 || playerLocation==22 || playerLocation==33 || playerLocation==36){
            myPopup = new ActionCardPopup("Action Card", "Need this from backend?", playerLocation);
        }
        else if (playerLocation==4 || playerLocation==38){
            myPopup = new TaxPopup("Tax", "Time to pay your taxes!", playerLocation);
        }
        else if (playerLocation==0 || playerLocation==10 || playerLocation==20 || playerLocation==30){
            myPopup = new CornerPopup("Other Space", playerLocation);
        }
        else {
            myPopup = new BuyPropertyPopup("Property", "Would you like to purchase this property?", playerLocation);
        }
        myPopup.display();
    }


    private void addTokensToGo(){
//    private void bindIconsToLocations(){
        for (AbstractPlayer p : myController.getPlayers()){
            ImageView img = myController.getPlayerImageView(p);
            addTokenToIndex(0,img);
            imagesOnBoard.add(img);
        }
    }

    private void createSpaces(){
        for (Map.Entry<Point2D.Double, AbstractSpace> entry : indexToName.entrySet()) {
            String name = entry.getValue().getMyName().replace("_", " ");
            if (entry.getValue() instanceof PropSpace) {
                String price = nameToPrice.get(name).toString();
                String color = nameToColor.get(name);
                if (entry.getKey().getY() == 10) {
                    BottomPropertyDisplay propSpaces = new BottomPropertyDisplay(name, price, color, myBoardPane, "#c7edc9");
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 10);
                }
                if (entry.getKey().getX() == 0) {
                    LeftPropertyDisplay propSpaces = new LeftPropertyDisplay(name, price, color, myBoardPane, "#c7edc9");
                    myGridPane.add(propSpaces.getMyPropStackPane(), 0, (int) entry.getKey().getY());
                }
                if (entry.getKey().getY() == 0) {
                    System.out.println((int) entry.getKey().getX());
                    TopPropertyDisplay propSpaces = new TopPropertyDisplay(name, price, color, myBoardPane, "#c7edc9");
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 0);
                }
                if (entry.getKey().getX() == 10) {
                    RightPropertyDisplay propSpaces = new RightPropertyDisplay(name, price, color, myBoardPane, "#c7edc9");
                    myGridPane.add(propSpaces.getMyPropStackPane(), 10, (int) entry.getKey().getY());
                }
            }
            if (entry.getValue() instanceof TaxSpace) {
                if (entry.getKey().getY() == 10) {
                    BottomPropertyDisplay propSpaces = new BottomPropertyDisplay(myBoardPane, "#c7edc9", "tax.png");
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 10);
                }
                if (entry.getKey().getX() == 10) {
                    RightPropertyDisplay propSpaces = new RightPropertyDisplay(myBoardPane, "#c7edc9", "tax.png");
                    myGridPane.add(propSpaces.getMyPropStackPane(), 10, (int) entry.getKey().getY());
                }
            }
            if (entry.getValue() instanceof ActionCardSpace) {
                String image;
                if (name.equals("COMMUNITY CHEST")) {
                    image = "chest.png";
                } else {
                    image = "chance.png";
                }
                if (entry.getKey().getY() == 10) {
                    BottomPropertyDisplay propSpaces = new BottomPropertyDisplay(myBoardPane, "#c7edc9", image);
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 10);
                }
                if (entry.getKey().getX() == 10) {
                    RightPropertyDisplay propSpaces = new RightPropertyDisplay(myBoardPane, "#c7edc9", image);
                    myGridPane.add(propSpaces.getMyPropStackPane(), 10, (int) entry.getKey().getY());
                }
                if (entry.getKey().getX() == 0) {
                    LeftPropertyDisplay propSpaces = new LeftPropertyDisplay(myBoardPane, "#c7edc9", image);
                    myGridPane.add(propSpaces.getMyPropStackPane(), 0, (int) entry.getKey().getY());
                }
                if (entry.getKey().getY() == 0) {
                    TopPropertyDisplay propSpaces = new TopPropertyDisplay(myBoardPane, "#c7edc9", image);
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 0);
                }
            } else {
                CornerDisplay goSpace = new CornerDisplay("#c7edc9", myBoardPane, "go.png");
                myGridPane.add(goSpace.getMyPropertyStackPane(), 10, 10);
                CornerDisplay parkingSpace = new CornerDisplay("#c7edc9", myBoardPane, "freeParking.png");
                myGridPane.add(parkingSpace.getMyPropertyStackPane(), 0, 0);
                CornerDisplay jailSpace = new CornerDisplay("#c7edc9", myBoardPane, "jail.png");
                myGridPane.add(jailSpace.getMyPropertyStackPane(), 0, 10);
                CornerDisplay goToJail = new CornerDisplay("#c7edc9", myBoardPane, "goToJail.png");
                myGridPane.add(goToJail.getMyPropertyStackPane(), 10, 0);

                //Todo: THIS IS JUST AN EXAMPLE FOR SPRINT 1 PURPOSES WILL REFACTOR OUT AFTER
                StackPane token = new StackPane();
//                Circle popUpExample = new Circle(10);
//                Label player1 = new Label("1");
//                player1.setId("popUp");
//                token.getChildren().addAll(player1);
//
//                Popup myPopup = new BuyPropertyPopup("Property", "Would you like to purchase this property?", 13);
//                popUpExample.setOnAction(e -> myPopup.display());
//                myGridPane.add(token, 1 ,10);

            }
        }
    }

    private int[] indexToCoord(int i){
        if (i>=0 && i<=10) return new int[]{10-i,10};
        if (i>=11 && i<=19) return new int[]{0,20-i};
        if (i>=19 && i<=30) return new int[]{i-20,0};
        if (i>=31 && i<= 39) return new int[]{10,i-30};
        else ; //throw some error
        return null;
    }

    private void setUpGridConstraints(){
        final int numCols = 11;
        final int numRows = 11;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(myBoardPane.getPrefHeight() / numCols);
            myGridPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(myBoardPane.getPrefHeight() / numRows);
            myGridPane.getRowConstraints().add(rowConst);
        }
    }

    private void setUpBoardConfig(){
        BoardConfigReader configs = new BoardConfigReader();
        indexToName = configs.getIndexToName();
        nameToColor = configs.getNameToColor();
        nameToPrice = configs.getNameToPrice();
    }

    public Pane getGridPane() {
        return myGridPane;
    }

    public ImageView getLogo() {
        var logo = new Image(this.getClass().getClassLoader().getResourceAsStream(BOARD_PATH));
        boardLogo = new ImageView(logo);
        boardLogo.setFitWidth((myBoardPane.getPrefWidth() / 13) * 9);
        boardLogo.setFitHeight((myBoardPane.getPrefWidth() / 13) * 9);
        boardLogo.setId("boardLogo");
        return boardLogo;
    }
}