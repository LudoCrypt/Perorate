package net.ludocrypt.perorate.world;

import java.util.Arrays;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.HashCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.perorate.util.BiomeList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.gen.ChunkRandom;

public class EndMultiNoiseBiomeSource extends BiomeSource {

	public static final Codec<EndMultiNoiseBiomeSource> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.LONG.fieldOf("seed").stable().forGetter((biomeSource) -> {
			return biomeSource.seed;
		}), MultiNoiseBiomeSource.CODEC.fieldOf("centerBiome").stable().forGetter((biomeSource) -> {
			return biomeSource.centerBiome;
		}), MultiNoiseBiomeSource.CODEC.fieldOf("highlandsBiome").stable().forGetter((biomeSource) -> {
			return biomeSource.highlandsBiome;
		}), MultiNoiseBiomeSource.CODEC.fieldOf("midlandsBiome").stable().forGetter((biomeSource) -> {
			return biomeSource.midlandsBiome;
		}), MultiNoiseBiomeSource.CODEC.fieldOf("smallIslandsBiome").stable().forGetter((biomeSource) -> {
			return biomeSource.smallIslandsBiome;
		}), MultiNoiseBiomeSource.CODEC.fieldOf("barrensBiome").stable().forGetter((biomeSource) -> {
			return biomeSource.barrensBiome;
		})).apply(instance, EndMultiNoiseBiomeSource::new);
	});

	private final SimplexNoiseSampler noise;
	private final long seed;
	private final MultiNoiseBiomeSource centerBiome;
	private final MultiNoiseBiomeSource highlandsBiome;
	private final MultiNoiseBiomeSource midlandsBiome;
	private final MultiNoiseBiomeSource smallIslandsBiome;
	private final MultiNoiseBiomeSource barrensBiome;
	private ThreadLocal<SimplexNoiseCache> tlCache;

	public EndMultiNoiseBiomeSource(long seed, MultiNoiseBiomeSource centerBiome, MultiNoiseBiomeSource highlandsBiome, MultiNoiseBiomeSource midlandsBiome, MultiNoiseBiomeSource smallIslandsBiome, MultiNoiseBiomeSource barrensBiome) {
		super(new BiomeList<Biome>().addFromMultiNoiseBiomeSource(centerBiome).addFromMultiNoiseBiomeSource(highlandsBiome).addFromMultiNoiseBiomeSource(midlandsBiome).addFromMultiNoiseBiomeSource(smallIslandsBiome).addFromMultiNoiseBiomeSource(barrensBiome));
		this.seed = seed;
		this.centerBiome = centerBiome;
		this.highlandsBiome = highlandsBiome;
		this.midlandsBiome = midlandsBiome;
		this.smallIslandsBiome = smallIslandsBiome;
		this.barrensBiome = barrensBiome;
		ChunkRandom chunkRandom = new ChunkRandom(seed);
		chunkRandom.consume(17292);
		this.noise = new SimplexNoiseSampler(chunkRandom);
		this.tlCache = ThreadLocal.withInitial(() -> new SimplexNoiseCache(this.noise));
	}

	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	public BiomeSource withSeed(long seed) {
		return new EndMultiNoiseBiomeSource(seed, this.centerBiome, this.highlandsBiome, this.midlandsBiome, this.smallIslandsBiome, this.barrensBiome);
	}

	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		int i = biomeX >> 2;
		int j = biomeZ >> 2;
		if ((long) i * (long) i + (long) j * (long) j <= 4096L) {
			return this.centerBiome.getBiomeForNoiseGen(biomeX, 0, biomeZ);
		} else {
			float f = tlCache.get().getNoiseAt(biomeX, biomeZ);
			if (f > 40.0F) {
				return this.highlandsBiome.getBiomeForNoiseGen(biomeX, 0, biomeZ);
			} else if (f >= 0.0F) {
				return this.midlandsBiome.getBiomeForNoiseGen(biomeX, 0, biomeZ);
			} else {
				return f < -20.0F ? this.smallIslandsBiome.getBiomeForNoiseGen(biomeX, 0, biomeZ) : this.barrensBiome.getBiomeForNoiseGen(biomeX, 0, biomeZ);
			}
		}
	}

	public boolean seedMatches(long seed) {
		return this.seed == seed;
	}

	public boolean matches(long seed, MultiNoiseBiomeSource centerBiome, MultiNoiseBiomeSource highlandsBiome, MultiNoiseBiomeSource midlandsBiome, MultiNoiseBiomeSource smallIslandsBiome, MultiNoiseBiomeSource barrensBiome) {
		return this.seed == seed && this.centerBiome == centerBiome && this.highlandsBiome == highlandsBiome && this.midlandsBiome == midlandsBiome && this.smallIslandsBiome == smallIslandsBiome && this.barrensBiome == barrensBiome;
	}

	// Stolen Shamelessly from lithium.
	public class SimplexNoiseCache {
		public static final int GRID_SIZE = 2;
		public static final float MIN = -100.0F;
		public static final float MAX = 80.0F;
		public static final float ISLAND_RADIUS = 100.0F;
		// The smallest encompassing power of two that can store all of the noise values
		// in a chunk.
		public static final int CACHE_SIZE = 8192;

		public final int mask;
		public final long[] keys;
		public final float[] values;

		public final SimplexNoiseSampler sampler;

		public SimplexNoiseCache(SimplexNoiseSampler sampler) {
			this.sampler = sampler;

			this.mask = CACHE_SIZE - 1;

			// Initialize default values
			this.keys = new long[CACHE_SIZE];
			Arrays.fill(this.keys, Long.MIN_VALUE);
			this.values = new float[CACHE_SIZE];
		}

		/**
		 * Attempt to get the cached distance factor, saving computation time.
		 */
		public float getDistanceFactor(int x, int z) {
			// Hash key and get index
			long key = ChunkPos.toLong(x, z);
			int idx = (int) HashCommon.mix(key) & this.mask;

			if (this.keys[idx] == key) {
				// Cache hit, return cached value
				return this.values[idx];
			}

			// Cache miss, compute and store value

			// A marker for no value.
			float value = -1.0F;

			long lx = x;
			long lz = z;
			long distanceFromOriginSq = lx * lx + lz * lz;

			// Ensure we are 64 grid cells away from the origin.
			if (distanceFromOriginSq > 64 * 64) {
				// Reduce the number of island-forming grid cells by sampling noise with a
				// threshold
				if (this.sampler.sample(x, z) < -0.9) {
					// Generate a pseudo-random value from 9 to 21
					value = (MathHelper.abs(x) * 3439.0F + MathHelper.abs(z) * 147.0F) % 13.0F + 9.0F;
				}
			}

			// Store values in cache
			this.keys[idx] = key;
			this.values[idx] = value;

			return value;
		}

		/**
		 * Mapped and cleaned up implementation of the End biome source's sampler. Tries
		 * to use cached values wherever possible.
		 */
		public float getNoiseAt(int x, int z) {
			// [VanillaCopy] TheEndBiomeSource#getNoiseAt

			int gridX = x / GRID_SIZE;
			int gridZ = z / GRID_SIZE;

			// This is the "center point", offset to center around the current grid cell
			int gridOriginX = x % GRID_SIZE;
			int gridOriginZ = z % GRID_SIZE;

			// Initialize density for the central island
			float density = ISLAND_RADIUS - MathHelper.sqrt(x * x + z * z) * 8.0F;
			if (density >= MAX) {
				return MAX;
			}

			// Iterate through 25x25 grid cells
			for (int offsetX = -12; offsetX <= 12; ++offsetX) {
				for (int offsetZ = -12; offsetZ <= 12; ++offsetZ) {
					int globalGridX = gridX + offsetX;
					int globalGridZ = gridZ + offsetZ;

					// Try to retrieve values from cache
					float distanceFactor = this.getDistanceFactor(globalGridX, globalGridZ);
					if (distanceFactor != -1.0F) {
						// Compute the distance to the origin
						float deltaX = gridOriginX - offsetX * GRID_SIZE;
						float deltaZ = gridOriginZ - offsetZ * GRID_SIZE;

						// Calculate the density at this grid cell
						float scaledDistance = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * distanceFactor;
						float densityHere = ISLAND_RADIUS - scaledDistance;

						// Try to return early if we're over the max
						if (densityHere > density) {
							if (densityHere >= MAX) {
								return MAX;
							}

							density = densityHere;
						}
					}
				}
			}

			return Math.max(density, MIN);
		}
	}

}
