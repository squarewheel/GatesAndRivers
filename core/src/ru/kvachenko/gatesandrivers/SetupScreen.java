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

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 23.06.2016.
 *         <p>
 *         Game setup screen.
 */
public class SetupScreen extends BaseScreen {
    private SetupItem[] options;
    private Stage setupStage;
    private Label errorList;
    private Dialog errorWindow;

    public SetupScreen(final GatesAndRivers game) {
        super();
        if (game.gameScreen != null) game.gameScreen.dispose();

        ArrayList<Color> colors = new ArrayList<Color>();
        ArrayList<String> names = new ArrayList<String>();

        // Default players names and colors
        colors.add(Color.RED);
        colors.add(Color.SKY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        names.add("Mr. Red");
        names.add("Mr. Blue");
        names.add("Mr. Green");
        names.add("Mr. Yellow");

        Label title = new Label("New Game Setup", game.skin, "infoLabelStyle");
        final TextButton startButton = new TextButton("Start Game", game.skin, "textButtonStyle");
        setupStage = new Stage(new ScreenViewport());

        options = new SetupItem[4];
        Table linesGroup = new Table();
        linesGroup.add(new Label("Name", game.skin)).left().pad(0, 5, 5, 0);
        linesGroup.add(new Label("Playable", game.skin)).pad(0, 0, 5, 0);
        linesGroup.add(new Label("Enable", game.skin)).pad(0, 5, 5, 0);
        for (int i = 0; i < options.length ; i++) {
            options[i] = new SetupItem(colors.remove(0), names.remove(0), game.skin);
            linesGroup.row();
            linesGroup.add(options[i].nameField).expandX().fillX();
            linesGroup.add(options[i].playable).padLeft(5);
            linesGroup.add(options[i].available).padLeft(5);
        }

        Table layout = new Table(game.skin) {
            @Override
            public void act(float delta) {
                super.act(delta);
                for (SetupItem l: options) {
                    l.colorImage.setColor(l.available.isChecked() ? l.playerColor : Color.LIGHT_GRAY);
                    l.nameFieldStyle.fontColor = l.available.isChecked() ? l.playerColor : Color.LIGHT_GRAY;
                }

            }
        };
        setupStage.addActor(layout);

        layout.top();
        layout.setFillParent(true);
        layout.add(title).top();
        layout.row().expandY().maxWidth(title.getWidth() - 100);
        layout.row().expandY();
        layout.add(linesGroup).bottom().expandX().maxWidth(title.getWidth() - 100).fillX();
        layout.row().expandY();
        layout.add(startButton).fillX().top().maxWidth(title.getWidth() - 100).padTop(10);
        //layout.debug();

        errorList = new Label("", game.skin);
        errorWindow = new Dialog("The following errors must be fixed", game.skin);
        errorWindow.padTop(30);
        errorWindow.setColor(Color.ORANGE);
        errorWindow.text(errorList);
        errorWindow.button(new TextButton("OK", game.skin, "textButtonStyle"));
        //errorWindow.setWidth(title.getWidth());
        //errorWindow.debug();

        startButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return !startButton.isDisabled();
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                errorList.setText("");
                if (x > 0 && x < startButton.getWidth() && y > 0 && y < startButton.getHeight()) {
                    boolean startGame = true;
                    int enabledPlayers = 0;
                    for (SetupItem l: options) {
                        if (l.available.isChecked()) {
                            if (l.nameField.getText().trim().isEmpty()) {
                                startGame = false;
                                errorList.setText(" - Name cannot be blank \n");
                            }
                            enabledPlayers++;
                        }
                    }
                    if (enabledPlayers < 2) {
                        startGame = false;
                        errorList.setText(errorList.getText() + " - At least two players must be enabled");
                    }
                    if (startGame) {
                        startButton.setDisabled(true);
                        game.im.removeProcessor(setupStage);
                        game.setScreen(new GameScreen(game, options));
                    }
                    else errorWindow.show(setupStage);
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
}
