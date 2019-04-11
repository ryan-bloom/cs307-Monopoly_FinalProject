package Model;

import Model.properties.Property;

import java.util.*;

public class Bank implements Transfer{

    Map<Property, AbstractPlayer> ownedPropsMap = new HashMap<>();
    Set<Property> unOwnedProps;
    double myBalance;

    @Deprecated
    public Bank(double startingBalance, List<Property> properties){
        myBalance=startingBalance;
        unOwnedProps = new HashSet<Property>(properties);
    }

    public Bank(List<Double> allInfo, List<Property> properties){
        myBalance=allInfo.get(0);
        unOwnedProps = new HashSet<Property>(properties);
    }


    /***
     * Returns the player that owns a specific property, or null if no one does
     * @param property
     * @return
     */
    public AbstractPlayer propertyOwnedBy(Property property){
        if(ownedPropsMap.containsKey(property)){
            return ownedPropsMap.get(property);
        }
        else if (unOwnedProps.contains(property)){
            return null;
        }
        //need to turn this into a try catch
        return null;
    }

    public void setPropertyOwner(Property property, AbstractPlayer newOwner){
        if(ownedPropsMap.containsKey(property)){
            ownedPropsMap.put(property, newOwner);
        }
        else if(unOwnedProps.contains(property)){
            System.out.println("here");
            ownedPropsMap.put(property, newOwner);
            unOwnedProps.remove(property);
        }
    }

    /***
     * Allows a player/bank (or anyone that implements Transfer) to pay others
     * @param amount
     * @param receiver
     */
    public void makePayment(double amount, Transfer receiver){
        if(myBalance-amount>0){
            myBalance -= amount;
            receiver.receivePayment(amount);
        }
        else{
            receiver.receivePayment(myBalance);
            myBalance = 0;
        }
    }

    /***
     * Allows anyone that implements Transfer to receive a payment from another
     * @param amount
     */
    public void receivePayment(double amount){
        myBalance+=amount;
    }

    /***
     * Sells a specific property to a player
     * @param property
     * @param purchaser
     */
    public void sellProperty(Property property, AbstractPlayer purchaser){}

    /***
     * Auctions off a property to the list of players at the auction
     * @param property
     * @param auctionGoers
     */
    public void auctionProperty(Property property, List<AbstractPlayer> auctionGoers){}

    public double getBankBalance(){
        return myBalance;
    }
}
