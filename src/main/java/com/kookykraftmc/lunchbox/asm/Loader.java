package com.kookykraftmc.lunchbox.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

/**
 * Created by TimeTheCat on 5/22/2016.
 */

public class Loader implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() { return null; }


    @Override
    public String getModContainerClass() {
        return LBDummy.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return AccessTransformerLB.class.getName();
    }
}
