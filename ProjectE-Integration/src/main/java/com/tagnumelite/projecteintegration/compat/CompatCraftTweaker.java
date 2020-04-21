package com.tagnumelite.projecteintegration.compat;

import java.util.Arrays;

import com.tagnumelite.projecteintegration.PEIntegration;
import com.tagnumelite.projecteintegration.api.PEIApi;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.emc.IngredientMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.projecteintegration.PEI")
public class CompatCraftTweaker {
    @ZenMethod
    public static void addConversion(IItemStack output, IIngredient[] input) {
        PEIntegration.LOG.info("Testing CraftTweaker Support: {}", output.getDisplayName());
        CraftTweakerAPI.apply(new AddConversion(output, input));
    }

    @ZenMethod
    public static void addConversion(ILiquidStack output, IIngredient[] input) {
        PEIntegration.LOG.info("Testing CraftTweaker Support: {}", output.getDisplayName());
        CraftTweakerAPI.apply(new AddConversion(output, input));
    }

    private static class AddConversion implements IAction {
        private final IItemStack item;
        private final ILiquidStack fluid;
        private final IIngredient[] ingredients;

        protected AddConversion(IItemStack output, IIngredient[] input) {
            this.item = output;
            this.ingredients = input;

            this.fluid = null;
        }

        protected AddConversion(ILiquidStack output, IIngredient[] input) {
            this.fluid = output;
            this.ingredients = input;

            this.item = null;
        }

        @Override
        public void apply() {
            IngredientMap<Object> map = new IngredientMap<Object>();

            for (IIngredient ingredient : ingredients) {
                Object input = null;

                if (ingredient.getItems().size() == 0 && ingredient.getLiquids().size() > 0) {
                    input = PEIApi.getList(Arrays.asList(
                        CraftTweakerMC.getLiquidStacks(ingredient.getLiquids().toArray(new ILiquidStack[0]))));
                    CraftTweakerMC.getFluid(null);
                } else if (ingredient.getItems().size() > 0) {
                    input = PEIApi.getIngredient(CraftTweakerMC.getIngredient(ingredient));
                }

                map.addIngredient(input, ingredient.getAmount());
            }

            if (item != null) {
                ItemStack output = CraftTweakerMC.getItemStack(item);
                ProjectEAPI.getConversionProxy().addConversion(output.getCount(), output, map.getMap());
            } else if (fluid != null) {
                FluidStack output = CraftTweakerMC.getLiquidStack(fluid);
                ProjectEAPI.getConversionProxy().addConversion(output.amount, output, map.getMap());
            } else {

            }
        }

        @Override
        public String describe() {
            return "Add your own custom conversion to ProjectE for any custom non supported crafting mechanics";
        }
    }
}
