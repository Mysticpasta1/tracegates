package jan.yuboingirirobobobiko.trace;

import jan.yuboingirirobobobiko.ModRegistries;
import jan.yuboingirirobobobiko.TraceGates;
import jan.yuboingirirobobobiko.block.BaseBaseTraceBlock;
import jan.yuboingirirobobobiko.block.BaseTraceBlock;
import jan.yuboingirirobobobiko.block.BusBlock;
import jan.yuboingirirobobobiko.block.TraceBlock;
import jan.yuboingirirobobobiko.block.gate.LedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GlobalTraceNetwork extends SavedData {
    private final MinecraftServer server;
    private ArrayList<TraceObject> traces;
    private ArrayList<Tunnel> tunnels;
    private boolean updateTracesLockout;
    private HashMap<TraceObject, Boolean> updateQueue;
    private HashMap<TraceObject, Boolean> visualUpdateQueue;
    private int tickRate;
    private boolean ticking;
    private int queuedStepTicks;
    private int ticksToRun;
    private boolean visualUpdatesEnabled;
    
    public GlobalTraceNetwork(MinecraftServer server) {
        this.server = server;
        traces = new ArrayList<>();
        tunnels = new ArrayList<>();
        updateTracesLockout = false;
        updateQueue = new HashMap<>();
        visualUpdateQueue = new HashMap<>();
        tickRate = 60;
        ticking = true;
        queuedStepTicks = 0;
        ticksToRun = 0;
        visualUpdatesEnabled = true;
        
        server.overworld().getDataStorage().computeIfAbsent(new Factory<>(() -> this, this::load, DataFixTypes.LEVEL), "trace_network");
    }
    
    private Level decodeLevel(String id) {
        if (id.isEmpty()) {
            return server.overworld();
        } else {
            return server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(id)));
        }
    }
    private String encodeLevel(Level level) {
        return level.dimension().location().toString();
    }
    
    private TraceObject loadTrace(CompoundTag tag) {
        Level level = decodeLevel(tag.getString("level"));
        
        int typeOrdinal = Math.clamp(tag.getInt("type"), 0, TraceType.values().length - 1);
        TraceType type = TraceType.values()[typeOrdinal];
        
        TraceObject trace = new TraceObject(level, type, tag.getInt("colour"), tag.getBoolean("state"),
                tag.getBoolean("prev_state"), tag.getBoolean("redstone_update"));
        boolean noBlocks = true;
        for (Tag anyPos : tag.getList("blocks", Tag.TAG_INT_ARRAY)) {
            noBlocks = false;
            if (anyPos instanceof IntArrayTag posTag) {
                BlockPos pos = new BlockPos(posTag.get(0).getAsInt(), posTag.get(1).getAsInt(), posTag.get(2).getAsInt());
                if (level.getBlockState(pos).getBlock() instanceof BaseBaseTraceBlock) {
                    trace.addBlock(pos);
                } else {
                    TraceGates.LOGGER.error("Block ({}) was in trace, but not in world!", pos.toShortString());
                }
            }
        }
        if (noBlocks) TraceGates.LOGGER.error("Loaded empty trace!");
        
        return trace;
    }
    
    private GlobalTraceNetwork load(CompoundTag tag, HolderLookup.Provider provider) {
        TraceGates.LOGGER.info("Loading NBT Data");
        
        tickRate = Math.max(tag.getInt("tick_rate"), 1);
        ticking = tag.getBoolean("ticking");
        visualUpdatesEnabled = tag.getBoolean("visual_updates");
        
        traces = new ArrayList<>();
        updateQueue = new HashMap<>();
        visualUpdateQueue = new HashMap<>();
        // First add all traces and updates
        for (Tag anyTag : tag.getList("traces", Tag.TAG_COMPOUND)) {
            if (anyTag instanceof CompoundTag traceTag) {
                TraceObject trace = loadTrace(traceTag);
                traces.add(trace);
                if (traceTag.getBoolean("update_queued")) updateQueue.put(trace, true);
            }
        }
        // Then connect them
        int traceIndex = 0;
        for (Tag anyTag : tag.getList("traces", Tag.TAG_COMPOUND)) {
            if (anyTag instanceof CompoundTag traceTag) {
                for (Tag anyConnection : traceTag.getList("connections", Tag.TAG_COMPOUND)) {
                    if (anyConnection instanceof CompoundTag connectionTag) {
                        int otherIndex = connectionTag.getInt("index");
                        if (otherIndex >= traces.size()) {
                            TraceGates.LOGGER.error("Trace {} Connects to invalid trace index {}!", traceIndex, otherIndex);
                            continue;
                        }
                        TraceObject otherTrace = traces.get(otherIndex);
                        TraceConnection.Direction direction = connectionTag.getBoolean("output") ?
                                TraceConnection.Direction.OUTPUT : TraceConnection.Direction.INPUT;
                        traces.get(traceIndex).addConnection(new TraceConnection(otherTrace, direction));
                    } else {
                        TraceGates.LOGGER.error("Trace {} has wrong connection tag type!", traceIndex);
                    }
                }
            } else {
                TraceGates.LOGGER.error("Trace {} has wrong tag type!", traceIndex);
            }
            traceIndex++;
        }
        
        traces.removeIf(trace -> trace.blocks.isEmpty());
        
        // Load tunnels
        tunnels = new ArrayList<>();
        for (Tag anyTag : tag.getList("tunnels", Tag.TAG_COMPOUND)) {
            if (anyTag instanceof CompoundTag tunnelTag) {
                String levelId = tunnelTag.getString("level");
                int[] posTag = tunnelTag.getIntArray("position");
                tunnels.add(new Tunnel(decodeLevel(levelId), new BlockPos(posTag[0], posTag[1], posTag[2])));
            }
        }
        
        return this;
    }
    
    private CompoundTag saveTrace(TraceObject trace) {
        CompoundTag tag = new CompoundTag();
        tag.putString("level", encodeLevel(trace.level));
        tag.putInt("type", trace.type.ordinal());
        if (trace.colour != 0) tag.putInt("colour", trace.colour);
        tag.putBoolean("state", trace.state);
        tag.putBoolean("prev_state", trace.latchPrevState);
        tag.putBoolean("redstone_update", trace.hasRedstoneUpdate);
        tag.putBoolean("update_queued", updateQueue.containsKey(trace));
        
        ListTag blocks = new ListTag();
        for (BlockPos pos : trace.blocks) {
            blocks.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()}));
        }
        tag.put("blocks", blocks);
        
        ListTag connections = new ListTag();
        for (TraceConnection connection : trace.connections) {
            CompoundTag connectionTag = new CompoundTag();
            int index = traces.indexOf(connection.trace());
            if (index == -1) {
                TraceGates.LOGGER.error("Trace {} has nonexistent reference!", traces.indexOf(trace));
                continue;
            }
            connectionTag.putInt("index", index);
            connectionTag.putBoolean("output", connection.direction() == TraceConnection.Direction.OUTPUT);
            connections.add(connectionTag);
        }
        tag.put("connections", connections);
        
        return tag;
    }
    
    @Override @NotNull
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        TraceGates.LOGGER.info("Saving NBT Data");
        
        tag.putInt("tick_rate", tickRate);
        tag.putBoolean("ticking", ticking);
        tag.putBoolean("visual_updates", visualUpdatesEnabled);
        
        // Save traces
        ListTag traceList = new ListTag();
        for (TraceObject trace : traces) {
            traceList.add(saveTrace(trace));
        }
        tag.put("traces", traceList);
        
        // Save tunnels
        ListTag tunnelList = new ListTag();
        for (Tunnel tunnel : tunnels) {
            CompoundTag tunnelTag = new CompoundTag();
            tunnelTag.putString("level", encodeLevel(tunnel.level()));
            BlockPos pos = tunnel.position();
            tunnelTag.putIntArray("position", new int[]{pos.getX(), pos.getY(), pos.getZ()});
            tunnelList.add(tunnelTag);
        }
        tag.put("tunnels", tunnelList);
        
        return tag;
    }
    
    /* =========================================================== */
    
    public TraceObject getTraceAtIndex(int index) {
        return traces.get(index);
    }
    
    public TraceObject getTraceAtPos(Level level, BlockPos pos) {
        for (TraceObject trace : traces) {
            if (trace.inLevel(level)) {
                for (BlockPos tracePos : trace.blocks) {
                    if (tracePos.equals(pos)) {
                        return trace;
                    }
                }
            }
        }
        
        return null;
    }
    
    private void connectTo(TraceObject traceFrom, TraceObject traceTo) {
        if (traceFrom == traceTo) {
            TraceGates.LOGGER.error("Connected trace to itself!");
            return;
        }
        if (traceFrom.type == traceTo.type) {
            TraceGates.LOGGER.error("Connected traces of same type!");
            TraceGates.LOGGER.error(traceFrom.type.name());
            return;
        }
        // Cannot read outputs from LEDs or Lamps
        if (traceFrom.type == TraceType.LED || traceFrom.type == TraceType.LAMP) return;
        boolean hasOutput = false;
        for (TraceConnection connection : traceFrom.connections) {
            if (connection.direction() == TraceConnection.Direction.OUTPUT && connection.trace() == traceTo) {
                hasOutput = true;
                break;
            }
        }
        if (!hasOutput) {
            traceFrom.addConnection(new TraceConnection(traceTo, TraceConnection.Direction.OUTPUT));
            queueUpdate(traceTo);
        }
        boolean hasInput = false;
        for (TraceConnection connection : traceTo.connections) {
            if (connection.direction() == TraceConnection.Direction.INPUT && connection.trace() == traceFrom) {
                hasInput = true;
                break;
            }
        }
        if (!hasInput) traceTo.addConnection(new TraceConnection(traceFrom, TraceConnection.Direction.INPUT));
    }
    
    /*
        Fixes trace connections and updates to point from the old trace to the new trace
     */
    private void migrateTrace(TraceObject oldTrace, TraceObject newTrace) {
        if (oldTrace == newTrace) TraceGates.LOGGER.error("Migrated trace to itself!");
        if (oldTrace.type != newTrace.type) TraceGates.LOGGER.error("Trace migrated to wrong type!");
        for (TraceConnection connection : oldTrace.connections) {
            ArrayList<TraceConnection> oppositeConnections = connection.trace().connections;
            for (int i = oppositeConnections.size() - 1; i >= 0; i--) {
                TraceConnection oppositeConnection = oppositeConnections.get(i);
                if (oppositeConnection.trace() == oldTrace) {
                    oppositeConnections.remove(i);
                    if (oppositeConnection.direction() == TraceConnection.Direction.OUTPUT) {
                        connectTo(connection.trace(), newTrace);
                    } else {
                        connectTo(newTrace, connection.trace());
                    }
                }
            }
        }
        if (updateQueue.containsKey(oldTrace)) {
            updateQueue.remove(oldTrace);
            queueUpdate(newTrace);
        }
        newTrace.blocks.addAll(oldTrace.blocks);
        for (TraceConnection connection : oldTrace.connections) {
            if (connection.direction() == TraceConnection.Direction.OUTPUT) {
                connectTo(newTrace, connection.trace());
            } else {
                connectTo(connection.trace(), newTrace);
            }
        }
        removeTrace(oldTrace);
    }
    
    /*
        Removes trace connections and updates that point to this trace
     */
    private void removeTrace(TraceObject trace) {
        for (TraceConnection connection : trace.connections) {
            ArrayList<TraceConnection> oppositeConnections = connection.trace().connections;
            oppositeConnections.removeIf(oppositeConnection -> oppositeConnection.trace() == trace);
        }
        updateQueue.remove(trace);
        propagateUpdates(updateQueue, trace);
        traces.remove(trace);
        if (trace.type == TraceType.BUS) {
            for (BlockPos blockPos : trace.blocks) {
                for (Direction dir : Direction.values()) {
                    BlockPos pos = blockPos.relative(dir);
                    if (trace.level.getBlockState(pos).getBlock() instanceof BaseBaseTraceBlock) {
                        TraceObject connectedTrace = getTraceAtPos(trace.level, pos);
                        if (connectedTrace != null && connectedTrace != trace) {
                            rebuildTrace(connectedTrace);
                        }
                    }
                }
            }
        }
    }
    
    public void mergeTrace(Level level, TraceObject trace, BlockPos otherPos, Direction direction, BlockState state, boolean canCross) {
        BlockState otherState = level.getBlockState(otherPos);
        TraceObject otherTrace = getTraceAtPos(level, otherPos);
        if (otherTrace == null) {
            if (!canCross) return;
            if (otherState.is(ModRegistries.Blocks.CROSS)) {
                BlockPos pos = otherPos.relative(direction);
                mergeTrace(level, trace, pos, null, state, false);
                return;
            }
            if (otherState.is(ModRegistries.Blocks.TUNNEL)) {
                BlockPos pos = crossTunnel(level, otherPos, direction, state);
                if (pos == null) return;
                mergeTrace(level, trace, pos, null, state, false);
                return;
            }
            return;
        }
        if (otherTrace == trace) return;
        
        if (trace.type == TraceType.BUS) {
            if (otherTrace.type != TraceType.BUS) {
                mergeBusTrace(trace, otherTrace, otherState);
                return;
            }
        } else if (otherTrace.type == TraceType.BUS) {
            mergeBusTrace(otherTrace, trace, state);
            return;
        }
        
        if (otherTrace.type != trace.type) {
            if (state.is(ModRegistries.Blocks.READ_TRACE)) connectTo(trace, otherTrace);
            if (state.is(ModRegistries.Blocks.WRITE_TRACE)) connectTo(otherTrace, trace);
            if (otherState.is(ModRegistries.Blocks.READ_TRACE)) connectTo(otherTrace, trace);
            if (otherState.is(ModRegistries.Blocks.WRITE_TRACE)) connectTo(trace, otherTrace);
            return;
        }
        
        migrateTrace(otherTrace, trace);
        
        if (trace.type == TraceType.BUS) {
            for (BlockPos blockPos : trace.blocks) {
                for (Direction dir : Direction.values()) {
                    BlockPos pos = blockPos.relative(dir);
                    BlockState connectedState = level.getBlockState(pos);
                    if (connectedState.getBlock() instanceof BaseBaseTraceBlock) {
                        TraceObject connectedTrace = getTraceAtPos(level, pos);
                        if (connectedTrace != null && connectedTrace != trace && connectedTrace.type != TraceType.BUS) {
                            mergeBusTrace(trace, connectedTrace, connectedState);
                        }
                    }
                }
            }
        }
    }
    
    private void mergeBusTrace(TraceObject bus, TraceObject trace, BlockState state) {
        if (bus.type != TraceType.BUS) {
            TraceGates.LOGGER.error("Merged with non-bus!");
            return;
        }
        
        for (BlockPos blockPos : bus.blocks) {
            for (Direction direction : Direction.values()) {
                BlockPos pos = blockPos.relative(direction);
                if (isSameTrace(bus.level, pos, state)) {
                    TraceObject otherTrace = getTraceAtPos(bus.level, pos);
                    if (otherTrace != null && otherTrace != trace) {
                        migrateTrace(otherTrace, trace);
                        traceFullUpdate(trace);
                        return;
                    }
                }
            }
        }
        
        connectTo(trace, bus);
    }
    
    private int getDirectionDistance(BlockPos from, BlockPos to, Direction direction) {
        if ((direction.getStepX() == 0 && from.getX() != to.getX()) ||
            (direction.getStepY() == 0 && from.getY() != to.getY()) ||
            (direction.getStepZ() == 0 && from.getZ() != to.getZ())) return -1;
        
        switch (direction) {
            case SOUTH -> { return to.getZ() - from.getZ(); }
            case NORTH -> { return from.getZ() - to.getZ(); }
            case EAST ->  { return to.getX() - from.getX(); }
            case WEST ->  { return from.getX() - to.getX(); }
            case UP ->    { return to.getY() - from.getY(); }
            case DOWN ->  { return from.getY() - to.getY(); }
            default -> {
                TraceGates.LOGGER.error("Pretty sure there's no such direction as {}", direction.getName());
                return -1;
            }
        }
    }
    
    private boolean isSameTrace(BlockState state, BlockState otherState, BlockPos pos) {
        Block block = state.getBlock();
        if (!otherState.is(block)) return false;
        if (block instanceof TraceBlock) {
            return state.getValue(TraceBlock.COLOUR).equals(otherState.getValue(TraceBlock.COLOUR));
        } else if (block instanceof BusBlock) {
            return state.getValue(BusBlock.COLOUR).equals(otherState.getValue(BusBlock.COLOUR));
        } else {
            return true;
        }
    }
    
    private boolean isSameTrace(Level level, BlockPos otherPos, BlockState state) {
        return isSameTrace(state, level.getBlockState(otherPos), otherPos);
    }
    
    private BlockPos crossTunnel(Level level, BlockPos pos, Direction direction, BlockState state) {
        int closestDistance = Integer.MAX_VALUE;
        BlockPos otherPos = null;
        boolean isInvalid = false;
        for (Tunnel tunnel : tunnels) {
            if (tunnel.level().dimensionType() == level.dimensionType()) {
                BlockPos tunnelPos = tunnel.position();
                int distance = getDirectionDistance(pos, tunnelPos, direction);
                if (distance > 0 && distance < closestDistance) {
                    // If we run into the start of another tunnel in this direction with the same block type
                    // then it's invalid, but we still count it to know if it's the closest match.
                    boolean invalidTunnel = isSameTrace(level, tunnelPos.relative(direction.getOpposite()), state);
                    if (invalidTunnel || isSameTrace(level, tunnelPos.relative(direction), state)) {
                        closestDistance = distance;
                        otherPos = tunnel.position();
                        isInvalid = invalidTunnel;
                    }
                }
            }
        }
        if (otherPos == null || isInvalid) return null;
        return otherPos.relative(direction);
    }
    
    public void placeTunnel(Level level, BlockPos pos) {
        tunnels.add(new Tunnel(level, pos));
        
        for (Direction direction : Direction.values()) {
            BlockPos tracePos = pos.relative(direction.getOpposite());
            TraceObject trace = getTraceAtPos(level, tracePos);
            if (trace != null) {
                mergeTrace(level, trace, pos, direction, level.getBlockState(tracePos), true);
                traceFullUpdate(trace);
            }
        }
    }
    public void removeTunnel(Level level, BlockPos pos) {
        tunnels.removeIf(tunnel -> tunnel.level().dimensionType() == level.dimensionType() && tunnel.position().equals(pos));
        
        for (Direction direction : Direction.values()) {
            TraceObject trace = getTraceAtPos(level, pos.relative(direction));
            if (trace != null) {
                rebuildTrace(trace);
            }
        }
    }

    public void placeTraceBlock(Level level, BlockPos pos) {
        if (updateTracesLockout) return;
        setDirty();
        
        BlockState state = level.getBlockState(pos);
        int colour = 0;
        if (state.is(ModRegistries.Blocks.TRACE)) colour = state.getValue(TraceBlock.COLOUR);
        if (state.is(ModRegistries.Blocks.BUS)) colour = state.getValue(BusBlock.COLOUR);
        TraceObject trace = new TraceObject(level, TraceType.getBlockType(state.getBlock()), colour);
        trace.addBlock(pos);
        for (Direction direction : Direction.values()) {
            mergeTrace(level, trace, pos.relative(direction), direction, state, true);
        }
        
        traceFullUpdate(trace);
        traces.add(trace);
    }
    
    public void rebuildTrace(TraceObject trace) {
        removeTrace(trace);
        for (BlockPos addPos : trace.blocks) {
            placeTraceBlock(trace.level, addPos);
        }
    }
    
    public void removeTraceBlock(Level level, BlockPos pos) {
        if (updateTracesLockout) return;
        
        TraceObject trace = getTraceAtPos(level, pos);
        if (trace == null) return;
        
        setDirty();
        
        trace.blocks.removeIf(pos::equals);
        if (trace.blocks.isEmpty()) {
            removeTrace(trace);
            return;
        }
        
        int NumConnections = 0;
        for (Direction direction : Direction.values()) {
            BlockState state = level.getBlockState(pos.relative(direction));
            if (state.is(level.getBlockState(pos).getBlock()) || state.is(ModRegistries.Blocks.BUS)) {
                NumConnections++;
            }
        }
        if (NumConnections < 2) return;
        
        rebuildTrace(trace);
    }
    
    public static boolean anyInputActive(TraceObject trace) {
        for (TraceConnection connection : trace.connections) {
            if (connection.direction() == TraceConnection.Direction.INPUT) {
                if (connection.trace().state) return true;
            }
        }
        return false;
    }
    
    public static boolean allInputsActive(TraceObject trace) {
        boolean hasInput = false;
        for (TraceConnection connection : trace.connections) {
            if (connection.direction() == TraceConnection.Direction.INPUT) {
                if (!connection.trace().state) return false;
                hasInput = true;
            }
        }
        return hasInput;
    }
    
    public static int numInputsActive(TraceObject trace) {
        int num = 0;
        for (TraceConnection connection : trace.connections) {
            if (connection.direction() == TraceConnection.Direction.INPUT) {
                if (connection.trace().state) num++;
            }
        }
        return num;
    }
    
    public void queueUpdate(TraceObject trace) {
        updateQueue.put(trace, true);
    }
    
    public void traceFullUpdate(TraceObject trace) {
        updateQueue.put(trace, true);
        propagateUpdates(updateQueue, trace);
        trace.forceVisualUpdate = true;
    }
    
    private void propagateUpdates(HashMap<TraceObject, Boolean> queue, TraceObject trace) {
        for (TraceConnection connection : trace.connections) {
            if (connection.direction() == TraceConnection.Direction.OUTPUT) {
                queue.put(connection.trace(), true);
            }
        }
    }
    
    private void tickTraces(HashMap<TraceObject, Boolean> visualUpdates) {
        HashMap<TraceObject, Boolean> updates = updateQueue;
        updateQueue = new HashMap<>();
        
        HashMap<TraceObject, Boolean> traceUpdateQueue = new HashMap<>();
        for (TraceObject trace : updates.keySet()) {
            if (trace.type == TraceType.TRACE) {
                traceUpdateQueue.put(trace, true);
            }
        }
        
        // Gates pass
        for (TraceObject trace : updates.keySet()) {
            // Skip traces in gate pass, but don't warn (Traces can be added to update queue)
            if (trace.type == TraceType.TRACE) continue;
            TraceDefinitions.tickTrace(trace);
        }
        for (TraceObject trace : updates.keySet()) {
            if (trace.type == TraceType.TRACE) continue;
            TraceDefinitions.cleanupLatchTick(trace);
            if (trace.stateBuffer != trace.state || trace.forceVisualUpdate) {
                trace.state = trace.stateBuffer;
                propagateUpdates(traceUpdateQueue, trace);
                visualUpdates.put(trace, !trace.forceVisualUpdate);
            }
        }
        
        // Traces pass
        for (TraceObject trace : traceUpdateQueue.keySet()) {
            if (trace.type != TraceType.TRACE) {
                if (trace.type != TraceType.BUS) TraceGates.LOGGER.warn("Trace type {} ticked in trace pass", trace.type.name());
                continue;
            }
            TraceDefinitions.tickTrace(trace);
            if (trace.hasRedstoneUpdate) {
                trace.hasRedstoneUpdate = false;
                boolean found = false;
                for (BlockPos pos : trace.blocks) {
                    if (trace.level.getBlockState(pos).is(ModRegistries.Blocks.WRITE_TRACE)) {
                        for (Direction direction : Direction.values()) {
                            if (trace.level.hasSignal(pos, direction)) {
                                trace.setState(true);
                                found = true;
                                break;
                            }
                        }
                    }
                    if (found) break;
                }
            }
        }
        for (TraceObject trace : traceUpdateQueue.keySet()) {
            if (trace.type != TraceType.TRACE) continue;
            if (trace.stateBuffer != trace.state || trace.forceVisualUpdate) {
                trace.state = trace.stateBuffer;
                propagateUpdates(updateQueue, trace);
                visualUpdates.put(trace, !trace.forceVisualUpdate);
            }
        }
        
        // Update buses
        Set<TraceObject> updateSet = new HashSet<>();
        updateSet.addAll(updateQueue.keySet());
        updateSet.addAll(traceUpdateQueue.keySet());
        for (TraceObject trace : updateSet) {
            if (trace.type == TraceType.BUS) {
                TraceDefinitions.tickTrace(trace);
                if (trace.stateBuffer != trace.state || trace.forceVisualUpdate) {
                    trace.state = trace.stateBuffer;
                    visualUpdates.put(trace, !trace.forceVisualUpdate);
                }
            }
        }
    }
    
    private void updateVisuals(Level level, HashMap<TraceObject, Boolean> updates) {
        if (!visualUpdatesEnabled) return;
        
        updateTracesLockout = true;
        
        HashMap<TraceObject, Boolean> newQueue = new HashMap<>();
        for (TraceObject trace : updates.keySet()) {
            // If any traces have changed, this will trigger
            setDirty();
            if (trace.inLevel(level)) {
                // A queue value of false means always update
                if (trace.type == TraceType.LED) {
                    int newState = numInputsActive(trace) % 8;
                    if (!updates.get(trace) || level.getBlockState(trace.blocks.getFirst()).getValue(LedBlock.STATE) != newState) {
                        for (BlockPos pos : trace.blocks) {
                            BlockState state = level.getBlockState(pos);
                            level.setBlock(pos, state.setValue(LedBlock.STATE, newState), 2);
                        }
                    }
                } else if (trace.state != trace.prevStateVisual || !updates.get(trace)) {
                    trace.prevStateVisual = trace.state;
                    boolean hasInvalidBlock = false;
                    for (BlockPos pos : trace.blocks) {
                        BlockState state = level.getBlockState(pos);
                        if (!(state.getBlock() instanceof BaseTraceBlock)) {
                            hasInvalidBlock = true;
                            continue;
                        }
                        int flags = 2;
                        if (state.is(ModRegistries.Blocks.READ_TRACE)) flags = 3;
                        level.setBlock(pos, state.setValue(BaseTraceBlock.LIT, trace.state), flags);
                    }
                    if (hasInvalidBlock) trace.blocks.removeIf(pos -> !(level.getBlockState(pos).getBlock() instanceof BaseTraceBlock));
                }
            } else {
                // Add to the next level tick, to let other dimensions update
                newQueue.put(trace, updates.get(trace));
            }
        }
        visualUpdateQueue = newQueue;
        
        updateTracesLockout = false;
    }
    
    public void tick(ServerLevel level) {
        // Only tick if we're in the overworld, since traces are global
        if (level.dimensionType() == server.overworld().dimensionType()) {
            for (int i = 0; i < queuedStepTicks; i++) {
                tickTraces(visualUpdateQueue);
            }
            queuedStepTicks = 0;
            
            if (level.tickRateManager().runsNormally() && ticking) {
                ticksToRun += tickRate;
                while (ticksToRun > 0) {
                    ticksToRun -= 20;
                    tickTraces(visualUpdateQueue);
                }
            }
        }
        
        updateVisuals(level, visualUpdateQueue);
    }
    
    public void debugUpdateAll() {
        for (TraceObject trace : traces) {
            queueUpdate(trace);
        }
    }
    
    public void debugRegenerate() {
        ArrayList<TraceObject> clone = new ArrayList<>(traces);
        for (TraceObject trace : clone) {
            if (traces.contains(trace)) {
                rebuildTrace(trace);
            }
        }
    }
    
    public void setVisualUpdatesEnabled(boolean enabled) {
        visualUpdatesEnabled = enabled;
    }
    
    public int debugGetTraceIndex(TraceObject trace) {
        return traces.indexOf(trace);
    }
    
    public int getTraceCount() { return traces.size(); }
    
    public int getTickRate() { return tickRate; }
    public void setTickRate(int speed) {
        if (tickRate != speed) {
            tickRate = speed;
            setDirty();
        }
    }
    public void setTicking(boolean ticking) {
        if (this.ticking != ticking) {
            this.ticking = ticking;
            setDirty();
        }
    }
    public void setTicksToRun(int toRun) { ticksToRun = toRun; }
}
