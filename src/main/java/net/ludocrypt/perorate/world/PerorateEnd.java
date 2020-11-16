package net.ludocrypt.perorate.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.ludocrypt.perorate.mixin.MixedNoisePointAccessor;
import net.ludocrypt.perorate.mixin.MultiNoiseBiomeSourceAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

public class PerorateEnd {

	public static final Map<RegistryKey<Biome>, Biome.MixedNoisePoint> CENTER_NOISE_POINTS = new HashMap<>();
	public static final Map<RegistryKey<Biome>, Biome.MixedNoisePoint> HIGHLANDS_NOISE_POINTS = new HashMap<>();
	public static final Map<RegistryKey<Biome>, Biome.MixedNoisePoint> MIDLANDS_NOISE_POINTS = new HashMap<>();
	public static final Map<RegistryKey<Biome>, Biome.MixedNoisePoint> SMALL_ISLANDS_NOISE_POINTS = new HashMap<>();
	public static final Map<RegistryKey<Biome>, Biome.MixedNoisePoint> BARRENS_NOISE_POINTS = new HashMap<>();

	public static void addCenterBiome(RegistryKey<Biome> biome, Biome.MixedNoisePoint noise) {
		CENTER_NOISE_POINTS.put(biome, noise);
	}

	public static void addHighlandsBiome(RegistryKey<Biome> biome, Biome.MixedNoisePoint noise) {
		HIGHLANDS_NOISE_POINTS.put(biome, noise);
	}

	public static void addMidlandsBiome(RegistryKey<Biome> biome, Biome.MixedNoisePoint noise) {
		MIDLANDS_NOISE_POINTS.put(biome, noise);
	}

	public static void addSmallIslandsBiome(RegistryKey<Biome> biome, Biome.MixedNoisePoint noise) {
		SMALL_ISLANDS_NOISE_POINTS.put(biome, noise);
	}

	public static void addBarrensBiome(RegistryKey<Biome> biome, Biome.MixedNoisePoint noise) {
		BARRENS_NOISE_POINTS.put(biome, noise);
	}

	public static final MultiNoiseBiomeSource.Preset CENTER_BIOME_SOURCE = new MultiNoiseBiomeSource.Preset(new Identifier("perorate", "center_biome_source"), (preset, registry, long_) -> {
		List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomes = new ArrayList<>();
		CENTER_NOISE_POINTS.forEach((biomeKey, noisePoint) -> {
			Biome biome = registry.getOrThrow(biomeKey);
			biomes.add(Pair.of(noisePoint, () -> biome));
		});
		return MultiNoiseBiomeSourceAccessor.createMultiNoiseBiomeSource(long_, biomes, Optional.of(Pair.of(registry, preset)));
	});

	public static final MultiNoiseBiomeSource.Preset HIGHLANDS_BIOME_SOURCE = new MultiNoiseBiomeSource.Preset(new Identifier("perorate", "highlands_biome_source"), (preset, registry, long_) -> {
		List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomes = new ArrayList<>();
		HIGHLANDS_NOISE_POINTS.forEach((biomeKey, noisePoint) -> {
			Biome biome = registry.getOrThrow(biomeKey);
			biomes.add(Pair.of(noisePoint, () -> biome));
		});
		return MultiNoiseBiomeSourceAccessor.createMultiNoiseBiomeSource(long_, biomes, Optional.of(Pair.of(registry, preset)));
	});

	public static final MultiNoiseBiomeSource.Preset MIDLANDS_BIOME_SOURCE = new MultiNoiseBiomeSource.Preset(new Identifier("perorate", "midlands_biome_source"), (preset, registry, long_) -> {
		List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomes = new ArrayList<>();
		MIDLANDS_NOISE_POINTS.forEach((biomeKey, noisePoint) -> {
			Biome biome = registry.getOrThrow(biomeKey);
			biomes.add(Pair.of(noisePoint, () -> biome));
		});
		return MultiNoiseBiomeSourceAccessor.createMultiNoiseBiomeSource(long_, biomes, Optional.of(Pair.of(registry, preset)));
	});

