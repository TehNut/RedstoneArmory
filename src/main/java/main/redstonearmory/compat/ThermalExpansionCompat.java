package main.redstonearmory.compat;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import main.redstonearmory.RedstoneArmory;
import main.redstonearmory.items.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import redstonearsenal.item.RAItems;
import thermalexpansion.item.TEItems;
import thermalfoundation.item.TFItems;
import tterrag.core.common.compat.ICompatability;

public class ThermalExpansionCompat implements ICompatability {

    public static void load() {
        registerTERecipes();
    }

    private static void registerTERecipes() {
        RedstoneArmory.logger.info("TE Recipes");
        ThermalExpansionHelper.addTransposerFill(12000, RAItems.gemCrystalFlux, new ItemStack(ItemRegistry.materials, 1, 2), new FluidStack(FluidRegistry.getFluid("cryotheum"), 1000), false);
        ThermalExpansionHelper.addTransposerFill(12000, TFItems.ingotEnderium, new ItemStack(ItemRegistry.materials, 1, 0), new FluidStack(FluidRegistry.getFluid("cryotheum"), 1000), false);
        ThermalExpansionHelper.addTransposerFill(12000, new ItemStack(ItemRegistry.armorPlating, 1, 5), new ItemStack(ItemRegistry.armorPlating, 1, 6), new FluidStack(FluidRegistry.getFluid("mana"), 1000), false);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemRegistry.armorPlating, 1, 4), "PPP", "DCD", "PPP", 'P', Items.potato, 'D', "dustRedstone", 'C', TEItems.capacitorPotato));
    }
}