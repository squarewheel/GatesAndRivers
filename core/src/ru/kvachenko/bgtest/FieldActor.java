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

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.*;

/**
 * @author Sasha Kvachenko
 *         Created on 25.05.2016.
 *         <p>
 *         Provides gameboard field.
 *         Field must be created before players;
 */
public class FieldActor extends Group{
    private static ArrayList<FieldActor> fieldsList = new ArrayList<FieldActor>();
    private FieldActor nextField;
    private FieldActor previousField;
    private Layout layout;
    private Mover mover;

    // Debug variable
    // private Label debugLabel;

    //static { fieldsList = new ArrayList<FieldActor>(); }

    public FieldActor() {
        fieldsList.add(this);
        ListIterator<FieldActor> fieldsItr = fieldsList.listIterator(fieldsList.indexOf(this));
        previousField = fieldsItr.hasPrevious() ? fieldsItr.previous() : null;
        if (hasPreviousField()) getPreviousField().setNextField(this);
        layout = new Layout();
        mover = null;
//        debug();
    }

//    /* Only for debug*/
//    public void setDebugLabel(String text, Skin skin, String style) {
//        //this.debugLabel = debugLabel;
//        debugLabel = new Label(text, skin, style);
//        addActor(debugLabel);
//        debugLabel.setPosition(0, 0);
//    }
//    /* delete after debug! */

    public FieldActor getNextField() {
        return nextField;
    }

    public FieldActor getPreviousField() {
        return previousField;
    }

    public Layout getLayout() {
        return layout;
    }

    public Mover getMover() { return mover; }

    public boolean hasNextField() {
        return nextField != null;
    }

    public boolean hasPreviousField() {
        return previousField != null;
    }

    public boolean hasMover() { return mover != null; }

    public static ArrayList<FieldActor> getFieldsList() {
        return fieldsList;
    }

