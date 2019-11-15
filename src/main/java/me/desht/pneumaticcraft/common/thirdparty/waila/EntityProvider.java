package me.desht.pneumaticcraft.common.thirdparty.waila;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import me.desht.pneumaticcraft.api.item.IPressurizable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class EntityProvider {

    public static class Data implements IServerDataProvider<Entity> {
        @Override
        public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, Entity entity) {
            if (entity instanceof IPressurizable) {
                compoundNBT.putFloat("Pressure", ((IPressurizable) entity).getPressure(ItemStack.EMPTY));
            }
        }
    }

    public static class Component implements IComponentProvider {
        @Override
        public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
            float pressure = accessor.getServerData().getFloat("Pressure");
            tooltip.add(new TranslationTextComponent("gui.tooltip.pressure", pressure));
        }
    }
}
