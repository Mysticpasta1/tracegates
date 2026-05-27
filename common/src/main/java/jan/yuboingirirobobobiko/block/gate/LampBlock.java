package jan.yuboingirirobobobiko.block.gate;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LampBlock extends BaseTraceBlock {
    public static final MapCodec<LampBlock> CODEC = simpleCodec(LampBlock::new);
    @Override @NotNull protected MapCodec<? extends LampBlock> codec() { return CODEC; }
    
    public static int getLight(BlockState state) {
        return state.getValue(LampBlock.LIT) ? 15 : 0;
    }
    
    public LampBlock(Properties properties) {
        super(properties, false);
    }
}
