package jan.yuboingirirobobobiko.block;

import jan.yuboingirirobobobiko.TraceGates;
import jan.yuboingirirobobobiko.trace.TraceObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CrossBlock extends Block {
    public CrossBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide()) return;
        for (Direction direction : new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST}) {
            TraceObject trace = TraceGates.traceNetwork.getTraceAtPos(level, pos.relative(direction));
            if (trace != null) {
                BlockPos otherPos = pos.relative(direction.getOpposite());
                TraceGates.traceNetwork.mergeTrace(level, trace, otherPos, direction, level.getBlockState(otherPos), false);
                TraceGates.traceNetwork.traceFullUpdate(trace);
            }
        }
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        for (Direction direction : new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST}) {
            TraceObject trace = TraceGates.traceNetwork.getTraceAtPos(level, pos.relative(direction));
            if (trace != null && trace == TraceGates.traceNetwork.getTraceAtPos(level, pos.relative(direction.getOpposite()))) {
                TraceGates.traceNetwork.rebuildTrace(trace);
            }
        }
    }
}
