package net.cheatercodes.seasources.world;

import net.minecraft.world.level.LevelGeneratorType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class LevelGeneratorTypeCreator {

    private static int getID() {

        //Get a free id
        int id;
        for(id = 0; id < LevelGeneratorType.TYPES.length; id++) {
            if(LevelGeneratorType.TYPES[id] == null)
                break;
        }

        //If no id free
        if(id == LevelGeneratorType.TYPES.length) {
            throw new RuntimeException("No free id for registering a LevelGenerator.");
        }

        return id;
    }

    public static LevelGeneratorType create(String identifier) {
        //Get private constructor and invoke it
        try {
            Constructor<LevelGeneratorType> ctor;
            ctor = LevelGeneratorType.class.getDeclaredConstructor(int.class, String.class);
            ctor.setAccessible(true);
            return ctor.newInstance(getID(), identifier);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            return null;
        }
    }
}
