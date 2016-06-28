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

package ru.kvachenko.gatesandrivers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ru.kvachenko.basegame.BaseActor;
import ru.kvachenko.gatesandrivers.gameboard.Gates;

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 15.06.2016.
 *         <p>
 *         Gate teleport chip to torget field.
 */
public class Gate extends BaseActor implements Mover {
    private FieldActor targetField;                                         // Endpoint of this gate

    public Gate(FieldActor target) {
        super(new TextureRegion(new Texture("portal2.png")));
        targetField = target;
        setSize(56, 64);

        setColor(Gates.getNextFreeColor());

        BaseActor endPoint = new BaseActor(new TextureRegion(new Texture("endpoint_portal.png")));
        endPoint.setColor(getColor());
        //endPoint.setOrigin(endPoint.getWidth()/2, endPoint.getHeight()/2);
        endPoint.setPosition(targetField.getWidth()/5*2, targetField.getHeight()/5*2);  // Hardcoding is bad!
        targetField.addActor(endPoint);

        Gates.add(this);
    }

    @Override
    public void move(ChipActor chip) {
        chip.addAction(Actions.parallel(Actions.scaleBy(-1, -1, 0.5f), Actions.fadeOut(1f)));
        chip.moveToField(targetField);
        chip.addAction(Actions.after(Actions.parallel(Actions.scaleBy(1, 1, 0.5f), Actions.fadeIn(1f))));
    }
}
