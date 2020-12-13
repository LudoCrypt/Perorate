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
