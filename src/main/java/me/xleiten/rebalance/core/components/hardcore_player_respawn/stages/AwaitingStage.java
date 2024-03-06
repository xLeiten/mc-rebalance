package me.xleiten.rebalance.core.components.hardcore_player_respawn.stages;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import me.xleiten.rebalance.api.config.Option;
import me.xleiten.rebalance.api.config.types.ArrayType;
import me.xleiten.rebalance.api.game.world.text_display.StaticTextDisplay;
import me.xleiten.rebalance.api.game.world.staged_process.Either;
import me.xleiten.rebalance.api.game.world.staged_process.Stage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static me.xleiten.rebalance.Settings.HARDCORE_PLAYER_RESPAWN;

public final class AwaitingStage extends Stage<RitualContext>
{
    private static final Option<Integer> PLAYER_SEARCH_COOLDOWN = HARDCORE_PLAYER_RESPAWN.option("player-search-cooldown", 10);
    private static final Option<IngredientsInfo> INGREDIENTS = HARDCORE_PLAYER_RESPAWN.option("Ingredients",
            new IngredientsInfo()
                    .addIngredient(Items.ROTTEN_FLESH, 10)
                    .addIngredient(Items.BONE, 3)
    );

    private final float playerWidth = context.player.getDimensions(context.player.getPose()).width * 0.8F;
    private final RegistryEntry<SoundEvent> TELEPORT_SOUND = RegistryEntry.of(SoundEvents.ENTITY_ENDERMAN_TELEPORT);

    private int searchPlayerTicks = PLAYER_SEARCH_COOLDOWN.getValue();
    private final ImmutableMap<RitualIngredient, StaticTextDisplay> ingredients;
    private ServerPlayerEntity lastActive;

