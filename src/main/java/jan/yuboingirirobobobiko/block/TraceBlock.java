package jan.yuboingirirobobobiko.block;

import com.mojang.serialization.MapCodec;
import jan.yuboingirirobobobiko.TraceGates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class TraceBlock extends BaseTraceBlock {
    public static final MapCodec<TraceBlock> CODEC = simpleCodec(TraceBlock::new);
    @Override @NotNull protected MapCodec<? extends TraceBlock> codec() { return CODEC; }
    
    public static final IntegerProperty COLOUR = IntegerProperty.create("colour", 0, 15);
    
    public TraceBlock(Properties properties) {
        super(properties, false);
        registerDefaultState(getStateDefinition().any().setValue(COLOUR, 0).setValue(LIT, false));
    }
    
    public static void setItemStack(ItemStack stack, int colour) {
        stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(TraceBlock.COLOUR, colour));
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(colour));
        stack.set(DataComponents.ITEM_NAME, Component.translatable("item.tracegates.trace." + TraceGates.TRACE_NAMES[colour]));
    }
    
    @Override @NotNull
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        setItemStack(stack, state.getValue(COLOUR));
        return stack;
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        
        builder.add(COLOUR);
    }
}