	public static final MultiNoiseBiomeSource.Preset SMALL_ISLANDS_BIOME_SOURCE = new MultiNoiseBiomeSource.Preset(new Identifier("perorate", "small_islands_biome_source"), (preset, registry, long_) -> {
		List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomes = new ArrayList<>();
		SMALL_ISLANDS_NOISE_POINTS.forEach((biomeKey, noisePoint) -> {
			Biome biome = registry.getOrThrow(biomeKey);
			biomes.add(Pair.of(noisePoint, () -> biome));
		});
		return MultiNoiseBiomeSourceAccessor.createMultiNoiseBiomeSource(long_, biomes, Optional.of(Pair.of(registry, preset)));
	});

	public static final MultiNoiseBiomeSource.Preset BARRENS_BIOME_SOURCE = new MultiNoiseBiomeSource.Preset(new Identifier("perorate", "barrens_biome_source"), (preset, registry, long_) -> {
		List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> biomes = new ArrayList<>();
		BARRENS_NOISE_POINTS.forEach((biomeKey, noisePoint) -> {
			Biome biome = registry.getOrThrow(biomeKey);
			biomes.add(Pair.of(noisePoint, () -> biome));
		});
		return MultiNoiseBiomeSourceAccessor.createMultiNoiseBiomeSource(long_, biomes, Optional.of(Pair.of(registry, preset)));
	});

