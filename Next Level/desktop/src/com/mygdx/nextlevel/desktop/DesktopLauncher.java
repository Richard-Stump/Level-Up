package com.mygdx.nextlevel.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.nextlevel.NextLevel;
import com.mygdx.nextlevel.dbHandlers.ServerDBHandler;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 960;
		config.height = 500;

		ServerDBHandler db = new ServerDBHandler();
		if (db.isDBActive()) {
			new LwjglApplication(new NextLevel(), config);
		} else {
			System.out.println();
			System.out.println("Please connect to the internet");
		}


	}
}