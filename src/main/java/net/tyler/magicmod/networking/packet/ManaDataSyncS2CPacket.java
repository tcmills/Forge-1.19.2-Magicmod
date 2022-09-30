package net.tyler.magicmod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tyler.magicmod.client.ClientManaData;

import java.util.function.Supplier;

public class ManaDataSyncS2CPacket {
    private final int mana;

    private final int max_mana;

    public ManaDataSyncS2CPacket(int mana, int max_mana) {
        this.mana = mana;
        this.max_mana = max_mana;
    }

    public ManaDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.mana = buf.readInt();
        this.max_mana = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(mana);
        buf.writeInt(max_mana);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientManaData.setMana(mana);
            ClientManaData.setMaxMana(max_mana);
        });
        return true;
    }

}
