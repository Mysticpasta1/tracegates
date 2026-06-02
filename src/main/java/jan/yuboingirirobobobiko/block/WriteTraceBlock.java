package jan.yuboingirirobobobiko.block;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.TraceGates;
import jan.yuboingirirobobobiko.trace.TraceObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WriteTraceBlock extends BaseTraceBlock {
    public static final MapCodec<WriteTraceBlock> CODEC = simpleCodec(WriteTraceBlock::new);
    @Override @NotNull protected MapCodec<? extends WriteTraceBlock> codec() { return CODEC; }
    
    public WriteTraceBlock(Properties properties) {
        super(properties, false);
    }
    
    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.isClientSide()) return;
        
        TraceObject trace = TraceGates.traceNetwork.getTraceAtPos(level, pos);
        if (trace == null) return;
        trace.setRedstoneUpdate();
        TraceGates.traceNetwork.queueUpdate(trace);
    }
}
