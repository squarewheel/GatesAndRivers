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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author Sasha Kvachenko
 *         Created on 20.05.2016.
 *         <p>
 *         File description.
 */
public class GameScreen implements Screen {
    Stage mainStage;
    //private Stage uiStage;
    OrthographicCamera tiledCamera;
    private OrthogonalTiledMapRenderer mapRenderer;
    int worldWidth;
    int worldHeight;
    int viewWidth = 1200;
    int viewHeight = 600;

    public GameScreen() {
        mainStage = new Stage();
        //uiStage = new Stage();
        tiledCamera = new OrthographicCamera();
        tiledCamera.setToOrtho(false, viewWidth, viewHeight);
        TiledMap tiledMap = new TmxMapLoader().load("main_screen.tmx");
        worldWidth = 87 * 32;
        worldHeight = 87 * 32;
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        BoardGame.im.addProcessor(new GameScreenInputListener(mainStage));
    }

    private void update(float dt) {

        // Update camera position
        Camera cam = mainStage.getCamera();
        tiledCamera.position.x = cam.position.x;
        tiledCamera.position.y = cam.position.y;
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
