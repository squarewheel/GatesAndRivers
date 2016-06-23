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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.kvachenko.basegame.BaseScreen;

/**
 * @author Sasha Kvachenko
 *         Created on 23.06.2016.
 *         <p>
 *         Title screen of game.
 */
public class TitleScreen extends BaseScreen{
    private Stage titleStage;

    public TitleScreen(final BoardGame game) {
        super();
        Label title = new Label("Gates and Rivers", game.skin, "infoLabelStyle");
        Label info = new Label("tap to continue", game.skin, "labelStyle");
        titleStage = new Stage(new ScreenViewport());
        Table layout = new Table(game.skin);
        titleStage.addActor(layout);
        layout.setFillParent(true);
        layout.add(title);
        layout.row();
        layout.add(info);

        titleStage.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.im.removeProcessor(titleStage);
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        game.im.addProcessor(titleStage);
    }

    @Override
    public void render(float delta) {
        titleStage.act();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        titleStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        titleStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        System.out.println("end of title");
        titleStage.dispose();
    }
}
