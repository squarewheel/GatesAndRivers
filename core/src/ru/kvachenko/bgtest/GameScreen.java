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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author Sasha Kvachenko
 *         Created on 20.05.2016.
 *         <p>
 *         File description.
 */
public class GameScreen implements Screen {
    protected Stage mainStage;
    //private Stage uiStage;
    protected OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    protected int worldWidth;
    protected int worldHeight;
    protected final int viewWidth = 1200;
    protected final int viewHeight = 600;
    int camOffsetX;
    int camOffsetY;
    //static Vector3 last_touch_down;

    public GameScreen() {
        mainStage = new Stage();
        //uiStage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewWidth, viewHeight);
        TiledMap tiledMap = new TmxMapLoader().load("main_screen.tmx");
        worldWidth = 87 * 32;
        worldHeight = 87 * 32;
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        BoardGame.im.addProcessor(new GameScreenInputListener(mainStage) );

            /*
            {
            //static Vector3 last_touch_down;

            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //mainStage.getCamera().translate(5,5,0);
                //camera.update();
                Vector3 last_touch_down = new Vector3( screenX, screenY, 0);
                return true;
            }

            public boolean touchDragged(int screenX, int screenY, int pointer) {
                //last_touch_down = new Vector3( screenX, screenY, 0);
                Vector3 new_position = last_touch_down;
                new_position.sub(screenX, screenY, 0);
                new_position.y = -new_position.y;
                new_position.add( mainStage.getCamera().position );
                mainStage.getCamera().translate( new_position.sub( mainStage.getCamera().position ) );
                last_touch_down.set( screenX, screenY, 0);

                //mainStage.getCamera().translate(5,5,0);
                return false;
            }
        }
        */
        //);
    }

    private void update(float dt) {
        //camera.update();
        Camera cam = mainStage.getCamera();
        //cam.position.x = camera.position.x;
        //cam.position.y = camera.position.y;
        //cam.update();
        //cam.position.x = cam.position.x + 1;
        //cam.position.y = cam.position.y + 1;
        //cam.translate(1, 1, 0);
        //System.out.println("offset: " + camOffsetX + " " + camOffsetY);
        camera.position.x = cam.position.x;
        camera.position.y = cam.position.y;
        //System.out.println("cam: " + camera.position.x + " " + camera.position.y);
        camera.update();
        mapRenderer.setView(camera);
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
