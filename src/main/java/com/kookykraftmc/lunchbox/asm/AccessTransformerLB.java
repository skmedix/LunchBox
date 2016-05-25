package com.kookykraftmc.lunchbox.asm;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

/**
 * Created by TimeTheCat on 5/22/2016.
 */
public class AccessTransformerLB extends AccessTransformer {
    public AccessTransformerLB() throws IOException {
        super("lunchbox_at.cfg");
    }
}
