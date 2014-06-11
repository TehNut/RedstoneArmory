package main.redstonearmory.items.tools;

import main.redstonearmory.items.itemutil.ItemToolRF;
import main.redstonearmory.util.KeyboardHandler;
import main.redstonearmory.util.TextHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.BonemealEvent;

import java.util.List;

public class ItemGelidEnderiumShovel extends ItemToolRF {

    int range = 5;

    public ItemGelidEnderiumShovel(int id, EnumToolMaterial toolMaterial) {

        super(id, toolMaterial);

        addToolClass("shovel");
        maxEnergy = 320000;
        energyPerUse = 350;
        energyPerUseCharged = 950;

        effectiveMaterials.add(Material.ground);
        effectiveMaterials.add(Material.sand);
        effectiveMaterials.add(Material.clay);
        effectiveMaterials.add(Material.craftedSnow);
        effectiveMaterials.add(Material.snow);
    }

    public ItemGelidEnderiumShovel(int id,EnumToolMaterial toolMaterial, int harvestLevel) {

        this(id ,toolMaterial);
        this.harvestLevel = harvestLevel;
    }

    //	@Override
    public boolean func_150897_b(Block block) {

        return block == Block.snow || block == Block.blockSnow;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityLivingBase entity) {

        Block block = Block.blocksList[world.getBlockId(x, y, z)];

        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        if (block.getBlockHardness(world, x, y, z) == 0.0D) {
            return true;
        }
        EntityPlayer player = (EntityPlayer) entity;

        if (effectiveBlocks.contains(block) && isEmpowered(stack)) {
            for (int i = x - 2; i <= x + 2; i++) {
                for (int k = z - 2; k <= z + 2; k++) {
                    for (int j = y - 2; j <= y + 2; j++) {
                        if (world.getBlockId(i, j, k) == block.blockID) {
                            harvestBlock(world, i, j, k, player);
                        }
                    }
                }
            }
        }

        if (!player.capabilities.isCreativeMode) {
            useEnergy(stack, false);
        }
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int hitSide, float hitX, float hitY, float hitZ) {
        if (!player.canPlayerEdit(x, y, z, hitSide, stack) || !player.capabilities.isCreativeMode && getEnergyStored(stack) < getEnergyPerUse(stack)) {
            return false;
        }

        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        BonemealEvent event = new BonemealEvent(player, world, block.blockID, x, y, z);

        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        if (event.getResult() == Result.ALLOW) {
            if (!player.capabilities.isCreativeMode) {
                useEnergy(stack, false);
            }
            return true;
        }

        if (applyBonemeal(stack, world, x, y, z, player)) {
            if (!world.isRemote) {
                world.playAuxSFX(2005, x, y, z, 0);
            }

            return true;
        }
        return false;
    }

    public static boolean applyBonemeal(ItemStack par0ItemStack, World par1World, int par2, int par3, int par4, EntityPlayer player) {
        int l = par1World.getBlockId(par2, par3, par4);

        BonemealEvent event = new BonemealEvent(player, par1World, l, par2, par3, par4);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        if (event.getResult() == Result.ALLOW) {
            if (!par1World.isRemote) {
                par0ItemStack.stackSize--;
            }
            return true;
        }

        if (l == Block.sapling.blockID) {
            if (!par1World.isRemote) {
                if ((double) par1World.rand.nextFloat() < 0.45D) {
                    ((BlockSapling) Block.sapling).markOrGrowMarked(par1World, par2, par3, par4, par1World.rand);
                }

                --par0ItemStack.stackSize;
            }

            return true;
        } else if (l != Block.mushroomBrown.blockID && l != Block.mushroomRed.blockID) {
            if (l != Block.melonStem.blockID && l != Block.pumpkinStem.blockID) {
                if (l > 0 && Block.blocksList[l] instanceof BlockCrops) {
                    if (par1World.getBlockMetadata(par2, par3, par4) == 7) {
                        return false;
                    } else {
                        if (!par1World.isRemote) {
                            ((BlockCrops) Block.blocksList[l]).fertilize(par1World, par2, par3, par4);
                            --par0ItemStack.stackSize;
                        }

                        return true;
                    }
                } else {
                    int i1;
                    int j1;
                    int k1;

                    if (l == Block.cocoaPlant.blockID) {
                        i1 = par1World.getBlockMetadata(par2, par3, par4);
                        j1 = BlockDirectional.getDirection(i1);
                        k1 = BlockCocoa.func_72219_c(i1);

                        if (k1 >= 2) {
                            return false;
                        } else {
                            if (!par1World.isRemote) {
                                ++k1;
                                par1World.setBlockMetadataWithNotify(par2, par3, par4, k1 << 2 | j1, 2);
                                --par0ItemStack.stackSize;
                            }

                            return true;
                        }
                    } else if (l != Block.grass.blockID) {
                        return false;
                    } else {
                        if (!par1World.isRemote) {
                            --par0ItemStack.stackSize;
                            label102:

                            for (i1 = 0; i1 < 128; ++i1) {
                                j1 = par2;
                                k1 = par3 + 1;
                                int l1 = par4;

                                for (int i2 = 0; i2 < i1 / 16; ++i2) {
                                    j1 += itemRand.nextInt(3) - 1;
                                    k1 += (itemRand.nextInt(3) - 1) * itemRand.nextInt(3) / 2;
                                    l1 += itemRand.nextInt(3) - 1;

                                    if (par1World.getBlockId(j1, k1 - 1, l1) != Block.grass.blockID || par1World.isBlockNormalCube(j1, k1, l1)) {
                                        continue label102;
                                    }
                                }

                                if (par1World.getBlockId(j1, k1, l1) == 0) {
                                    if (itemRand.nextInt(10) != 0) {
                                        if (Block.tallGrass.canBlockStay(par1World, j1, k1, l1)) {
                                            par1World.setBlock(j1, k1, l1, Block.tallGrass.blockID, 1, 3);
                                        }
                                    } else {
                                        ForgeHooks.plantGrass(par1World, j1, k1, l1);
                                    }
                                }
                            }
                        }

                        return true;
                    }
                }
            } else if (par1World.getBlockMetadata(par2, par3, par4) == 7) {
                return false;
            } else {
                if (!par1World.isRemote) {
                    ((BlockStem) Block.blocksList[l]).fertilizeStem(par1World, par2, par3, par4);
                    --par0ItemStack.stackSize;
                }

                return true;
            }
        } else {
            if (!par1World.isRemote) {
                if ((double) par1World.rand.nextFloat() < 0.4D) {
                    ((BlockMushroom) Block.blocksList[l]).fertilizeMushroom(par1World, par2, par3, par4, par1World.rand);
                }

                --par0ItemStack.stackSize;
            }

            return true;
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean check) {

        super.addInformation(stack, player, list, check);
        if (!KeyboardHandler.isShiftDown()) {
            return;
        }
        list.add(TextHelper.localize("info.redstonearmory.tool.gelidenderium.shovel"));
    }

}