package com.davigj.blasted_barrens.core.other;

import com.teamabnormals.upgrade_aquatic.core.UAConfig;
import net.minecraftforge.fml.ModList;

public class BBConstants {
    public static final boolean renewableSand;

    static {
        renewableSand = ModList.get().isLoaded("upgrade_aquatic") ? UAConfig.COMMON.renewableSand.get() : false;
    }
}