    public AwaitingStage(@NotNull RitualContext context) {
        super(context);
        this.ingredients = createIngredientDisplays(context.position.add(0, 0.8, 0));
        context.deadBody.onInteract((player, hand) -> {
            if (hand == Hand.MAIN_HAND) {
                var stack = player.getStackInHand(hand);
                getIngredient(stack.getItem()).ifPresentOrElse(ingredientEntry -> {
                    var ingredient = ingredientEntry.getKey();
                    lastActive = (ServerPlayerEntity) player;
                    context.world.playSound(player, context.position.x, context.position.y, context.position.z, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.PLAYERS);
                    context.world.spawnParticles(ParticleTypes.COMPOSTER, context.position.x, context.position.y, context.position.z, context.random.nextBetween(5, 7), 0.95, 0.95, 0.95, 2);
                    ingredient.increment();
                    ingredientEntry.getValue().setText(ingredient.buildInfo());
                    player.swingHand(Hand.MAIN_HAND);
                    ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityAnimationS2CPacket(player, EntityAnimationS2CPacket.SWING_MAIN_HAND));
                    stack.decrement(1);
                }, () -> {
                    /*player.openHandledScreen(new SimpleNamedScreenHandlerFactory(new ScreenHandlerFactory() {
                        @Override
                        public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player2) {
                            return new DeadBodyInventoryInterface(context.deadBody, playerInventory, syncId);
                        }
                    }, Text.of("Инвентарь " + context.player.getName().getString())));*/
                });
            }
        });
    }

    @Override
    public @NotNull Either<Stage<RitualContext>, Boolean> tick() {
        checkDiedPlayerState();
        if (ingredients.entrySet().stream().allMatch(ingredientEntry -> ingredientEntry.getKey().isSatisfied())) {
            if (lastActive != null) {
                return Either.left(new SummoningStage(context, lastActive));
            } else {
                if (searchPlayerTicks-- <= 0) {
                    lastActive = (ServerPlayerEntity) context.world.getClosestPlayer(context.deadBody, RitualContext.RADIUS.getValue());
                    searchPlayerTicks = PLAYER_SEARCH_COOLDOWN.getValue();
                }
            }
        }
        return Either.left(this);
    }

    @Override
    public void onComplete() {
        ingredients.forEach((ingredient, display) -> display.discard());
    }

    private ImmutableMap<RitualIngredient, StaticTextDisplay> createIngredientDisplays(Vec3d position) {
        var map = ImmutableMap.<RitualIngredient, StaticTextDisplay>builder();
        var startPos = position;
        for (Map.Entry<Identifier, Integer> ingredientInfo : INGREDIENTS.getValue().getInfo()) {
            Item item = Registries.ITEM.get(ingredientInfo.getKey());
            if (item == Items.AIR) continue;
            var ingredient = new RitualIngredient(item, ingredientInfo.getValue());
            var display = new StaticTextDisplay(context.world, startPos, ingredient.buildInfo());
            display.setBillboardMode(DisplayEntity.BillboardMode.CENTER);
            startPos = startPos.add(0, 0.3, 0);
            map.put(ingredient, display);
            display.spawn();
        }
        return map.build();
    }

    private Optional<Map.Entry<RitualIngredient, StaticTextDisplay>> getIngredient(Item item) {
        for (Map.Entry<RitualIngredient, StaticTextDisplay> entry: ingredients.entrySet()) {
            if (entry.getKey().item == item)
                return Optional.of(entry);
        }
        return Optional.empty();
    }

    private void checkDiedPlayerState() {
        if (!context.player.isSpectator()) context.player.changeGameMode(GameMode.SPECTATOR);
        checkDistance();
        checkNoClip();
    }

    private void checkDistance() {
        var current = context.player.getEyePos();
        var distance = context.player.squaredDistanceTo(context.position);
        if (distance >= 100) {
            context.player.setVelocity(new Vec3d(context.position.x - current.x, context.position.y - current.y, context.position.z - current.z).normalize().multiply(distance > 400 ? 1 : (distance / 400)));
            context.player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(context.player));
        }
    }

    private void checkNoClip() {
        Box box = Box.of(context.player.getEyePos().subtract(0, 0.1, 0), playerWidth, playerWidth, playerWidth);
        if (BlockPos.stream(box).anyMatch(pos -> {
            BlockState blockState = context.world.getBlockState(pos);
            return !blockState.isAir() &&
                    blockState.shouldSuffocate(context.world, pos) &&
                    VoxelShapes.matchesAnywhere(blockState.getCollisionShape(context.world, pos).offset(pos.getX(), pos.getY(), pos.getZ()), VoxelShapes.cuboid(box), BooleanBiFunction.AND);
        })) {
            context.player.teleport(context.position.x, context.position.y, context.position.z);
            context.player.networkHandler.sendPacket(new PlaySoundS2CPacket(TELEPORT_SOUND, SoundCategory.PLAYERS, context.position.x, context.position.y, context.position.z, 1f, 1f, context.world.getRandom().nextLong()));
        }
    }

    private static final class RitualIngredient
    {
        public final Item item;
        public final int amountNeeded;

        private int current = 0;

        RitualIngredient(Item item, int amountNeeded)
        {
            this.item = item;
            this.amountNeeded = amountNeeded;
        }

        public Text buildInfo() {
            return Text.literal("")
                    .append(item.getName().copy().formatted(Formatting.GOLD))
                    .append(": ")
                    .append(Text.literal(getCurrent() + "").formatted(isSatisfied() ? Formatting.GREEN : Formatting.RED))
                    .append("/")
                    .append(Text.literal(amountNeeded + "").formatted(Formatting.GREEN));
        }

        public boolean isSatisfied() {
            return current >= amountNeeded;
        }

        public int getCurrent() {
            return current;
        }

        public void increment() {
            if (this.current < amountNeeded)
                this.current++;
        }
    }

    public static final class IngredientsInfo
    {
        private final Map<Identifier, Integer> info;

        IngredientsInfo() {
            this.info = new HashMap<>();
        }

        IngredientsInfo(Map<Identifier, Integer> ingredients)
        {
            this.info = ingredients;
        }

        public IngredientsInfo addIngredient(Item item, int amount) {
            this.info.putIfAbsent(Registries.ITEM.getId(item), amount);
            return this;
        }

        public Set<Map.Entry<Identifier, Integer>> getInfo() {
            return info.entrySet();
        }

        public static class Type extends ArrayType<IngredientsInfo>
        {

            @Override
            public @NotNull String getTypeKey() {
                return "II";
            }

            @Override
            public @NotNull Class<IngredientsInfo> getTypeClass() {
                return IngredientsInfo.class;
            }

            @Override
            public @NotNull IngredientsInfo deserialize(JsonArray array, JsonDeserializationContext context) {
                Map<Identifier, Integer> data = new HashMap<>();
                for (JsonElement element: array) {
                    if (element instanceof JsonObject object)
                        data.put(Identifier.tryParse(object.get("item").getAsString()), object.get("amount").getAsInt());
                }
                return new IngredientsInfo(data);
            }

            @NotNull
            @Override
            public JsonArray serialize(IngredientsInfo ingredients, JsonSerializationContext context) {
                var array = new JsonArray();
                for (Map.Entry<Identifier, Integer> entry: ingredients.info.entrySet()) {
                    var object = new JsonObject();
                    object.addProperty("item", entry.getKey().toString());
                    object.addProperty("amount", entry.getValue());
                    array.add(object);
                }
                return array;
            }
        }
    }
}
