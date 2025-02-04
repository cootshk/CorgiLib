package corgitaco.corgilib.world.level.feature.gen.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import corgitaco.corgilib.serialization.codec.CodecUtil;
import corgitaco.corgilib.serialization.codec.CollectionCodec;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.*;
import java.util.function.Supplier;

public record TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                         IntProvider height,
                                         BlockStateProvider logProvider, BlockStateProvider leavesProvider,
                                         Set<Block> logTarget, Set<Block> leavesTarget,
                                         BlockPredicate growableOn, BlockPredicate leavesPlacementFilter,
                                         int maxLogDepth,
                                         List<TreeDecorator> treeDecorators,
                                         Set<Block> placeFromNBT) implements FeatureConfiguration {

    public static final CollectionCodec<Block, Set<Block>> BLOCK_SET_CODEC = new CollectionCodec<>(CodecUtil.BLOCK_CODEC, ObjectOpenHashSet::new);

    public static final Codec<TreeFromStructureNBTConfig> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    ResourceLocation.CODEC.fieldOf("base_location").forGetter(TreeFromStructureNBTConfig::baseLocation),
                    ResourceLocation.CODEC.fieldOf("canopy_location").forGetter(TreeFromStructureNBTConfig::canopyLocation),
                    IntProvider.CODEC.fieldOf("height").forGetter(TreeFromStructureNBTConfig::height),
                    BlockStateProvider.CODEC.fieldOf("log_provider").forGetter(TreeFromStructureNBTConfig::logProvider),
                    BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(TreeFromStructureNBTConfig::leavesProvider),
                    BLOCK_SET_CODEC.fieldOf("log_target").forGetter(TreeFromStructureNBTConfig::logTarget),
                    BLOCK_SET_CODEC.fieldOf("leaves_target").forGetter(TreeFromStructureNBTConfig::leavesTarget),
                    BlockPredicate.CODEC.fieldOf("can_grow_on_filter").forGetter(TreeFromStructureNBTConfig::growableOn),
                    BlockPredicate.CODEC.fieldOf("can_leaves_place_filter").forGetter(TreeFromStructureNBTConfig::leavesPlacementFilter),
                    Codec.INT.optionalFieldOf("max_log_depth", 5).forGetter(TreeFromStructureNBTConfig::maxLogDepth),
                    TreeDecorator.CODEC.listOf().optionalFieldOf("decorators", new ArrayList<>()).forGetter(TreeFromStructureNBTConfig::treeDecorators),
                    BLOCK_SET_CODEC.fieldOf("place_from_nbt").forGetter(TreeFromStructureNBTConfig::placeFromNBT)
            ).apply(builder, TreeFromStructureNBTConfig::new)
    );

    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Collection<Block> logTarget,
                                      List<Block> leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, new ObjectOpenHashSet<>(logTarget), new ObjectOpenHashSet<>(leavesTarget), BlockPredicate.matchesTag(growableOn), BlockPredicate.replaceable(), maxLogDepth, treeDecorators, Set.of());
    }

    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Block logTarget,
                                      Block leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, Collections.singleton(logTarget), Collections.singleton(leavesTarget), BlockPredicate.matchesTag(growableOn), BlockPredicate.replaceable(), maxLogDepth, treeDecorators, Set.of());
    }

    public TreeFromStructureNBTConfig(ResourceLocation baseLocation, ResourceLocation canopyLocation,
                                      IntProvider height, BlockStateProvider logProvider,
                                      BlockStateProvider leavesProvider, Supplier<? extends Block> logTarget,
                                      Supplier<? extends Block> leavesTarget, TagKey<Block> growableOn, int maxLogDepth, List<TreeDecorator> treeDecorators) {
        this(baseLocation, canopyLocation, height, logProvider, leavesProvider, logTarget.get(), leavesTarget.get(), growableOn, maxLogDepth, treeDecorators);
    }
}
