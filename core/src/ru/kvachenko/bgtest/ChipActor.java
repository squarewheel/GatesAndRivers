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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ru.kvachenko.basegame.AbstractActor;

/**
 * @author Sasha Kvachenko
 *         Created on 24.05.2016.
 *         <p>
 *         Provides player chip code.
 */
public class ChipActor extends AbstractActor {

//    /** Possible chip states */
//    public enum State {
//        READY,
//        MOVEMENT,
//        POSITIONING,
//        MOVED
//    }

    public enum Direction {
        FORWARD,
        BACKWARD
    }

    private FieldActor currentField;    // Current location of chip
    //private Vector2 offset;
    //private State state;
    private boolean busy;               // While chip moves she is busy
    private Direction direction;

    public ChipActor() {
        super();
        Texture playerTexture = (new Texture("chip_white.png"));
        playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setTexture(new TextureRegion(playerTexture, 64, 64));
        currentField = FieldActor.getFieldsList().get(0);
        busy = false;
        direction = Direction.FORWARD;

        //state = State.READY;
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

    public boolean isBusy() { return busy; }

    //public Actor getCurrentField() { return currentField; }

    /** Move chip to next Field */
    public void moveForward() {
        if (direction == Direction.FORWARD && currentField.hasNextField()) moveToField(currentField.getNextField());
        else {
            direction = Direction.BACKWARD;
            moveBackward();
        }
    }

    /** Move chip to previous Field */
    public void moveBackward() {
        if (direction == Direction.BACKWARD && currentField.hasPreviousField()) moveToField(currentField.getPreviousField());
        else {
            direction = Direction.FORWARD;
            moveForward();
        }
    }

    /** Move chip to center of target field. By default chip takes center of target field.
     *  @param targetField field, which should be occupied by chip */
    private void moveToField(FieldActor targetField) {
        //state = State.MOVEMENT;
        currentField.getLayout().removeChip(this);
        busy = true;
        addAction(Actions.after(Actions.sequence(
                Actions.moveBy(targetField.getX() + targetField.getLayout().getFieldCenterX() - getX(),
                               targetField.getY() + targetField.getLayout().getFieldCenterY() - getY(), 1.5f), Actions.delay(0.1f))));
        currentField = targetField;
    }

    /** Sets the position of the chip relative to other chips on the current field */
    public void takePosition() {
        //state = State.POSITIONING;
        busy = true;
        currentField.getLayout().addChip(this);
    }

    public Direction getDirection() {
        return direction;
    }

    /** Switch chip movement direction. */
    public void changeDirection() {
        if (direction == Direction.FORWARD) direction = Direction.BACKWARD;
        else direction = Direction.FORWARD;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // If chip has not actions, but still busy, need to release it
        if (!hasActions() && isBusy()) busy = false;
    }

}
