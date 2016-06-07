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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author Sasha Kvachenko
 *         Created on 25.05.2016.
 *         <p>
 *         Provides gameboard field.
 *         Field must be created before players;
 */
public class FieldActor extends Actor{
    private static ArrayList<FieldActor> fieldsList = new ArrayList<FieldActor>();
    private FieldActor nextField;
    private FieldActor previousField;
    private PositionsLayout layout;

    //static { fieldsList = new ArrayList<FieldActor>(); }

    public FieldActor() {
        fieldsList.add(this);
        ListIterator<FieldActor> fieldsItr = fieldsList.listIterator(fieldsList.indexOf(this));
        previousField = fieldsItr.hasPrevious() ? fieldsItr.previous() : null;
        if (hasPreviousField()) getPreviousField().setNextField(this);
        layout = new PositionsLayout();
    }

    public FieldActor getNextField() {
        return nextField;
    }

    public FieldActor getPreviousField() {
        return previousField;
    }

    public PositionsLayout getLayout() {
        return layout;
    }

    public boolean hasNextField() {
        return nextField != null;
    }

    public boolean hasPreviousField() {
        return previousField != null;
    }

    public static ArrayList<FieldActor> getFieldsList() {
        return fieldsList;
    }

    private void setNextField(FieldActor field) {
        nextField = field;
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        layout.setSize();
    }

    /** Determines chips location on the field */
    private class FieldLayoutPosition {
        private ChipActor owner;            // Current owner of this position
        //private boolean availability;     // True if position is free
        private float x, y;                 // Current position (relative to field)

        FieldLayoutPosition() {
            owner = null;
            //availability = true;
            // Initial position is center of field 5x5
            x = 2 * (getWidth()/5);
            y = 2 * (getHeight()/5);
        }

        FieldLayoutPosition(float initX, float initY) {
            owner = null;
            setPosition(initX, initY);
        }

        //void setAvailability(boolean newState) { availability = newState; }

        void setOwner(ChipActor chip) {
            owner = chip;
            owner.addAction(Actions.moveBy(getX() - layout.getFieldCenterX(), getY() - layout.getFieldCenterY(), 0.3f));
        }

        void release() { owner = null; }

        void setPosition(float newX, float newY) {
            if (!isFree()) owner.addAction(Actions.moveBy(newX - getX(), newY - getY(), 0.3f));
            x = newX;
            y = newY;
        }

        //boolean isAvailable() { return availability; }

        boolean isFree() {
            if (owner == null) return true;
            return false;
        }

        float getX() {
            return x;
        }

        float getY() {
            return y;
        }

        int getIndex() { return layout.positionsList.indexOf(this); }
    }

    /** Field have four possible chip positions.
     *  Positions arranged in rows, each field divided on two rows, by two positions in row. */
    class PositionsLayout {
        public final int FIRST_POS = 0;
        public final int SECOND_POS = 1;
        public final int THIRD_POS = 2;
        public final int FOURTH_POS = 3;

        private ArrayList<FieldLayoutPosition> positionsList;
        //private ArrayList<ChipActor> chips; // Chips on the current field
        //private LinkedHashMap<FieldLayoutPosition, ChipActor> positionsMap;
        /*  Indent from left edge of field; determines X position of chip */
        //private float position1, position2, position3, position4;
        private float positionWidth;            // By default, 1/5 of field width
        private float positionHeight;           // By default, 1/5 of field height
        private float fieldCenterX;             // Indent from left edge to center of field
        private float fieldCenterY;             // Indent from bottom edge to center of field
        private float rowOneIndent;               /* First row indent from bottom edge of field;
                                                 determines Y position of each chip in row */
        private float rowTwoIndent;               /* Second row indent from bottom edge of field;
                                                 determines Y position of each chip in row */

        PositionsLayout() {
            positionsList = new ArrayList<FieldLayoutPosition>(4);
            setSize();
            for (int i = 0; i < 4; i++) positionsList.add(new FieldLayoutPosition());
        }

        void setSize() {
            positionWidth = getWidth()/5;
            positionHeight = getHeight()/5;
            fieldCenterX = 2 * positionWidth;
            fieldCenterY = 2 * positionHeight;
            rowOneIndent = 2 * positionHeight;
            rowTwoIndent = positionHeight;

            if (!positionsList.isEmpty()) {
                positionsList.get(FIRST_POS).setPosition(fieldCenterX, rowOneIndent);
                positionsList.get(SECOND_POS).setPosition(fieldCenterX + positionWidth, rowOneIndent);
                positionsList.get(THIRD_POS).setPosition(fieldCenterX, rowTwoIndent);
                positionsList.get(FOURTH_POS).setPosition(fieldCenterX + positionWidth, rowTwoIndent);
            }
        }

        void addChip(ChipActor chip) {
            FieldLayoutPosition p = nextFreePosition();
            switch (p.getIndex()) {
                case FIRST_POS:
                    //chip.addAction(Actions.moveBy());
                    p.setOwner(chip);
                    break;
                case SECOND_POS:
                    positionsList.get(FIRST_POS).setPosition(fieldCenterX - positionWidth, rowOneIndent);
                    p.setOwner(chip);
                    //Vector2 offset = new Vector2(p.getX() - fieldCenterX, p.getY() - rowOneIndent);
                    //chip.addAction(Actions.moveBy(offset.x, offset.y, 0.3f));
                    break;
            }
        }

        FieldLayoutPosition nextFreePosition() {
            int index = FIRST_POS;
            while (!positionsList.get(index).isFree()) index++;
            return positionsList.get(index);
        }

        float getFieldCenterX() {
            return fieldCenterX;
        }

        float getFieldCenterY() {
            return fieldCenterY;
        }
    }

}
