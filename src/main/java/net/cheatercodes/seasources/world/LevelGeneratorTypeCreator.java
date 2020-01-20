package net.cheatercodes.seasources.world;

import net.cheatercodes.seasources.mixin.LevelGeneratorTypeMixin;
import net.minecraft.world.level.LevelGeneratorType;

public class LevelGeneratorTypeCreator {

    private static int getID() {
        int id;
        for(id = 0; id < LevelGeneratorType.TYPES.length; id++) {
            if(LevelGeneratorType.TYPES[id] == null)
                break;
        }

        if(id == LevelGeneratorType.TYPES.length) {
            throw new RuntimeException("No free id for registering a LevelGenerator.");
        }

        return id;
    }

    public static LevelGeneratorType create(String identifier) {
        return LevelGeneratorTypeMixin.createLevelGeneratorType(getID(), identifier);
    }
}
