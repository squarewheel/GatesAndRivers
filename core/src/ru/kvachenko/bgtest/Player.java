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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 31.05.2016.
 *         <p>
 *         Provides gameboard player.
 *         Players must be created after gameboard fields.
 */
public class Player {
    private static ArrayList<Color> colors = new ArrayList<Color>();
    private static ArrayList<Player> playersList = new ArrayList<Player>();
    private static final int MAX_PLAYERS = 4;

    private ChipActor chip;
    private boolean moved;      // indicates whether or not the player has made his move
    private boolean playable;   // indicates who control this player: human or ai

    static {
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
    }

    public Player() {
        // Do not create a player if already created 4
        if (playersList.size() > MAX_PLAYERS) return;

        // Basic initialization
        playersList.add(this);
        moved = false;
        playable = false;

        // Create actor chip
        Texture playerTexture = new Texture("chip_white.png");
        playerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        chip = new ChipActor(new TextureRegion(playerTexture, 64, 64), FieldActor.getFieldsList().get(0));
        chip.setColor(colors.remove(0));
        chip.setSize(32, 32);
    }

    public ChipActor getChip() { return chip; }

    public boolean isMoved() { return moved; }

    public boolean isPlayable() { return playable; }

    public static ArrayList<Player> getPlayersList() {
        return playersList;
    }

}
