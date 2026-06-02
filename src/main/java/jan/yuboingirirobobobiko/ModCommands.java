package jan.yuboingirirobobobiko;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import jan.yuboingirirobobobiko.trace.TraceObject;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;

public class ModCommands {
    protected static void buildCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tracegates")
                .requires(source -> source.hasPermission(Commands.LEVEL_MODERATORS))
                .then(
                        Commands.literal("tick")
                        .then(
                                Commands.literal("rate")
                                .executes(ModCommands::executeRateGet)
                                .then(
                                        Commands.literal("set")
                                        .then(
                                                Commands.argument("rate", IntegerArgumentType.integer(1, 2000))
                                                .executes(ModCommands::executeRateSet)
                                        )
                                )
                        ).then(
                                Commands.literal("step")
                                .executes(ModCommands::executeRateStepOnce)
                                .then(
                                        Commands.argument("ticks", IntegerArgumentType.integer(1, 200))
                                        .executes(ModCommands::executeRateStep)
                                )
                        ).then(
                                Commands.literal("freeze")
                                .executes(ModCommands::executeRateFreeze)
                        ).then(
                                Commands.literal("unfreeze")
                                .executes(ModCommands::executeRateUnfreeze)
                        )
                ).then(
                        Commands.literal("debug")
                        .executes(ModCommands::executeDebug)
                        .then(
                                Commands.literal("trace")
                                .then(
                                        Commands.literal("block")
                                        .then(
                                                Commands.argument("position", BlockPosArgument.blockPos())
                                                .executes(ModCommands::executeDebugTraceBlock)
                                        )
                                ).then(
                                        Commands.literal("index")
                                        .then(
                                                Commands.argument("index", IntegerArgumentType.integer(0))
                                                .executes(ModCommands::executeDebugTraceIndex)
                                        )
                                )
                        ).then(
                                Commands.literal("updateall")
                                .executes(ModCommands::executeDebugUpdateAll)
                        ).then(
                                Commands.literal("regenerate")
                                .executes(ModCommands::executeDebugRegenerate)
                        ).then(
                                Commands.literal("visualupdates")
                                .then(
                                        Commands.argument("enabled", BoolArgumentType.bool())
                                        .executes(ModCommands::executeDebugVisualUpdates)
                                )
                        )
                )
        );
    }
    
    private static int executeDebug(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.translatable("tracegates.command.debug", TraceGates.traceNetwork.getTraceCount()), false);
        return 1;
    }
    
    private static int executeDebugTraceBlock(CommandContext<CommandSourceStack> context) {
        TraceObject trace = TraceGates.traceNetwork.getTraceAtPos(context.getSource().getLevel(),
                BlockPosArgument.getBlockPos(context, "position"));
        if (trace == null) {
            context.getSource().sendFailure(Component.translatable("tracegates.command.debug.trace.fail.position"));
            return 0;
        }
        return executeDebugTrace(context, trace);
    }
    private static int executeDebugTraceIndex(CommandContext<CommandSourceStack> context) {
        int index = IntegerArgumentType.getInteger(context, "index");
        if (index >= TraceGates.traceNetwork.getTraceCount()) {
            context.getSource().sendFailure(Component.translatable("tracegates.command.debug.trace.fail.index"));
            return 0;
        }
        return executeDebugTrace(context, TraceGates.traceNetwork.getTraceAtIndex(index));
    }
    private static int executeDebugTrace(CommandContext<CommandSourceStack> context, TraceObject trace) {
        context.getSource().sendSuccess(() -> Component.translatable("tracegates.command.debug.trace",
                TraceGates.traceNetwork.debugGetTraceIndex(trace), trace.state, trace.type.name(),
                trace.numOutputs(), trace.numInputs(), trace.numBlocks()), false);
        return 1;
    }
    
    private static int executeDebugUpdateAll(CommandContext<CommandSourceStack> context) {
        TraceGates.traceNetwork.debugUpdateAll();
        return 1;
    }
    
    private static int executeDebugRegenerate(CommandContext<CommandSourceStack> context) {
        TraceGates.traceNetwork.debugRegenerate();
        return 1;
    }
    
    private static int executeDebugVisualUpdates(CommandContext<CommandSourceStack> context) {
        TraceGates.traceNetwork.setVisualUpdatesEnabled(BoolArgumentType.getBool(context, "enabled"));
        return 1;
    }
    
    
    private static int executeRateGet(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.translatable("tracegates.command.rate.get", TraceGates.traceNetwork.getTickRate()), false);
        return 1;
    }
    
    private static int executeRateSet(CommandContext<CommandSourceStack> context) {
        TraceGates.traceNetwork.setTickRate(IntegerArgumentType.getInteger(context, "rate"));
        return 1;
    }
    
    private static int executeRateFreeze(CommandContext<CommandSourceStack> context) {
        TraceGates.traceNetwork.setTicking(false);
        return 1;
    }
    private static int executeRateUnfreeze(CommandContext<CommandSourceStack> context) {
        TraceGates.traceNetwork.setTicking(true);
        return 1;
    }
    
    private static int executeRateStepOnce(CommandContext<CommandSourceStack> context) {
        TraceGates.traceNetwork.setTicksToRun(1);
        return 1;
    }
    private static int executeRateStep(CommandContext<CommandSourceStack> context) {
        TraceGates.traceNetwork.setTicksToRun(IntegerArgumentType.getInteger(context, "ticks"));
        return 1;
    }
}