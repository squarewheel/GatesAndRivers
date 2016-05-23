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

import com.badlogic.gdx.*;

public class BoardGame extends Game {
	GameScreen mainScreen;
	static InputMultiplexer im;
	@Override
	public void create () {

        im = new InputMultiplexer();
        //im.addProcessor(new MyUiInputProcessor());
        //im.addProcessor(new GameScreenInputListener(mainScreen));
        Gdx.input.setInputProcessor(im);
        mainScreen = new GameScreen();
        System.out.println(im.getProcessors());
        setScreen(new GameScreen());
	}
}
