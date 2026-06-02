package jan.yuboingirirobobobiko.item;

import jan.yuboingirirobobobiko.ModRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class CrossItem extends BlockItem {
    public CrossItem(Block block, Properties properties) {
        super(block, properties);
    }
    
    @Override @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = new ItemStack(ModRegistries.Items.TUNNEL.get());
        stack.setCount(player.getItemInHand(usedHand).getCount());
        return InteractionResultHolder.pass(stack);
    }
}
