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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ru.kvachenko.basegame.AbstractActor;

/**
 * @author Sasha Kvachenko
 *         Created on 24.05.2016.
 *         <p>
 *         Provides player chip code.
 */
public class ChipActor extends AbstractActor {
    private FieldActor currentField;
    private Vector2 offset;
    private boolean busy;

    public ChipActor(TextureRegion r, FieldActor startingField) {
        super(r);
        currentField = startingField;
        busy = false;
        offset = new Vector2();
        setPosition(currentField.getX() + 32, currentField.getY() + 32*3);
    }

    public boolean isBusy() { return busy; }

    public Actor getCurrentField() { return currentField; }

    public void moveForward() {
        if (isBusy()) return;
        if (currentField.hasNextField()) { // TODO: Chip must change movement direction if false
            moveToField(currentField.getNextField());
        }
    }

    public void moveBackward() {
        if (isBusy()) return;
        if (currentField.hasPreviousField()) { // TODO: Chip must change movement direction if false
            moveToField(currentField.getPreviousField());
        }
    }

    private void moveToField(FieldActor targetField) {
        busy = true;
        offset.set(targetField.getX() + targetField.getWidth()/2 - getX(),
                   targetField.getY() + targetField.getHeight()/2 - getY());
        addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, 2), Actions.delay(0.2f)));
        currentField = targetField;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // If chip has not actions, but still busy, need to release him
        if (!hasActions() && isBusy()) {
            busy = false;
        }
    }

}
