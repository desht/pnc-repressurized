package me.desht.pneumaticcraft.common.fluid;

import me.desht.pneumaticcraft.common.core.ModBlocks;
import me.desht.pneumaticcraft.common.core.ModFluids;
import me.desht.pneumaticcraft.common.core.ModItems;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.state.StateContainer;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import static me.desht.pneumaticcraft.common.util.PneumaticCraftUtils.RL;

public abstract class FluidLubricant extends ForgeFlowingFluid {
    private static final FluidAttributes.Builder ATTRS = FluidAttributes.builder(
            RL("fluid/lubricant_still"), RL("fluid/lubricant_flow")
    ).color(MaterialColor.PURPLE.colorValue);

    private static final ForgeFlowingFluid.Properties PROPS =
            new ForgeFlowingFluid.Properties(
                    () -> ModFluids.LUBRICANT_SOURCE, () -> ModFluids.LUBRICANT_FLOWING, ATTRS)
                    .block(() -> ModBlocks.LUBRICANT_BLOCK).bucket(() -> ModItems.LUBRICANT_BUCKET
            );

    FluidLubricant(String name) {
        super(PROPS);

        setRegistryName(RL(name));
    }

    public static class Source extends FluidLubricant {
        public Source() {
            super("lubricant_source");
        }

        @Override
        public boolean isSource(IFluidState state) {
            return true;
        }

        @Override
        public int getLevel(IFluidState state) {
            return 8;
        }
    }

    public static class Flowing extends FluidLubricant {
        public Flowing() {
            super("lubricant_flowing");
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public boolean isSource(IFluidState state) {
            return false;
        }

        @Override
        public int getLevel(IFluidState state) {
            return state.get(LEVEL_1_8);
        }
    }
}
