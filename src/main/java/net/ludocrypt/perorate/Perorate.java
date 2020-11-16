package net.ludocrypt.perorate;

import net.fabricmc.api.ModInitializer;
import net.ludocrypt.perorate.util.NoiseCollisionChecker;
import net.ludocrypt.perorate.world.EndMultiNoiseBiomeSource;
import net.ludocrypt.perorate.world.PerorateEnd;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class Perorate implements ModInitializer {

	@Override
	public void onInitialize() {

		Registry.register(Registry.BIOME_SOURCE, new Identifier("perorate", "end_multi_noise"), EndMultiNoiseBiomeSource.CODEC);

		// Default End Biomes
		PerorateEnd.addCenterBiome(BiomeKeys.THE_END, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PerorateEnd.addHighlandsBiome(BiomeKeys.END_HIGHLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PerorateEnd.addMidlandsBiome(BiomeKeys.END_MIDLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PerorateEnd.addSmallIslandsBiome(BiomeKeys.SMALL_END_ISLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
		PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		NoiseCollisionChecker.init();
	}

}
