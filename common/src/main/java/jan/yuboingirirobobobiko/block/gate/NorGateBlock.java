package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import org.jetbrains.annotations.NotNull;

public class NorGateBlock extends BaseTraceBlock {
    public static final MapCodec<NorGateBlock> CODEC = simpleCodec(NorGateBlock::new);
    @Override @NotNull protected MapCodec<? extends NorGateBlock> codec() { return CODEC; }
    
    public NorGateBlock(Properties properties) {
        super(properties, true);
    }
}
