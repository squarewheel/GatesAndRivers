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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.kvachenko.basegame.AbstractActor;

/**
 * @author Sasha Kvachenko
 *         Created on 30.05.2016.
 *         <p>
 *         Class provides d6.
 */
public class DiceActor extends AbstractActor {
    private Animation diceRoll;

    public DiceActor() {
        int        FRAME_COLS = 6;         // #1
        int        FRAME_ROWS = 5;         // #2
        Texture diceSheet;                                      // #4
        TextureRegion[]                 walkFrames;             // #5
        float stateTime;                                        // #8

        diceSheet = new Texture(Gdx.files.internal("animation_sheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(diceSheet, diceSheet.getWidth()/FRAME_COLS,
                                                               diceSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        diceRoll = new Animation(0.025f, walkFrames);      // #11
        stateTime = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        //diceRoll.
    }
}
