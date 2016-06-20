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
    private GameplayController gameController;
    private Stage mainStage;
    private Stage uiStage;
    private Table uiTable;
    private Image background;
    //private ScreenViewport tiledViewport;
    //private OrthographicCamera tiledCamera;
    //private OrthogonalTiledMapRenderer mapRenderer;
    private int worldWidth;
    private int worldHeight;
    private Camera camera;
//    private Vector2 camTarget;
    private DiceWidget dice;
    private Label infoLabel;

    // Temp variables
    private boolean debug = false;
    //private Player currentPlayer;
    //int viewWidth = 1200;
    //int viewHeight = 600;

    GameScreen(BoardGame bg) {
        // Base initialization
        game = bg;
        worldWidth = 64 * 32;
        worldHeight = 64 * 32;
        mainStage = new Stage(new ScreenViewport());
        camera = mainStage.getCamera();
//        camTarget = new Vector2();
        background = new Image(new TextureRegion(new Texture("main_screen3.png")));
        mainStage.addActor(background);
        uiStage = new Stage(new ScreenViewport());
        uiTable = new Table(bg.skin);
        uiStage.addActor(uiTable);
        dice = new DiceWidget();
        dice.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Dice can be rolled only in START phase
                if (gameController.getTurnPhase() == GameplayController.TurnPhase.START) {
                    if (dice.isActive() && dice.getState() == DiceWidget.State.READY) {
                        dice.roll();
                        gameController.setTurnPhase(GameplayController.TurnPhase.DICE_ROLLING);
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // Gameboard fields
        // TODO: may be need special class for creating gameboard
        //TiledMap tiledMap = new AtlasTmxMapLoader().load("main_screen3.tmx");
        TiledMap tiledMap = new TmxMapLoader().load("main_screen3.tmx");
        MapObjects fieldObjects = tiledMap.getLayers().get("fields").getObjects();
        for (MapObject mo: fieldObjects) {
            Rectangle fieldRectangle = ((RectangleMapObject) mo).getRectangle();
            FieldActor field = new FieldActor();
//            field.setDebugLabel("0", bg.skin, "labelStyle");
            field.setSize(fieldRectangle.width, fieldRectangle.height);
            field.setPosition(fieldRectangle.x, fieldRectangle.y);
            MapProperties fieldProperties = mo.getProperties();
            if (fieldProperties.containsKey("portalEndpoint")) {
                Gate g = new Gate(field); //g.debug();
                FieldActor gateField = FieldActor.getFieldsList().get(Integer.valueOf((String) fieldProperties.get("portalEndpoint")) - 1);
                gateField.addActor(g);
                gateField.setMover(g);
                g.setPosition(gateField.getWidth()/2 - g.getWidth()/2, gateField.getHeight()/2 - g.getHeight()/2);
                //gateField.debug();
            }
            else if (fieldProperties.containsKey("river")) {
                River r = new River(FieldActor.getFieldsList().get(Integer.valueOf((String) fieldProperties.get("riverEndpoint")) - 1));
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

//        for (MapObject mo: fieldObjects) {
//            MapProperties properties = mo.getProperties();
//            if (properties.containsKey("portal")) {
//                System.out.println(properties.get("portal"));
//                //int targetId = Integer.valueOf((String) p.get("portal"));
//                new Gate(FieldActor.getFieldsList().get(Integer.valueOf((String) properties.get("portal"))));
//            }
//        }

        // Players and Round initialization
        new Player();
        new Player();
        new Player();
        new Player();
        //Player.getPlayersList().get(0).makePlayable();
        for (Player p: Player.getPlayersList()) { mainStage.addActor(p.getChip()); }
        gameController = new GameplayController(Player.getPlayersList());

        // User interface
        infoLabel = new Label("Im Love my Wife", bg.skin, "infoLabelStyle");
        gameController.setInfoLabel(infoLabel);
        //Label rollResultLabel = new Label("Roll Result: ", bg.skin, "labelStyle");
        //Label rollResult = new Label("-", bg.skin, "labelStyle");
        //dice.setRollResultLabel(rollResult);
        Table statsList = new Table();
        //statsList.add(customLabel).left().top().pad(5, 5, 0, 0);
        //statsList.row();
        //statsList.add(rollResultLabel).right().top().pad(5, 5, 0, 0);
        //statsList.add(rollResult).left().top().padTop(5);
        //statsList.row();
        statsList.add(gameController.new RoundCounterLabel("", bg.skin, "labelStyle")).right().top().padTop(5);
        uiTable.setFillParent(true);
        uiTable.top();
        uiTable.add(statsList).left().top().pad(5, 5, 0, 0);
        uiTable.row().expandY();
        uiTable.add(infoLabel);//.spaceBottom(10);
        if (debug) uiTable.setDebug(true);
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
        //statsList.debug();
        //uiStage.addActor(customLabel);
        //uiTable.getCell(customLabel).getMaxHeight();
        //infoLabel.setOrigin(infoLabel.getWidth()/2, infoLabel.getHeight()/2);
        //infoLabel.addAction(Actions.moveTo(uiStage.getWidth()/2, uiStage.getHeight()/2));
        //infoLabel.addAction(Actions.fadeOut(2));
    }

    private void update(float dt) {
        // Update UI

        // Update game state
        switch (gameController.getTurnPhase()) {
            // TODO: encapsulate game progress into Round class
            case PREPARATION:   // Prepare game state to new turn
                if (debug) System.out.println("TurnPhase.PREPARATION");
                if (!infoLabel.hasActions()) {
                    //camera.translate(gameController.getCurrentPlayer().getChip().getX() - camera.position.x,
                    //                 gameController.getCurrentPlayer().getChip().getY() - camera.position.y, camera.position.z);
                    //camTarget.add(gameController.getCurrentPlayer().getChip().getX() - camera.position.x,
                    //              gameController.getCurrentPlayer().getChip().getY() - camera.position.y);
                    infoLabel.setText(gameController.getCurrentPlayer().getName() + " TURN");
                    infoLabel.setColor(gameController.getCurrentPlayer().getChip().getColor());
                    infoLabel.addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(1.5f), Actions.fadeOut(0.5f)));
                    if (gameController.getCurrentPlayer().isPlayable()) dice.activate();
                    dice.setState(DiceWidget.State.READY);
                    gameController.setTurnPhase(GameplayController.TurnPhase.START);
                }
                break;

            case START: // Start of current player turn. Phase end when user or ai roll dice
                if (debug) System.out.println("TurnPhase.START");
                if (!infoLabel.hasActions()) {
                    if (!gameController.getCurrentPlayer().isPlayable()) {
                        // TODO: method calls many times what it need; possible solution it add players state
                        dice.roll();
                    }
//                    else {
//                        // Only for debug! Delete else block after debug
//                        dice.setRollResult(1);
//                        gameController.setTurnPhase(Round.TurnPhase.DICE_ROLLED);
//                    }
                    if (dice.getState() == DiceWidget.State.ROLLING)
                        gameController.setTurnPhase(GameplayController.TurnPhase.DICE_ROLLING);
                }
                break;

            case DICE_ROLLING:  // Wait when dice roll animation finish
                if (debug) System.out.println("TurnPhase.DICE_ROLLING");
                if (dice.getState() == DiceWidget.State.ROLLED) {
                    dice.unActivate();
                    gameController.setTurnPhase(GameplayController.TurnPhase.DICE_ROLLED);
                }
                break;

            case DICE_ROLLED:   // Determines number of fields what current player must go
                // TODO: looks like this phase is not necessary
                if (debug) System.out.println("TurnPhase.DICE_ROLLED");
                gameController.getCurrentPlayer().getChip().moveOn(dice.getRollResult());
                gameController.setTurnPhase(GameplayController.TurnPhase.MOVEMENT);
                break;

            case MOVEMENT:  // Move current player chip
                if (debug) System.out.println("TurnPhase.MOVEMENT");
                //gameController.getCurrentPlayer().move();
                if (gameController.getCurrentPlayer().isMoved()) {
//                    infoLabel.setText("TURN COMPLETE");
//                    infoLabel.addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(1.5f), Actions.fadeOut(0.5f)));
                    gameController.setTurnPhase(GameplayController.TurnPhase.END);
                }
                break;

            case END:   // End current turn
                if (debug) System.out.println("TurnPhase.END");
                if (!infoLabel.hasActions()) {
                    //infoLabel.setText("NEW TURN");
                    //infoLabel.addAction(Actions.sequence(Actions.fadeIn(0.5f), Actions.delay(1.5f), Actions.fadeOut(0.5f)));
                    //dice.unActivate();
                    gameController.endTurn();
                    if (!gameController.gameOver()) gameController.setTurnPhase(GameplayController.TurnPhase.PREPARATION);
                }
                break;
        }

        // Update camera position
        //Camera mainCamera = mainStage.getCamera();
        //for (Player p: Player.getPlayersList()) {    // if player chip movement centralize camera on chip
        ChipActor currentPlayerChip = gameController.getCurrentPlayer().getChip();
        if (currentPlayerChip.getState() != ChipActor.State.WAIT) {
            camera.position.x = currentPlayerChip.getX() - currentPlayerChip.getWidth() / 2;
            camera.position.y = currentPlayerChip.getY() - currentPlayerChip.getHeight() / 2;
            //mainCamera.update();

            // debug
            //System.out.println("cam pos: " + mainCamera.position);
            //System.out.println("chip pos: " + p.getPositionX() / 2 + " " + p.getPositionY() / 2);
            //System.out.println();
        }

        //}
        camera.position.x = MathUtils.clamp(camera.position.x,
                camera.viewportWidth/2,
                worldWidth - camera.viewportWidth/2);
        camera.position.y = MathUtils.clamp(camera.position.y,
                camera.viewportHeight/2,
                worldHeight - camera.viewportHeight/2);
        //tiledCamera.position.set(mainStage.getCamera().position);
        //tiledCamera.update();
        //mapRenderer.setView(tiledCamera);
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
        //mapRenderer.render();
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
        //tiledViewport.update(width, height);
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
