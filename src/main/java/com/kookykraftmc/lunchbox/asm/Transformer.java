package com.kookykraftmc.lunchbox.asm;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * Created by TimeTheCat on 5/22/2016.
 */
public class Transformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return new byte[0];
    }
}
