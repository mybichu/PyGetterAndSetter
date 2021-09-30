package com.neoyu.plugin.action;

import com.intellij.ui.IconManager;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public final class MyIcon {
    public static final @NotNull
    Icon GEN_CODE = load("pluginIcon.svg");

    private static @NotNull
    Icon load(@NotNull @NonNls String path) {
        return IconManager.getInstance().getIcon(path, MyIcon.class);
    }
}
