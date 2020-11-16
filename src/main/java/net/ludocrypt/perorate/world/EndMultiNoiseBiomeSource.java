package net.ludocrypt.perorate.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.perorate.util.BiomeList;
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

	public EndMultiNoiseBiomeSource(long seed, MultiNoiseBiomeSource centerBiome, MultiNoiseBiomeSource highlandsBiome, MultiNoiseBiomeSource midlandsBiome, MultiNoiseBiomeSource smallIslandsBiome, MultiNoiseBiomeSource barrensBiome) {
		super(new BiomeList<Biome>().addFromMultiNoiseESource(centerBiome).addFromMultiNoiseESource(highlandsBiome).addFromMultiNoiseESource(midlandsBiome).addFromMultiNoiseESource(smallIslandsBiome).addFromMultiNoiseESource(barrensBiome));
		this.seed = seed;
		this.centerBiome = centerBiome;
		this.highlandsBiome = highlandsBiome;
		this.midlandsBiome = midlandsBiome;
		this.smallIslandsBiome = smallIslandsBiome;
		this.barrensBiome = barrensBiome;
		ChunkRandom chunkRandom = new ChunkRandom(seed);
		chunkRandom.consume(17292);
		this.noise = new SimplexNoiseSampler(chunkRandom);
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
			return this.centerBiome.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
		} else {
			float f = getNoiseAt(this.noise, i * 2 + 1, j * 2 + 1);
			if (f > 40.0F) {
				return this.highlandsBiome.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
			} else if (f >= 0.0F) {
				return this.midlandsBiome.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
			} else {
				return f < -20.0F ? this.smallIslandsBiome.getBiomeForNoiseGen(biomeX, biomeY, biomeZ) : this.barrensBiome.getBiomeForNoiseGen(biomeX, biomeY, biomeZ);
			}
		}
	}

	public boolean seedMatches(long seed) {
		return this.seed == seed;
	}

	public boolean matches(long seed, MultiNoiseBiomeSource centerBiome, MultiNoiseBiomeSource highlandsBiome, MultiNoiseBiomeSource midlandsBiome, MultiNoiseBiomeSource smallIslandsBiome, MultiNoiseBiomeSource barrensBiome) {
		return this.seed == seed && this.centerBiome == centerBiome && this.highlandsBiome == highlandsBiome && this.midlandsBiome == midlandsBiome && this.smallIslandsBiome == smallIslandsBiome && this.barrensBiome == barrensBiome;
	}

	public static float getNoiseAt(SimplexNoiseSampler simplexNoiseSampler, int i, int j) {
		int k = i / 2;
		int l = j / 2;
		int m = i % 2;
		int n = j % 2;
		float f = 100.0F - MathHelper.sqrt((float) (i * i + j * j)) * 8.0F;
		f = MathHelper.clamp(f, -100.0F, 80.0F);

		for (int o = -12; o <= 12; ++o) {
			for (int p = -12; p <= 12; ++p) {
				long q = (long) (k + o);
				long r = (long) (l + p);
				if (q * q + r * r > 4096L && simplexNoiseSampler.sample((double) q, (double) r) < -0.8999999761581421D) {
					float g = (MathHelper.abs((float) q) * 3439.0F + MathHelper.abs((float) r) * 147.0F) % 13.0F + 9.0F;
					float h = (float) (m - o * 2);
					float s = (float) (n - p * 2);
					float t = 100.0F - MathHelper.sqrt(h * h + s * s) * g;
					t = MathHelper.clamp(t, -100.0F, 80.0F);
					f = Math.max(f, t);
				}
			}
		}

		return f;
	}
}
