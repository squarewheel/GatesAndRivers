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

package ru.kvachenko.basegame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author Sasha Kvachenko
 *         Created on 30.05.2016.
 *         <p>
 *         Abstaract libGDX actor with texture.
 */
public class BaseActor extends Actor {
    private TextureRegion texture;

    public BaseActor(TextureRegion r) {
        super();
        setTexture(r);
        setSize(r.getRegionWidth(), r.getRegionHeight());
    }

    public BaseActor(){
        super();
    }

    protected void setTexture(TextureRegion r) {
        r.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texture = r;
    }

    protected TextureRegion getTexture() {
        return texture;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        if (texture != null) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }
}
