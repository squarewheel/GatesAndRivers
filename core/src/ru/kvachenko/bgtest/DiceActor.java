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
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ru.kvachenko.basegame.AbstractActor;

/**
 * @author Sasha Kvachenko
 *         Created on 30.05.2016.
 *         <p>
 *         Class provides d6 dice.
 */

@Deprecated // Use DiceImage class
public class DiceActor extends AbstractActor {
    private Animation diceRoll;
    private float stateTime;
    private boolean rolled;

    public DiceActor() {
        stateTime = 0;
        rolled = false;

        /*
         * Create animation
         * @SEE https://github.com/libgdx/libgdx/wiki/2D-Animation
         */
        int frameCols = 3;
        int frameRows = 2;
        Texture diceSheet = new Texture(Gdx.files.internal("diceWhite.png"));
        TextureRegion[][] tmp = TextureRegion.split(diceSheet,
                diceSheet.getWidth()/3,
                diceSheet.getHeight()/2);
        TextureRegion[] rollFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                rollFrames[index++] = tmp[i][j];
            }
        }
        setTexture(rollFrames[0]);
        diceRoll = new Animation(0.025f, rollFrames);
    }

    public int roll() {
        rolled = true;
        return MathUtils.random(1, 6);
    }

    public boolean isRolled() { return rolled; }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isRolled()) setTexture(diceRoll.getKeyFrame(stateTime));
        super.draw(batch, parentAlpha);
    }
}
