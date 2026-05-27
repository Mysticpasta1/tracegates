package jan.yuboingirirobobobiko;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import jan.yuboingirirobobobiko.trace.GlobalTraceNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    public static void init() {
        ModRegistries.register();
        
        LifecycleEvent.SERVER_STARTING.register((server) -> {
            // GlobalTraceNetwork could be static, but would be more work to track cleanup.
            traceNetwork = new GlobalTraceNetwork(server);
        });
        
        TickEvent.SERVER_LEVEL_POST.register((level) -> {
            traceNetwork.tick(level);
        });
        
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) ->
                ModCommands.buildCommands(dispatcher));
    }
}
