package itemrender;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import itemrender.client.KeybindRenderEntity;
import itemrender.client.KeybindRenderInventoryBlock;
import itemrender.client.KeybindToggleRender;
import itemrender.client.RenderTickHandler;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLContext;

@Mod(modid = "itemrender", name = "ItemRender", version = "@MOD_VERSION@")
public class ItemRenderMod {
    @Mod.Instance("itemrender")
    public static ItemRenderMod instance;

    public static boolean gl32_enabled = false;

    public static final int DEFAULT_MAIN_TEXTURE_SIZE = 128;
    public static final int DEFAULT_GRID_TEXTURE_SIZE = 32;

    private int mainTextureSize;
    private int gridTextureSize;

    public static Logger log;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        log = e.getModLog();
        gl32_enabled = GLContext.getCapabilities().OpenGL32;

        Configuration config = new Configuration(e.getSuggestedConfigurationFile());
        mainTextureSize = config.get(Configuration.CATEGORY_GENERAL, "mainTextureSize", DEFAULT_MAIN_TEXTURE_SIZE).getInt();
        gridTextureSize = config.get(Configuration.CATEGORY_GENERAL, "gridTextureSize", DEFAULT_GRID_TEXTURE_SIZE).getInt();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        if(gl32_enabled) {
            FMLCommonHandler.instance().bus().register(new RenderTickHandler());
            KeybindRenderInventoryBlock defaultRender = new KeybindRenderInventoryBlock(mainTextureSize, "");
            RenderTickHandler.keybindToRender = defaultRender;
            FMLCommonHandler.instance().bus().register(defaultRender);
            FMLCommonHandler.instance().bus().register(new KeybindRenderInventoryBlock(gridTextureSize, "_grid"));
            FMLCommonHandler.instance().bus().register(new KeybindRenderEntity(mainTextureSize, ""));
            FMLCommonHandler.instance().bus().register(new KeybindRenderEntity(gridTextureSize, "_grid"));
            FMLCommonHandler.instance().bus().register(new KeybindToggleRender());
            ClientRegistry.registerKeyBinding(KeybindRenderInventoryBlock.KEY_BINDING);
            ClientRegistry.registerKeyBinding(KeybindRenderEntity.KEY_BINDING);
            ClientRegistry.registerKeyBinding(KeybindToggleRender.KEY_BINDING);
        } else {
            log.fatal("OpenGL 3.2 not detected, mod will not work!");
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    }
}

