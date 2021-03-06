package ghost606.networkcraft.block;

import ghost606.networkcraft.Networkcraft;
import ghost606.networkcraft.information.BlockInfo;
import ghost606.networkcraft.information.ModInfo;
import ghost606.networkcraft.tileentities.TileEntitySafe;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import net.minecraft.util.MathHelper;

public class BlockSafe extends BlockContainer {

	public BlockSafe(int id) {
		super(id, Material.iron);
		this.setHardness(3.0F);
		this.setCreativeTab(Networkcraft.tabNetworkcraft);
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySafe();
	}

	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {

        int direction = 0;
        int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

        if (facing == 0) {
            direction = ForgeDirection.NORTH.ordinal();
        }
        else if (facing == 1) {
            direction = ForgeDirection.EAST.ordinal();
        }
        else if (facing == 2) {
            direction = ForgeDirection.SOUTH.ordinal();
        }
        else if (facing == 3) {
            direction = ForgeDirection.WEST.ordinal();
        }

        world.setBlockMetadataWithNotify(x, y, z, direction, 3);

        ((TileEntitySafe) world.getBlockTileEntity(x, y, z)).setOrientation(direction);
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (player.isSneaking())
            return true;
        else if (world.isBlockSolidOnSide(x, y + 1, z, ForgeDirection.DOWN))
            return true;
        else {
            if (!world.isRemote) {
                TileEntitySafe tileEntitySafe = (TileEntitySafe) world.getBlockTileEntity(x, y, z);

                if (tileEntitySafe != null) {
                	FMLNetworkHandler.openGui(player, Networkcraft.instance, ModInfo.GuiID.SAFE, world, x, y, z);
                }
            }

            return true;
        }
	}	
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te != null && te instanceof IInventory) {
			IInventory inventory = (IInventory) te;

			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack stack = inventory.getStackInSlotOnClosing(i);

				if (stack != null) {
					float spawnX = x + world.rand.nextFloat();
					float spawnY = y + world.rand.nextFloat();
					float spawnZ = z + world.rand.nextFloat();

					Entity droppedItem = new EntityItem(world, spawnX, spawnY,
							spawnZ, stack);

					float mult = 0.05F;

					droppedItem.motionX = (-0.5F + world.rand.nextFloat())
							* mult;
					droppedItem.motionY = (4 + world.rand.nextFloat()) * mult;
					droppedItem.motionZ = (-0.5F + world.rand.nextFloat())
							* mult;

					world.spawnEntityInWorld(droppedItem);
				}
			}
		}
		super.breakBlock(world, x, y, z, id, meta);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		world.markBlockForUpdate(x, y, z);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return BlockInfo.Safe.RENDER_ID;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
