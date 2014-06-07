package main.redstonearmory;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import main.redstonearmory.blocks.BlockRecipeRegistry;
import main.redstonearmory.blocks.BlockRegistry;
import main.redstonearmory.gui.CreativeTabRedstoneArmory;
import main.redstonearmory.items.ItemRecipeRegistry;
import main.redstonearmory.items.ItemRegistry;
import main.redstonearmory.proxies.CommonProxy;
import main.redstonearmory.util.CapeHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by Nick on 6/6/14.
 */
@Mod(modid = ModInformation.ID, name = ModInformation.NAME, version = ModInformation.VERSION)
public class RedstoneArmory {

	public static CreativeTabs tabRedstoneArmory = new CreativeTabRedstoneArmory(ModInformation.ID + ".creativetab.name");
	public static Logger logger = LogManager.getLogManager().getLogger(ModInformation.NAME);

	@Mod.Instance(ModInformation.ID)
	public static RedstoneArmory instance;

	@SidedProxy(clientSide = "main.redstonearmory.proxies.ClientProxy", serverSide = "main.redstonearmory.proxies.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.registerConfig(event.getSuggestedConfigurationFile());

		BlockRegistry.registerFullBlocks();
		BlockRecipeRegistry.registerRecipes();
		ItemRegistry.registerItems();
		ItemRecipeRegistry.registerFullRecipes();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new CapeHandler());
		}

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
