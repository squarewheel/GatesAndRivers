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

/**
 * @author Sasha Kvachenko
 *         Created on 31.05.2016.
 *         <p>
 *         Provides gameboard player.
 *         Players must be created after gameboard fields.  // TODO: unbind class from fields class
 */
public class Player {
    private ChipActor chip;
    private String name;
    private boolean playable;       // indicates who control this player: human or ai
    private boolean autoRoll;       // If true, player makes roll automatic

    public Player(String name, Color color) {
        this.name = name;
        playable = false;
        autoRoll = true;

        chip = new ChipActor();
        chip.setColor(color);
        chip.setSize(32, 32);
        chip.setOrigin(16, 16);
    }

//    public Player() {
//        if (playersList.size() > MAX_PLAYERS) return;   // Do not create a player if already created 4
//        playersList.add(this);
//
//        // Basic initialization
//        name = "Mr. " + names.remove(0);
//        playable = false;
//        autoRoll = true;
//
//        chip = new ChipActor();
//        chip.setColor(colors.remove(0));
//        chip.setSize(32, 32);
//        chip.setOrigin(16, 16);
//    }

    public void makePlayable() {
        playable = true;
        autoRoll = false;
    }

//    public void setPlayable(boolean b) { playable = b; }

    public ChipActor getChip() { return chip; }

    public String getName() { return name; }

    public boolean getAutoRoll() { return autoRoll; }

    public void setAutoRoll(boolean b) { autoRoll = b; }

//    public boolean isMoved() { return chip.getState() == ChipActor.State.MOVED; }

    public boolean isPlayable() { return playable; }

}
