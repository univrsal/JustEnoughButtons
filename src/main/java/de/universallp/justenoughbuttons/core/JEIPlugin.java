package de.universallp.justenoughbuttons.core;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;

import java.lang.annotation.Annotation;

/**
 * Created by universallp on 19.01.2017.
 * This file is part of JEI Buttons which is licenced
 * under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
 * github.com/UniversalLP/JEI Buttons
 */
@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {

    private static IJeiRuntime runtime;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @Override
    public void register(IModRegistry registry) {

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    public static void setJEIText(String text) {
        runtime.getItemListOverlay().setFilterText(text);
    }
}
