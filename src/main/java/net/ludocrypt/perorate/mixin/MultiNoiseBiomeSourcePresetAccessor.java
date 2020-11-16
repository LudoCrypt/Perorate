package net.ludocrypt.perorate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;

@Mixin(MultiNoiseBiomeSource.Preset.class)
public interface MultiNoiseBiomeSourcePresetAccessor {
	@Accessor("id")
	public Identifier id();
}
