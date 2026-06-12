package jan.yuboingirirobobobiko.block;

import jan.yuboingirirobobobiko.TraceGates;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BusBlock extends BaseTraceBlock {
    public static final IntegerProperty COLOUR = IntegerProperty.create("colour", 0, 5);

    public BusBlock(Properties properties) {
        super(properties, false);
        registerDefaultState(getStateDefinition().any().setValue(COLOUR, 0).setValue(LIT, false));
    }

    public static void setItemStack(ItemStack stack, int colour) {
        stack.getOrCreateTagElement("BlockStateTag").putString("colour", String.valueOf(colour));
        stack.getOrCreateTag().putInt("CustomModelData", colour);
        stack.setHoverName(Component.translatable("item.tracegates.bus." + TraceGates.BUS_TRACE_NAMES[colour]));
    }

    @Override @NotNull
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        int current = state.getValue(COLOUR);
        int direction = player.isSecondaryUseActive() ? -1 : 1;
        int next = current + direction;
        if (next > 5) next = 0;
        if (next < 0) next = 5;

        level.setBlock(pos, state.setValue(COLOUR, next), Block.UPDATE_ALL);
        return InteractionResult.CONSUME;
    }

    @Override @NotNull
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
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