	public static ChunkGenerator createEndGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
		return new NoiseChunkGenerator(new EndMultiNoiseBiomeSource(seed,
				// Center Island
				CENTER_BIOME_SOURCE.getBiomeSource(biomeRegistry, seed),
				// Highlands Biome(s)
				HIGHLANDS_BIOME_SOURCE.getBiomeSource(biomeRegistry, seed),
				// Midlands Biome(s)
				MIDLANDS_BIOME_SOURCE.getBiomeSource(biomeRegistry, seed),
				// Small End Islands Biome(s)
				SMALL_ISLANDS_BIOME_SOURCE.getBiomeSource(biomeRegistry, seed),
				// Barrens Biome(s)
				BARRENS_BIOME_SOURCE.getBiomeSource(biomeRegistry, seed)), seed, () -> {
					return (ChunkGeneratorSettings) chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.END);
				});
	}

	// Adds biomes to all non-center areas
	public static void addEndBiomeWithWeight(RegistryKey<Biome> biomeKey, Biome.MixedNoisePoint noisePoint, int weight, boolean temperature, boolean humidity, boolean altitude, boolean weirdness) {
		addHighlandsBiomeWithWeight(biomeKey, noisePoint, weight, temperature, humidity, altitude, weirdness);
		addMidlandsBiomeWithWeight(biomeKey, noisePoint, weight, temperature, humidity, altitude, weirdness);
		addSmallIslandsBiomeWithWeight(biomeKey, noisePoint, weight, temperature, humidity, altitude, weirdness);
		addBarrensBiomeWithWeight(biomeKey, noisePoint, weight, temperature, humidity, altitude, weirdness);
	}

	// Adds biomes to all biomes that (would've) had land
	public static void addLandBiomeWithWeight(RegistryKey<Biome> biomeKey, Biome.MixedNoisePoint noisePoint, int weight, boolean temperature, boolean humidity, boolean altitude, boolean weirdness) {
		addHighlandsBiomeWithWeight(biomeKey, noisePoint, weight, temperature, humidity, altitude, weirdness);
		addMidlandsBiomeWithWeight(biomeKey, noisePoint, weight, temperature, humidity, altitude, weirdness);
		addBarrensBiomeWithWeight(biomeKey, noisePoint, weight, temperature, humidity, altitude, weirdness);
	}

	// Adds biomes to the Center
	public static void addCenterBiomeWithWeight(RegistryKey<Biome> biomeKey, Biome.MixedNoisePoint noisePoint, int weight, boolean temperature, boolean humidity, boolean altitude, boolean weirdness) {
		MixedNoisePointAccessor noise = ((MixedNoisePointAccessor) noisePoint);
		for (int i = 0; i < weight; i++) {
			addCenterBiome(biomeKey, new Biome.MixedNoisePoint(!temperature ? noise.temperature() : (1 / weight) * i + noise.temperature(), !humidity ? noise.humidity() : (1 / weight) * i + noise.humidity(), !altitude ? noise.altitude() : (1 / weight) * i + noise.altitude(), !weirdness ? noise.weirdness() : (1 / weight) * i + noise.weirdness(), noise.weight()));
		}
	}

	// Adds biomes to the Highlands
	public static void addHighlandsBiomeWithWeight(RegistryKey<Biome> biomeKey, Biome.MixedNoisePoint noisePoint, int weight, boolean temperature, boolean humidity, boolean altitude, boolean weirdness) {
		MixedNoisePointAccessor noise = ((MixedNoisePointAccessor) noisePoint);
		for (int i = 0; i < weight; i++) {
			addHighlandsBiome(biomeKey, new Biome.MixedNoisePoint(!temperature ? noise.temperature() : (1 / weight) * i + noise.temperature(), !humidity ? noise.humidity() : (1 / weight) * i + noise.humidity(), !altitude ? noise.altitude() : (1 / weight) * i + noise.altitude(), !weirdness ? noise.weirdness() : (1 / weight) * i + noise.weirdness(), noise.weight()));
		}
	}

	// Adds biomes to the Midlands
	public static void addMidlandsBiomeWithWeight(RegistryKey<Biome> biomeKey, Biome.MixedNoisePoint noisePoint, int weight, boolean temperature, boolean humidity, boolean altitude, boolean weirdness) {
		MixedNoisePointAccessor noise = ((MixedNoisePointAccessor) noisePoint);
		for (int i = 0; i < weight; i++) {
			addMidlandsBiome(biomeKey, new Biome.MixedNoisePoint(!temperature ? noise.temperature() : (1 / weight) * i + noise.temperature(), !humidity ? noise.humidity() : (1 / weight) * i + noise.humidity(), !altitude ? noise.altitude() : (1 / weight) * i + noise.altitude(), !weirdness ? noise.weirdness() : (1 / weight) * i + noise.weirdness(), noise.weight()));
		}
	}

	// Adds biomes to the Small Islands
	public static void addSmallIslandsBiomeWithWeight(RegistryKey<Biome> biomeKey, Biome.MixedNoisePoint noisePoint, int weight, boolean temperature, boolean humidity, boolean altitude, boolean weirdness) {
		MixedNoisePointAccessor noise = ((MixedNoisePointAccessor) noisePoint);
		for (int i = 0; i < weight; i++) {
			addSmallIslandsBiome(biomeKey, new Biome.MixedNoisePoint(!temperature ? noise.temperature() : (1 / weight) * i + noise.temperature(), !humidity ? noise.humidity() : (1 / weight) * i + noise.humidity(), !altitude ? noise.altitude() : (1 / weight) * i + noise.altitude(), !weirdness ? noise.weirdness() : (1 / weight) * i + noise.weirdness(), noise.weight()));
		}
	}

	// Adds biomes to the Barrens
	public static void addBarrensBiomeWithWeight(RegistryKey<Biome> biomeKey, Biome.MixedNoisePoint noisePoint, int weight, boolean temperature, boolean humidity, boolean altitude, boolean weirdness) {
		MixedNoisePointAccessor noise = ((MixedNoisePointAccessor) noisePoint);
		for (int i = 0; i < weight; i++) {
			addBarrensBiome(biomeKey, new Biome.MixedNoisePoint(!temperature ? noise.temperature() : (1 / weight) * i + noise.temperature(), !humidity ? noise.humidity() : (1 / weight) * i + noise.humidity(), !altitude ? noise.altitude() : (1 / weight) * i + noise.altitude(), !weirdness ? noise.weirdness() : (1 / weight) * i + noise.weirdness(), noise.weight()));
		}
	}

}
