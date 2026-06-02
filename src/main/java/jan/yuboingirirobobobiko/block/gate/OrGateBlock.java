package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import org.jetbrains.annotations.NotNull;

public class OrGateBlock extends BaseTraceBlock {
    public static final MapCodec<OrGateBlock> CODEC = simpleCodec(OrGateBlock::new);
    @Override @NotNull protected MapCodec<? extends OrGateBlock> codec() { return CODEC; }
    
    public OrGateBlock(Properties properties) {
        super(properties, false);
    }
}
