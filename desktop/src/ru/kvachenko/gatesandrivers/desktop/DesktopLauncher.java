package ru.kvachenko.gatesandrivers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.kvachenko.gatesandrivers.GatesAndRivers;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Gates and Rivers";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new GatesAndRivers(), config);
	}
}
