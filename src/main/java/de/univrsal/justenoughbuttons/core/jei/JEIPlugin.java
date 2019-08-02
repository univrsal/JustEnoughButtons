//package de.univrsal.justenoughbuttons.core.jei;
//
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.runtime.IJeiRuntime;
//import net.minecraft.util.ResourceLocation;
//
///**
// * Created by universal on 19.01.2017.
// * This file is part of JEI Buttons which is licenced
// * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
// * github.com/univrsal/JEI Buttons
// */
//public class JEIPlugin implements IModPlugin {
//
//    private static IJeiRuntime runtime;
//
//    @Override
//    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
//        runtime = jeiRuntime;
//    }
//
//    @Override
//    public ResourceLocation getPluginUid() {
//        return null;
//    }
//
//    public static void setJEIText(String text) {
//        runtime.getIngredientFilter().setFilterText(text);
//    }
//}
