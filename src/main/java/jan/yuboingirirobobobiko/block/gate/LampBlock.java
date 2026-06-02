package jan.yuboingirirobobobiko.block.gate;

import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LampBlock extends BaseTraceBlock {
    public static int getLight(BlockState state) {
        return state.getValue(LampBlock.LIT) ? 15 : 0;
    }
    
    public LampBlock(Properties properties) {
        super(properties, false);
    }
}
