package de.tobiasvonmassow.kaffees_dual_ride.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHorseEntity.class)
public abstract class MixinAbstractHorseEntity extends AnimalEntity {
	protected MixinAbstractHorseEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	private float lastAngryAnimationProgress;

	@Inject(at = @At(value = "TAIL"), method = "updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V")
	public void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater, CallbackInfo ci) {
		AbstractHorseEntity self = (AbstractHorseEntity) (Object) this;
		int n = self.getPassengerList().indexOf(passenger);
		float offset = 0;
		if (self.getPassengerList().size() > 1) {
			offset = n == 0 ? 0.2f : -0.6f;
		}
		Vec3d vec3d = (new Vec3d(offset, 0.0, 0.0)).rotateY(-self.getYaw() * 0.017453292F - 1.5707964F);
		float f = MathHelper.sin(self.bodyYaw * 0.017453292F);
		float g = MathHelper.cos(self.bodyYaw * 0.017453292F);
		float h = 0.7F * this.lastAngryAnimationProgress;
		float i = 0.15F * this.lastAngryAnimationProgress;
		positionUpdater.accept(passenger, self.getX() + vec3d.x + (double) (h * f), self.getY() + self.getMountedHeightOffset() + passenger.getHeightOffset() + (double) i, self.getZ() + vec3d.z - (double) (h * g));
	}

	@ModifyExpressionValue(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AbstractHorseEntity;hasPassengers()Z"))
	public boolean interactMob(boolean original, PlayerEntity player, Hand hand) {
		return original && !this.canAddPassenger(player);
	}
}
