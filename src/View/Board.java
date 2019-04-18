package View;

import Controller.*;
import Model.*;
import Model.properties.Property;
import Model.spaces.AbstractSpace;
import Model.spaces.ActionCardSpace;
import Model.spaces.PropSpace;
import Model.spaces.TaxSpace;
import View.PopUps.*;
import View.SpaceDisplay.*;
import View.SpaceDisplay.CornerDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board implements PropertyChangeListener {
    //Todo: needs to be refactored but wanted to make it work, all data is read in from file

    private static final String BOARD_PATH = "classic.jpg";

    private Controller myController;
    private AbstractGame myGame;
    private Pane myBoardPane;
    private GridPane myGridPane;
    private ImageView boardLogo;
    private Map<Point2D.Double, AbstractSpace> indexToName;
    private Map<String,String> nameToColor;
    private Map<String,Integer> nameToPrice;
    private List<ImageView> imagesOnBoard = new ArrayList<>();
    private List<Property> myProps;
    private List<AbstractSpace> allSpaces;
    private AbstractSpace myAbstractSpace;
    private Property myProperty;

    public Board(Pane board, Controller controller, AbstractGame myGame) {
        this.myController = controller;
        this.myBoardPane = board;
        this.myGame = myGame;
        for (AbstractPlayer p : controller.getPlayers()) {
            p.addPropertyChangeListener("currentLocation",this);
        }

        myGridPane = new GridPane();
        myGridPane.setGridLinesVisible(true);
        setUpGridConstraints();
        setUpBoardConfig();
        createSpaces();
        addTokensToGo();
    }

    public void addTokenToIndex(int i, ImageView image){
        BoardConfigReader reader = new BoardConfigReader();
        Map<Integer, Point2D.Double> myPoint = reader.getIndexToCoord();
        myGridPane.add(image,(int)myPoint.get(i).getX(),(int)myPoint.get(i).getY());
        imagesOnBoard.add(image);
    }

    public void renderPlayers(){
        for (ImageView i : imagesOnBoard){
            myGridPane.getChildren().remove(i);
        }
        int playerLocation = 0;
        for (AbstractPlayer pl : myController.getPlayers()){
            addTokenToIndex(pl.getCurrentLocation(),myController.getPlayerImageView(pl));
        }
        Popup myPopup;
        playerLocation = myGame.getCurrPlayer().getCurrentLocation();

        if (playerLocation==2 || playerLocation==7 || playerLocation==17 || playerLocation==22 || playerLocation==33 || playerLocation==36){
            myPopup = new ActionCardPopup( playerLocation, myController);
        }
        else if (playerLocation==4 || playerLocation==38){
            myPopup = new TaxPopup(playerLocation,myController);
        }
        else if (playerLocation==0 || playerLocation==10 || playerLocation==20 || playerLocation==30){
            myPopup = new CornerPopup(playerLocation, myController);
        }
        else {

            for (AbstractSpace sp : allSpaces){
                if (sp.getMyLocation()==playerLocation){
                    myAbstractSpace = sp;
                }
            }
            for (Property p : myProps){
                if (myAbstractSpace.getMyName().equalsIgnoreCase(p.getName())){
                    myProperty = p;
                }
            }

            if (myController.getGame().getBank().propertyOwnedBy(myProperty)!= null && myController.getGame().getBank().propertyOwnedBy(myProperty)!=myGame.getCurrPlayer()){
                myPopup = new PayRentPopup(playerLocation, myController);
            }
            else if (myController.getGame().getBank().propertyOwnedBy(myProperty)!= null && myController.getGame().getBank().propertyOwnedBy(myProperty)==myGame.getCurrPlayer()) {
                myPopup = null;
            }
            else{
                myPopup = new BuyPropertyPopup(playerLocation, myController);
            }
        }
        if (myPopup!=null){
            myPopup.display();
        }
    }


    private void addTokensToGo(){
        for (AbstractPlayer p : myController.getPlayers()){
            ImageView img = myController.getPlayerImageView(p);
            addTokenToIndex(0,img);
            imagesOnBoard.add(img);
        }
    }

    private void createSpaces(){
        String baseColor = "#c7edc9";

        for (Map.Entry<Point2D.Double, AbstractSpace> entry : indexToName.entrySet()) {
            String name = entry.getValue().getMyName().replace("_", " ");
            if (entry.getValue() instanceof PropSpace) {
                String price = nameToPrice.get(name).toString();
                String color = nameToColor.get(name);
                if (entry.getKey().getY() == 10) {
                    BottomPropertyDisplay propSpaces = new BottomPropertyDisplay(name, price, color, myBoardPane, baseColor);
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 10);
                }
                if (entry.getKey().getX() == 0) {
                    LeftPropertyDisplay propSpaces = new LeftPropertyDisplay(name, price, color, myBoardPane, baseColor);
                    myGridPane.add(propSpaces.getMyPropStackPane(), 0, (int) entry.getKey().getY());
                }
                if (entry.getKey().getY() == 0) {
                    System.out.println((int) entry.getKey().getX());
                    TopPropertyDisplay propSpaces = new TopPropertyDisplay(name, price, color, myBoardPane, baseColor);
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 0);
                }
                if (entry.getKey().getX() == 10) {
                    RightPropertyDisplay propSpaces = new RightPropertyDisplay(name, price, color, myBoardPane, baseColor);
                    myGridPane.add(propSpaces.getMyPropStackPane(), 10, (int) entry.getKey().getY());
                }
            }
            if (entry.getValue() instanceof TaxSpace) {
                if (entry.getKey().getY() == 10) {
                    BottomPropertyDisplay propSpaces = new BottomPropertyDisplay(myBoardPane, baseColor, "tax.png");
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 10);
                }
                if (entry.getKey().getX() == 10) {
                    RightPropertyDisplay propSpaces = new RightPropertyDisplay(myBoardPane, baseColor, "tax.png");
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
                    BottomPropertyDisplay propSpaces = new BottomPropertyDisplay(myBoardPane, baseColor, image);
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 10);
                }
                if (entry.getKey().getX() == 10) {
                    RightPropertyDisplay propSpaces = new RightPropertyDisplay(myBoardPane, baseColor, image);
                    myGridPane.add(propSpaces.getMyPropStackPane(), 10, (int) entry.getKey().getY());
                }
                if (entry.getKey().getX() == 0) {
                    LeftPropertyDisplay propSpaces = new LeftPropertyDisplay(myBoardPane, baseColor, image);
                    myGridPane.add(propSpaces.getMyPropStackPane(), 0, (int) entry.getKey().getY());
                }
                if (entry.getKey().getY() == 0) {
                    TopPropertyDisplay propSpaces = new TopPropertyDisplay(myBoardPane, baseColor, image);
                    myGridPane.add(propSpaces.getMyPropStackPane(), (int) entry.getKey().getX(), 0);
                }
            } else {
                CornerDisplay goSpace = new CornerDisplay(baseColor, myBoardPane, "go.png");
                myGridPane.add(goSpace.getMyPropertyStackPane(), 10, 10);
                CornerDisplay parkingSpace = new CornerDisplay(baseColor, myBoardPane, "freeParking.png");
                myGridPane.add(parkingSpace.getMyPropertyStackPane(), 0, 0);
                CornerDisplay jailSpace = new CornerDisplay(baseColor, myBoardPane, "jail.png");
                myGridPane.add(jailSpace.getMyPropertyStackPane(), 0, 10);
                CornerDisplay goToJail = new CornerDisplay(baseColor, myBoardPane, "goToJail.png");
                myGridPane.add(goToJail.getMyPropertyStackPane(), 10, 0);

            }
        }
    }

    private void setUpGridConstraints(){
        final int numCols = 11;
        final int numRows = 11;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(myBoardPane.getPrefWidth() / numCols);
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
        allSpaces = configs.getSpaces();
        myProps = new ArrayList<>(myController.getGame().getBank().getUnOwnedProps());
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        renderPlayers();
    }
}