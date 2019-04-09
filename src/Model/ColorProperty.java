package Model;

public class ColorProperty extends Property{

    String myColor;

    public ColorProperty(double price, String color){
        super(price);
        myColor=color;
    }
    /***
     * A getter method that returns the cost to place a house on this property
     * @return the cost to place a house on this property
     */
    public double getCostofHouse(){
        return 0.0;
    }

    /***
     * A getter method that returns the cost to place a hotel on this property
     * @return the cost to place a hotel on this property
     */
    public double getCostofHotel(){
        return 0.0;
    }

    /***
     * Allows for users to see the color of a property which is crucial for monopolies
     * @return the color of this property
     */
    public String getPropertyColor(){
        return myColor;
    }

    /***
     * A method that utilizes the member variables to calculate how
     * much it costs when someone lands on this property
     * @return the total rent value to be paid
     */
    public double calculateRent(){
        return 0.0;
    }
}