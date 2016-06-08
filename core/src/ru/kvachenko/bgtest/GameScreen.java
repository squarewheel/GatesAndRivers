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
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * @author Sasha Kvachenko
 *         Created on 20.05.2016.
 *         <p>
 *         Class for main game screen.
 */
// TODO: add dot in center of screen and bind camera position ti that dot
class GameScreen implements Screen {
    private BoardGame game;
    private Round currentRound;
    private Stage mainStage;
    private Stage uiStage;
    private Table uiTable;
    private ScreenViewport tiledViewport;
    private OrthographicCamera tiledCamera;
    private OrthogonalTiledMapRenderer mapRenderer;
    private int worldWidth;
    private int worldHeight;
    private DiceWidget dice;
    private Label infoLabel;

    // Temp variables
    boolean debug = false;
    //private Player currentPlayer;
    //int viewWidth = 1200;
    //int viewHeight = 600;

    GameScreen(BoardGame bg) {
        // Base initialization
        game = bg;
        worldWidth = 64 * 32;
        worldHeight = 64 * 32;
        mainStage = new Stage(new ScreenViewport());
        uiStage = new Stage(new ScreenViewport());
        uiTable = new Table(bg.skin);
        uiStage.addActor(uiTable);
        dice = new DiceWidget();
        dice.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Dice can be rolled only in START phase
                if (currentRound.getTurnPhase() == Round.TurnPhase.START) {
                    if (dice.isActive() && dice.getState() == DiceWidget.State.READY) {
                        dice.roll();
                        currentRound.setTurnPhase(Round.TurnPhase.DICE_ROLLING);
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // tmx map and renderer
        TiledMap tiledMap = new TmxMapLoader().load("main_screen2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        tiledCamera = new OrthographicCamera();
        tiledViewport = new ScreenViewport(tiledCamera);
        //tiledCamera.setToOrtho(false, viewWidth, viewHeight);

        // Gameboard fields
        MapObjects fieldObjects = tiledMap.getLayers().get("fields").getObjects();
        for (MapObject mo: fieldObjects) {
            Rectangle fieldRectangle = ((RectangleMapObject) mo).getRectangle();
            FieldActor field = new FieldActor();
            field.setSize(fieldRectangle.width, fieldRectangle.height);
            field.setPosition(fieldRectangle.x, fieldRectangle.y);
            mainStage.addActor(field);
        }

        // Players and Round initialization
        new Player();
        new Player();
        new Player();
        new Player();
        //Player.getPlayersList().get(0).makePlayable();
        for (Player p: Player.getPlayersList()) { mainStage.addActor(p.getChip()); }
        currentRound = new Round(Player.getPlayersList());

        // User interface
        infoLabel = new Label("Im Love my Wife", bg.skin, "infoLabelStyle");
        Label rollResultLabel = new Label("Roll Result: ", bg.skin, "labelStyle");
        Label rollResult = new Label("-", bg.skin, "labelStyle");
        dice.setRollResultLabel(rollResult);
        Table statsList = new Table();
        //statsList.add(customLabel).left().top().pad(5, 5, 0, 0);
        //statsList.row();
        statsList.add(rollResultLabel).right().top().pad(5, 5, 0, 0);
        statsList.add(rollResult).left().top().padTop(5);
        statsList.row();
        statsList.add(currentRound.new RoundCounterLabel("", bg.skin, "labelStyle")).right().top().padTop(5);
        final TextButton moveForwardButton = new TextButton("Move Forward", bg.skin, "textButtonStyle");
        moveForwardButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // debug
                //System.out.println("touch down: " + x + " " + y);
                //System.out.println();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //debug
                //System.out.println("touch up: " + x + " " + y);
                //System.out.println();

                if (x > 0 && x < moveForwardButton.getWidth() && y > 0 && y < moveForwardButton.getHeight()) {
                    ChipActor chip = Player.getPlayersList().get(0).getChip();
                    chip.moveForward();
                }
            }
        });
        final TextButton moveBackwardButton = new TextButton("Move Backward", bg.skin, "textButtonStyle");
        moveBackwardButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // debug
                //System.out.println("touch down: " + x + " " + y);
                //System.out.println();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //debug
                //System.out.println("touch up: " + x + " " + y);
                //System.out.println();

                if (x > 0 && x < moveBackwardButton.getWidth() && y > 0 && y < moveBackwardButton.getHeight()) {
                    ChipActor chip = Player.getPlayersList().get(0).getChip();
                    chip.moveBackward();
                }
            }
        });
        uiTable.setFillParent(true);
        uiTable.top();
        uiTable.add(statsList).left().top();
        uiTable.row().expandY();
        uiTable.add(infoLabel);
        if (debug) {
            uiTable.add(moveForwardButton).expandX().right().bottom().padRight(5);
            uiTable.row();
            uiTable.add(moveBackwardButton).expandX().right().bottom().padRight(5);
            uiTable.row();
        }
        uiTable.row();
        uiTable.add(dice).expandX().right().bottom().pad(1, 0, 5, 5);

        // Input handlers
        //uiStage.addListener(new InputListener(){});
        game.im.addProcessor(uiStage);
        game.im.addProcessor(new InputAdapter(){
            Vector3 lastTouchDown;

            public boolean touchDown(int x, int y, int pointer, int button) {
                lastTouchDown = new Vector3(x, y, 0);
                return false;
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                // Map scrolling with touch
                Camera stageCamera = mainStage.getCamera();
                Vector3 newPos = new Vector3(x, y, 0);
                Vector3 offset = newPos.sub(lastTouchDown);
                stageCamera.position.x = stageCamera.position.x - offset.x;
                stageCamera.position.y = stageCamera.position.y + offset.y;
                lastTouchDown.add(offset);
                return false;
            }

        });

        // Temp and debug section
        uiTable.setDebug(true);
        //statsList.debug();
        //uiStage.addActor(customLabel);
        System.out.println(uiTable.getCell(infoLabel).getMaxWidth());
        System.out.println(uiTable.getCell(infoLabel).getMaxHeight());
        //uiTable.getCell(customLabel).getMaxHeight();
        //infoLabel.setOrigin(infoLabel.getWidth()/2, infoLabel.getHeight()/2);
        //infoLabel.addAction(Actions.moveTo(uiStage.getWidth()/2, uiStage.getHeight()/2));
        infoLabel.addAction(Actions.fadeOut(2));
    }

    private void update(float dt) {
        // Update UI

        // Update players state
        switch (currentRound.getTurnPhase()) {
            case PREPARATION:   // Prepare game state to new turn
                if (!infoLabel.hasActions()) {
                    if (currentRound.getCurrentPlayer().isPlayable()) dice.activate();
                    dice.setState(DiceWidget.State.READY);
                    currentRound.setTurnPhase(Round.TurnPhase.START);
                }
                break;

            case START: // Start of current player turn. Phase end when user or ai roll dice
                //System.out.println("TurnPhase.START");
                if (!currentRound.getCurrentPlayer().isPlayable()) {
                    System.out.println(dice.getState());
                    dice.roll();
                }
                if (dice.getState() == DiceWidget.State.ROLLING)
                    currentRound.setTurnPhase(Round.TurnPhase.DICE_ROLLING);
                break;

            case DICE_ROLLING:  // Wait when dice roll animation finish
                //System.out.println("TurnPhase.DICE_ROLLING");
                if (dice.getState() == DiceWidget.State.ROLLED) {
                    dice.unActivate();
                    currentRound.setTurnPhase(Round.TurnPhase.DICE_ROLLED);
                }
                break;

            case DICE_ROLLED:   // Determines number of fields what current player must go
                // TODO: looks like this phase is not necessary
                //System.out.println("TurnPhase.DICE_ROLLED");
                currentRound.getCurrentPlayer().setMoves(dice.getRollResult());
                currentRound.setTurnPhase(Round.TurnPhase.MOVEMENT);
                break;

            case MOVEMENT:  // Move current player chip
                //System.out.println("TurnPhase.MOVEMENT");
                currentRound.getCurrentPlayer().move();
                if (currentRound.getCurrentPlayer().isMoved()) {
                    infoLabel.setText("TURN COMPLETE");
                    infoLabel.addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(1.5f), Actions.fadeOut(0.5f)));
                    currentRound.setTurnPhase(Round.TurnPhase.END);
                }
                break;

            case END:   // End current turn
                //System.out.println("TurnPhase.END");
                if (!infoLabel.hasActions()) {
                    infoLabel.setText("NEW TURN");
                    infoLabel.addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(1.5f), Actions.fadeOut(0.5f)));
                    //dice.unActivate();
                    currentRound.endTurn();
                    currentRound.setTurnPhase(Round.TurnPhase.PREPARATION);
                }
                break;
        }

        // Update camera position
        Camera mainCamera = mainStage.getCamera();
        // TODO: track only current player chip
        for (Player p: Player.getPlayersList()) {    // if player chip movement centralize camera on chip
            ChipActor chip = p.getChip();
            if (chip.isBusy()) {
                mainCamera.position.x = chip.getX() - chip.getWidth()/2;
                mainCamera.position.y = chip.getY() - chip.getHeight()/2;
                //mainCamera.update();

                // debug
                //System.out.println("cam pos: " + mainCamera.position);
                //System.out.println("chip pos: " + p.getPositionX() / 2 + " " + p.getPositionY() / 2);
                //System.out.println();
            }
        }
        mainCamera.position.x = MathUtils.clamp(mainCamera.position.x,
                mainCamera.viewportWidth/2,
                worldWidth - mainCamera.viewportWidth/2);
        mainCamera.position.y = MathUtils.clamp(mainCamera.position.y,
                mainCamera.viewportHeight/2,
                worldHeight - mainCamera.viewportHeight/2);
        tiledCamera.position.set(mainStage.getCamera().position);
        tiledCamera.update();
        mapRenderer.setView(tiledCamera);
    }

    @Override
    public void render(float dt) {
        /*-----------------------------------UPDATE SECTION-------------------------------------------------*/
        uiStage.act(dt);
        //if (!isPaused()) {
        mainStage.act(dt);
        update(dt);
        //}

        /*-----------------------------------OUTPUT SECTION-------------------------------------------------*/
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        mainStage.draw();
        uiStage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height);
        uiStage.getViewport().update(width, height, true);
        tiledViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mainStage.dispose();
        uiStage.dispose();
    }
}
