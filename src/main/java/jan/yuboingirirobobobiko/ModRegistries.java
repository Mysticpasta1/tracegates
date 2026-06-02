package jan.yuboingirirobobobiko;

import jan.yuboingirirobobobiko.block.*;
import jan.yuboingirirobobobiko.block.gate.*;
import jan.yuboingirirobobobiko.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModRegistries {
    public static class Blocks {
        private static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, TraceGates.MOD_ID);

        private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
            return REGISTRY.register(name, block);
        }

        public static final RegistryObject<TraceBlock> TRACE = registerBlock("trace",
                () -> new TraceBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<BusBlock> BUS = registerBlock("bus",
                () -> new BusBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<WriteTraceBlock> WRITE_TRACE = registerBlock("write_trace",
                () -> new WriteTraceBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<ReadTraceBlock> READ_TRACE = registerBlock("read_trace",
                () -> new ReadTraceBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<BufferGateBlock> BUFFER_GATE = registerBlock("buffer_gate",
                () -> new BufferGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<NotGateBlock> NOT_GATE = registerBlock("not_gate",
                () -> new NotGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<OrGateBlock> OR_GATE = registerBlock("or_gate",
                () -> new OrGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<NorGateBlock> NOR_GATE = registerBlock("nor_gate",
                () -> new NorGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<AndGateBlock> AND_GATE = registerBlock("and_gate",
                () -> new AndGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<NandGateBlock> NAND_GATE = registerBlock("nand_gate",
                () -> new NandGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<XorGateBlock> XOR_GATE = registerBlock("xor_gate",
                () -> new XorGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<XnorGateBlock> XNOR_GATE = registerBlock("xnor_gate",
                () -> new XnorGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<LatchGateOffBlock> LATCH_GATE_OFF = registerBlock("latch_gate_off",
                () -> new LatchGateOffBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<LatchGateOnBlock> LATCH_GATE_ON = registerBlock("latch_gate_on",
                () -> new LatchGateOnBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<LedBlock> LED = registerBlock("led",
                () -> new LedBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<LampBlock> LAMP = registerBlock("lamp",
                () -> new LampBlock(BlockBehaviour.Properties.of().lightLevel(LampBlock::getLight)));
        public static final RegistryObject<CrossBlock> CROSS = registerBlock("cross",
                () -> new CrossBlock(BlockBehaviour.Properties.of()));
        public static final RegistryObject<TunnelBlock> TUNNEL = registerBlock("tunnel",
                () -> new TunnelBlock(BlockBehaviour.Properties.of()));
    }

    public static class Items {
        private static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, TraceGates.MOD_ID);

        private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item) {
            return REGISTRY.register(name, item);
        }

        public static final RegistryObject<TraceCycleItem> TRACE = registerItem("trace",
                () -> new TraceCycleItem(Blocks.TRACE.get(), new Item.Properties(), 15));
        public static final RegistryObject<TraceCycleItem> BUS = registerItem("bus",
                () -> new TraceCycleItem(Blocks.BUS.get(), new Item.Properties(), 5));
        public static final RegistryObject<WriteTraceItem> WRITE_TRACE = registerItem("write_trace",
                () -> new WriteTraceItem(Blocks.WRITE_TRACE.get(), new Item.Properties()));
        public static final RegistryObject<ReadTraceItem> READ_TRACE = registerItem("read_trace",
                () -> new ReadTraceItem(Blocks.READ_TRACE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> BUFFER_GATE = registerItem("buffer_gate",
                () -> new GateCycleItem(Blocks.BUFFER_GATE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> NOT_GATE = registerItem("not_gate",
                () -> new GateCycleItem(Blocks.NOT_GATE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> OR_GATE = registerItem("or_gate",
                () -> new GateCycleItem(Blocks.OR_GATE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> NOR_GATE = registerItem("nor_gate",
                () -> new GateCycleItem(Blocks.NOR_GATE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> AND_GATE = registerItem("and_gate",
                () -> new GateCycleItem(Blocks.AND_GATE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> NAND_GATE = registerItem("nand_gate",
                () -> new GateCycleItem(Blocks.NAND_GATE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> XOR_GATE = registerItem("xor_gate",
                () -> new GateCycleItem(Blocks.XOR_GATE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> XNOR_GATE = registerItem("xnor_gate",
                () -> new GateCycleItem(Blocks.XNOR_GATE.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> LATCH_GATE_OFF = registerItem("latch_gate_off",
                () -> new GateCycleItem(Blocks.LATCH_GATE_OFF.get(), new Item.Properties()));
        public static final RegistryObject<GateCycleItem> LATCH_GATE_ON = registerItem("latch_gate_on",
                () -> new GateCycleItem(Blocks.LATCH_GATE_ON.get(), new Item.Properties()));
        public static final RegistryObject<LedItem> LED = registerItem("led",
                () -> new LedItem(Blocks.LED.get(), new Item.Properties()));
        public static final RegistryObject<LampItem> LAMP = registerItem("lamp",
                () -> new LampItem(Blocks.LAMP.get(), new Item.Properties()));
        public static final RegistryObject<CrossItem> CROSS = registerItem("cross",
                () -> new CrossItem(Blocks.CROSS.get(), new Item.Properties()));
        public static final RegistryObject<TunnelItem> TUNNEL = registerItem("tunnel",
                () -> new TunnelItem(Blocks.TUNNEL.get(), new Item.Properties()));
    }

    public static class Tabs {
        private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TraceGates.MOD_ID);

        public static final RegistryObject<CreativeModeTab> TRACES_TAB = REGISTRY.register("traces_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("category.traces_tab"))
                        .icon(() -> new ItemStack(Items.BUFFER_GATE.get()))
                        .displayItems((itemDisplayParameters, output) -> {
                            for (int i = 0; i <= 15; i++) {
                                ItemStack stack = new ItemStack(Items.TRACE.get());
                                TraceBlock.setItemStack(stack, i);
                                output.accept(stack);
                            }
                            output.accept(new ItemStack(Items.WRITE_TRACE.get()));
                            output.accept(new ItemStack(Items.READ_TRACE.get()));
                            output.accept(new ItemStack(Items.CROSS.get()));
                            output.accept(new ItemStack(Items.TUNNEL.get()));
                            for (int i = 0; i <= 5; i++) {
                                ItemStack stack = new ItemStack(Items.BUS.get());
                                BusBlock.setItemStack(stack, i);
                                output.accept(stack);
                            }
                            output.accept(new ItemStack(Items.BUFFER_GATE.get()));
                            output.accept(new ItemStack(Items.NOT_GATE.get()));
                            output.accept(new ItemStack(Items.OR_GATE.get()));
                            output.accept(new ItemStack(Items.NOR_GATE.get()));
                            output.accept(new ItemStack(Items.AND_GATE.get()));
                            output.accept(new ItemStack(Items.NAND_GATE.get()));
                            output.accept(new ItemStack(Items.XOR_GATE.get()));
                            output.accept(new ItemStack(Items.XNOR_GATE.get()));
                            output.accept(new ItemStack(Items.LATCH_GATE_OFF.get()));
                            output.accept(new ItemStack(Items.LATCH_GATE_ON.get()));
                            output.accept(new ItemStack(Items.LED.get()));
                            output.accept(new ItemStack(Items.LAMP.get()));
                        }).build());
    }

    public static void register(IEventBus modEventBus) {
        Blocks.REGISTRY.register(modEventBus);
        Items.REGISTRY.register(modEventBus);
        Tabs.REGISTRY.register(modEventBus);
    }
}
