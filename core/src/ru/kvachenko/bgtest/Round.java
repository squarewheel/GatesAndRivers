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

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 31.05.2016.
 *         <p>
 *         Round of game iformation.
 */
public class Round {
    private ArrayList<Player> players; // list of players
    private int roundCounter;
    //private int turnsInRound;
    //private int turnCounter;
    private Player currentPlayer;       // indicates whose turn now
    private Label roundCounterLabel;

    public Round(ArrayList<Player> p) {
        // Basic initialization
        players = p;
        roundCounter = 1;
        currentPlayer = p.get(0);
    }

    public void setRoundCounterLabel(Label l) { roundCounterLabel = l; }

    public void update() {
        // if current player made his turn, change currentPlayer to next Player
        if (currentPlayer.isMoved()) currentPlayer = players.get(players.indexOf(currentPlayer) + 1);

        // if all players made his turns, start new round
        int movedPlayers = 0;
        for (Player p: players) if (p.isMoved()) movedPlayers++;
        if (movedPlayers >= players.size()) {
            roundCounter++;
            for (Player p: players) p.setMovedState(false);
        }

        // if label presents, update him
        if (roundCounterLabel != null) roundCounterLabel.setText(toString());
    }

    @Override
    public String toString() {
        return Integer.toString(roundCounter);
    }
}
