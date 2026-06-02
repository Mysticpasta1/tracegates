package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import org.jetbrains.annotations.NotNull;

public class XorGateBlock extends BaseTraceBlock {
    public static final MapCodec<XorGateBlock> CODEC = simpleCodec(XorGateBlock::new);
    @Override @NotNull protected MapCodec<? extends XorGateBlock> codec() { return CODEC; }
    
    public XorGateBlock(Properties properties) {
        super(properties, false);
    }
}
