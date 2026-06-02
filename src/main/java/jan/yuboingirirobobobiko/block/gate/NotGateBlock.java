package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import org.jetbrains.annotations.NotNull;

public class NotGateBlock extends BaseTraceBlock {
    public static final MapCodec<NotGateBlock> CODEC = simpleCodec(NotGateBlock::new);
    @Override @NotNull protected MapCodec<? extends NotGateBlock> codec() { return CODEC; }
    
    public NotGateBlock(Properties properties) {
        super(properties, true);
    }
}
