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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ru.kvachenko.basegame.BaseActor;
import ru.kvachenko.gatesandrivers.gameboard.Fields;

/**
 * @author Sasha Kvachenko
 *         Created on 24.05.2016.
 *         <p>
 *         Provides player chip code.
 */
public class ChipActor extends BaseActor {

    /** Possible chip states */
    public enum State {
        WAIT,
        READY,
        MOVEMENT,
        POSITIONING,
        MOVED
    }

    /** Movement directions */
    public enum Direction {
        FORWARD,
        BACKWARD
    }

    private FieldActor currentField;    // Current location of chip
    private int moves;                  // Num of fields what chip must move to take MOVED state
    private State state;                // Current state
    private State previousState;        // Previous state
    private Direction direction;

    public ChipActor() {
        super();
        Texture playerTexture = (new Texture("chip_white.png"));
        playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setTexture(new TextureRegion(playerTexture, 64, 64));
        currentField = Fields.getFieldsList().get(0);
        moves = 0;
        //busy = false;
        direction = Direction.FORWARD;
        state = State.READY;
        previousState = State.WAIT;
        setPosition(currentField.getX() + currentField.getLayout().getFieldCenterX(),
                    currentField.getY() + currentField.getLayout().getFieldCenterY());
    }

/*
    public ChipActor(TextureRegion r, FieldActor startingField) {
        super(r);
        currentField = startingField;
        busy = false;
        offset = new Vector2();
        setPosition(currentField.getPositionX() + 32, currentField.getPositionY() + 32*3);
    }
*/

    public FieldActor getCurrentField() { return currentField; }

    /** Move chip to next Field. */
    private void moveForward() {
        if (direction == Direction.FORWARD && currentField.hasNextField()) moveToField(currentField.getNextField());
        else {
            direction = Direction.BACKWARD;
            moveBackward();
        }
    }

    /** Move chip to previous Field. */
    private void moveBackward() {
        if (direction == Direction.BACKWARD && currentField.hasPreviousField()) moveToField(currentField.getPreviousField());
        else {
            direction = Direction.FORWARD;
            moveForward();
        }
    }

    /** Move chip to center of target field. By default chip takes center of target field.
     *  @param targetField field, which should be occupied by chip */
    protected void moveToField(FieldActor targetField) {
        if (state == State.READY) {
            state = State.MOVEMENT;
            currentField.getLayout().removeChip(this);
            addAction(Actions.after(Actions.sequence(
                    Actions.moveBy(targetField.getX() + targetField.getLayout().getFieldCenterX() - getX(),
                                   targetField.getY() + targetField.getLayout().getFieldCenterY() - getY(), 1.5f), Actions.delay(0.1f))));
            currentField = targetField;
        }
    }

    /** Sets the position of the chip relative to other chips on the current field. */
    protected void takePosition() {
        if (state != State.MOVEMENT && state != State.POSITIONING) {
            previousState = state;
            state = State.POSITIONING;
            if (currentField.hasMover()) {
                state = State.READY;
                currentField.getMover().move(this);
            }
            else currentField.getLayout().addChip(this);
        }
    }

    /** Set num of fields what chip must move in to take MOVED state. */
    public void moveOn(int i) {
        if (state == State.WAIT) moves = i;
    }

    /** Switch chip movement direction. */
    private void changeDirection() {
        if (direction == Direction.FORWARD) direction = Direction.BACKWARD;
        else direction = Direction.FORWARD;
    }

    //public Direction getDirection() { return direction; }

    public State getState() { return state; }

    public void setState(State newState) { state = newState; }

    @Override
    public void act(float delta) {
        super.act(delta);

        switch (state) {
            case WAIT:              // Chip wait until moves been set
                if (moves > 0) state = State.READY;
                break;

            case READY:
                if (moves > 0) {    // While has moves, chip moves forward
                    moves--;
                    moveForward();
                }
                else {              // When moves ends, positing chip on field
                    if (direction == ChipActor.Direction.BACKWARD) changeDirection();
                    takePosition();
                    //state = State.POSITIONING;
                }
                break;

            case MOVEMENT:          // When chip end current action, release chip
                if (!hasActions()) state = State.READY;
                break;

            case POSITIONING:       // When chip end positing, chip is moved
                if (!hasActions()) state = (previousState != State.READY) ? previousState : State.MOVED;
                break;
        }
    }
}
