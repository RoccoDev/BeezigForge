package tk.roccodev.beezig.forge;

import tk.roccodev.beezig.forge.listener.games.timv.EnderchestsListener;
import tk.roccodev.beezig.forge.modules.pointstag.PointsTagCache;

public class ActiveGame {

    private static String current = "", last = "";

    public static void set(String game) {
        if(!last.equals(game)) PointsTagCache.clear();
        last = current;
        current = game;
        EnderchestsListener.customSpawnPt = false;
    }

    public static String current() {
        return current;
    }

}