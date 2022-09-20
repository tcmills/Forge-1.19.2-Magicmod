package net.tyler.magicmod.client;

public class ClientInfoData {

    private static int playerSchoolLevel;

    private static boolean playerFire;
    private static boolean playerWater;
    private static boolean playerEarth;
    private static boolean playerAir;
    private static boolean playerSummoning;
    private static boolean playerForge;
    private static boolean playerStorm;
    private static boolean playerEnder;
    private static boolean playerLife;
    private static boolean playerDeath;
    private static boolean playerSun;
    private static boolean playerMoon;

    private static boolean playerDungeonParty;

    public static void setPlayerInfo(int schoolLevel, boolean fire, boolean water, boolean earth, boolean air,
                                      boolean summoning, boolean forge, boolean storm, boolean ender, boolean life,
                                      boolean death, boolean sun, boolean moon, boolean dungeonParty) {
        ClientInfoData.playerSchoolLevel = schoolLevel;

        ClientInfoData.playerFire = fire;
        ClientInfoData.playerWater = water;
        ClientInfoData.playerEarth = earth;
        ClientInfoData.playerAir = air;
        ClientInfoData.playerSummoning = summoning;
        ClientInfoData.playerForge = forge;
        ClientInfoData.playerStorm = storm;
        ClientInfoData.playerEnder = ender;
        ClientInfoData.playerLife = life;
        ClientInfoData.playerDeath = death;
        ClientInfoData.playerSun = sun;
        ClientInfoData.playerMoon = moon;

        ClientInfoData.playerDungeonParty = dungeonParty;
    }

    public static int getPlayerSchoolLevel() {
        return playerSchoolLevel;
    }

    public static boolean getPlayerFire() {
        return playerFire;
    }

    public static boolean getPlayerWater() {
        return playerWater;
    }

    public static boolean getPlayerEarth() {
        return playerEarth;
    }

    public static boolean getPlayerAir() {
        return playerAir;
    }

    public static boolean getPlayerSummoning() {
        return playerSummoning;
    }

    public static boolean getPlayerForge() {
        return playerForge;
    }

    public static boolean getPlayerStorm() {
        return playerStorm;
    }

    public static boolean getPlayerEnder() {
        return playerEnder;
    }

    public static boolean getPlayerLife() {
        return playerLife;
    }

    public static boolean getPlayerDeath() {
        return playerDeath;
    }

    public static boolean getPlayerSun() {
        return playerSun;
    }

    public static boolean getPlayerMoon() {
        return playerMoon;
    }

    public static boolean getPlayerDungeonParty() {
        return playerDungeonParty;
    }
}
