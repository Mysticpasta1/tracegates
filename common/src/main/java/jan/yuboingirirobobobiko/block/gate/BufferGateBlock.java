package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import org.jetbrains.annotations.NotNull;

public class BufferGateBlock extends BaseTraceBlock {
    public static final MapCodec<BufferGateBlock> CODEC = simpleCodec(BufferGateBlock::new);
    @Override @NotNull protected MapCodec<? extends BufferGateBlock> codec() { return CODEC; }
    
    public BufferGateBlock(Properties properties) {
        super(properties, false);
    }
}
