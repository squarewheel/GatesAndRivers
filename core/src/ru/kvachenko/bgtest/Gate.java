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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ru.kvachenko.basegame.BaseActor;

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 15.06.2016.
 *         <p>
 *         Gate teleport chip to torget field.
 */
public class Gate extends BaseActor implements Mover {
    private static ArrayList<Color> colorsList = new ArrayList<Color>();// List of possible portals colors
    private static ArrayList<Gate> gatesList = new ArrayList<Gate>();   // List of all created gates
    private FieldActor targetField;                                     // Endpoint of this gate

    static {
        colorsList.add(Color.ROYAL);
        colorsList.add(Color.SKY);
        colorsList.add(Color.LIME);
        colorsList.add(Color.GOLD);
        colorsList.add(Color.PINK);
    }

    public Gate(FieldActor target) {
        super(new TextureRegion(new Texture("portal2.png")));
        targetField = target;
        setSize(56, 64);
        setColor(colorsList.remove(0));
        BaseActor endPoint = new BaseActor(new TextureRegion(new Texture("endpoint_portal.png")));
        endPoint.setColor(getColor());
        //endPoint.setOrigin(endPoint.getWidth()/2, endPoint.getHeight()/2);
        endPoint.setPosition(targetField.getWidth()/5*2, targetField.getHeight()/5*2);
        targetField.addActor(endPoint);

        gatesList.add(this);
    }

    @Override
    public void move(ChipActor chip) {
        chip.addAction(Actions.parallel(Actions.scaleBy(-1, -1, 0.5f), Actions.fadeOut(1f)));
        chip.moveToField(targetField);
        chip.addAction(Actions.after(Actions.parallel(Actions.scaleBy(1, 1, 0.5f), Actions.fadeIn(1f))));
        //chip.takePosition();
        //chip.addAction();
    }
}
