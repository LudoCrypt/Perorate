package net.ludocrypt.perorate;

import net.fabricmc.api.ModInitializer;
import net.ludocrypt.perorate.util.NoiseCollisionChecker;

public class Perorate implements ModInitializer {

	@Override
	public void onInitialize() {
		NoiseCollisionChecker.init();
	}

}
