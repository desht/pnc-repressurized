package me.desht.pneumaticcraft.common.ai;

import me.desht.pneumaticcraft.common.progwidgets.ICountWidget;
import me.desht.pneumaticcraft.common.progwidgets.ILiquidFiltered;
import me.desht.pneumaticcraft.common.progwidgets.ISidedWidget;
import me.desht.pneumaticcraft.common.progwidgets.ProgWidgetInventoryBase;
import me.desht.pneumaticcraft.common.util.FluidUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import static net.minecraftforge.fluids.FluidAttributes.BUCKET_VOLUME;

public class DroneAILiquidImport extends DroneAIImExBase<ProgWidgetInventoryBase> {

    public DroneAILiquidImport(IDroneBase drone, ProgWidgetInventoryBase widget) {
        super(drone, widget);
    }

    @Override
    protected boolean isValidPosition(BlockPos pos) {
        return emptyTank(pos, true);
    }

    @Override
    protected boolean doBlockInteraction(BlockPos pos, double distToBlock) {
        return emptyTank(pos, false) && super.doBlockInteraction(pos, distToBlock);
    }

    private boolean emptyTank(BlockPos pos, boolean simulate) {
        if (drone.getTank().getFluidAmount() == drone.getTank().getCapacity()) {
            drone.addDebugEntry("gui.progWidget.liquidImport.debug.fullDroneTank");
            abort();
            return false;
        } else {
            TileEntity te = drone.world().getTileEntity(pos);
            if (te != null) {
                boolean didWork = false;
                for (Direction side : Direction.VALUES) {
                    if (ISidedWidget.checkSide(progWidget, side)) {
                        didWork = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)
                                .map(handler -> tryImportFluid(handler, simulate)).orElse(false);
                        if (didWork) break;
                    }
                }
                if (didWork) return true;
                drone.addDebugEntry("gui.progWidget.liquidImport.debug.emptiedToMax", pos);
            }

            // try to pick up a bucket of fluid from the world
            if (!((ICountWidget) progWidget).useCount() || getRemainingCount() >= BUCKET_VOLUME) {
                LazyOptional<IFluidHandler> cap = drone.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
                FluidStack stack = FluidUtils.tryPickupFluid(cap, drone.world(), pos, false, FluidAction.SIMULATE);
                if (!stack.isEmpty() && stack.getAmount() == BUCKET_VOLUME
                    && ((ILiquidFiltered) progWidget).isFluidValid(stack.getFluid())
                    && drone.getTank().fill(stack, FluidAction.SIMULATE) == BUCKET_VOLUME) {
                    if (!simulate) {
                        decreaseCount(BUCKET_VOLUME);
                        FluidUtils.tryPickupFluid(cap, drone.world(), pos, false, FluidAction.EXECUTE);
                    }
                    return true;
                }
            }

            return false;
        }
    }

    private boolean tryImportFluid(IFluidHandler sourceHandler, boolean simulate) {
        FluidStack importedFluid = sourceHandler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE);
        if (importedFluid != null && ((ILiquidFiltered) progWidget).isFluidValid(importedFluid.getFluid())) {
            int filledAmount = drone.getTank().fill(importedFluid, FluidAction.SIMULATE);
            if (filledAmount > 0) {
                if (((ICountWidget) progWidget).useCount())
                    filledAmount = Math.min(filledAmount, getRemainingCount());
                if (!simulate) {
                    decreaseCount(drone.getTank().fill(sourceHandler.drain(filledAmount, FluidAction.EXECUTE), FluidAction.EXECUTE));
                }
                return true;
            }
        }
        return false;
    }
}
