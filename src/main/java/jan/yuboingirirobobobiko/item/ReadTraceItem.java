package jan.yuboingirirobobobiko.item;

import jan.yuboingirirobobobiko.ModRegistries;
import jan.yuboingirirobobobiko.block.TraceBlock;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ReadTraceItem extends BlockItem {
    public ReadTraceItem(Block block, Properties properties) {
        super(block, properties);
    }
    
    @Override @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isSecondaryUseActive()) {
            ItemStack stack = new ItemStack(ModRegistries.Items.WRITE_TRACE.get());
            stack.setCount(player.getItemInHand(usedHand).getCount());
            return InteractionResultHolder.pass(stack);
        } else {
            ItemStack stack = new ItemStack(ModRegistries.Items.TRACE.get());
            stack.setCount(player.getItemInHand(usedHand).getCount());
            TraceBlock.setItemStack(stack, 0);
            return InteractionResultHolder.pass(stack);
        }
    }
}
