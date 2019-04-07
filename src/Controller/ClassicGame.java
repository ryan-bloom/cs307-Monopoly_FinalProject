package Controller;

import Model.AbstractPlayer;
import Model.Bank;
import Controller.Die;
import Model.ActionDeck;

import java.util.ArrayList;

public class ClassicGame extends AbstractGame {

    public ClassicGame(ArrayList<AbstractPlayer> players, Bank bank, Board board, Die[] dice, ArrayList<ActionDeck> decks) {
        super(players, bank, board, dice, decks);
    }

}