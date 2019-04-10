package Controller;

import Model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractGame {
    private int boardSize = 0;

    //RULES
    private double startFunds;
    private double jailBail;
    private double passGo;

    private List<AbstractPlayer> players;
    private Bank bank;
    private Board board;
    private List<AbstractSpace> spaces;
    private List<Property> properties;
    private AbstractPlayer currPlayer;
    private List<Die> dice;
    private List<ActionDeck> decks;
    private HashMap<Integer, ArrayList<Integer>> diceHistory = new HashMap<Integer, ArrayList<Integer>>();
    private List<String> possibleTokens;

    public AbstractGame(String filename) {
        parseXMLFile(filename);
        for(int i = 0; i < dice.size(); i++) {
            diceHistory.put(i, new ArrayList<>());
        }
    }

    private void parseXMLFile(String filename) {
        ConfigReader configReader = new ConfigReader(filename);
        try {
            decks = configReader.parseActionDecks();
            List<AbstractActionCard>  allCards = configReader.parseActionCards();
            for(ActionDeck d : decks) {
                d.fillLiveDeck(allCards);
            }
            possibleTokens = configReader.parseTokens();
            dice = configReader.parseDice();
            double funds = configReader.parseBank();

            boardSize = configReader.parseBoard();
            List<List> spaceProps= configReader.parseSpaces();
            spaces = spaceProps.get(0);
            properties = spaceProps.get(1);
            bank = new Bank(funds, properties);
            board = new Board(boardSize, spaceProps.get(0));
            startFunds = configReader.getRuleDouble("StartFunds");
            jailBail = configReader.getRuleDouble("JailBail");
            passGo = configReader.getRuleDouble("PassGo");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayers(List<AbstractPlayer> p){
        if (p.size() <=0 ) {
            //TODO: throw some "can't initialize players w empty list" exception
        }
        players = p;
        this.currPlayer = p.get(0);
        for (AbstractPlayer pl : players){
            System.out.println(pl);
        }
        System.out.println("set players in game done");
    }

    public AbstractPlayer getCurrPlayer() {
        return currPlayer;
    }

    public AbstractPlayer getLeftPlayer() {
        int playerIndex = players.indexOf(currPlayer);
        int leftIndex = playerIndex + 1;
        if(leftIndex > players.size() - 1) {
            leftIndex = 0;
        }
        return players.get(leftIndex);
    }

    public AbstractPlayer getRightPlayer() {
        int playerIndex = players.indexOf(currPlayer);
        int rightIndex = playerIndex - 1;
        if(rightIndex < 0) {
            rightIndex = players.size() - 1;
        }
        return players.get(rightIndex);
    }

    public List<AbstractPlayer> getPlayers() {
        return players;
    }

    public int rollDice() {
        int value = 0;
        for(int i = 0; i < dice.size(); i++) {
            int roll = dice.get(i).rollDie();
            value += roll;
            diceHistory.get(i).add(roll);
        }
        return value;
    }

    public Bank getBank() {
        return bank;
    }

    public Board getBoard() {
        return board;
    }

    public void startNextTurn() {
        int index = players.indexOf(currPlayer) + 1;
        if(index > (players.size() - 1)) {
            index = 0;
        }
        currPlayer = players.get(index);
    }

    //checks 3 matching all dice in a row
    public boolean checkDoubles() {
        ArrayList<Integer> firstDie = diceHistory.get(0);
        List<Integer> check = firstDie.subList(firstDie.size() - 3, firstDie.size());
        for(Integer key : diceHistory.keySet()) {
            ArrayList<Integer> otherDie = diceHistory.get(key);
            List<Integer> other = otherDie.subList(otherDie.size() - 3, otherDie.size());
            if(!check.equals(other)) {
                return false;
            }
        }
        return true;
    }

    public List<ActionDeck> getMyActionDecks(){return decks;}

    //instantiate a player and add it to the list
    public void addPlayer(AbstractPlayer player) {
        player.setFunds(startFunds);
        Token token = new Token(0);
        player.setToken(token);
    }

    public List<String> getPossibleTokens() {
        return possibleTokens;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void endTurn() {

    }


}
