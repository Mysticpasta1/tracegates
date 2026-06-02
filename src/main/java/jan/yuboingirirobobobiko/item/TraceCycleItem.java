package jan.yuboingirirobobobiko.item;

import jan.yuboingirirobobobiko.ModRegistries;
import jan.yuboingirirobobobiko.block.BusBlock;
import jan.yuboingirirobobobiko.block.TraceBlock;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class TraceCycleItem extends BlockItem {
    private final int maxValue;
    private final Block block;

    public TraceCycleItem(Block block, Item.Properties properties, int max) {
        super(block, properties);
        maxValue = max;
        this.block = block;
    }

    @Override @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack oldStack = player.getItemInHand(usedHand);

        int direction = player.isSecondaryUseActive() ? -1 : 1;
        var oldTag = oldStack.getTag();
        int oldState = (oldTag != null && oldTag.contains("CustomModelData")) ? oldTag.getInt("CustomModelData") : 0;
        int newState = oldState + direction;

        if (newState == 16) return InteractionResultHolder.pass(new ItemStack(ModRegistries.Items.WRITE_TRACE.get()));
        if (newState == -1 && block instanceof TraceBlock) return InteractionResultHolder.pass(new ItemStack(ModRegistries.Items.READ_TRACE.get()));

        if (newState > maxValue) newState = 0;
        if (newState < 0) newState = maxValue;

        ItemStack stack = new ItemStack(this);
        stack.setCount(oldStack.getCount());
        if (oldStack.hasCustomHoverName()) {
            stack.setHoverName(oldStack.getHoverName());
        }
        if (block instanceof TraceBlock) TraceBlock.setItemStack(stack, newState);
        if (block instanceof BusBlock) BusBlock.setItemStack(stack, newState);
        return InteractionResultHolder.pass(stack);
    }
}
