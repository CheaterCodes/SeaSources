package net.cheatercodes.seasources.mixin;

import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelGeneratorType.class)
public interface LevelGeneratorTypeMixin {
    @Invoker("<init>")
    static LevelGeneratorType createLevelGeneratorType(int id, String name) { return null; }
}
