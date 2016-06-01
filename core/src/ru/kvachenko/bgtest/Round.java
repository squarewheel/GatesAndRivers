/*******************************************************************************
 * Copyright (c) 2016 Kvachenko A. [feedback@kvachenko.ru]
 * Licensed under the GNU General Public License version 3 as published
 *  by the Free Software Foundation.
 *
 * You may obtain a copy of the License at:
 *  http://www.gnu.org/licenses/
 *
 * This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 ******************************************************************************/

package ru.kvachenko.bgtest;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Sasha Kvachenko
 *         Created on 31.05.2016.
 *         <p>
 *         The game is divided into rounds.
 *         Round ends when all players have complete their turns (made their move). In round end checks win conditions,
 *         if mo one player resolve win conditions, starts new round.
 *         Turn of each player divided into several phases.
 */
public class Round {

    public class RoundCounterLabel extends Label {
        public RoundCounterLabel(CharSequence text, Skin skin, String styleName) {
            super(text, skin, styleName);
            updateCounter();
            setRoundCounterLabel(this);
        }

        private void updateCounter() { setText("Round: " + roundCounter); }

        @Override
        public void act(float delta) {
            super.act(delta);
            updateCounter();
        }
    }

    /** Possible turn phases */
    public enum TurnPhase {
        START,
        DICE_ROLLING,
        DICE_ROLLED,
        MOVEMENT,
        END
    }

    private ArrayList<Player> players;  // list of players
    private Player currentPlayer;       // indicates whose turn now
    private TurnPhase turnPhase;
    private int roundCounter;
    private Label roundCounterLabel;
    //private int turnsInRound;
    //private int turnCounter;

    public Round(ArrayList<Player> p) {
        // Basic initialization
        players = p;
        roundCounter = 1;
        currentPlayer = p.get(0);
        turnPhase = TurnPhase.START;
    }

    public void setRoundCounterLabel(Label l) { roundCounterLabel = l; }

    public void setTurnPhase(TurnPhase phase) { turnPhase = phase; }

    public Player getCurrentPlayer() { return currentPlayer; }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public void endTurn() {
        // if all players made his turns, start new round
        Iterator<Player> playersItr = players.listIterator(players.indexOf(currentPlayer)+1);
        if (playersItr.hasNext()) {
            currentPlayer = playersItr.next();
        }
        else {
            for (Player p: players) p.setMovedState(false);
            roundCounter++;
            currentPlayer = players.get(0);
        }
        //setTurnPhase(TurnPhase.START);
    }

//    public void update() {
//        // if current player made his turn, change currentPlayer to next Player
//        if (currentPlayer.isMoved()) currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
//
//        // if all players made his turns, start new round
//        int movedPlayers = 0;
//        for (Player p: players) if (p.isMoved()) movedPlayers++;
//        if (movedPlayers >= players.size()) {
//            roundCounter++;
//            for (Player p: players) p.setMovedState(false);
//        }
//
//        // if label presents, update him
//        if (roundCounterLabel != null) roundCounterLabel.setText(toString());
//    }

    @Override
    public String toString() {
        return Integer.toString(roundCounter);
    }
}
