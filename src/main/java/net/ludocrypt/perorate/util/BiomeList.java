package net.ludocrypt.perorate.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.mixin.biome.MultiNoiseBiomeSourceAccessor;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.MixedNoisePoint;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

public class BiomeList<E> extends ArrayList<E> {

	private static final long serialVersionUID = -4413563891178735978L;

	public BiomeList<Biome> addStream(Iterator<Pair<MixedNoisePoint, Supplier<Biome>>> iterator) {
		BiomeList<Biome> list = new BiomeList<Biome>();

		iterator.forEachRemaining((pair) -> {
			Supplier<Biome> supplier = pair.getSecond();
			list.add(supplier.get());
		});

		return list;
	}

	public BiomeList<Biome> addFromMultiNoiseESource(MultiNoiseBiomeSource source) {
		return addStream(((MultiNoiseBiomeSourceAccessor) source).getBiomePoints().iterator());
	}

}
