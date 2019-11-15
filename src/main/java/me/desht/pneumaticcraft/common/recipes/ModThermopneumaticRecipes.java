package me.desht.pneumaticcraft.common.recipes;

import me.desht.pneumaticcraft.api.recipe.IThermopneumaticProcessingPlantRecipe;
import me.desht.pneumaticcraft.api.recipe.RegisterMachineRecipesEvent;
import me.desht.pneumaticcraft.api.recipe.TemperatureRange;
import me.desht.pneumaticcraft.common.core.ModFluids;
import me.desht.pneumaticcraft.lib.Names;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

import static me.desht.pneumaticcraft.common.util.PneumaticCraftUtils.RL;

@Mod.EventBusSubscriber(modid= Names.MOD_ID)
public class ModThermopneumaticRecipes {
    @SubscribeEvent
    public static void register(RegisterMachineRecipesEvent evt) {
        Consumer<IThermopneumaticProcessingPlantRecipe> tpp = evt.getThermopneumatic();

        tpp.accept(new BasicThermopneumaticProcessingPlantRecipe(
                RL("plastic"),
                new FluidStack(ModFluids.LPG_SOURCE, 100),
                Ingredient.fromTag(ItemTags.COALS),
                new FluidStack(ModFluids.PLASTIC_SOURCE, 1000),
                TemperatureRange.min(373), 0f,
                false
        ));

        tpp.accept(new BasicThermopneumaticProcessingPlantRecipe(
                RL("lubricant"),
                new FluidStack(ModFluids.DIESEL_SOURCE, 1000),
                Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE),
                new FluidStack(ModFluids.LUBRICANT_SOURCE, 1000),
                TemperatureRange.min(373), 0f,
                false
        ));

        tpp.accept(new BasicThermopneumaticProcessingPlantRecipe(
                RL("kerosene"),
                new FluidStack(ModFluids.DIESEL_SOURCE, 100),
                null,
                new FluidStack(ModFluids.KEROSENE_SOURCE, 80),
                TemperatureRange.min(573), 2.0f,
                false
        ));

        tpp.accept(new BasicThermopneumaticProcessingPlantRecipe(
                RL("gasoline"),
                new FluidStack(ModFluids.KEROSENE_SOURCE, 100),
                null,
                new FluidStack(ModFluids.GASOLINE_SOURCE, 80),
                TemperatureRange.min(573), 2.0f,
                false
        ));

        tpp.accept(new BasicThermopneumaticProcessingPlantRecipe(
                RL("lpg"),
                new FluidStack(ModFluids.GASOLINE_SOURCE, 100),
                null,
                new FluidStack(ModFluids.LPG_SOURCE, 80),
                TemperatureRange.min(573), 2.0f,
                false
        ));
    }

}
