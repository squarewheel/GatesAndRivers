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
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.kvachenko.basegame.BaseScreen;
import ru.kvachenko.gatesandrivers.gameboard.Fields;
import ru.kvachenko.gatesandrivers.gameboard.Gates;

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 20.05.2016.
 *         <p>
 *         Class for main game screen.
 */
class GameScreen extends BaseScreen {
    private GatesAndRivers game;
    private int worldWidth;
    private int worldHeight;
    private Stage mainStage;
    private Stage uiStage;
    private Camera camera;
    private Actor camTarget;    // Camera always bind to cam target

    private GameplayController gameController;
    private Label playerName;
    private Label gameOverLabel;
    private TextButton exitButton;
    private TextButton newGameButton;
    private Dialog gameOverWindow;
    private Dialog pauseWindow;

    // Temp variables
    private boolean debug = false;
    //private Player currentPlayer;

    public GameScreen(final GatesAndRivers game, SetupItem[] options) {
        super();
        game.gameScreen = this;

        this.game = game;
        worldWidth = 64 * 32;
        worldHeight = 64 * 32;
        mainStage = new Stage(new ScreenViewport());
        mainStage.addActor(new Image(new TextureRegion(new Texture("main_screen3.png"))));  // Background
        uiStage = new Stage(new ScreenViewport());
        camera = mainStage.getCamera();
        camTarget = new Actor();
        mainStage.addActor(camTarget);

        /*-----------------------------------GAMEBOARD INITIALIZATION---------------------------------------*/
        // TODO: may be need special class for creating gameboard
        TiledMap tiledMap = new TmxMapLoader().load("main_screen3.tmx");
        MapObjects fieldObjects = tiledMap.getLayers().get("fields").getObjects();
        for (MapObject mo: fieldObjects) {
            Rectangle fieldRectangle = ((RectangleMapObject) mo).getRectangle();
            FieldActor field = new FieldActor();
            field.setSize(fieldRectangle.width, fieldRectangle.height);
            field.setPosition(fieldRectangle.x, fieldRectangle.y);
            MapProperties fieldProperties = mo.getProperties();
            if (fieldProperties.containsKey("portalEndpoint")) {
                Gate g = new Gate(field); //g.debug();
                FieldActor gateField = Fields.getFieldsList().get(Integer.valueOf((String) fieldProperties.get("portalEndpoint")) - 1);
                gateField.addActor(g);
                gateField.setMover(g);
                g.setPosition(gateField.getWidth()/2 - g.getWidth()/2, gateField.getHeight()/2 - g.getHeight()/2);
                //gateField.debug();
            }
            else if (fieldProperties.containsKey("river")) {
                River r = new River(Fields.getFieldsList().get(Integer.valueOf((String) fieldProperties.get("riverEndpoint")) - 1));
                String riverName = "river" + fieldProperties.get("river");
                MapObjects waypoints = tiledMap.getLayers().get(riverName).getObjects();
                for (MapObject waypoint: waypoints) {
                    Rectangle point = ((RectangleMapObject) waypoint).getRectangle();
                    r.addWaypoint(new Vector2(point.x, point.y));
                }
                field.setMover(r);
            }
            mainStage.addActor(field);
        }
        tiledMap.dispose();

        /*-----------------------------------PLAYERS AND GAME MECHANIC INITIALIZATION-----------------------*/
        ArrayList<Player> playersList = new ArrayList<Player>();
        for (SetupItem item: options) {
            if (!item.available.isChecked()) continue;
            Player player = new Player(item.getName(), item.playerColor);
            if (item.playable.isChecked()) player.makePlayable();
            playersList.add(player);
            mainStage.addActor(player.getChip());
        }
        gameController = new GameplayController(playersList, game.skin);

        /*-----------------------------------USER INTERFACE INITIALIZATION----------------------------------*/
        Table diceBox = new Table().background(game.skin.getDrawable("frameImg"));
        Label autoRollLabel = gameController.getAutoRollLabel();
        playerName = new Label(gameController.getCurrentPlayer().getName(), game.skin);
        diceBox.add(playerName).colspan(2).uniformY();
        diceBox.row();
        diceBox.add(gameController.getDice()).colspan(2);
        diceBox.row().uniformY();
        diceBox.add(autoRollLabel).right();
        diceBox.add(gameController.getAutoRollSwitch());
        if (debug) diceBox.debug();

        Table statsList = new Table();
        statsList.add(gameController.new RoundCounterLabel("", game.skin, "default")).right().top().padTop(5);

        final TextButton pauseButton = new TextButton("PAUSE", game.skin, "textButtonStyle");

        Table uiTable = new Table(game.skin);
        uiStage.addActor(uiTable);
        uiTable.setFillParent(true);
        uiTable.top();
        uiTable.add(statsList).expandX().left().top().pad(5, 5, 0, 0);
        uiTable.add(pauseButton).fill().right().top().pad(5, 0, 0, 5);
        uiTable.row().expandY();
        uiTable.add(gameController.getInfoLabel()).colspan(2);
        uiTable.row();
        uiTable.add().expandX();
        uiTable.add(diceBox).right().bottom().pad(0, 0, 5, 5);
        if (debug) uiTable.setDebug(true);

        final TextButton resumeButton = new TextButton("Resume Game", game.skin, "textButtonStyle");
        exitButton = new TextButton("Exit", game.skin, "textButtonStyle");
        newGameButton = new TextButton("New Game", game.skin, "textButtonStyle");

        pauseWindow = new Dialog("Game Paused", game.skin);
        pauseWindow.padTop(30);
        pauseWindow.getButtonTable().add(resumeButton).fillX();
        pauseWindow.getButtonTable().row();
        pauseWindow.getButtonTable().add(newGameButton).fillX();
        pauseWindow.getButtonTable().row();
        pauseWindow.getButtonTable().add(exitButton).fillX();

        gameOverWindow = new Dialog("Game Over", game.skin);
        gameOverWindow.setVisible(false);
        gameOverWindow.padTop(30);
        gameOverLabel = new Label("", game.skin, "infoLabelStyle");
        gameOverWindow.text(gameOverLabel);

        /*-----------------------------------INPUT HANDLERS SECTION-----------------------------------------*/
        resumeButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x > 0 && x < resumeButton.getWidth() && y > 0 && y < resumeButton.getHeight()) {
                    paused = false;
                    pauseButton.setText((paused) ? "PAUSED" : "PAUSE");
                    pauseWindow.remove();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        pauseButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x > 0 && x < pauseButton.getWidth() && y > 0 && y < pauseButton.getHeight()) {
                    paused = true;
                    pauseButton.setText((paused) ? "PAUSED" : "PAUSE");
                    pauseWindow.show(uiStage);
                    //pauseButton.setDisabled(paused);
                    //pauseButton.set(paused);
                }
            }
        });
        newGameButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x > 0 && x < newGameButton.getWidth() && y > 0 && y < newGameButton.getHeight()) {
                    gameOverWindow.cancel();
                    newGameButton.setDisabled(true);
                    game.setScreen(new SetupScreen(game));
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        exitButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x > 0 && x < exitButton.getWidth() && y > 0 && y < exitButton.getHeight()) {
                    gameOverWindow.cancel();
                    exitButton.setDisabled(true);
                    Gdx.app.exit();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        game.im.addProcessor(uiStage);
        game.im.addProcessor(new InputAdapter(){
            Vector3 lastTouchDown;

            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                lastTouchDown = new Vector3(x, y, 0);
                return true;
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                // Map scrolling with touch
                Vector3 newPos = new Vector3(x, y, 0);
                Vector3 offset = newPos.sub(lastTouchDown);
                camTarget.setPosition(camera.position.x - offset.x, camera.position.y + offset.y);
                lastTouchDown.add(offset);
                return true;
            }
        });
    }

    @Override
    public void render(float dt) {
        /*-----------------------------------UPDATE SECTION-------------------------------------------------*/
        if (!paused) {
            uiStage.act(dt);
            mainStage.act(dt);
            gameController.update();
            if (gameController.getTurnPhase() == GameplayController.TurnPhase.PREPARATION) {
                playerName.setText(gameController.getCurrentPlayer().getName());
                playerName.setColor(gameController.getCurrentPlayer().getChip().getColor());
            }
            if (gameController.gameOver()) {
                if (!gameOverWindow.isVisible()) {
                    gameOverLabel.setText("Winner is " + gameController.getWinner().getName() + "!");
                    gameOverLabel.setColor(gameController.getWinner().getChip().getColor());
                    gameOverWindow.getButtonTable().add(newGameButton);
                    gameOverWindow.getButtonTable().add(exitButton);
                    gameOverWindow.setVisible(true);
                    gameOverWindow.show(uiStage);
                    if (debug) gameOverWindow.setPosition(0, 0);
                }
            }

            // Update camera position
            ChipActor c = gameController.getCurrentPlayer().getChip();  // Current player chip
            switch (gameController.getTurnPhase()) {
                case DICE_ROLLED:
                case PREPARATION:
                    //if (!camTarget.hasActions()) camTarget.addAction(Actions.after(Actions.moveTo(
                    camTarget.addAction(Actions.after(Actions.moveTo(
                            c.getX() + c.getOriginX(),
                            c.getY() + c.getOriginY(), 0.4f)));
                    break;

                case MOVEMENT:
                    //if (!camTarget.hasActions()) camTarget.setPosition(c.getX() + c.getOriginX(), c.getY() + c.getOriginY());
                    camTarget.addAction(Actions.after(Actions.moveTo(
                            c.getX() + c.getOriginX(),
                            c.getY() + c.getOriginY(), 0.018f)));
                    break;
            }
        }

        /*-----------------------------------OUTPUT SECTION-------------------------------------------------*/
        camera.position.x = camTarget.getX();
        camera.position.y = camTarget.getY();

        camera.position.x = MathUtils.clamp(camera.position.x,
                camera.viewportWidth/2,
                worldWidth - camera.viewportWidth/2);
        camera.position.y = MathUtils.clamp(camera.position.y,
                camera.viewportHeight/2,
                worldHeight - camera.viewportHeight/2);

        // Clean screen and draw stages
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        mainStage.dispose();
        uiStage.dispose();
        Gates.clear();
        Fields.clear();
        game.im.clear();
    }
}
