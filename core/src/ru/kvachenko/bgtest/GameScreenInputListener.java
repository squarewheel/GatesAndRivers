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

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author Sasha Kvachenko
 *         Created on 20.05.2016.
 *         <p>
 *         File description.
 */
public class GameScreenInputListener extends InputAdapter {
    Stage stage;
    Vector3 lastTouchDown;
    //Vector3 new_position;

    GameScreenInputListener(Stage s) {
        stage = s;
        //prevCamPos = stage.getCamera().position;
        //new_position = new Vector3();
        //stage = s;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        //Camera cam =  stage.getCamera();
        //cam.translate(1,1,0);
        //cam.update();
        lastTouchDown = new Vector3(x, y, 0);
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        /*new_position = last_touch_down;
        new_position = new_position.sub(screenX, screenY, 0);
        new_position.y = -new_position.y;
        new_position.add( s.mainStage.getCamera().position );
        s.mainStage.getCamera().translate( new_position.sub( s.mainStage.getCamera().position ) );
        System.out.println("mouse: " + screenX + " " + screenY);*/

        /*
        System.out.println("prev: " + prevCamPos);
        Vector3 offset = prevCamPos.sub(new Vector3(x, y, 0));
        System.out.println("offset: " + offset);
        stage.getCamera().translate(offset);
        prevCamPos = stage.getCamera().position;
        System.out.println("translated: " + prevCamPos);
        System.out.println();
        */

        Camera sc = stage.getCamera();
        System.out.println("start pos: " + stage.getCamera().position);

        Vector3 newPos = new Vector3(x, y, 0);
        Vector3 offset = newPos.sub(lastTouchDown);
        //offset.x *= -1;
        System.out.println("offset: " + offset);

        sc.position.x = sc.position.x - offset.x;
        sc.position.y = sc.position.y + offset.y;
        lastTouchDown.add(offset);
        System.out.println("new pos: " + stage.getCamera().position);

        System.out.println();
        //stage.getCamera().translate(1,1,0);
        return false;
    }
}
