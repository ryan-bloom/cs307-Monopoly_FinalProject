package View.PopUps;

import Controller.Controller;
import Model.AbstractPlayer;
import Model.spaces.AbstractSpace;
import View.BoardConfigReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;

public class BuyPropertyPopup extends Popup {

    private Map<Integer, ArrayList> colorPropInfo;
    private Map<Point2D.Double, AbstractSpace> indexToName;
    private int propLocation;
    private ArrayList propDetails;
    private String name;
    private Controller myController;

    public BuyPropertyPopup(String title, String message, int propLocation, Controller controller) {
        super(title, message);
        this.propLocation = propLocation;
        BoardConfigReader spaceInfo = new BoardConfigReader();
        colorPropInfo = spaceInfo.getColorPropInfo();
        this.myController = controller;
    }

    public BuyPropertyPopup(String title, int propLocation) {
        super(title);
        this.propLocation = propLocation;
        BoardConfigReader spaceInfo = new BoardConfigReader();
        colorPropInfo = spaceInfo.getColorPropInfo();
    }


    @Override
    protected Pane createImage(Scene scene, Stage popUpWindow) {
        propDetails = new ArrayList();
        Pane imagePane = new Pane();
        Rectangle rectangle = new Rectangle(scene.getWidth()/2.5, scene.getHeight()/1.5);
        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        for (Map.Entry<Integer,ArrayList> key : colorPropInfo.entrySet()) {
            if ((key.getKey()).equals(propLocation)){
                for (Object item : key.getValue()){
                    System.out.println(item);
                    propDetails.add(item);
                }
            }
        }

        if (propLocation==5 || propLocation==15 || propLocation==25 || propLocation==35){
            var imageFile = new Image(this.getClass().getClassLoader().getResourceAsStream("railroad.png"));
            ImageView image = new ImageView(imageFile);
            imagePane = new Pane(rectangle, image);
            name = propDetails.get(6).toString().replace("_", " ");
        }
        else if (propLocation==12 || propLocation==28){
            imagePane = new Pane(rectangle);
            name = propDetails.get(4).toString().replace("_", " ");
        }
        else{
            Rectangle propColor = new Rectangle(scene.getWidth()/2.5, scene.getHeight()/7);
            propColor.setStroke(Color.BLACK);
            propColor.setFill(Color.web(propDetails.get(0).toString()));
            imagePane = new Pane( rectangle, propColor );
            name = propDetails.get(10).toString().replace("_"," ");

        }
        StackPane fullImage = new StackPane(imagePane, propertyInfo(scene));

        return fullImage;
    }

    @Override
    protected Pane createButtons(Stage popUpWindow) {
        HBox buttons = new HBox(10);
        Button button1= new Button("YES");
        Button button2= new Button("NO");
        button1.setId("button2");
        button2.setId("button2");
//        button2.setOnAction(e -> new AuctionPopup("Auction", "Player #, would you like to purchase this property?", propLocation, name).display());
        button2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                popUpWindow.close();
                Popup myPopup = new AuctionPopup("Auction",  myController.getGame().getRightPlayer().getName() +", would you like to purchase this property?", propLocation, name, myController);
                myPopup.display();
            }
        });
        button1.setOnAction(e -> popUpWindow.close());
        buttons.getChildren().addAll(button1,button2);

        return buttons;
    }

//    @Override
//    protected Scene setSizeOfPopup(BorderPane layout) {
//        Scene scene1= new Scene(layout, Controller.WIDTH/2, Controller.HEIGHT/1.5);
//        return scene1;
//    }

    private FlowPane propertyInfo(Scene scene){
        FlowPane textPane = new FlowPane();
        HBox priceProp = new HBox();
        priceProp.setPrefWidth(scene.getWidth()/2.5);
        priceProp.setAlignment(Pos.CENTER);
        Text rent;
        Text mortgage;
        Text rent1House;
        Text rent2House;
        Text rent3House;
        if (name.toLowerCase().contains("railroad")){
            priceProp.getChildren().add(new Text("Price: $" + propDetails.get(0)));
            rent = new Text("Rent: $" + propDetails.get(1));
            rent1House = new Text("Rent if 2 owned: $" + propDetails.get(2));
            rent2House = new Text("Rent if 3 owned: $" + propDetails.get(3));
            rent3House = new Text("Rent if 4 owned: $" + propDetails.get(4));
            mortgage = new Text("Mortgage: $" + propDetails.get(5));
            textPane.getChildren().addAll(priceProp, rent,rent1House,rent2House,rent3House,mortgage);
        }
        else if (name.toLowerCase().contains("works") || name.toLowerCase().contains("company")){
            priceProp.getChildren().add(new Text("Price: $" + propDetails.get(0)));
            rent = new Text("If one utility is owned, rent is " + propDetails.get(1) + " times amount shown on dice.");
            rent2House = new Text("If both utilities are owned, rent is " + propDetails.get(2) + " times amount shown on dice.");
            mortgage = new Text("Mortgage: $" + propDetails.get(3));
            textPane.getChildren().addAll(priceProp, rent,rent2House,mortgage);
        }
        else{
            priceProp.getChildren().add(new Text("Price: $" + propDetails.get(1)));
            rent = new Text("Rent: $" + propDetails.get(2));
            rent1House = new Text("Rent w/ 1 House: $" + propDetails.get(3));
            rent2House = new Text("Rent w/ 2 Houses: $" + propDetails.get(4));
            rent3House = new Text("Rent w/ 3 Houses: $" + propDetails.get(5));
            Text rent4Houses = new Text("Rent w/ 4 Houses: $" + propDetails.get(6));
            Text rentHotel = new Text("Rent w/ Hotel: $" + propDetails.get(7));
            Text costHouse = new Text("Cost of 1 House: $" + propDetails.get(8));
            mortgage = new Text("Mortgage: $" + propDetails.get(9));
            textPane.getChildren().addAll(priceProp, rent,rent1House,rent2House,rent3House,rent4Houses,rentHotel,costHouse,mortgage);
        }
        textPane.setPrefWrapLength(scene.getWidth()/2.5);
        textPane.setAlignment(Pos.BOTTOM_CENTER);
        textPane.setVgap(2);
        textPane.setId("propPopUp");
        textPane.setPadding(new Insets(0,0,10,0));
        return textPane;
    }


    @Override
    protected Label createHeader() {
        Label title = new Label("Welcome to " + name + "!");
        return title;
    }


}