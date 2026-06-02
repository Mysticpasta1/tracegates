package jan.yuboingirirobobobiko.block;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.TraceGates;
import jan.yuboingirirobobobiko.trace.TraceObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CrossBlock extends Block {
    public static final MapCodec<CrossBlock> CODEC = simpleCodec(CrossBlock::new);
    @Override @NotNull protected MapCodec<? extends CrossBlock> codec() { return CODEC; }
    
    public CrossBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
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
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        for (Direction direction : new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST}) {
            TraceObject trace = TraceGates.traceNetwork.getTraceAtPos(level, pos.relative(direction));
            if (trace != null && trace == TraceGates.traceNetwork.getTraceAtPos(level, pos.relative(direction.getOpposite()))) {
                TraceGates.traceNetwork.rebuildTrace(trace);
            }
        }
    }
}
