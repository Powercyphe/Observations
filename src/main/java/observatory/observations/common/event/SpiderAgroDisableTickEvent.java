package observatory.observations.common.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import observatory.observations.common.component.TraitComponent;
import observatory.observations.common.registry.Trait;

import java.util.List;

public class SpiderAgroDisableTickEvent {
    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                if (TraitComponent.get(player).hasTrait(Trait.BUG_LIKE_APPEARANCE)) {

                    List<SpiderEntity> nearbySpiders = world.getEntitiesByClass(
                            SpiderEntity.class,
                            player.getBoundingBox().expand(16),
                            spider -> true
                    );

                    for (SpiderEntity spider : nearbySpiders) {
                        if (spider.getTarget() == player) {
                            spider.setTarget(null);
                        }
                    }
                }
            }
        });
    }
}
