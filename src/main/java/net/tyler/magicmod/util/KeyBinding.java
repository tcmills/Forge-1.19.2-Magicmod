package net.tyler.magicmod.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_MAGIC = "key.category.magicmod.magic";
    public static final String KEY_PRESS_J = "key.magicmod.press_j";

    public static final KeyMapping PRESS_J_KEY = new KeyMapping(KEY_PRESS_J, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, KEY_CATEGORY_MAGIC);

}
