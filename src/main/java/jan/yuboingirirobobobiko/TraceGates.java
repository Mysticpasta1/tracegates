package jan.yuboingirirobobobiko;

import jan.yuboingirirobobobiko.trace.GlobalTraceNetwork;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TraceGates.MOD_ID)
public final class TraceGates {
    public static final String MOD_ID = "tracegates";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static GlobalTraceNetwork traceNetwork = null;

    public static final String[] TRACE_NAMES = new String[]{
            "grey",
            "white",
            "red",
            "orange",
            "gold",
            "yellow",
            "lemon",
            "lime",
            "green",
            "turquoise",
            "sky",
            "blue",
            "sapphire",
            "purple",
            "violet",
            "pink",
    };
    public static final String[] BUS_TRACE_NAMES = new String[]{
            "red",
            "green",
            "blue",
            "cyan",
            "magenta",
            "yellow",
    };

    public TraceGates() {
        ModRegistries.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        traceNetwork = new GlobalTraceNetwork(event.getServer());
    }

    @SubscribeEvent
    public void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && traceNetwork != null && event.level instanceof ServerLevel level) {
            traceNetwork.tick(level);
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.buildCommands(event.getDispatcher());
    }
}
