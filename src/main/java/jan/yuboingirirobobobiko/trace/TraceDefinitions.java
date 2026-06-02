package jan.yuboingirirobobobiko.trace;

import jan.yuboingirirobobobiko.TraceGates;

public class TraceDefinitions {
    public static void tickTrace(TraceObject trace) {
        switch (trace.type) {
            case LED -> trace.forceVisualUpdate = true;
            case TRACE, BUS, BUFFER_GATE, OR_GATE, LAMP -> tickOrGate(trace);
            case NOT_GATE, NOR_GATE -> tickNorGate(trace);
            case AND_GATE -> tickAndGate(trace);
            case NAND_GATE -> tickNandGate(trace);
            case XOR_GATE -> tickXorGate(trace);
            case XNOR_GATE -> tickXnorGate(trace);
            case LATCH_GATE_OFF, LATCH_GATE_ON -> tickLatchGate(trace);
            default -> TraceGates.LOGGER.error("Invalid trace type ticked: {}", trace.type);
        }
    }
    
    
    private static void tickOrGate(TraceObject trace) {
        trace.setState(GlobalTraceNetwork.anyInputActive(trace));
    }
    private static void tickNorGate(TraceObject trace) {
        trace.setState(!GlobalTraceNetwork.anyInputActive(trace));
    }
    
    private static void tickAndGate(TraceObject trace) {
        trace.setState(GlobalTraceNetwork.allInputsActive(trace));
    }
    private static void tickNandGate(TraceObject trace) {
        trace.setState(!GlobalTraceNetwork.allInputsActive(trace));
    }
    
    private static void tickXorGate(TraceObject trace) {
        trace.setState(GlobalTraceNetwork.numInputsActive(trace) % 2 == 1);
    }
    private static void tickXnorGate(TraceObject trace) {
        trace.setState(GlobalTraceNetwork.numInputsActive(trace) % 2 == 0);
    }
    
    private static void tickLatchGate(TraceObject trace) {
        int inputs = 0;
        for (TraceConnection connection : trace.connections) {
            if (connection.direction() == TraceConnection.Direction.INPUT) {
                TraceObject inputTrace = connection.trace();
                if (inputTrace.state && !inputTrace.latchPrevState) inputs++;
            }
        }
        if (trace.clicked) {
            trace.clicked = false;
            inputs++;
        }
        if (inputs % 2 == 1) trace.setState(!trace.state);
    }
    public static void cleanupLatchTick(TraceObject trace) {
        if (trace.type != TraceType.LATCH_GATE_OFF && trace.type != TraceType.LATCH_GATE_ON) return;
        for (TraceConnection connection : trace.connections) {
            if (connection.direction() == TraceConnection.Direction.INPUT) {
                TraceObject inputTrace = connection.trace();
                inputTrace.latchPrevState = inputTrace.state;
            }
        }
    }
}
