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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.kvachenko.basegame.BaseScreen;

/**
 * @author Sasha Kvachenko
 *         Created on 23.06.2016.
 *         <p>
 *         Game setup screen.
 */
public class SetupScreen extends BaseScreen {
    private SetupLine[] setupLines;
    private Stage setupStage;

    public SetupScreen(final BoardGame game) {
        super();
        Label title = new Label("New Game Setup", game.skin, "infoLabelStyle");
        final TextButton startButton = new TextButton("Start Game", game.skin, "textButtonStyle");
        setupStage = new Stage(new ScreenViewport());
        Table layout = new Table(game.skin).top();
        setupStage.addActor(layout);
        layout.setFillParent(true);

        layout.add(title).top();
        setupLines = new SetupLine[4];
        Table linesGroup = new Table();
        for (int i = 0; i < setupLines.length ; i++) {
            setupLines[i] = new SetupLine(Color.GOLDENROD, "Player " + (i+1), game.skin);
            linesGroup.row();
            linesGroup.add(setupLines[i]).expandX().fillX();
        }
        layout.row().expandY();
        layout.add(linesGroup).bottom().expandX().maxWidth(title.getWidth() - 100).fillX();
        layout.row().expandY();
        layout.add(startButton).fillX().top().maxWidth(title.getWidth() - 100).padTop(10);
        //layout.debug();

        startButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return !startButton.isDisabled();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x > 0 && x < startButton.getWidth() && y > 0 && y < startButton.getHeight()) {
                    startButton.setDisabled(true);
                    game.im.removeProcessor(setupStage);
                    game.setScreen(new GameScreen(game));
                }
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

    private class SetupLine extends Table {
        private Color playerColor;
        private Image colorImage;
        private TextField.TextFieldStyle nameFieldStyle;
        private TextField nameField;
        private CheckBox available;

        public SetupLine(Color c, String name, Skin skin) {
            super();
            playerColor = c;
            colorImage = new Image(skin.getDrawable("checkBoxImg"));
            colorImage.setColor(playerColor);
            nameFieldStyle = new TextField.TextFieldStyle(skin.get("textFieldStyle", TextField.TextFieldStyle.class));
            nameField = new TextField(name, nameFieldStyle);
            available = new CheckBox("", skin, "checkBoxStyle");
            available.setChecked(true);

            //add(colorImage).left();
            add(nameField).left().expandX().fillX().padRight(5);
            add(available).right();
            //debug();
        }

        public boolean isAvailable() { return available.isChecked(); }

        @Override
        public void act(float delta) {
            super.act(delta);
            colorImage.setColor(available.isChecked() ? playerColor : Color.LIGHT_GRAY);
            nameFieldStyle.fontColor = available.isChecked() ? playerColor : Color.LIGHT_GRAY;
        }
    }
}
