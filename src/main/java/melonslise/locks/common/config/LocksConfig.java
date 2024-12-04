package melonslise.locks.common.config;

import com.google.common.collect.Lists;
import melonslise.locks.common.util.LocksUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.regex.Pattern;

public final class LocksConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue GENERATION_ENCHANT_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> GEN_LOCKABLE_BLOCKS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLIST_LOOT_TABLES;

    public static Pattern[] lockableGenBlocks;


    static {
        ForgeConfigSpec.Builder cfg = new ForgeConfigSpec.Builder();

        GENERATION_ENCHANT_CHANCE = cfg
                .comment("Chance to randomly enchant a generated lock during world generation. Set to 0 to disable")
                .comment("在世界生成过程中，随机附魔生成的锁的概率。设置为0以禁用此功能。")
                .defineInRange("Generation Enchant Chance", 0.4d, 0d, 1d);
        GEN_LOCKABLE_BLOCKS = cfg
                .comment("Blocks that can be locked during the world generation")
                .comment("当世界生成时锁定的方块")
                .defineList("Lockable Generated Blocks", Lists.newArrayList("minecraft:chest", "minecraft:barrel", "lootr:.*", "quark:.*_chest"), e -> e instanceof String);
        BLACKLIST_LOOT_TABLES = cfg
                .comment("Loot tables that should not be affected by Loot Table Relativity")
                .comment("This is used because of items like treasure maps, which look for structures and cause a freeze while generating")
                .comment("战利品表不应受 Loot Table Relativity 影响")
                .comment("这是因为像宝藏地图这样的物品，会寻找结构并在生成时导致卡顿")
                .defineList("Loot Table Relativity Blacklist", Lists.newArrayList(
                        "minecraft:chests/shipwreck_map",
                        "repurposed_structures:chests/ruins/nether"
                ), e -> e instanceof String);

        SPEC = cfg.build();
    }

    private LocksConfig() {
    }
    
    public static void init() {
        lockableGenBlocks = GEN_LOCKABLE_BLOCKS.get().stream().map(Pattern::compile).toArray(Pattern[]::new);
    }

    public static boolean matchString(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).toString();
        for (Pattern p : lockableGenBlocks) {
            if (p.matcher(name).matches()) {
                return true;
            }
        }
        return false;
    }

    public static boolean canEnchant(RandomSource rng) {
        return LocksUtil.chance(rng, GENERATION_ENCHANT_CHANCE.get());
    }
}