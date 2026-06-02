package jan.yuboingirirobobobiko.trace;

import jan.yuboingirirobobobiko.block.*;
import jan.yuboingirirobobobiko.block.gate.*;
import net.minecraft.world.level.block.Block;

public enum TraceType {
    TRACE,
    BUS,
    BUFFER_GATE,
    NOT_GATE,
    OR_GATE,
    NOR_GATE,
    AND_GATE,
    NAND_GATE,
    XOR_GATE,
    XNOR_GATE,
    LATCH_GATE_OFF,
    LATCH_GATE_ON,
    LED,
    LAMP;
    
    public static TraceType getBlockType(Block block) {
        if (block instanceof TraceBlock) return TraceType.TRACE;
        if (block instanceof BusBlock) return TraceType.BUS;
        if (block instanceof WriteTraceBlock) return TraceType.TRACE;
        if (block instanceof ReadTraceBlock) return TraceType.TRACE;
        if (block instanceof BufferGateBlock) return TraceType.BUFFER_GATE;
        if (block instanceof NotGateBlock) return TraceType.NOT_GATE;
        if (block instanceof OrGateBlock) return TraceType.OR_GATE;
        if (block instanceof NorGateBlock) return TraceType.NOR_GATE;
        if (block instanceof AndGateBlock) return TraceType.AND_GATE;
        if (block instanceof NandGateBlock) return TraceType.NAND_GATE;
        if (block instanceof XorGateBlock) return TraceType.XOR_GATE;
        if (block instanceof XnorGateBlock) return TraceType.XNOR_GATE;
        if (block instanceof LatchGateOffBlock) return TraceType.LATCH_GATE_OFF;
        if (block instanceof LatchGateOnBlock) return TraceType.LATCH_GATE_ON;
        if (block instanceof LedBlock) return TraceType.LED;
        if (block instanceof LampBlock) return TraceType.LAMP;
        throw new RuntimeException("No trace type for block " + block.toString());
    }
    
    public static boolean onByDefault(TraceType type) {
        if (type == NOT_GATE) return true;
        if (type == NOR_GATE) return true;
        if (type == NAND_GATE) return true;
        if (type == XNOR_GATE) return true;
        if (type == LATCH_GATE_ON) return true;
        return false;
    }
}
