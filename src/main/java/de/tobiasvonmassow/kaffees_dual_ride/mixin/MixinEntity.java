package de.tobiasvonmassow.kaffees_dual_ride.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class MixinEntity {
	@ModifyReturnValue(method = "canAddPassenger", at = @At("RETURN"))
	private boolean acceptMultiplePassengers(boolean original) {
		Entity self = (Entity) (Object) this;
		return original || self instanceof AbstractHorseEntity horse && horse.getPassengerList().size() < 2;
	}
}
