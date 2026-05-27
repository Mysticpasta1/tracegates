package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import org.jetbrains.annotations.NotNull;

public class AndGateBlock extends BaseTraceBlock {
    public static final MapCodec<AndGateBlock> CODEC = simpleCodec(AndGateBlock::new);
    @Override @NotNull protected MapCodec<? extends AndGateBlock> codec() { return CODEC; }
    
    public AndGateBlock(Properties properties) {
        super(properties, false);
    }
}
