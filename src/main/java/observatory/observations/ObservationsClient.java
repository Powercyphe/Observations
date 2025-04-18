package observatory.observations;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import observatory.observations.client.particle.ShockwaveParticle;
import observatory.observations.client.shader.HeatShader;
import observatory.observations.client.shader.ShittyShader;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

public class ObservationsClient implements ClientModInitializer {

	public static final DefaultParticleType SHOCKWAVE = Registry.register(Registries.PARTICLE_TYPE, Observations.id("shockwave"), FabricParticleTypes.simple());

	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(SHOCKWAVE, ShockwaveParticle.Factory::new);

		PostProcessHandler.addInstance(HeatShader.INSTANCE);
		PostProcessHandler.addInstance(ShittyShader.INSTANCE);
	}
}