package jan.yuboingirirobobobiko.block.gate;

import jan.yuboingirirobobobiko.block.BaseBaseTraceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class LedBlock extends BaseBaseTraceBlock {
    public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 7);
    
    public LedBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(STATE, 0));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }
}
