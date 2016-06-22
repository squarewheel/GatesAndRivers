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

import java.util.ArrayList;

/**
 * @author Sasha Kvachenko
 *         Created on 31.05.2016.
 *         <p>
 *         Provides gameboard player.
 *         Players must be created after gameboard fields.  // TODO: unbind class from fields class
 */
public class Player {
    private static ArrayList<Color> colors = new ArrayList<Color>();
    private static ArrayList<String> names = new ArrayList<String>();
    private static ArrayList<Player> playersList = new ArrayList<Player>();
    private static final int MAX_PLAYERS = 4;

    private ChipActor chip;
    private String name;
    private boolean playable;       // indicates who control this player: human or ai

    /** Possible colors of Players */
    static {
        colors.add(Color.RED);
        colors.add(Color.SKY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);

        // Default players names
        names.add("RED");
        names.add("BLUE");
        names.add("GREEN");
        names.add("YELLOW");
    }

    public Player() {
        if (playersList.size() > MAX_PLAYERS) return;   // Do not create a player if already created 4
        playersList.add(this);

        // Basic initialization
        name = "Mr " + names.remove(0);
        playable = false;

        chip = new ChipActor();
        chip.setColor(colors.remove(0));
        chip.setSize(32, 32);
        chip.setOrigin(16, 16);
    }

    public void makePlayable() { playable = true; }

//    public void setPlayable(boolean b) { playable = b; }

    public ChipActor getChip() { return chip; }

    public String getName() { return name; }

    public boolean isMoved() { return chip.getState() == ChipActor.State.MOVED; }

    public boolean isPlayable() { return playable; }

    public static ArrayList<Player> getPlayersList() {
        return playersList;
    }

}
