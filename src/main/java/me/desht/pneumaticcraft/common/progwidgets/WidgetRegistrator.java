package me.desht.pneumaticcraft.common.progwidgets;

import me.desht.pneumaticcraft.api.event.PuzzleRegistryEvent;
import me.desht.pneumaticcraft.common.ai.DroneInteractRFExport;
import me.desht.pneumaticcraft.common.ai.DroneInteractRFImport;
import me.desht.pneumaticcraft.common.config.aux.ProgWidgetConfig;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public class WidgetRegistrator {

    public static final List<IProgWidget> registeredWidgets = new ArrayList<>();
    private static final Map<String, IProgWidget> allRegisteredWidgets = new LinkedHashMap<>();

    public static void init() {
        register(new ProgWidgetComment());
        register(new ProgWidgetStart());
        register(new ProgWidgetArea());
        register(new ProgWidgetString());
        register(new ProgWidgetItemFilter());
        register(new ProgWidgetItemAssign());
        register(new ProgWidgetLiquidFilter());
        register(new ProgWidgetCoordinate());
        register(new ProgWidgetCoordinateOperator());
        register(new ProgWidgetEntityAttack());
        register(new ProgWidgetDig());
        register(new ProgWidgetHarvest());
        register(new ProgWidgetPlace());
        register(new ProgWidgetBlockRightClick());
        register(new ProgWidgetEntityRightClick());
        register(new ProgWidgetPickupItem());
        register(new ProgWidgetDropItem());
        register(new ProgWidgetInventoryExport());
        register(new ProgWidgetInventoryImport());
        register(new ProgWidgetLiquidExport());
        register(new ProgWidgetLiquidImport());
        register(new ProgWidgetEntityExport());
        register(new ProgWidgetEntityImport());
        register(new ProgWidgetGoToLocation());
        register(new ProgWidgetTeleport());
        register(new ProgWidgetEmitRedstone());
        register(new ProgWidgetLabel());
        register(new ProgWidgetJump());
        register(new ProgWidgetWait());
        register(new ProgWidgetRename());
        register(new ProgWidgetSuicide());
        register(new ProgWidgetExternalProgram());
        register(new ProgWidgetCrafting());
        register(new ProgWidgetStandby());
        register(new ProgWidgetLogistics());
        register(new ProgWidgetForEachCoordinate());
        register(new ProgWidgetForEachItem());
        register(new ProgWidgetEditSign());
        register(new ProgWidgetCoordinateCondition());
        register(new ProgWidgetRedstoneCondition());
        register(new ProgWidgetLightCondition());
        register(new ProgWidgetItemInventoryCondition());
        register(new ProgWidgetBlockCondition());
        register(new ProgWidgetLiquidInventoryCondition());
        register(new ProgWidgetEntityCondition());
        register(new ProgWidgetPressureCondition());
        register(new ProgWidgetItemCondition());
        register(new ProgWidgetDroneConditionItem());
        register(new ProgWidgetDroneConditionFluid());
        register(new ProgWidgetDroneConditionEntity());
        register(new ProgWidgetDroneConditionPressure());
        register(new ProgWidgetEnergyCondition());
        register(new ProgWidgetDroneConditionEnergy());

        // TODO: should really be their own widgets, but historical reasons...
        register(new ProgWidgetCustomBlockInteract().setInteractor(new DroneInteractRFExport()));
        register(new ProgWidgetCustomBlockInteract().setInteractor(new DroneInteractRFImport()));

        MinecraftForge.EVENT_BUS.post(new PuzzleRegistryEvent());

        compileBlacklist();
    }

    public static void register(IProgWidget widget) {
        allRegisteredWidgets.put(widget.getWidgetString(), widget);
    }

    public static Set<String> getAllWidgetNames() {
        return allRegisteredWidgets.keySet();
    }

    public static IProgWidget getWidgetFromName(String name) {
        return allRegisteredWidgets.get(name);
    }

    private static void compileBlacklist() {
        registeredWidgets.clear();
        for (Map.Entry<String, IProgWidget> entry : allRegisteredWidgets.entrySet()) {
            if (!ProgWidgetConfig.blacklistedPieces.contains(entry.getKey())) {
                registeredWidgets.add(entry.getValue());
            }
        }
    }
}
