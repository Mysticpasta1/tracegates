package jan.yuboingirirobobobiko.item;

import jan.yuboingirirobobobiko.ModRegistries;
import jan.yuboingirirobobobiko.block.BusBlock;
import jan.yuboingirirobobobiko.block.TraceBlock;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
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
        CustomModelData data = oldStack.get(DataComponents.CUSTOM_MODEL_DATA);
        int oldState = 0;
        if (data != null) oldState = data.value();
        int newState = oldState + direction;
        
        if (newState == 16) return InteractionResultHolder.pass(new ItemStack(ModRegistries.Items.WRITE_TRACE));
        if (newState == -1 && block instanceof TraceBlock) return InteractionResultHolder.pass(new ItemStack(ModRegistries.Items.READ_TRACE));
        
        if (newState > maxValue) newState = 0;
        if (newState < 0) newState = maxValue;
        
        ItemStack stack = new ItemStack(this);
        stack.setCount(oldStack.getCount());
        stack.set(DataComponents.CUSTOM_NAME, oldStack.get(DataComponents.CUSTOM_NAME));
        if (block instanceof TraceBlock) TraceBlock.setItemStack(stack, newState);
        if (block instanceof BusBlock) BusBlock.setItemStack(stack, newState);
        return InteractionResultHolder.pass(stack);
    }
}
