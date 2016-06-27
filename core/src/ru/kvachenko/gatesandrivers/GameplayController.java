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

package ru.kvachenko.gatesandrivers;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Sasha Kvachenko
 *         Created on 31.05.2016.
 *         <p>
 *         The game is divided into rounds.
 *         Round ends when all players have complete their turns (made their move).
 *         In entTurn checks win conditions, if no one player resolve win conditions, starts new round.
 *         Turn of each player divided into several phases.
 *         TODO: totally refactor this class
 */
public class GameplayController {

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
        PREPARATION,
        START,
        DICE_ROLLING,
        DICE_ROLLED,
        MOVEMENT,
        END
    }

    private DiceWidget dice;
    private CheckBox autoRollSwitch;

    private ArrayList<Player> players;  // list of players
    private Player currentPlayer;       // indicates whose turn now
    private TurnPhase turnPhase;        // Current phase of turn
    private int roundCounter;
    private Label roundCounterLabel;
    private Label infoLabel;
    private Player winner;              // If win condition is done, game not ends
    //private int turnsInRound;
    //private int turnCounter;

    public GameplayController(ArrayList<Player> p, Skin skin) {
        // Basic initialization
        dice = new DiceWidget();
        autoRollSwitch = new CheckBox("", skin, "checkBoxStyle");
        infoLabel = new Label("Im Love my Wife", skin, "infoLabelStyle");

        players = p;
        roundCounter = 0;
        currentPlayer = p.get(p.size()-1);
        turnPhase = TurnPhase.END;

        dice.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Dice can be rolled only in START phase
                if (turnPhase == TurnPhase.START) {
                    if (dice.isActive() && dice.getState() == DiceWidget.State.READY) {
                        dice.roll();
                        setTurnPhase(TurnPhase.DICE_ROLLING);
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //return !paused;   // TODO: return paused check
                return true;
            }
        });
        autoRollSwitch.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dice.isActive()) dice.unActivate();
                else if (currentPlayer.isPlayable() && dice.getState() == DiceWidget.State.READY) dice.activate();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    public void setRoundCounterLabel(Label l) { roundCounterLabel = l; }

    //public void setInfoLabel(Label l) { infoLabel = l; }

    public void setTurnPhase(TurnPhase phase) { turnPhase = phase; }

    public Player getCurrentPlayer() { return currentPlayer; }

    public Label getInfoLabel() {
        return infoLabel;
    }

    public DiceWidget getDice() { return dice; }

    public CheckBox getAutoRollSwitch() {
        return autoRollSwitch;
    }

//    public TurnPhase getTurnPhase() { return turnPhase; }

    public boolean gameOver() {
        return winner != null;
    }

    public void endTurn() {
        if (winner != null) return; // If game over, do nothing

        // if all players made his turns, start new round
        Iterator<Player> playersItr = players.listIterator(players.indexOf(currentPlayer)+1);
        if (playersItr.hasNext()) currentPlayer = playersItr.next();
        else {
            int chipsFinished = 0;
            Player possibleWinner = null;
            for (Player p: players) {
                if (FieldActor.getFieldsList().indexOf(p.getChip().getCurrentField()) ==
                        (FieldActor.getFieldsList().size() - 1)) {
                    chipsFinished++;
                    possibleWinner = p;
                }
                p.getChip().setState(ChipActor.State.WAIT);
            }
            if (chipsFinished == 1) {
                winner = possibleWinner;
                if (infoLabel != null) {
                    infoLabel.setText("Winner is " + winner.getName());
                    infoLabel.setColor(winner.getChip().getColor());
                    infoLabel.addAction(Actions.after(Actions.fadeIn(0.1f)));
                }
            }
            else {
                roundCounter++;
                currentPlayer = players.get(0);
            }
        }
        //setTurnPhase(TurnPhase.START);
    }

    public void update() {
        // Update game state
        switch (turnPhase) {
            // TODO: encapsulate game progress into Round class
            case PREPARATION:   // Prepare game state to new turn
                //if (debug) System.out.println("TurnPhase.PREPARATION");
                if (!infoLabel.hasActions()) {
                    infoLabel.setText(currentPlayer.getName() + " Turn");
                    infoLabel.setColor(currentPlayer.getChip().getColor());
                    infoLabel.addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(1.5f), Actions.fadeOut(0.5f)));
                    if (currentPlayer.isPlayable() && !autoRollSwitch.isChecked()) dice.activate();
                    dice.setState(DiceWidget.State.READY);
                    setTurnPhase(TurnPhase.START);
                }
                break;

            case START: // Start of current player turn. Phase end when user or ai roll dice
                //if (debug) System.out.println("TurnPhase.START");
                if (!infoLabel.hasActions()) {
                    if (!currentPlayer.isPlayable() || autoRollSwitch.isChecked()) dice.roll();
                    if (dice.getState() == DiceWidget.State.ROLLING) setTurnPhase(TurnPhase.DICE_ROLLING);
                }
                break;

            case DICE_ROLLING:  // Wait when dice roll animation finish
                //if (debug) System.out.println("TurnPhase.DICE_ROLLING");
                if (dice.getState() == DiceWidget.State.ROLLED) {
                    dice.unActivate();
                    setTurnPhase(TurnPhase.DICE_ROLLED);
                }
                break;

            case DICE_ROLLED:   // Determines number of fields what current player must go
                // TODO: looks like this phase is not necessary
                //if (debug) System.out.println("TurnPhase.DICE_ROLLED");
                currentPlayer.getChip().moveOn(dice.getRollResult());
                setTurnPhase(TurnPhase.MOVEMENT);
                break;

            case MOVEMENT:  // Move current player chip
                //if (debug) System.out.println("TurnPhase.MOVEMENT");
                //gameController.getCurrentPlayer().move();
                if (currentPlayer.isMoved()) setTurnPhase(TurnPhase.END);
                break;

            case END:   // End current turn
                //if (debug) System.out.println("TurnPhase.END");
                if (!infoLabel.hasActions()) {
                    endTurn();
                    if (!gameOver()) setTurnPhase(TurnPhase.PREPARATION);
                }
                break;
        }
    }

    @Override
    public String toString() {
        return Integer.toString(roundCounter);
    }
}
