package dev.rainimator.mod;

import com.afoxxvi.asteorbar.overlay.FabricGuiRegistry;
import dev.rainimator.mod.compat.asteorbar.ManaHud;
import dev.rainimator.mod.compat.trinkets.TrinketsRegistry;
import dev.rainimator.mod.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(EnvType.CLIENT)
public class RainimatorModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RainimatorEntities.registerEntityRenderers();
        RainimatorKeybindings.init();
        RainimatorModels.registerLayerDefinitions();
        RainimatorParticles.registerParticles();
        RainimatorScreenHandlers.registerGui();
        RainimatorSkulls.clientInit();

        TrinketsRegistry.registerClient();

        if (FabricLoader.getInstance().isModLoaded("asteorbar"))
            FabricGuiRegistry.REGISTRY.add(FabricGuiRegistry.REGISTRY.size() - 1, new ManaHud());
    }
}
