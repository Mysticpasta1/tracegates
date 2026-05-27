package jan.yuboingirirobobobiko.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ReadTraceBlock extends BaseTraceBlock {
    public static final MapCodec<ReadTraceBlock> CODEC = simpleCodec(ReadTraceBlock::new);
    @Override @NotNull protected MapCodec<? extends ReadTraceBlock> codec() { return CODEC; }
    
    public ReadTraceBlock(Properties properties) {
        super(properties, false);
    }
    
    @Override
    protected boolean isSignalSource(BlockState state) {
        return state.getValue(LIT);
    }
    
    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(LIT) ? 15 : 0;
    }
}
