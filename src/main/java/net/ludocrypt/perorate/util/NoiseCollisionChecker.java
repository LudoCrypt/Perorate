package net.ludocrypt.perorate.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.mixin.biome.MultiNoiseBiomeSourceAccessor;
import net.ludocrypt.perorate.mixin.MultiNoiseBiomeSourcePresetAccessor;
import net.ludocrypt.perorate.world.PerorateEnd;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

public class NoiseCollisionChecker {

	public static void init() {

		check(PerorateEnd.CENTER_BIOME_SOURCE);
		check(PerorateEnd.HIGHLANDS_BIOME_SOURCE);
		check(PerorateEnd.MIDLANDS_BIOME_SOURCE);
		check(PerorateEnd.SMALL_ISLANDS_BIOME_SOURCE);
		check(PerorateEnd.BARRENS_BIOME_SOURCE);

		RegistryEntryAddedCallback.event(BuiltinRegistries.BIOME).register(((i, identifier, biome) -> {
			check(PerorateEnd.CENTER_BIOME_SOURCE);
			check(PerorateEnd.HIGHLANDS_BIOME_SOURCE);
			check(PerorateEnd.MIDLANDS_BIOME_SOURCE);
			check(PerorateEnd.SMALL_ISLANDS_BIOME_SOURCE);
			check(PerorateEnd.BARRENS_BIOME_SOURCE);
		}));

	}

	private static void check(MultiNoiseBiomeSource.Preset biomeSourcePreset) {

		MultiNoiseBiomeSource biomeSource = biomeSourcePreset.getBiomeSource(BuiltinRegistries.BIOME, 0L);

		List<Pair<Biome.MixedNoisePoint, Supplier<Biome>>> points = ((MultiNoiseBiomeSourceAccessor) biomeSource).getBiomePoints();

		// Build a reverse-map from noise point to Biome
		Map<Biome.MixedNoisePoint, Biome> noisePoints = new HashMap<>();
		for (Pair<Biome.MixedNoisePoint, Supplier<Biome>> point : points) {
			Biome biome = point.getSecond().get();
			Biome.MixedNoisePoint noisePoint = point.getFirst();
			if (biome == null) {
				System.out.println("WARNING: Found null-biome for noise-point " + toString(noisePoint));
				continue;
			}

			Biome prev = noisePoints.put(noisePoint, biome);
			if (prev != null) {
				System.out.println("WARNING: " + biome + " and " + prev + " have the same mixed noise point in source of " + ((MultiNoiseBiomeSourcePresetAccessor) biomeSourcePreset).id() + toString(noisePoint) + "! They won't generate properly!!!!");
			}
		}
	}

	private static String toString(Biome.MixedNoisePoint noisePoint) {
		return Biome.MixedNoisePoint.CODEC.encodeStart(JsonOps.INSTANCE, noisePoint).get().left().get().toString();
	}

}
