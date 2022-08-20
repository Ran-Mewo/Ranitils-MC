package io.github.ran.minecraft.ranitils.forge;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;

public class PreLaunchSetup {
    public static void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }
}
