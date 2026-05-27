package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public class LatchGateOffBlock extends BaseLatchGateBlock {
    public static final MapCodec<LatchGateOffBlock> CODEC = simpleCodec(LatchGateOffBlock::new);
    @Override @NotNull protected MapCodec<? extends LatchGateOffBlock> codec() { return CODEC; }
    
    public LatchGateOffBlock(Properties properties) {
        super(properties, false);
    }
}
