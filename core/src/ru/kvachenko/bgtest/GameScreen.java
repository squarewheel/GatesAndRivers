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
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * @author Sasha Kvachenko
 *         Created on 20.05.2016.
 *         <p>
 *         Class for main game screen.
 */
public class GameScreen implements Screen {
    Stage mainStage;
    //private Stage uiStage;
    ScreenViewport tiledViewport;
    OrthographicCamera tiledCamera;
    private OrthogonalTiledMapRenderer mapRenderer;
    int worldWidth;
    int worldHeight;
    int viewWidth = 1200;
    int viewHeight = 600;

    public GameScreen() {
        worldWidth = 64 * 32;
        worldHeight = 64 * 32;
        //mainStage = new Stage(new FitViewport(viewWidth, viewHeight));
        mainStage = new Stage(new ScreenViewport());
        //uiStage = new Stage();
        tiledCamera = new OrthographicCamera();
        tiledViewport = new ScreenViewport(tiledCamera);
        //tiledCamera.setToOrtho(false, viewWidth, viewHeight);
        TiledMap tiledMap = new TmxMapLoader().load("main_screen2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        BoardGame.im.addProcessor(new InputAdapter(){
            Vector3 lastTouchDown;

            public boolean touchDown(int x, int y, int pointer, int button) {
                lastTouchDown = new Vector3(x, y, 0);
                return false;
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                Camera stageCamera = mainStage.getCamera();
                //System.out.println("start pos: " + stageCamera.position);

                Vector3 newPos = new Vector3(x, y, 0);
                Vector3 offset = newPos.sub(lastTouchDown);
                //System.out.println("offset: " + offset);

                /*
                stageCamera.position.x = MathUtils.clamp(stageCamera.position.x - offset.x,
                        stageCamera.viewportWidth/2,
                        worldWidth - stageCamera.viewportWidth/2);
                stageCamera.position.y = MathUtils.clamp(stageCamera.position.y + offset.y,
                        stageCamera.viewportHeight/2,
                        worldHeight - stageCamera.viewportHeight/2);
                */

                stageCamera.position.x = stageCamera.position.x - offset.x;
                stageCamera.position.y = stageCamera.position.y + offset.y;

                lastTouchDown.add(offset);

                //System.out.println("new pos: " + stageCamera.position);

                //System.out.println();
                //stage.getCamera().translate(1,1,0);
                return false;
            }


        });
    }

    private void update(float dt) {

        // Update camera position
        Camera mainCamera = mainStage.getCamera();
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
