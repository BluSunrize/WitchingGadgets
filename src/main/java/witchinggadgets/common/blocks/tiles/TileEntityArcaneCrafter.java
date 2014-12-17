package witchinggadgets.common.blocks.tiles;

import thaumcraft.common.tiles.TileMagicWorkbench;


public class TileEntityArcaneCrafter extends TileMagicWorkbench
{
//	public ItemStack[] inv = new ItemStack[19];
//	public EntityPlayer owner;
//	public String ownerName;
//	public Container container;
//	private int tick;
//	private int tickMax = 60;
//
//	@Override
//	public void updateEntity()
//	{
//		owner = new FakeThaumcraftPlayer(worldObj, ownerName);
//		//System.out.println(owner);
//		if(getCraftingResult() == null)
//			return;
//		tick++;
//		if(tick < tickMax)
//			return;
//		//System.out.println("debug1");
//		tick = 0;
//		//System.out.println(owner);
//		//owner = new FakePlayer(worldObj, "Dannyl");
//		ItemStack craftResult = getCraftingResult();
//		boolean isArcane = owner == null ? false : InventoryHelper.areItemStacksEqualStrict(craftResult, ThaumcraftCraftingManager.findMatchingArcaneRecipe(this, owner));
//		if(isArcane)
//		{
//			//System.out.println("debug2 - Arcane");
//			IArcaneRecipe iArcRec = Utilities.findMatchingArcaneRecipe(this, owner);
//			if(iArcRec instanceof ShapedArcaneRecipe)
//			{
//				ShapedArcaneRecipe shaped = (ShapedArcaneRecipe) iArcRec;
//				//System.out.println("Has Ingredients: "+consumeIngredients(shaped.input, false));
//				//System.out.println("Has Aspects: "+useAspectsToCraft(false));
//				//System.out.println("Can Output: "+outputItem(craftResult,false));
//				if( useAspectsToCraft(false) && consumeIngredients(shaped.input, false) && outputItem(craftResult,false))
//				{
//					//System.out.println("debug4 - Will Craft");
//					useAspectsToCraft(true);
//					consumeIngredients(shaped.input, true);
//					outputItem(craftResult,true);
//				}
//			}
//			else if(iArcRec instanceof ShapelessArcaneRecipe)
//			{
//				ShapelessArcaneRecipe shapeless = (ShapelessArcaneRecipe) iArcRec;
//				//System.out.println("Has Ingredients: "+consumeIngredients(shapeless.getInput().toArray(), false));
//				//System.out.println("Has Aspects: "+useAspectsToCraft(false));
//				//System.out.println("Can Output: "+outputItem(craftResult,false));
//				if( useAspectsToCraft(false) && consumeIngredients(shapeless.getInput().toArray(), false) && outputItem(craftResult,false))
//				{
//					//System.out.println("debug4 - Will Craft");
//					useAspectsToCraft(true);
//					consumeIngredients(shapeless.getInput().toArray(), true);
//					outputItem(craftResult,true);
//				}
//			}
//		}
//		else
//		{
//			//System.out.println("debug2 - Normal");
//			IRecipe normalRecipe = Utilities.findMatchingRecipe(getDummyCrafter(), worldObj);
//			if(normalRecipe instanceof ShapedRecipes)
//			{
//				//System.out.println("debug3 - Shaped");
//				if(consumeIngredients(((ShapedRecipes)normalRecipe).recipeItems, false) && outputItem(craftResult,false))
//				{
//					//System.out.println("debug4 - Will Craft");
//					consumeIngredients(((ShapedRecipes)normalRecipe).recipeItems, true);
//					outputItem(craftResult,true);
//				}
//			}
//			else if(normalRecipe instanceof ShapelessRecipes)
//			{
//				//System.out.println("debug3 - Shapeless");
//				if(consumeIngredients(((ShapelessRecipes)normalRecipe).recipeItems.toArray(), false) && outputItem(craftResult,false))
//				{
//					//System.out.println("debug4 - Will Craft");
//					consumeIngredients(((ShapelessRecipes)normalRecipe).recipeItems.toArray(), true);
//					outputItem(craftResult,true);
//				}
//			}
//			else if(normalRecipe instanceof ShapedOreRecipe)
//			{
//				//System.out.println("debug3 - ShapedOre");
//				//System.out.println("Has Ingredients: "+consumeIngredients(((ShapedOreRecipe)normalRecipe).getInput(), false));
//				//System.out.println("Can Output: "+outputItem(craftResult,false));
//				if(consumeIngredients(((ShapedOreRecipe)normalRecipe).getInput(), false) && outputItem(craftResult,false))
//				{
//					//System.out.println("debug4 - Will Craft");
//					consumeIngredients(((ShapedOreRecipe)normalRecipe).getInput(), true);
//					outputItem(craftResult,true);
//				}
//			}
//			else if(normalRecipe instanceof ShapelessOreRecipe)
//			{
//				//System.out.println("debug3 - ShapelessOre");
//				if(consumeIngredients(((ShapelessOreRecipe)normalRecipe).getInput().toArray(), false) && outputItem(craftResult,false))
//				{
//					//System.out.println("debug4 - Will Craft");
//					consumeIngredients(((ShapelessOreRecipe)normalRecipe).getInput().toArray(), true);
//					outputItem(craftResult,true);
//				}
//			}
//		}
//		//this.updateContainer();
//	}
//
//	public ItemStack getCraftingResult()
//	{
//		ItemStack wandStack = getStackInSlot(9);
//		ItemStack standardResult = CraftingManager.getInstance().findMatchingRecipe(getDummyCrafter(),worldObj);
//		ItemStack arcaneResult = null;
//		if(owner != null && wandStack != null)
//			arcaneResult = ThaumcraftCraftingManager.findMatchingArcaneRecipe(this, owner);
//		if(arcaneResult != null) //Arcane recipe is the primary focus
//			return arcaneResult;
//		return standardResult;
//	}
//
//	public boolean useAspectsToCraft(boolean doit)
//	{
//		ItemWandCasting wand = (ItemWandCasting)getStackInSlot(9).getItem();
//		return wand.consumeAllVisCrafting(getStackInSlot(9), owner, ThaumcraftCraftingManager.findMatchingArcaneRecipeAspects(this, owner), doit);
//	}
//
//	private boolean consumeIngredients(Object[] ingr, boolean doit)
//	{
//		HashMap<ItemStack, Integer> required = new HashMap<ItemStack, Integer>();
//		int ingrCount = 0;
//		for(Object s: ingr)
//		{
//			if(s instanceof ItemStack)
//			{	
//				boolean isPutIn = false;
//				for(ItemStack alreadyReq : required.keySet())
//					if(InventoryHelper.areItemStacksEqual(alreadyReq, (ItemStack) s, true, false) && !isPutIn)
//					{
//						required.put(alreadyReq, required.get(alreadyReq)+1);
//						isPutIn = true;
//					}
//				if(!isPutIn)
//				{
//					required.put((ItemStack) s, 1);
//				}
//			}
//			else if(s instanceof ArrayList)
//			{
//				ItemStack s2 = (ItemStack)((ArrayList)s).toArray()[0];
//				boolean isPutIn = false;
//				for(ItemStack alreadyReq : required.keySet())
//					if(InventoryHelper.areItemStacksEqual(alreadyReq, s2, true, false) && !isPutIn)
//					{
//						required.put(alreadyReq, required.get(alreadyReq)+1);
//						isPutIn = true;
//					}
//				if(!isPutIn)
//					required.put(s2, 1);
//			}
//		}
//		List<ItemStack> forRemoval = new ArrayList<ItemStack>();
//		for(Entry<ItemStack, Integer> e : required.entrySet())
//		{
//			int amountRequired = e.getValue();
//			//System.out.println("Stack: "+e.getKey()+", Amount:"+e.getValue());
//
//			for(int j=10; j<19; j++)
//			{
//				ItemStack s = getStackInSlot(j);
//				//System.out.println("List Item: "+e.getKey()+" and inv pos: "+j+"("+s+") are equal: "+InventoryHelper.areItemStacksEqual(e.getKey(), s, true, true));
//				if(InventoryHelper.areItemStacksEqual(e.getKey(), s, true, false))
//				{
//					if(amountRequired >= s.stackSize)
//					{
//						amountRequired -= s.stackSize;
//						if(doit)
//							s = null;
//					}
//					else
//					{
//						if(doit)
//							s.stackSize -= amountRequired;
//						amountRequired = 0;
//					}
//					if(s != null && s.stackSize <= 0)
//						s=null;
//					e.setValue(amountRequired);
//					if(doit)
//						setInventorySlotContents(j, s);
//					if(amountRequired <= 0)
//					{
//						forRemoval.add(e.getKey());
//						//required.remove(e.getKey());
//						break;
//					}
//				}
//			}
//		}
//		for(ItemStack fd: forRemoval)
//			required.remove(fd);
//		return required.isEmpty();
//
//		//		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
//		//		for(Object s: ingr)
//		//			if(s instanceof ItemStack)
//		//				list.add((ItemStack)s);
//		//			else if(s instanceof ArrayList)
//		//				list.add((ItemStack)((ArrayList)s).toArray()[0]);
//		//		for(ItemStack sI : list)
//		//		{
//		//			//ItemStack sI = list.get(i);
//		//			for(int j=10; j<19; j++)
//		//			{
//		//				ItemStack s = getStackInSlot(j);
//		//				System.out.println("List Item: "+sI+" and inv pos: "+j+"("+s+") are equal: "+InventoryHelper.areItemStacksEqual(sI, s, true, true));
//		//				if(InventoryHelper.areItemStacksEqual(sI, s, true, true))
//		//				{
//		//					if(sI.stackSize >= s.stackSize)
//		//					{
//		//						sI.stackSize -= s.stackSize;
//		//						if(doit)
//		//							s = null;
//		//					}
//		//					else
//		//					{
//		//						if(doit)
//		//							s.stackSize -= sI.stackSize;
//		//						sI = null;
//		//					}
//		//					if(sI == null || sI.stackSize <= 0)
//		//					{
//		//						list.
//		//						break;
//		//					}
//		//				}
//		//			}
//		//		}
//		//		return list.isEmpty();
//	}
//
//	private boolean outputItem(ItemStack output, boolean doit)
//	{
//		for(int slot=10; slot<19; slot++)
//		{
//			if(output==null)
//				return true;
//			ItemStack s = getStackInSlot(slot);
//			if(s == null)
//			{
//				if(doit)
//					setInventorySlotContents(slot, output.copy());
//				return true;
//			} 
//			else if(InventoryHelper.areItemStacksEqual(s, output, false, true))
//			{
//				if(s.stackSize + output.stackSize <= getInventoryStackLimit())
//				{
//					if(doit)
//					{
//						s.stackSize += output.stackSize;
//						updateContainer();
//					}
//					return true;
//				}
//				if(doit)
//				{
//					int freeSpace = getInventoryStackLimit()-s.stackSize;
//					s.stackSize += freeSpace;
//					output.stackSize -= freeSpace;
//					updateContainer();
//				}
//			}
//		}
//		return false;
//	}
//
//	public InventoryCrafting getDummyCrafter()
//	{
//		InventoryCrafting ic = new InventoryCrafting(new ContainerDummy(), 3, 3);
//		for (int a = 0; a < 9; a++)
//			ic.setInventorySlotContents(a, this.getStackInSlot(a));
//		return ic;
//	}
//
//	@Override
//	public void writeCustomNBT(NBTTagCompound tag)
//	{
//		NBTTagList tagList = new NBTTagList();
//		for (int i = 0; i < this.inv.length; i++) {
//			if (this.inv[i] != null)
//			{
//				NBTTagCompound tagPart = new NBTTagCompound();
//				tagPart.setByte("Slot", (byte)i);
//				this.inv[i].writeToNBT(tagPart);
//				tagList.appendTag(tagPart);
//			}
//		}
//		tag.setTag("Inventory", tagList);
//		if(ownerName != null)
//			tag.setString("ownerName", ownerName);
//	}
//	@Override
//	public void readCustomNBT(NBTTagCompound tag)
//	{
//		NBTTagList tagList = tag.getTagList("Inventory");
//		this.inv = new ItemStack[getSizeInventory()];
//		for(int i = 0; i < tagList.tagCount(); i++)
//		{
//			NBTTagCompound tagPart = (NBTTagCompound)tagList.tagAt(i);
//			int slot = tagPart.getByte("Slot") & 0xFF;
//			if ((slot >= 0) && (slot < this.inv.length)) {
//				this.inv[slot] = ItemStack.loadItemStackFromNBT(tagPart);
//			}
//		}
//		ownerName = tag.getString("ownerName");
//		//if(tag.hasKey("owner"))
//		//	owner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(tag.getString("owner"));
//	}
//
//	@Override
//	public int getSizeInventory()
//	{
//		return inv.length;
//	}
//
//	@Override
//	public ItemStack getStackInSlot(int slot)
//	{
//		return inv[slot];
//	}
//
//	@Override
//	public ItemStack decrStackSize(int slot, int amt)
//	{
//		ItemStack stack = getStackInSlot(slot);
//		if (stack != null) {
//			if (stack.stackSize <= amt) {
//				setInventorySlotContents(slot, null);
//			} else {
//				stack = stack.splitStack(amt);
//				if (stack.stackSize == 0) {
//					setInventorySlotContents(slot, null);
//				}
//			}
//		}
//		return stack;
//	}
//
//	@Override
//	public ItemStack getStackInSlotOnClosing(int slot)
//	{
//		ItemStack stack = getStackInSlot(slot);
//		if (stack != null) {
//			setInventorySlotContents(slot, null);
//		}
//		return stack;
//	}
//
//	@Override
//	public void setInventorySlotContents(int slot, ItemStack stack)
//	{
//		inv[slot] = stack;
//		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
//			stack.stackSize = getInventoryStackLimit();
//		}              
//	}
//
//	@Override
//	public String getInvName()
//	{
//		return "wg.arcaneCrafter";
//	}
//
//	@Override
//	public boolean isInvNameLocalized()
//	{
//		return false;
//	}
//
//	@Override
//	public int getInventoryStackLimit()
//	{
//		return 64;
//	}
//
//	@Override
//	public boolean isUseableByPlayer(EntityPlayer entityplayer)
//	{
//		return true;
//	}
//
//	@Override
//	public void openChest() {}
//
//	@Override
//	public void closeChest() {}
//
//	@Override
//	public boolean isItemValidForSlot(int slot, ItemStack itemStack)
//	{
//		if (slot == 9 && itemStack != null)
//			return itemStack.getItem() instanceof ItemWandCasting;
//		return slot >= 10;
//	}
//
//	@Override
//	public int[] getAccessibleSlotsFromSide(int side)
//	{
//		if(side == 1)
//			return new int[] {9};
//		return new int[] {10,11,12,13,14,15,16,17,18};
//	}
//
//	@Override
//	public boolean canInsertItem(int slot, ItemStack itemStack, int side)
//	{
//		return slot>=9 && slot<=18;
//	}
//
//	@Override
//	public boolean canExtractItem(int slot, ItemStack itemStack, int side)
//	{
//		return slot>=9 && slot<=18;
//	}
//
//
//	private void updateContainer()
//	{
//		if(container != null)
//			container.onCraftMatrixChanged(this);
//	}
//
//	@Override
//	public ItemStack getStackInRowAndColumn(int par1, int par2)
//	{
//		return super.getStackInRowAndColumn(par1, par2);
//	}
}