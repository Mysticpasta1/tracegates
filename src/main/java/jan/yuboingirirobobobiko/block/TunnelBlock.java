package jan.yuboingirirobobobiko.block;

import jan.yuboingirirobobobiko.TraceGates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TunnelBlock extends Block {
    public TunnelBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide()) return;
        
        TraceGates.traceNetwork.placeTunnel(level, pos);
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level.isClientSide()) return;
        // Just rebuild all traces
        TraceGates.traceNetwork.removeTunnel(level, pos);
        
    }
}
