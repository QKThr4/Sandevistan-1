package com.sandevistanmod.client;

import com.sandevistanmod.SandevistanMod;
import com.sandevistanmod.net.ActionPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = SandevistanMod.MODID, value = Dist.CLIENT)
public final class ClientGameEvents {
    private ClientGameEvents() {}

    @SubscribeEvent
    public static void tick(ClientTickEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        while (ModKeys.SCREEN.consumeClick()) {
            if (mc.screen == null) mc.setScreen(new SandevistanScreen());
        }
        while (ModKeys.TOGGLE.consumeClick()) {
            PacketDistributor.sendToServer(new ActionPayload(0));
        }
    }

    /**
     * Com a velocidade absurda o campo de visao abriria ao maximo o tempo todo: com o Sandevistan ligado
     * ele fica num valor fixo e suave.
     */
    @SubscribeEvent
    public static void fov(ComputeFovModifierEvent e) {
        if (ClientState.active(e.getPlayer().getUUID())) {
            e.setNewFovModifier(1.12f);
        }
    }

    /** Ao sair do mundo, limpa o estado do cliente para nao vazar para o proximo mundo. */
    @SubscribeEvent
    public static void loggingOut(ClientPlayerNetworkEvent.LoggingOut e) {
        ClientState.clear();
        Afterimages.clear();
        ClientData.DATA.deserializeNBT(RegistryAccess.EMPTY, new CompoundTag());
    }
}
