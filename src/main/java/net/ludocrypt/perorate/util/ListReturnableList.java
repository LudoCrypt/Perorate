package net.ludocrypt.perorate.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.mixin.biome.MultiNoiseBiomeSourceAccessor;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.MixedNoisePoint;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

public class ListReturnableList<E> extends ArrayList<E> {

	private static final long serialVersionUID = -4413563891178735978L;

	public ListReturnableList<E> ListReturnableadd(E e) {
		super.add(e);
		return this;
	}

	public ListReturnableList<E> ListReturnableadd(int index, E element) {
		super.add(index, element);
		return this;
	}

	public ListReturnableList<E> ListReturnableaddAll(Collection<? extends E> c) {
		super.addAll(c);
		return this;
	}

	public ListReturnableList<E> ListReturnableaddAll(int index, Collection<? extends E> c) {
		super.addAll(index, c);
		return this;
	}

	public ListReturnableList<Biome> addStream(Iterator<Pair<MixedNoisePoint, Supplier<Biome>>> iterator) {
		ListReturnableList<Biome> list = new ListReturnableList<Biome>();

		iterator.forEachRemaining((pair) -> {
			Supplier<Biome> supplier = pair.getSecond();
			list.add(supplier.get());
		});

		return list;
	}

	public ListReturnableList<Biome> addFromMultiNoiseESource(MultiNoiseBiomeSource source) {
		return addStream(((MultiNoiseBiomeSourceAccessor) source).getBiomePoints().iterator());
	}

}
