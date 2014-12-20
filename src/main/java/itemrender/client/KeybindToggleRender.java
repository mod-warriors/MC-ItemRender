package itemrender.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeybindToggleRender {
    public static final KeyBinding KEY_BINDING = new KeyBinding("Toggle Render Preview", Keyboard.KEY_O, "ItemRender");

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(KEY_BINDING.isPressed()) {
            RenderTickHandler.renderPreview = !RenderTickHandler.renderPreview;
        }
    }
}