    public void setMover(Mover m) {
        mover = m;
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
    private class LayoutPosition {
        private ChipActor owner;            // Current owner of this position
        private float x, y;                 // Current position (relative to field)

        LayoutPosition() {
            owner = null;
            // Initial position is center of field 5x5
            x = 2 * (getWidth()/5);
            y = 2 * (getHeight()/5);
        }

        void setOwner(ChipActor chip) {
            owner = chip;
            owner.addAction(Actions.after(Actions.moveBy(
                    getPositionX() - (chip.getX() - getX()),
                    getPositionY() - (chip.getY() - getY()), 0.3f)));
//                    getPositionX() - layout.getFieldCenterX(),
//                    getPositionY() - layout.getFieldCenterY(), 0.3f)));
        }

        void removeOwner() {
            //ChipActor chip = owner;
            owner = null;
            //return chip;
        }

        void setPosition(float newX, float newY) {
            if (!isFree()) owner.addAction(Actions.after(Actions.moveBy(newX - x, newY - y, 0.3f)));
            x = newX;
            y = newY;
        }

        boolean isFree() {
            return owner == null;
        }

        ChipActor getOwner() {
            return owner;
        }

        float getPositionX() {
            return x;
        }

        float getPositionY() {
            return y;
        }

        //int getIndex() { return layout.positionsList.indexOf(this); }
    }

    /** One row of layout. Allows to manage all positions added to row together. */
    private class LayoutRow {
        private float indent;                       // Indent from bottom edge of field, determines Y position
        private int length;                         // Num of positions in current row, by default two
        private LayoutPosition[] positions;    // Contains all positions of this row

        LayoutRow() {
            indent = 0;
            length = 2;
            positions = new LayoutPosition[length];
            for (int i = 0; i < positions.length; i++) positions[i] = new LayoutPosition();
        }

        float getIndent() {
            return indent;
        }

        void setIndent(float newIndent) {
            indent = newIndent;
            for (LayoutPosition p: positions) p.setPosition(p.getPositionX(), indent);
        }

        LayoutPosition[] getPositions() {
            return positions;
        }

    }

    /** Field have four possible chip positions.
     *  Positions arranged in rows, each field divided on two rows, by two positions in row. */
    class Layout {
        final static int FIRST_POS = 0;
        final static int SECOND_POS = 1;
        final static int THIRD_POS = 2;
        final static int FOURTH_POS = 3;

        private ArrayList<LayoutPosition> positionsList;   // All positions
        private ArrayDeque<ChipActor> shiftQueue;   // Chips to repositioning
        private float positionWidth;                // By default, 1/5 of field width
        private float positionHeight;               // By default, 1/5 of field height
        private float fieldCenterX;                 // Indent from left edge to center of field
        private float fieldCenterY;                 // Indent from bottom edge to center of field
        private LayoutRow firstRow;
        private LayoutRow secondRow;

        private int counter; // TODO: delete after debug

        Layout() {
            positionsList = new ArrayList<LayoutPosition>(4);
            shiftQueue = new ArrayDeque<ChipActor>(4);
            firstRow = new LayoutRow();
            secondRow = new LayoutRow();
            Collections.addAll(positionsList, firstRow.getPositions());
            Collections.addAll(positionsList, secondRow.getPositions());
            setSize();
//            counter = 0;
            //for (int i = 0; i < 4; i++) positionsList.add(new FieldLayoutPosition());
        }

        /** Set layout size. Use after resize Field. */
        void setSize() {
            positionWidth = getWidth()/5;
            positionHeight = getHeight()/5;
            fieldCenterX = 2 * positionWidth;
            fieldCenterY = 2 * positionHeight;
            firstRow.setIndent(2 * positionHeight);
            secondRow.setIndent(positionHeight);

            if (!positionsList.isEmpty()) {
                positionsList.get(FIRST_POS).setPosition(fieldCenterX, firstRow.getIndent());
                positionsList.get(SECOND_POS).setPosition(fieldCenterX + positionWidth, firstRow.getIndent());
                positionsList.get(THIRD_POS).setPosition(fieldCenterX, secondRow.getIndent());
                positionsList.get(FOURTH_POS).setPosition(fieldCenterX + positionWidth, secondRow.getIndent());
            }
        }

        void addChip(ChipActor chip) {

            LayoutPosition p = nextFreePosition();
            switch (positionsList.indexOf(p)) {
                case FIRST_POS:
                    p.setOwner(chip);
                    break;
                case SECOND_POS:
                    positionsList.get(FIRST_POS).setPosition(fieldCenterX - positionWidth, firstRow.getIndent());
                    p.setOwner(chip);
                    break;
                case THIRD_POS:
                    firstRow.setIndent(3 * positionHeight);
                    p.setOwner(chip);
                    break;
                case FOURTH_POS:
                    positionsList.get(THIRD_POS).setPosition(fieldCenterX - positionWidth, secondRow.getIndent());
                    p.setOwner(chip);
                    break;
            }
//            counter++;
//            if (debugLabel != null) {
//                //int counter = 0;
////                    for (LayoutPosition p: positionsList) {
////                        if (!p.isFree()) counter++;
////                    }
//                debugLabel.setText("" + counter);
//            }
        }

        void removeChip(ChipActor chip) {
            for (LayoutPosition p: positionsList) {
                if (p.getOwner() == chip) {
                    p.removeOwner();
                    counter--;
                    //update();
                    int index = positionsList.indexOf(p);
                    if (index < positionsList.size() - 1) { // if p has next
                        LayoutPosition nextPosition = positionsList.get(index + 1);
                        if (!nextPosition.isFree()) {
                            shiftQueue.add(nextPosition.getOwner());
                            removeChip(nextPosition.getOwner());
                        }
                    }
//                    p.removeOwner();
                    //update();
                }
            }
            update();
            while (shiftQueue.size() > 0) shiftQueue.remove().takePosition();
        }

        //void addPosition(FieldLayoutPosition p) { positionsList.add(p); }

        /** Moves freed positions to its default places. */
        void update() {
//            if (getDebug()) {
//                if (debugLabel != null) {
//                    //int counter = 0;
//                    for (LayoutPosition p: positionsList) {
//                        if (!p.isFree()) counter++;
//                    }
//                    debugLabel.setText("" + counter);
//                }
//            }
            //int index = FOURTH_POS;
            if (positionsList.get(FOURTH_POS).isFree()) {
                positionsList.get(THIRD_POS).setPosition(fieldCenterX, secondRow.getIndent());
                if (positionsList.get(THIRD_POS).isFree()) {
                    firstRow.setIndent(fieldCenterY);
                    if (positionsList.get(SECOND_POS).isFree())
                        positionsList.get(FIRST_POS).setPosition(fieldCenterX, firstRow.getIndent());
                }
            }
//            LayoutPosition lastOccupiedPosition = positionsList.get(FOURTH_POS);
//            ListIterator<LayoutPosition> posItr = positionsList.listIterator(positionsList.size());
//            while (posItr.hasPrevious()) {
//                lastOccupiedPosition = posItr.previous();
//                if (!lastOccupiedPosition.isFree()) break;
//            }
//            switch (positionsList.indexOf(lastOccupiedPosition)) {
//                case FOURTH_POS:
//                    positionsList.get(THIRD_POS).setPosition(fieldCenterX, secondRow.getIndent());
//                    break;
//
//                case THIRD_POS:
//                    firstRow.setIndent(fieldCenterY);
//                    break;
//
//                case SECOND_POS:
//                    positionsList.get(FIRST_POS).setPosition(fieldCenterX, firstRow.getIndent());
//                    break;
//
//                case FIRST_POS:
//                    break;
//            }
        }

        LayoutPosition nextFreePosition() {
            int index = FIRST_POS;
            while (!positionsList.get(index).isFree()) index++;
            return positionsList.get(index);
        }

//        private LayoutPosition getPositionOwnedBy(ChipActor chip) {
//            for (LayoutPosition p: positionsList) {
//                if (p.getOwner() == chip) return p;
//                else return null;
//            }
//        }

        float getFieldCenterX() {
            return fieldCenterX;
        }

        float getFieldCenterY() {
            return fieldCenterY;
        }
    }

}
