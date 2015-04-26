package tehnut.redstonearmory.items.armor;

import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.redstonearsenal.item.armor.ItemArmorRF;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tehnut.redstonearmory.ModInformation;
import tehnut.redstonearmory.RedstoneArmory;
import tehnut.redstonearmory.items.baubles.CapacitorType;
import tehnut.redstonearmory.util.TextHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

@SuppressWarnings("all")
public class ItemEnderiumArmor extends ItemArmorRF {

    public static final ArmorProperties UNBLOCKABLE = new ArmorProperties(0, 0.0D, 0);
    public static final ArmorProperties FLUX = new ArmorProperties(0, 0.5D, Integer.MAX_VALUE);

    public ItemEnderiumArmor(ArmorMaterial material, int type) {

        super(material, type);
        this.setNoRepair();
        this.setCreativeTab(RedstoneArmory.tabRArm);
        this.setMaxDamage(5);

        maxEnergy = 1000000;
        energyPerDamage = 200;
        absorbRatio = 1.2D;
        maxTransfer = 4500;

        switch (type) {
            case 0: {
                this.setTextureName(ModInformation.ID + ":armor/enderiumHelm");
                this.setUnlocalizedName(ModInformation.ID + ".armor.enderium.helm");
                break;
            }
            case 1: {
                this.setTextureName(ModInformation.ID + ":armor/enderiumChestplate");
                this.setUnlocalizedName(ModInformation.ID + ".armor.enderium.chestplate");
                break;
            }
            case 2: {
                this.setTextureName(ModInformation.ID + ":armor/enderiumLeggings");
                this.setUnlocalizedName(ModInformation.ID + ".armor.enderium.leggings");
                break;
            }
            case 3: {
                this.setTextureName(ModInformation.ID + ":armor/enderiumBoots");
                this.setUnlocalizedName(ModInformation.ID + ".armor.enderium.boots");
                break;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack Stack, Entity entity, int Slot, String type) {
        if (Slot == 2)
            return ModInformation.ID + ":textures/models/armor/enderiumArmor_2.png";
        else
            return ModInformation.ID + ":textures/models/armor/enderiumArmor_1.png";

    }

    protected int getAbsorptionRatio() {

        switch (armorType) {
            case 0:
                return 15;
            case 1:
                return 40;
            case 2:
                return 30;
            case 3:
                return 10;
        }
        return 0;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {

        list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, 0), 0));
        list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, 0), maxEnergy));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean check) {
        int currentEnergy = stack.stackTagCompound.getInteger("Energy");

        String getCurrentEnergy = "" + currentEnergy;
        String getMaxEnergy = "" + maxEnergy;

        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown())
            list.add(StringHelper.shiftForDetails());

        if (!StringHelper.isShiftKeyDown())
            return;

        if (stack.stackTagCompound == null)
            EnergyHelper.setDefaultEnergyTag(stack, 0);

        list.add(TextHelper.localize("info.RArm.tooltip.getenergy").replace("%current%", getCurrentEnergy).replace("%max%", getMaxEnergy));
        list.add(TextHelper.ORANGE + TextHelper.localizeFormatted("info.RArm.tooltip.perdamage", String.valueOf(energyPerDamage)));
        list.add("");
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.stackTagCompound == null)
            EnergyHelper.setDefaultEnergyTag(stack, 0);

        int currentEnergy = stack.stackTagCompound.getInteger("Energy");

        return 1.0 - ((double) currentEnergy / (double) getMaxEnergyStored(stack));
    }

    protected int getBaseAbsorption() {

        return 20;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {

        return EnumRarity.uncommon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return TextHelper.BRIGHT_BLUE + super.getItemStackDisplayName(itemStack);
    }
}
