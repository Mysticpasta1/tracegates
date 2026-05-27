package jan.yuboingirirobobobiko;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import jan.yuboingirirobobobiko.block.*;
import jan.yuboingirirobobobiko.block.gate.*;
import jan.yuboingirirobobobiko.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ModRegistries {
    public static class Blocks {
        private static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(TraceGates.MOD_ID, Registries.BLOCK);

        private static RegistrySupplier<Block> registerBlock(String name, Supplier<Block> block) {
            return REGISTRY.register(ResourceLocation.fromNamespaceAndPath(TraceGates.MOD_ID, name), block);
        }
        
        public static final RegistrySupplier<Block> TRACE = registerBlock("trace",
                () -> new TraceBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> BUS = registerBlock("bus",
                () -> new BusBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> WRITE_TRACE = registerBlock("write_trace",
                () -> new WriteTraceBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> READ_TRACE = registerBlock("read_trace",
                () -> new ReadTraceBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> BUFFER_GATE = registerBlock("buffer_gate",
                () -> new BufferGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> NOT_GATE = registerBlock("not_gate",
                () -> new NotGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> OR_GATE = registerBlock("or_gate",
                () -> new OrGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> NOR_GATE = registerBlock("nor_gate",
                () -> new NorGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> AND_GATE = registerBlock("and_gate",
                () -> new AndGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> NAND_GATE = registerBlock("nand_gate",
                () -> new NandGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> XOR_GATE = registerBlock("xor_gate",
                () -> new XorGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> XNOR_GATE = registerBlock("xnor_gate",
                () -> new XnorGateBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> LATCH_GATE_OFF = registerBlock("latch_gate_off",
                () -> new LatchGateOffBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> LATCH_GATE_ON = registerBlock("latch_gate_on",
                () -> new LatchGateOnBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> LED = registerBlock("led",
                () -> new LedBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> LAMP = registerBlock("lamp",
                () -> new LampBlock(BlockBehaviour.Properties.of().lightLevel(LampBlock::getLight)));
        public static final RegistrySupplier<Block> CROSS = registerBlock("cross",
                () -> new CrossBlock(BlockBehaviour.Properties.of()));
        public static final RegistrySupplier<Block> TUNNEL = registerBlock("tunnel",
                () -> new TunnelBlock(BlockBehaviour.Properties.of()));
    }

    public static class Items {
        private static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(TraceGates.MOD_ID, Registries.ITEM);

        private static RegistrySupplier<Item> registerItem(String name, Supplier<Item> item) {
            return REGISTRY.register(ResourceLocation.fromNamespaceAndPath(TraceGates.MOD_ID, name), item);
        }
        
        public static final RegistrySupplier<Item> TRACE = registerItem("trace",
                () -> new TraceCycleItem(Blocks.TRACE.get(), new Item.Properties(), 15));
        public static final RegistrySupplier<Item> BUS = registerItem("bus",
                () -> new TraceCycleItem(Blocks.BUS.get(), new Item.Properties(), 5));
        public static final RegistrySupplier<Item> WRITE_TRACE = registerItem("write_trace",
                () -> new WriteTraceItem(Blocks.WRITE_TRACE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> READ_TRACE = registerItem("read_trace",
                () -> new ReadTraceItem(Blocks.READ_TRACE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> BUFFER_GATE = registerItem("buffer_gate",
                () -> new GateCycleItem(Blocks.BUFFER_GATE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> NOT_GATE = registerItem("not_gate",
                () -> new GateCycleItem(Blocks.NOT_GATE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> OR_GATE = registerItem("or_gate",
                () -> new GateCycleItem(Blocks.OR_GATE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> NOR_GATE = registerItem("nor_gate",
                () -> new GateCycleItem(Blocks.NOR_GATE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> AND_GATE = registerItem("and_gate",
                () -> new GateCycleItem(Blocks.AND_GATE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> NAND_GATE = registerItem("nand_gate",
                () -> new GateCycleItem(Blocks.NAND_GATE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> XOR_GATE = registerItem("xor_gate",
                () -> new GateCycleItem(Blocks.XOR_GATE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> XNOR_GATE = registerItem("xnor_gate",
                () -> new GateCycleItem(Blocks.XNOR_GATE.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> LATCH_GATE_OFF = registerItem("latch_gate_off",
                () -> new GateCycleItem(Blocks.LATCH_GATE_OFF.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> LATCH_GATE_ON = registerItem("latch_gate_on",
                () -> new GateCycleItem(Blocks.LATCH_GATE_ON.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> LED = registerItem("led",
                () -> new LedItem(Blocks.LED.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> LAMP = registerItem("lamp",
                () -> new LampItem(Blocks.LAMP.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> CROSS = registerItem("cross",
                () -> new CrossItem(Blocks.CROSS.get(), new Item.Properties()));
        public static final RegistrySupplier<Item> TUNNEL = registerItem("tunnel",
                () -> new TunnelItem(Blocks.TUNNEL.get(), new Item.Properties()));
    }
    
    public static class Tabs {
        private static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(TraceGates.MOD_ID, Registries.CREATIVE_MODE_TAB);
        
        public static final RegistrySupplier<CreativeModeTab> TRACES = REGISTRY.register("traces_tab",
                () -> CreativeTabRegistry.create(builder -> builder
                        .title(Component.translatable("category.traces_tab"))
                        .icon(() -> new ItemStack(Items.BUFFER_GATE))
                        .displayItems((itemDisplayParameters, output) -> {
                            for (int i = 0; i <= 15; i++) {
                                ItemStack stack = new ItemStack(Items.TRACE);
                                TraceBlock.setItemStack(stack, i);
                                output.accept(stack);
                            }
                            output.accept(new ItemStack(Items.WRITE_TRACE));
                            output.accept(new ItemStack(Items.READ_TRACE));
                            output.accept(new ItemStack(Items.CROSS));
                            output.accept(new ItemStack(Items.TUNNEL));
                            for (int i = 0; i <= 5; i++) {
                                ItemStack stack = new ItemStack(Items.BUS);
                                BusBlock.setItemStack(stack, i);
                                output.accept(stack);
                            }
                            output.accept(new ItemStack(Items.BUFFER_GATE));
                            output.accept(new ItemStack(Items.NOT_GATE));
                            output.accept(new ItemStack(Items.OR_GATE));
                            output.accept(new ItemStack(Items.NOR_GATE));
                            output.accept(new ItemStack(Items.AND_GATE));
                            output.accept(new ItemStack(Items.NAND_GATE));
                            output.accept(new ItemStack(Items.XOR_GATE));
                            output.accept(new ItemStack(Items.XNOR_GATE));
                            output.accept(new ItemStack(Items.LATCH_GATE_OFF));
                            output.accept(new ItemStack(Items.LATCH_GATE_ON));
                            output.accept(new ItemStack(Items.LED));
                            output.accept(new ItemStack(Items.LAMP));
                        }).build()));
    }

    public static void register() {
        Blocks.REGISTRY.register();
        Items.REGISTRY.register();
        Tabs.REGISTRY.register();
    }
}
