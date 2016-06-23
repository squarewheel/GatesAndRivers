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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.kvachenko.basegame.BaseScreen;

/**
 * @author Sasha Kvachenko
 *         Created on 23.06.2016.
 *         <p>
 *         Game setup screen.
 */
public class SetupScreen extends BaseScreen {
    private Stage setupStage;

    public SetupScreen(final BoardGame game) {
        super();
        Label title = new Label("New Game Setup", game.skin, "infoLabelStyle");
//        Label info = new Label("tap to continue", game.skin, "labelStyle");
        TextButton startButton = new TextButton("Start Game", game.skin, "textButtonStyle");
        setupStage = new Stage(new ScreenViewport());
        Table layout = new Table(game.skin);
        setupStage.addActor(layout);
        layout.setFillParent(true);
        layout.add(title).top().pad(5, 5, 0, 5);
//        layout.row();
//        layout.add(info);
        layout.row().expandY();
        layout.add(startButton).fillX().bottom().pad(0, 5, 5, 5);

        startButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.im.removeProcessor(setupStage);
                game.setScreen(new GameScreen(game));
            }
        });

        game.im.addProcessor(setupStage);
    }

    @Override
    public void render(float delta) {
        setupStage.act();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        setupStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        setupStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        //System.out.println("end of title");
        setupStage.dispose();
    }
}
