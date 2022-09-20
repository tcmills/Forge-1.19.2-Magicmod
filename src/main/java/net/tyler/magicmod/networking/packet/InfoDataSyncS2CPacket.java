package net.tyler.magicmod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tyler.magicmod.client.ClientInfoData;

import java.util.function.Supplier;

public class InfoDataSyncS2CPacket {

    private final int schoolLevel;

    private final boolean fire;
    private final boolean water;
    private final boolean earth;
    private final boolean air;
    private final boolean summoning;
    private final boolean forge;
    private final boolean storm;
    private final boolean ender;
    private final boolean life;
    private final boolean death;
    private final boolean sun;
    private final boolean moon;

    private final boolean dungeonParty;

    public InfoDataSyncS2CPacket(int schoolLevel, boolean fire, boolean water, boolean earth, boolean air,
                                 boolean summoning, boolean forge, boolean storm, boolean ender, boolean life,
                                 boolean death, boolean sun, boolean moon, boolean dungeonParty) {
        this.schoolLevel = schoolLevel;

        this.fire = fire;
        this.water = water;
        this.earth = earth;
        this.air = air;
        this.summoning = summoning;
        this.forge = forge;
        this.storm = storm;
        this.ender = ender;
        this.life = life;
        this.death = death;
        this.sun = sun;
        this.moon = moon;

        this.dungeonParty = dungeonParty;
    }

    public InfoDataSyncS2CPacket(FriendlyByteBuf buf) {
        this.schoolLevel = buf.readInt();

        this.fire = buf.readBoolean();
        this.water = buf.readBoolean();
        this.earth = buf.readBoolean();
        this.air = buf.readBoolean();
        this.summoning = buf.readBoolean();
        this.forge = buf.readBoolean();
        this.storm = buf.readBoolean();
        this.ender = buf.readBoolean();
        this.life = buf.readBoolean();
        this.death = buf.readBoolean();
        this.sun = buf.readBoolean();
        this.moon = buf.readBoolean();

        this.dungeonParty = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(schoolLevel);

        buf.writeBoolean(fire);
        buf.writeBoolean(water);
        buf.writeBoolean(earth);
        buf.writeBoolean(air);
        buf.writeBoolean(summoning);
        buf.writeBoolean(forge);
        buf.writeBoolean(storm);
        buf.writeBoolean(ender);
        buf.writeBoolean(life);
        buf.writeBoolean(death);
        buf.writeBoolean(sun);
        buf.writeBoolean(moon);

        buf.writeBoolean(dungeonParty);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientInfoData.setPlayerInfo(schoolLevel, fire, water, earth, air, summoning, forge, storm, ender, life,
                    death, sun, moon, dungeonParty);
        });
        return true;
    }

}
