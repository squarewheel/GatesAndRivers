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

    //static { fieldsList = new ArrayList<FieldActor>(); }

    FieldActor() {
        fieldsList.add(this);
        ListIterator<FieldActor> fieldsItr = fieldsList.listIterator(fieldsList.indexOf(this));
        previousField = fieldsItr.hasPrevious() ? fieldsItr.previous() : null;
        if (hasPreviousField()) getPreviousField().setNextField(this);
    }

    private void setNextField(FieldActor field) {
        nextField = field;
    }

    public FieldActor getNextField() {
        return nextField;
    }

    public FieldActor getPreviousField() {
        return previousField;
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

}
