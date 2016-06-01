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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author Sasha Kvachenko
 *         Created on 31.05.2016.
 *         <p>
 *         Dice image. Represent d6 .
 */
public class DiceWidget extends Image {

    /** Possible dice states */
    public enum State {
        NOT_ROLLED,
        ROLLING,
        ROLLED
    }

    private TextureRegionDrawable currentImage;
    private TextureRegion[] rollFrames;
    private Animation diceRoll;
    private float stateTime;
    private float rollingTimer;
    private int lastRollResult;
    private int previousRollResult;
    private Label rollResultLabel;
    //private boolean rolled;
    private State state;

    public DiceWidget() {
        // Base initialization
        currentImage = new TextureRegionDrawable();
        stateTime = 0;
        rollingTimer = 0;
        lastRollResult = 6;
        previousRollResult = 5;
        //rolled = false;
        state = State.NOT_ROLLED;

        /*
         * Create animation
         * @SEE https://github.com/libgdx/libgdx/wiki/2D-Animation
         */
        int frameCols = 3;
        int frameRows = 2;
        Texture diceSheet = new Texture(Gdx.files.internal("diceWhite.png"));
        TextureRegion[][] tmp = TextureRegion.split(diceSheet,
                diceSheet.getWidth()/frameCols,
                diceSheet.getHeight()/frameRows);
        rollFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                rollFrames[index++] = tmp[i][j];
            }
        }
        diceRoll = new Animation(0.1f, rollFrames);
        diceRoll.setPlayMode(Animation.PlayMode.LOOP_RANDOM);

        // Input listener
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x > 0 && x < 64 && y > 0 && y < 64) {
                    roll();
                }
            }
        });

        // set start image
        setSide();
    }

    public void setRollResultLabel(Label l) {
        rollResultLabel = l;
    }

    public void setState(State newState) { state = newState; }

    public int getRollResult() {
        if (state == State.ROLLED) return lastRollResult;
        else return previousRollResult;
    }

    //public boolean isRolled() { return rolled; }

    public State getState() { return state; }

    /**
     * Method set upper (visible) side of dice to lastRollResult.
     */
    public void setSide() {
        setSide(lastRollResult);
    }

    public void setSide(int side) {
        currentImage.setRegion(rollFrames[MathUtils.clamp(side, 1, 6) - 1]);
    }

    public void roll() {
        if (state == State.NOT_ROLLED) {
            System.out.println("debug");
            previousRollResult = lastRollResult;
            lastRollResult = MathUtils.random(1, 6);
            rollingTimer = MathUtils.random(2f, 6f);
            state = State.ROLLING;
            //setSide();
            //rolled = true;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (state == State.ROLLING && rollingTimer > 0) {
            rollingTimer -= delta;

            // If animation time is over, set image equal lastRollResult.
            // Otherwise set random dice side image
            if (rollingTimer <= 0) {
                //rolled = false;
                state = State.ROLLED;
                setSide();
                if (rollResultLabel != null) rollResultLabel.setText(toString());
            }
            else currentImage.setRegion(diceRoll.getKeyFrame(stateTime, true));
        }
        setDrawable(currentImage);
    }

    @Override
    public String toString() {
        return Integer.toString(lastRollResult);
    }
}
