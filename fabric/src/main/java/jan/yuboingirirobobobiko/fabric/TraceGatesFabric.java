package jan.yuboingirirobobobiko.fabric;

import net.fabricmc.api.ModInitializer;

import jan.yuboingirirobobobiko.TraceGates;

public final class TraceGatesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        TraceGates.init();
    }
}
