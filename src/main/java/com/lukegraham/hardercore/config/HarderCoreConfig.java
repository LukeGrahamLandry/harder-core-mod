package com.lukegraham.hardercore.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class HarderCoreConfig {
    public static final ForgeConfigSpec server_config;

    public static ForgeConfigSpec.BooleanValue doGraves;
    public static ForgeConfigSpec.BooleanValue doDeadlyShadows;
    public static ForgeConfigSpec.BooleanValue doMobBuffs;
    public static ForgeConfigSpec.BooleanValue doThirst;
    public static ForgeConfigSpec.BooleanValue doAirQuality;
    public static ForgeConfigSpec.BooleanValue doTemperature;
    public static ForgeConfigSpec.BooleanValue doExhaustion;
    public static ForgeConfigSpec.BooleanValue doHungerPains;
    public static ForgeConfigSpec.BooleanValue doFoodPoisoning;

    public static ForgeConfigSpec.IntValue bloodmoonInterval;

    static {
        final ForgeConfigSpec.Builder serverBuilder = new ForgeConfigSpec.Builder();

        serverBuilder.comment("harder core server side configuration settings")
                .push("server");

        bloodmoonInterval = serverBuilder
                .comment("How many days between bloodmoons (nights with extremely high mob spawns). Make it 0 for every night or -1 to disable")
                .defineInRange("bloodmoonInterval", 8, -1, Integer.MAX_VALUE);
        doGraves = serverBuilder
                .comment("Graves will spawn when you die to your items (with an undead guarding it). Disable if using other grave mods")
                .define("doGraves", true);
        doDeadlyShadows = serverBuilder
                .comment("Deadly shadows will spawn in darkness and kill you ")
                .define("doDeadlyShadows", true);
        doMobBuffs = serverBuilder
                .comment("Mobs have a chance to spawn with buffs: fiery, poisonous, ninja")
                .define("doMobBuffs", true);
        doThirst = serverBuilder
                .comment("You need to drink water ")
                .define("doThirst", true);
        doAirQuality = serverBuilder
                .comment("Being near torches and furnaces will give you Smokey Lungs")
                .define("doAirQuality", true);
        doTemperature = serverBuilder
                .comment("Some biomes will eventually give you Heat Stroke or Hypothermia eventually leading to death ")
                .define("doTemperature", true);
        doExhaustion = serverBuilder
                .comment("Not sleeping enough will slow movement and attack speed")
                .define("doExhaustion", true);
        doHungerPains = serverBuilder
                .comment("Not eating enough will cause slowness and weakness")
                .define("doHungerPains", true);
        doFoodPoisoning = serverBuilder
                .comment("Eating raw meat will give you nausea and hunger / poison")
                .define("doFoodPoisoning", true);

        server_config = serverBuilder.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path){
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}