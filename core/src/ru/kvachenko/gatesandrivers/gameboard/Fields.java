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

package ru.kvachenko.gatesandrivers.gameboard;

import ru.kvachenko.gatesandrivers.FieldActor;

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 28.06.2016.
 *         <p>
 *         All created fields.
 */
public class Fields {
    private static ArrayList<FieldActor> fieldsList = new ArrayList<FieldActor>();

    public static void add(FieldActor f) { fieldsList.add(f); }

    public static void clear() { fieldsList.clear(); }

    public static ArrayList<FieldActor> getFieldsList() { return fieldsList; }
}
