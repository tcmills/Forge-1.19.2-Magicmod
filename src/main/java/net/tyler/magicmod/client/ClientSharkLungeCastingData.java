package net.tyler.magicmod.client;

public class ClientSharkLungeCastingData {
    private static boolean playerSharkLungeCasting;

    public static void setPlayerSharkLungeCasting(boolean playerSharkLungeCasting) {
        ClientSharkLungeCastingData.playerSharkLungeCasting = playerSharkLungeCasting;
    }

    public static boolean getPlayerSharkLungeCasting() {
        return playerSharkLungeCasting;
    }
}
