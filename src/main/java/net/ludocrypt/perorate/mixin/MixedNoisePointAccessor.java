package net.ludocrypt.perorate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.biome.Biome;

@Mixin(Biome.MixedNoisePoint.class)
public interface MixedNoisePointAccessor {

	@Accessor("temperature")
	float temperature();

	@Accessor("humidity")
	float humidity();

	@Accessor("altitude")
	float altitude();

	@Accessor("weirdness")
	float weirdness();

	@Accessor("weight")
	float weight();
}
