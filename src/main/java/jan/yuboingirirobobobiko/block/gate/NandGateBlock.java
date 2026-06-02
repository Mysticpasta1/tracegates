package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import org.jetbrains.annotations.NotNull;

public class NandGateBlock extends BaseTraceBlock {
    public static final MapCodec<NandGateBlock> CODEC = simpleCodec(NandGateBlock::new);
    @Override @NotNull protected MapCodec<? extends NandGateBlock> codec() { return CODEC; }
    
    public NandGateBlock(Properties properties) {
        super(properties, true);
    }
}
