package net.tyler.magicmod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tyler.magicmod.client.ClientManaData;
import net.tyler.magicmod.client.ClientSharkLungeCastingData;

import java.util.function.Supplier;

public class SharkLungeSyncS2CPacket {
    private final boolean sharkLungeCasting;

    public SharkLungeSyncS2CPacket(boolean sharkLungeCasting) {
        this.sharkLungeCasting = sharkLungeCasting;
    }

    public SharkLungeSyncS2CPacket(FriendlyByteBuf buf) {
        this.sharkLungeCasting = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(sharkLungeCasting);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientSharkLungeCastingData.setPlayerSharkLungeCasting(sharkLungeCasting);
        });
        return true;
    }

}
