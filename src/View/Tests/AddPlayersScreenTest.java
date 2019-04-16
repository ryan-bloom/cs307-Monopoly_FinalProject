package View.Tests;

import View.Main;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;
import static org.junit.jupiter.api.Assertions.*;


public class AddPlayersScreenTest extends DukeApplicationTest {
    // allow easy access within tests to elements of GUI
    private Labeled myLabel;
    private Button myButton;
    private TextInputControl myTextField;
    private Slider mySlider;
    private ColorPicker myPicker;
    private ComboBox<String> myCombo;

    private Button classicButton;
    private Button addPlayerButton;
    private Button startGameButton;


    @BeforeEach
    public void setUp () throws Exception {
        // start GUI new for each test
        launch(Main.class);
        // various ways to find elements in GUI
        // by ID
        myLabel = lookup("#label").queryLabeled();
        myPicker = lookup("#picker").queryAs(ColorPicker.class);
        // by path of IDs
        myTextField = lookup("#pane #inputField").queryTextInputControl();
        // by being the only one of its kind
        mySlider = lookup(".slider").queryAs(Slider.class);
        // by being the only one of its kind within another element
        myCombo = lookup(".grid-pane .combo-box").queryComboBox();
        // by complete text in button
        myButton = lookup("Apply").queryButton();

        // clear text field, just in case
        myTextField.clear();

        classicButton = lookup("#button1").queryButton();
        addPlayerButton = lookup("Add").queryButton();
        startGameButton = lookup("Start Game").queryButton();
    }


    @BeforeEach
    public void startClassicGame(){
        clickOn(classicButton);
    }

    @Test
    public void startGameButtonDisabledIfNoPlayersAddedYet(){
        assertTrue(addPlayerButton.isDisabled());
    }

    @Test
    public void startGameButtonDisabledIfPlayerAddedThenDeleted(){

    }

    @Test
    public void addButtonDisabledIfMaxNumberPlayersAdded(){

    }

    @Disabled
    public void testTextFieldAction () {
        var expected = "ENTER test!";
        clickOn(myTextField).write(expected).write(KeyCode.ENTER.getChar());
        assertEquals(expected, myLabel.getText());
    }

    @Test
    public void testButtonAction () {
        var expected = "CLICK test!";
        clickOn(myTextField).write(expected);
        clickOn(myButton);
        assertEquals(expected, myLabel.getText());
    }

    @Disabled
    public void testComboBoxAction () {
        var expected = "b";
        select(myCombo, expected);
        assertEquals(expected, myLabel.getText());
    }

    @Disabled
    public void testSliderAction () {
        var expected = "50.0";
        setValue(mySlider, 50);
        assertEquals(expected, myLabel.getText());
    }

    @Disabled
    public void testColorPickerAction () {
        var expected = Color.RED;
        setValue(myPicker, expected);
        assertEquals(expected.toString(), myLabel.getText());
    }
}
