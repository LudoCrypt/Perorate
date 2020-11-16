# Perorate
 Easily add end biomes with a MixedNoisePoint!

## As a library
build.gradle
```groovy
repositories {
 maven {
  url 'https://jitpack.io'
 }
}
```

```groovy
dependencies {
 modImplementation "com.github.LudoCrypt:Perorate:${project.perorate_version}"
 include "com.github.LudoCrypt:Perorate:${project.perorate_version}"
}
```

gradle.properties
```groovy
perorate_version=a.b.c
```
replace a.b.c with the latest version. For example, 1.0.3, or 1.0.0.

## Use in code

```java
// Add a biome to the center island
PerorateEnd.addCenterBiome(BiomeKeys.THE_END, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

// Add a biome to the highlands portion of the end
PerorateEnd.addHighlandsBiome(BiomeKeys.END_HIGHLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

// Add a biome to the midlands portion of the end
PerorateEnd.addMidlandsBiome(BiomeKeys.END_MIDLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

// Add a biome to the small end islands portion of the end
PerorateEnd.addSmallIslandsBiome(BiomeKeys.SMALL_END_ISLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

// Add a biome to the barrens portion of the end
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
```

Or you can add weight to biomes!

```java
// Add a biome to the center island with weight
PerorateEnd.addCenterBiomeWithWeight(BiomeKeys.THE_END, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), 10, false, false, false, false);

// Add a biome to the highlands portion of the end with weight
PerorateEnd.addHighlandsBiomeWithWeight(BiomeKeys.END_HIGHLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), 5, false, false, false, false);

// Add a biome to the midlands portion of the end with weight
PerorateEnd.addMidlandsBiomeWithWeight(BiomeKeys.END_MIDLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), 68, false, false, false, false);

// Add a biome to the small end islands portion of the end with weight
PerorateEnd.addSmallIslandsBiomeWithWeight(BiomeKeys.SMALL_END_ISLANDS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), 25, false, false, false, false);
	
// Add a biome to the barrens portion of the end with weight
PerorateEnd.addBarrensBiomeWithWeight(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), 4, false, false, false, false);
```

The Integer after the noise is how much of that biome to add, and the boolean values tells which part of the noise to modify, corresponding to the values there.
The first boolean is for temperature, the second one is for humidity, the third for altitude, and the fourth for weirdness.

For example,

```java
PerorateEnd.addBarrensBiomeWithWeight(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), 4, false, false, false, false);
```

will make,

```java
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
```

And this is no good, because it has the same noise values for all of them! Instead, we do this

```java
// See how humidity is set to true!
PerorateEnd.addBarrensBiomeWithWeight(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F), 4, false, true, false, false);
```

and that makes,

```java
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.25F, 0.0F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.5F, 0.0F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.75F, 0.0F, 0.0F, 0.0F));
```

See how here, no biome noise's collide.

Lets see another example, lets take this,

```java
// See how humidity and altitude are set to true, and there is a 0.1 in the altitude!
PerorateEnd.addBarrensBiomeWithWeight(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.1F, 0.0F, 0.0F), 4, false, true, true, false);
```

Oh no! We have a 0.1 in the altitude! Thats no problem though, it doesnt override, it adds. So we get,

```java
// See how it adds for altitude
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.0F, 0.1F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.25F, 0.35F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.5F, 0.6F, 0.0F, 0.0F));
PerorateEnd.addBarrensBiome(BiomeKeys.END_BARRENS, new Biome.MixedNoisePoint(0.0F, 0.75F, 0.85F, 0.0F, 0.0F));
```
