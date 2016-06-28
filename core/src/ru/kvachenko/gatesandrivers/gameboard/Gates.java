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

import com.badlogic.gdx.graphics.Color;
import ru.kvachenko.gatesandrivers.Gate;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Sasha Kvachenko
 *         Created on 28.06.2016.
 *         <p>
 *         All created gates.
 */
public class Gates {
    private static ArrayList<Color> colorsList = new ArrayList<Color>();        // List of possible portals colors
    private static ArrayList<Gate> gatesList = new ArrayList<Gate>();       // List of all created gates

    static {
        colorsList.add(Color.RED);
        colorsList.add(Color.SKY);
        colorsList.add(Color.LIME);
        colorsList.add(Color.GOLD);
        colorsList.add(Color.PINK);
    }

    public static void add(Gate g) { gatesList.add(g); }

    public static Color getNextFreeColor() { return colorsList.get(gatesList.size()); }

    public static void clear() { gatesList.clear(); }
}
