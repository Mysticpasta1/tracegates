package jan.yuboingirirobobobiko.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class ReadTraceBlock extends BaseTraceBlock {
    public ReadTraceBlock(Properties properties) {
        super(properties, false);
    }
    
    @Override
    public boolean isSignalSource(BlockState state) {
        return state.getValue(LIT);
    }
    
    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(LIT) ? 15 : 0;
    }
}
