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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ru.kvachenko.basegame.AbstractActor;

/**
 * @author Sasha Kvachenko
 *         Created on 24.05.2016.
 *         <p>
 *         Provides player chip code.
 */
public class ChipActor extends AbstractActor {

    /** Possible chip states */
    public enum State {
        READY,
        MOVEMENT,
        POSITIONING,
        MOVED
    }

    private FieldActor currentField;    // Current location of chip
    private Vector2 offset;
    private State state;
    private boolean busy;               // While chip moves she is busy

    public ChipActor() {
        super();
        Texture playerTexture = (new Texture("chip_white.png"));
        playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setTexture(new TextureRegion(playerTexture, 64, 64));
        currentField = FieldActor.getFieldsList().get(0);
        busy = false;
        offset = new Vector2();
        state = State.READY;
        setPosition(currentField.getX() + 64, currentField.getY() + 64);
        //takePosition();
    }

/*
    public ChipActor(TextureRegion r, FieldActor startingField) {
        super(r);
        currentField = startingField;
        busy = false;
        offset = new Vector2();
        setPosition(currentField.getX() + 32, currentField.getY() + 32*3);
    }
*/

    public boolean isBusy() { return busy; }

    //public Actor getCurrentField() { return currentField; }

    /** Move chip to next Field */
    public void moveForward() {
        if (isBusy()) return;
        if (currentField.hasNextField()) { // TODO: Chip must change movement direction if false
            moveToField(currentField.getNextField());
        }
    }

    /** Move chip to previous Field */
    public void moveBackward() {
        if (isBusy()) return;
        if (currentField.hasPreviousField()) { // TODO: Chip must change movement direction if false
            moveToField(currentField.getPreviousField());
        }
    }

    private void moveToField(FieldActor targetField) {
        state = State.MOVEMENT;
        // TODO: remove chip from current field layout
        busy = true;
        offset.set(targetField.getX() + targetField.getWidth()/2 - getX(),
                   targetField.getY() + targetField.getHeight()/2 - getY());
        addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, 2), Actions.delay(0.2f)));
        currentField = targetField;
    }

    /** Sets the position of the chip relative to other chips on the current field */
    public void takePosition() {
        state = State.POSITIONING;
        currentField.getLayout().addChip(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // If chip has not actions, but still busy, need to release she
        if (!hasActions() && isBusy()) busy = false;
    }

}
