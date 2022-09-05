package net.tyler.magicmod.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tyler.magicmod.MagicMod;
import net.tyler.magicmod.networking.packet.*;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MagicMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(Add100ManaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(Add100ManaC2SPacket::new)
                .encoder(Add100ManaC2SPacket::toBytes)
                .consumerMainThread(Add100ManaC2SPacket::handle)
                .add();

        net.messageBuilder(Add10MaxManaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(Add10MaxManaC2SPacket::new)
                .encoder(Add10MaxManaC2SPacket::toBytes)
                .consumerMainThread(Add10MaxManaC2SPacket::handle)
                .add();

        net.messageBuilder(Set100MaxManaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(Set100MaxManaC2SPacket::new)
                .encoder(Set100MaxManaC2SPacket::toBytes)
                .consumerMainThread(Set100MaxManaC2SPacket::handle)
                .add();

        net.messageBuilder(Add30ManaC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(Add30ManaC2SPacket::new)
                .encoder(Add30ManaC2SPacket::toBytes)
                .consumerMainThread(Add30ManaC2SPacket::handle)
                .add();

        net.messageBuilder(ManaDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ManaDataSyncS2CPacket::new)
                .encoder(ManaDataSyncS2CPacket::toBytes)
                .consumerMainThread(ManaDataSyncS2CPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
