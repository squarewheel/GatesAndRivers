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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Sasha Kvachenko
 *         Created on 24.05.2016.
 *         <p>
 *         Provides player chip code.
 */
public class ChipActor extends Actor {
    private TextureRegion texture;
    private LinkedList<Actor> fields;
    private Actor currentField;
    private Vector2 offset;
    boolean inMove;
    boolean playable;

    public ChipActor(TextureRegion r, LinkedList<Actor> f) {
        //super();
        inMove = false;
        playable = false;
        texture = r;
        fields = f;
        offset = new Vector2();
        currentField = fields.get(0);
    }

    public boolean isPlayable() { return playable; }

    public boolean isInMove() { return inMove; }

    public Actor getCurrentField() { return currentField; }

    public void moveForward() {
        if (isInMove()) return;

        inMove = true;
        ListIterator<Actor> fieldsItr = fields.listIterator(fields.indexOf(currentField)+1);
        if (fieldsItr.hasNext()) {
            Actor nextField = fieldsItr.next();
            // debug
            //System.out.println("curr: " + currentField.getX() + " " + currentField.getY());
            //System.out.println("next: " + nextField.getX() + " " + nextField.getY());

            offset.set(nextField.getX() + nextField.getWidth()/2 - getX(),
                       nextField.getY() + nextField.getHeight()/2 - getY());
            addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, 2), Actions.delay(0.2f)));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!hasActions() && isInMove()) {
            inMove = false;
            ListIterator<Actor> fieldsItr = fields.listIterator(fields.indexOf(currentField) + 1);
            if (fieldsItr.hasNext()) currentField = fieldsItr.next();
        }
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

}
