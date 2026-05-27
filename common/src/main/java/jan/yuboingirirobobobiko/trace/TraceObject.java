package jan.yuboingirirobobobiko.trace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class TraceObject {
    protected Level level;
    public TraceType type;
    protected int colour;
    protected ArrayList<BlockPos> blocks;
    protected ArrayList<TraceConnection> connections;
    protected boolean latchPrevState;
    protected boolean prevStateVisual;
    public boolean state;
    protected boolean stateBuffer;
    protected boolean forceVisualUpdate;
    protected boolean clicked;
    protected boolean hasRedstoneUpdate;
    
    public TraceObject(Level level, TraceType type, int colour, boolean state, boolean prevState, boolean redstoneUpdate) {
        this.level = level;
        this.type = type;
        this.colour = colour;
        blocks = new ArrayList<>();
        connections = new ArrayList<>();
        this.latchPrevState = prevState;
        prevStateVisual = state;
        this.state = state;
        stateBuffer = state;
        forceVisualUpdate = false;
        clicked = false;
        hasRedstoneUpdate = redstoneUpdate;
    }
    
    public TraceObject(Level level, TraceType type, int colour) {
        this(level, type, colour, TraceType.onByDefault(type), TraceType.onByDefault(type), false);
    }
    
    public boolean inLevel(Level level) { return this.level.dimensionType() == level.dimensionType(); }
    
    public void addBlock(BlockPos pos) {
        blocks.add(pos);
    }
    public void addConnection(TraceConnection connection) {
        connections.add(connection);
    }
    
    protected void setState(boolean state) {
        stateBuffer = state;
    }
    
    public void click() { clicked = true; }
    public void setRedstoneUpdate() { hasRedstoneUpdate = true; }
    
    public int numOutputs() {
        int num = 0;
        for (TraceConnection connection : connections) {
            if (connection.direction() == TraceConnection.Direction.OUTPUT) num++;
        }
        return num;
    }
    public int numInputs() {
        int num = 0;
        for (TraceConnection connection : connections) {
            if (connection.direction() == TraceConnection.Direction.INPUT) num++;
        }
        return num;
    }
    public int numBlocks() {
        return blocks.size();
    }
}
