package net.tyler.magicmod.networking.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.tyler.magicmod.mana.PlayerManaProvider;
import net.tyler.magicmod.networking.ModMessages;

import java.util.function.Supplier;

public class Add100ManaC2SPacket {

    public Add100ManaC2SPacket() {

    }

    public Add100ManaC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                mana.addMana(100);
                player.sendSystemMessage(Component.literal("Current Mana " + mana.getMana())
                        .withStyle(ChatFormatting.AQUA));
                ModMessages.sendToPlayer(new ManaDataSyncS2CPacket(mana.getMana()), player);
            });

        });
        return true;
    }

}
