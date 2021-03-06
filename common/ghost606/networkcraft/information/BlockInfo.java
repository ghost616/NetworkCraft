package ghost606.networkcraft.information;

public class BlockInfo {
	public static class Sections
	{
		public static final String BLOCKS = "Blocks_ID";
	}
	
	public static class Safe
	{
		public static int ID;
		public static int RENDER_ID;
		public static final String NAME = "Safe";
		public static final String TE_NAME = "SafeTileEntity";
		public static final int DEFAULT_ID = 801;
	}
	public static class RightManager
	{
		public static int ID;
		public static final String NAME= "RightManager";
		public static final String TE_NAME = "RightManagerTileEntity";
		public static final String ICON_NAME = "rightmanager";
		public static final int DEFAULT_ID = 802;
	}
	public static class UserManager
	{
		public static int ID;
		public static final String NAME = "UserManager";
		public static final String TE_NAME = "UserManagerTileEntity";
		public static final String ICON_NAME = "usermanager";
		public static final int DEFAULT_ID = 803;
	}
}
