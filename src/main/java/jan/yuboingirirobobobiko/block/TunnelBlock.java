package jan.yuboingirirobobobiko.block;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.TraceGates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TunnelBlock extends Block {
    public static final MapCodec<TunnelBlock> CODEC = simpleCodec(TunnelBlock::new);
    @Override @NotNull protected MapCodec<? extends TunnelBlock> codec() { return CODEC; }
    
    public TunnelBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (level.isClientSide()) return;
        
        TraceGates.traceNetwork.placeTunnel(level, pos);
    }
    
    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (level.isClientSide()) return;
        // Just rebuild all traces
        TraceGates.traceNetwork.removeTunnel(level, pos);
        
    }
}
