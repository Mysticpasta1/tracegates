package jan.yuboingirirobobobiko.item;

import jan.yuboingirirobobobiko.ModRegistries;
import jan.yuboingirirobobobiko.TraceGates;
import jan.yuboingirirobobobiko.trace.TraceType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class GateCycleItem extends BlockItem {
    private final Block block;

    public GateCycleItem(Block block, Properties properties) {
        super(block, properties);
        this.block = block;
    }

    @Override @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack oldStack = player.getItemInHand(usedHand);

        Item item;
        if (player.isSecondaryUseActive()) {
            item = prevItem(block);
        } else {
            item = nextItem(block);
        }
        if (item == null) return InteractionResultHolder.pass(oldStack);
        ItemStack stack = new ItemStack(item);
        stack.setCount(oldStack.getCount());
        return InteractionResultHolder.pass(stack);
    }

    private static Item nextItem(Block block) {
        if (block == null) {
            TraceGates.LOGGER.error("Null block in gate cycle");
            return null;
        }
        switch (TraceType.getBlockType(block)) {
            case BUFFER_GATE -> { return ModRegistries.Items.NOT_GATE.get(); }
            case NOT_GATE -> { return ModRegistries.Items.OR_GATE.get(); }
            case OR_GATE -> { return ModRegistries.Items.NOR_GATE.get(); }
            case NOR_GATE -> { return ModRegistries.Items.AND_GATE.get(); }
            case AND_GATE -> { return ModRegistries.Items.NAND_GATE.get(); }
            case NAND_GATE -> { return ModRegistries.Items.XOR_GATE.get(); }
            case XOR_GATE -> { return ModRegistries.Items.XNOR_GATE.get(); }
            case XNOR_GATE -> { return ModRegistries.Items.LATCH_GATE_OFF.get(); }
            case LATCH_GATE_OFF -> { return ModRegistries.Items.LATCH_GATE_ON.get(); }
            case LATCH_GATE_ON -> { return ModRegistries.Items.BUFFER_GATE.get(); }
            case TRACE, BUS, LED, LAMP -> TraceGates.LOGGER.error("Invalid block in gate cycle");
        }
        return null;
    }
    private static Item prevItem(Block block) {
        if (block == null) {
            TraceGates.LOGGER.error("Null block in gate cycle");
            return null;
        }
        switch (TraceType.getBlockType(block)) {
            case BUFFER_GATE -> { return ModRegistries.Items.LATCH_GATE_ON.get(); }
            case NOT_GATE -> { return ModRegistries.Items.BUFFER_GATE.get(); }
            case OR_GATE -> { return ModRegistries.Items.NOT_GATE.get(); }
            case NOR_GATE -> { return ModRegistries.Items.OR_GATE.get(); }
            case AND_GATE -> { return ModRegistries.Items.NOR_GATE.get(); }
            case NAND_GATE -> { return ModRegistries.Items.AND_GATE.get(); }
            case XOR_GATE -> { return ModRegistries.Items.NAND_GATE.get(); }
            case XNOR_GATE -> { return ModRegistries.Items.XOR_GATE.get(); }
            case LATCH_GATE_OFF -> { return ModRegistries.Items.XNOR_GATE.get(); }
            case LATCH_GATE_ON -> { return ModRegistries.Items.LATCH_GATE_OFF.get(); }
            case TRACE, BUS, LED, LAMP -> TraceGates.LOGGER.error("Invalid block in gate cycle");
        }
        return null;
    }
}
