package itemrender.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import itemrender.client.rendering.FBOHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.UnsupportedEncodingException;

@SideOnly(Side.CLIENT)
public class KeybindRenderInventoryBlock {
    public static final KeyBinding KEY_BINDING = new KeyBinding("Render Inventory Block", Keyboard.KEY_P, "ItemRender");

    public FBOHelper fbo;

    private String filenameSuffix = "";

    private RenderItem itemRenderer = new RenderItem();

    public KeybindRenderInventoryBlock(int textureSize, String filename_suffix) {
        fbo = new FBOHelper(textureSize);
        filenameSuffix = filename_suffix;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(KEY_BINDING.isPressed()) {
            Minecraft minecraft = FMLClientHandler.instance().getClient();
            if(minecraft.thePlayer != null) {
                ItemStack current = minecraft.thePlayer.getCurrentEquippedItem();
                if(current != null && current.getItem() != null) {

                    fbo.begin();

                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glPushMatrix();
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0, 16, 0, 16, -100.0, 100.0);

                    GL11.glMatrixMode(GL11.GL_MODELVIEW);

                    RenderHelper.enableGUIStandardItemLighting();

                    RenderBlocks renderBlocks = ReflectionHelper.getPrivateValue(Render.class, itemRenderer,
                            "field_76988_d",
                            "renderBlocks");
                    if(!ForgeHooksClient
                            .renderInventoryItem(renderBlocks, minecraft.renderEngine, current, true, 0.0f,
                                    (float) 0, (float) 0)) {
                        itemRenderer.renderItemIntoGUI(null, minecraft.renderEngine, current, 0, 0);
                    }

                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glPopMatrix();

                    RenderHelper.disableStandardItemLighting();

                    fbo.end();



                    fbo.saveToFile(new File(minecraft.mcDataDir,
                            String.format("rendered/" + GetValidFileName(current.getItem().getUnlocalizedName()) + "_%d%s.png", current.getMetadata(), filenameSuffix)));

                    fbo.restoreTexture();
                }
            }
        }
    }
    
    private static String GetValidFileName(String fileName)
    {
        String cleanedFilename = null;
        try
        {
            cleanedFilename = new String(fileName.getBytes(), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        cleanedFilename = cleanedFilename.replaceAll("[\\?\\\\/:|<>\\*]", " "); //filter ? \ / : | < > *
        cleanedFilename = cleanedFilename.replaceAll("\\s+", "_");
        return cleanedFilename;
    }
}
