package jan.yuboingirirobobobiko.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class BaseTraceBlock extends BaseBaseTraceBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
    public BaseTraceBlock(Properties properties, boolean lit) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(LIT, lit));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
