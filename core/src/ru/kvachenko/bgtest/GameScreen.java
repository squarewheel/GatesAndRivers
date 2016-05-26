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
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author Sasha Kvachenko
 *         Created on 20.05.2016.
 *         <p>
 *         Class for main game screen.
 */
public class GameScreen implements Screen {
    Stage mainStage;
    //Stage uiStage;
    ScreenViewport tiledViewport;
    OrthographicCamera tiledCamera;
    OrthogonalTiledMapRenderer mapRenderer;
    int worldWidth;
    int worldHeight;
    ArrayList<ChipActor> players;
    LinkedList<Actor> fields;

    //int viewWidth = 1200;
    //int viewHeight = 600;

    public GameScreen() {
        worldWidth = 64 * 32;
        worldHeight = 64 * 32;
        mainStage = new Stage(new ScreenViewport());
        //uiStage = new Stage();

        // tmx map and renderer
        TiledMap tiledMap = new TmxMapLoader().load("main_screen2.tmx");
        fields = new LinkedList<Actor>();
        MapObjects fieldObjects = tiledMap.getLayers().get("fields").getObjects();
        for (MapObject mo: fieldObjects) {
            Rectangle fieldRectangle = ((RectangleMapObject) mo).getRectangle();
            Actor field = new Actor();
            fields.add(field);
            mainStage.addActor(field);
            field.setSize(fieldRectangle.width, fieldRectangle.height);
            field.setPosition(fieldRectangle.x, fieldRectangle.y);
            // debug output
            //System.out.println(field.getX() + " " + field.getY());
            //System.out.println();
        }

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        tiledCamera = new OrthographicCamera();
        tiledViewport = new ScreenViewport(tiledCamera);
        //tiledCamera.setToOrtho(false, viewWidth, viewHeight);

        // GameBoard fields

        // Players
        Texture playerTexture = new Texture("chip_white.png");
        playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        players = new ArrayList<ChipActor>();
        players.add(new ChipActor(new TextureRegion(playerTexture, 64, 64), fields));
        ChipActor mainCharacter = players.get(0);
        mainStage.addActor(mainCharacter);
        mainCharacter.setColor(Color.RED);
        mainCharacter.setSize(32, 32);
        mainCharacter.setPosition(fields.get(0).getX() + 32, fields.get(0).getY() + 32*3);

        // Input handlers
        BoardGame.im.addProcessor(new InputAdapter(){
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
    }

    private void update(float dt) {
        ChipActor p1 = players.get(0);
        p1.moveForward();

        // Update camera position
        Camera mainCamera = mainStage.getCamera();
        for (ChipActor p: players) {    // if player chip movement centralize camera on chip
            if (p.isInMove()) {
                mainCamera.position.x = p.getX() - p.getWidth()/2;
                mainCamera.position.y = p.getY() - p.getHeight()/2;
                //mainCamera.update();

                // debug
                //System.out.println("cam pos: " + mainCamera.position);
                //System.out.println("chip pos: " + p.getX() / 2 + " " + p.getY() / 2);
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
        //uiStage.act(dt);
        //if (!isPaused()) {
        mainStage.act(dt);
        update(dt);
        //}

        /*-----------------------------------OUTPUT SECTION-------------------------------------------------*/
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        mainStage.draw();
        //uiStage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height);
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

    }
}
