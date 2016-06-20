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

package ru.kvachenko.basegame;

/**
 * @author Sasha Kvachenko
 *         Created on 17.06.2016.
 *         <p>
 *
 */
public abstract class WinCondition {
    private boolean state;   // State of condition. If true, condition is accomplished.

    public WinCondition() {
         state= false;
    }

    public boolean isAccomplished() {
        return state;
    }

    protected void setState(boolean b) {
        state = b;
    }

    /** Method checks conditions, and if is done, set accomplished var to true. */
    public abstract void checkCondition();
}
