package observatory.observations.mixin.shiny;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Invoker("adjustMovementForCollisions")
    public Vec3d observations$adjustMovementForCollisions(Vec3d movement);
}
