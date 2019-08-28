package net.cheatercodes.seasources;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class DryingRecipe implements Recipe<Inventory> {

    public static Type TYPE;

    public static Serializer SERIALIZER;

    private Identifier id;
    private Ingredient input;
    private ItemStack output;
    private int time;

    public DryingRecipe(Identifier id, Ingredient input, ItemStack output, int time){
        this.id = id;
        this.input = input;
        this.output = output;
        this.time = time;
    }

    @Override
    public boolean matches(Inventory inv, World var2) {
        if(input.method_8093(inv.getInvStack(0)))
            return true;
        else
            return false;
    }

    @Override
    public ItemStack craft(Inventory var1) {
        var1.getInvStack(0).decrement(1);
        return output;
    }

    @Override
    public boolean fits(int var1, int var2) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    public int getTime() {
        return time;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return DryingRecipe.TYPE;
    }

    public static class Type implements RecipeType<DryingRecipe> {
        @Override
        public String toString() {
            return "drying";
        }
    }

    public static class Serializer implements RecipeSerializer<DryingRecipe> {

        @Override
        public DryingRecipe read(Identifier id, JsonObject json) {
            JsonObject input = JsonHelper.getObject(json, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(input);

            String output = JsonHelper.getString(json, "result");
            ItemStack out = new ItemStack(Registry.ITEM.get(new Identifier(output)));

            int time = JsonHelper.getInt(json, "time");

            return new DryingRecipe(id, ingredient, out, time);
        }

        @Override
        public DryingRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            int time = buf.readInt();

            return new DryingRecipe(id, input, output, time);
        }

        @Override
        public void write(PacketByteBuf buf, DryingRecipe recipe) {
            recipe.input.write(buf);
            buf.writeItemStack(recipe.output);
            buf.writeInt(recipe.time);
        }
    }
}
