package com.davigj.blasted_barrens.core.mixin;

import com.davigj.blasted_barrens.core.BlastedBarrens;
import com.davigj.blasted_barrens.core.registry.BBBiomes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import java.util.Map;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LocateCommand.class})
public abstract class LocateCommandMixin {
    @Unique
    private static final Map<ResourceLocation, ResourceLocation> bb$BIOME_TO_STRUCTURE = Map.of(
            BBBiomes.BLASTED_BARRENS.location(),
            ResourceLocation.fromNamespaceAndPath(BlastedBarrens.MOD_ID, "blasted_barrens")
    );
    @Unique
    private static boolean bb$locatingIslandStructure = false;
    @Unique
    private static final DynamicCommandExceptionType ERROR_CANT_BE_LOCATED = new DynamicCommandExceptionType((id) -> Component.translatable("commands.blasted_barrens.locate.island", new Object[]{id}));

    @Unique
    private static ResourceLocation bb$id(String path) {
        return ResourceLocation.fromNamespaceAndPath(BlastedBarrens.MOD_ID, path);
    }

    @Inject(at = {@At("HEAD")}, method = {"locateBiome"}, cancellable = true)
    private static void S$locateBiome(CommandSourceStack source, ResourceOrTagArgument.Result<Biome> biome, CallbackInfoReturnable<Integer> cir) {
        Either<Holder.Reference<Biome>, HolderSet.Named<Biome>> either = biome.unwrap();
        if (either.left().isPresent()) {
            bb$redirectToStructure(source, ((Holder.Reference)either.left().get()).key().location(), cir);
        } else {
            if (either.right().isPresent()) {
                Registry<Biome> registry = source.getServer().registryAccess().registryOrThrow(Registries.BIOME);
                HolderSet.Named<Biome> holders = (HolderSet.Named)registry.getTag(((HolderSet.Named)either.right().get()).key()).orElse((Object)null);
                if (holders == null) {
                    return;
                }

                for(Holder<Biome> holder : holders) {
                    Optional<ResourceKey<Biome>> key = holder.unwrapKey();
                    if (!key.isEmpty() && bb$redirectToStructure(source, ((ResourceKey)key.get()).location(), cir)) {
                        return;
                    }
                }
            }

        }
    }

    @Unique
    private static boolean bb$redirectToStructure(CommandSourceStack source, ResourceLocation biomeId, CallbackInfoReturnable<Integer> cir) {
        ResourceLocation structureId = (ResourceLocation)bb$BIOME_TO_STRUCTURE.get(biomeId);
        if (structureId == null) {
            return false;
        } else {
            CommandDispatcher<CommandSourceStack> dispatcher = source.getServer().getCommands().getDispatcher();
            bb$locatingIslandStructure = true;

            try {
                cir.setReturnValue(dispatcher.execute(dispatcher.parse("locate structure " + String.valueOf(structureId), source)));
            } catch (CommandSyntaxException var9) {
                source.sendFailure(Component.translatable("commands.blasted_barrens.locate.island_not_found", new Object[]{Component.literal("[" + String.valueOf(structureId) + "]").withStyle(ChatFormatting.RED)}));
                cir.setReturnValue(0);
            } finally {
                bb$locatingIslandStructure = false;
            }

            cir.cancel();
            return true;
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"locateStructure"})
    private static void S$locateStructure(CommandSourceStack source, ResourceOrTagKeyArgument.Result<Structure> structure, CallbackInfoReturnable<Integer> cir) throws CommandSyntaxException {
        if (!bb$locatingIslandStructure) {
            Either<ResourceKey<Structure>, TagKey<Structure>> either = structure.unwrap();
            if (either.left().isPresent()) {
                ResourceLocation id = ((ResourceKey)either.left().get()).location();
            }
        }
    }
}
