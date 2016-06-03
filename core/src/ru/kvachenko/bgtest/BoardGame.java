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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class BoardGame extends Game {
    InputMultiplexer im;
	//private GameScreen mainScreen;
	Skin skin;

    @Override
	public void create () {
        im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        // System.out.println(im.getProcessors());

        // skin
        skin = new Skin();
        BitmapFont defaultFont = new BitmapFont(Gdx.files.local("pixelfont16.fnt"));
        //defaultFont.setColor(Color.DARK_GRAY);
        skin.add("defaultFont", defaultFont);
        skin.add("infoLabelFont", new BitmapFont(Gdx.files.local("8bit64.fnt")));
        skin.add("infoLabelStyle", new Label.LabelStyle(skin.getFont("infoLabelFont"), Color.PURPLE));
        skin.add("labelStyle", new Label.LabelStyle(skin.getFont("defaultFont"), Color.LIME));
        skin.add("buttonUpImg", new NinePatch(new Texture("grey_button12.png"), 10, 10, 10, 10));
        skin.add("buttonDownImg", new NinePatch(new Texture("grey_button13.png"), 10, 10, 10, 10));
        skin.add("defaultButtonStyle", new Button.ButtonStyle(skin.getDrawable("buttonUpImg"),
                                                              skin.getDrawable("buttonDownImg"),
                                                              skin.getDrawable("buttonUpImg")));
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(
                skin.getDrawable("buttonUpImg"),
                skin.getDrawable("buttonDownImg"),
                skin.getDrawable("buttonUpImg"),
                skin.getFont("defaultFont"));
        //tbs.fontColor = Color.DARK_GRAY;
        tbs.pressedOffsetY = -4;
        skin.add("textButtonStyle", tbs);
        //skin.get("textButtonStyle", TextButton.TextButtonStyle.class).fontColor.set(Color.DARK_GRAY);

        // load star screen
        setScreen(new GameScreen(this));
	}

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }
}
