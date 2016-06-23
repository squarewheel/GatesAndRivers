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

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;

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
        BitmapFont defaultFont = new BitmapFont(Gdx.files.local("sansman16.fnt"));
        //defaultFont.setColor(Color.DARK_GRAY);
        //skin.add("backgroundImage", new Image(new TextureRegion(new Texture("main_screen2.png"))));
        skin.add("defaultFont", defaultFont);
        skin.add("infoLabelFont", new BitmapFont(Gdx.files.local("sansman64.fnt")));
        skin.add("infoLabelStyle", new Label.LabelStyle(skin.getFont("infoLabelFont"), Color.WHITE));
        skin.add("labelStyle", new Label.LabelStyle(skin.getFont("defaultFont"), Color.WHITE));
        skin.add("buttonUpImg", new NinePatch(new Texture("grey_button12.png"), 10, 10, 10, 10));
        skin.add("buttonDownImg", new NinePatch(new Texture("grey_button13.png"), 10, 10, 10, 10));
        skin.add("checkBoxImg", new NinePatch(new Texture("grey_box.png"), 8, 8, 8, 8));
        skin.add("checkBoxMarkImg", new NinePatch(new Texture("grey_boxCheckmark.png"), 8, 8, 8, 8));
        skin.add("frameImg", new NinePatch(new Texture("grey_frame.png"), 8, 8, 8, 8));
        skin.add("defaultButtonStyle", new Button.ButtonStyle(
                skin.getDrawable("buttonUpImg"),
                skin.getDrawable("buttonDownImg"),
                skin.getDrawable("buttonUpImg")));
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(
                skin.getDrawable("buttonUpImg"),
                skin.getDrawable("buttonDownImg"),
                skin.getDrawable("buttonUpImg"),
                skin.getFont("defaultFont"));
        CheckBox.CheckBoxStyle cbs = new CheckBox.CheckBoxStyle(
                skin.getDrawable("checkBoxImg"),
                skin.getDrawable("checkBoxMarkImg"),
                skin.getFont("defaultFont"),
                Color.WHITE);
        tbs.pressedOffsetY = -4;
        tbs.fontColor = Color.LIGHT_GRAY;
        tbs.disabled = skin.getDrawable("buttonDownImg");
        //tbs.
        skin.add("textButtonStyle", tbs);
        skin.add("checkBoxStyle", cbs);
        //skin.get("textButtonStyle", TextButton.TextButtonStyle.class).fontColor.set(Color.DARK_GRAY);

        // load star screen
        setScreen(new TitleScreen(this));
	}

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }
}
