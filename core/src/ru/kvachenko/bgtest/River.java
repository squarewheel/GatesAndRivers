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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 17.06.2016.
 *         <p>
 *         Move chip via a path composed of several points.
 */
public class River implements Mover {
    private ArrayList<Vector2> path;    // All waypoints which chip must go
    //private Vector2 currentPoint;       // Current way-point
    private ChipActor currentChip;      // Chip which now movements
    private FieldActor targetField;     // Endpoint of this river

    public River(FieldActor target) {
        path = new ArrayList<Vector2>();
        targetField = target;
    }

    public void addWaypoint(Vector2 point) {
        path.add(point);
    }

    @Override
    public void move(ChipActor chip) {
        if (currentChip != chip) {
            currentChip = chip;
            for (Vector2 point : path) {
                chip.setState(ChipActor.State.MOVEMENT);
                chip.addAction(Actions.after(Actions.moveTo(point.x, point.y, (float) 450/point.len())));
            }
        }
        else if (currentChip.getState() == ChipActor.State.READY) {
            currentChip = null;
            chip.moveToField(targetField);
        }
    }
}
