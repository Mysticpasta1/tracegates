package jan.yuboingirirobobobiko.block.gate;

import jan.yuboingirirobobobiko.TraceGates;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import jan.yuboingirirobobobiko.trace.TraceObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BaseLatchGateBlock extends BaseTraceBlock {
    public BaseLatchGateBlock(Properties properties, boolean lit) {
        super(properties, lit);
    }
    
    @Override @NotNull
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            TraceObject trace = TraceGates.traceNetwork.getTraceAtPos(level, pos);
            if (trace != null) {
                trace.click();
                TraceGates.traceNetwork.queueUpdate(trace);
            }
        }
        
        return InteractionResult.SUCCESS;
    }
}
