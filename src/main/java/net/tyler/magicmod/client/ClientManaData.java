package net.tyler.magicmod.client;

public class ClientManaData {
    private static int playerMana;

    private static int playerMaxMana;

    public static void setMana(int mana) {
        ClientManaData.playerMana = mana;
    }

    public static void setMaxMana(int max_mana) {
        ClientManaData.playerMaxMana = max_mana;
    }

    public static int getPlayerMana() {
        return playerMana;
    }

    public static int getPlayerMaxMana() {
        return playerMaxMana;
    }
}
