package jan.yuboingirirobobobiko.block;

import jan.yuboingirirobobobiko.TraceGates;
import jan.yuboingirirobobobiko.trace.TraceObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class BaseBaseTraceBlock extends Block {
    public BaseBaseTraceBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide()) return;
        
        TraceGates.traceNetwork.placeTraceBlock(level, pos);
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level.isClientSide()) return;
        
        if (movedByPiston) {
            TraceObject trace = TraceGates.traceNetwork.getTraceAtPos(level, pos);
            TraceGates.traceNetwork.removeTraceBlock(level, pos);
            TraceGates.traceNetwork.rebuildTrace(trace);
        } else {
            TraceGates.traceNetwork.removeTraceBlock(level, pos);
        }
    }
}
