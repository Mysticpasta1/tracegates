package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public class LatchGateOnBlock extends BaseLatchGateBlock {
    public static final MapCodec<LatchGateOnBlock> CODEC = simpleCodec(LatchGateOnBlock::new);
    @Override @NotNull protected MapCodec<? extends LatchGateOnBlock> codec() { return CODEC; }
    
    public LatchGateOnBlock(Properties properties) {
        super(properties, true);
    }
}
