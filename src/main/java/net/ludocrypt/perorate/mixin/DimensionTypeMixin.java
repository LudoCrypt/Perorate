package net.ludocrypt.perorate.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.ludocrypt.perorate.world.PerorateEnd;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

@Mixin(value = DimensionType.class, priority = 69)
public class DimensionTypeMixin {

	@Inject(method = "createEndGenerator", at = @At("HEAD"), cancellable = true)
	private static void PERORATE_replaceEndGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed, CallbackInfoReturnable<ChunkGenerator> ci) {
		ci.setReturnValue(PerorateEnd.createEndGenerator(biomeRegistry, chunkGeneratorSettingsRegistry, seed));
		ci.cancel();
	}
}
