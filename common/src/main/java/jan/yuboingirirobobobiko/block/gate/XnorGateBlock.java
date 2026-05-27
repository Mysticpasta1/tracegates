package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import org.jetbrains.annotations.NotNull;

public class XnorGateBlock extends BaseTraceBlock {
    public static final MapCodec<XnorGateBlock> CODEC = simpleCodec(XnorGateBlock::new);
    @Override @NotNull protected MapCodec<? extends XnorGateBlock> codec() { return CODEC; }
    
    public XnorGateBlock(Properties properties) {
        super(properties, true);
    }
}
