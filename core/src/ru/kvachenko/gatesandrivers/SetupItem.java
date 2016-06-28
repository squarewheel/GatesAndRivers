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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * @author Sasha Kvachenko
 *         Created on 27.06.2016.
 *         <p>
 *         File description.
 */
public class SetupItem {
    Color playerColor;
    Image colorImage;
    TextField.TextFieldStyle nameFieldStyle;
    TextField nameField;
    CheckBox available;
    CheckBox playable;

    SetupItem(Color c, String name, Skin skin) {
        playerColor = c;
        colorImage = new Image(skin.getDrawable("checkBoxImg"));
        colorImage.setColor(playerColor);
        nameFieldStyle = new TextField.TextFieldStyle(skin.get("textFieldStyle", TextField.TextFieldStyle.class));
        nameField = new TextField(name, nameFieldStyle);
        available = new CheckBox("", skin, "checkBoxStyle");
        playable = new CheckBox("", skin, "checkBoxStyle");
        available.setChecked(true);
    }

    public Color getColor() { return playerColor; };

    public String getName() { return nameField.getText().trim(); }
}