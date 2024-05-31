package me.xleiten.rebalance.core.mixins.world.item.fireball_throw;

import me.xleiten.rebalance.core.game.world.entity.SmallExplosiveFireballEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireChargeItem.class)
public abstract class MixinFireChargeItem extends Item
{
    protected MixinFireChargeItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var result = super.use(world, user, hand);
        if (result.getResult() == ActionResult.PASS) {
            user.getStackInHand(hand).decrementUnlessCreative(1, user);
            var vel = user.getRotationVector().normalize().multiply(2 * user.getVelocity().length());
            var pos = user.getEyePos();
            var entity = new SmallExplosiveFireballEntity(world, user, pos.x, pos.y - user.getDimensions(user.getPose()).width() / 4, pos.z, 2);
            entity.deflect(ProjectileDeflection.REDIRECTED, user, user, true);
            user.getItemCooldownManager().set(this, me.xleiten.rebalance.Settings.FIREBALL_THROWING__COOLDOWN.value());
            world.spawnEntity(entity);
        }
        return result;
    }
}
