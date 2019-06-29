package com.tagnumelite.projecteintegration.plugins;

import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipe;
import com.blakebr0.extendedcrafting.crafting.CompressorRecipeManager;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.tagnumelite.projecteintegration.api.PEIPlugin;
import com.tagnumelite.projecteintegration.api.RegPEIPlugin;
import com.tagnumelite.projecteintegration.api.mappers.PEIMapper;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;

@RegPEIPlugin(modid = "extendedcrafting")
public class PluginExtendedCrafting extends PEIPlugin {
	public PluginExtendedCrafting(String modid, Configuration config) {
		super(modid, config);
	}

	@Override
	public void setup() {
		addMapper(new ECCompressorMapper());
		addMapper(new ECCombinationMapper());
		addMapper(new ECTableMapper());
	}

	private class ECCompressorMapper extends PEIMapper {
		public ECCompressorMapper() {
			super("compressor");
		}

		@Override
		public void setup() {
			for (CompressorRecipe recipe : CompressorRecipeManager.getInstance().getRecipes()) {
				addRecipe(recipe.getOutput(), recipe.getInput());
			}
		}
	}

	private class ECCombinationMapper extends PEIMapper {
		public ECCombinationMapper() {
			super("combination");
		}

		@Override
		public void setup() {
			for (CombinationRecipe recipe : CombinationRecipeManager.getInstance().getRecipes()) {
				addRecipe(recipe.getOutput(), recipe.getInput(), recipe.getPedestalItems().toArray());
			}
		}
	}

	private class ECTableMapper extends PEIMapper {
		public ECTableMapper() {
			super("table", "Enable Ender and Tiered crafting recipe mapper?");
		}

		@Override
		public void setup() {
			for (Object recipe : EnderCrafterRecipeManager.getInstance().getRecipes()) {
				addRecipe((IRecipe) recipe);
			}

			for (Object recipe : TableRecipeManager.getInstance().getRecipes()) {
				addRecipe((IRecipe) recipe);
			}
		}
	}
}
