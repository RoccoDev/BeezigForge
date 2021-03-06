/*
 * This file is part of BeezigForge.
 *
 * BeezigForge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BeezigForge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BeezigForge.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.forge.gui.briefing.recentgames;

import eu.beezig.forge.ForgeMessage;
import eu.beezig.forge.gui.briefing.recentgames.csv.CsvMerger;
import eu.beezig.forge.gui.briefing.recentgames.csv.GameData;
import eu.beezig.forge.gui.briefing.recentgames.csv.LoggingGame;
import eu.beezig.forge.gui.briefing.tabs.Tab;
import eu.beezig.forge.gui.briefing.tabs.TabGuiButton;
import eu.beezig.forge.gui.briefing.tabs.TabRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.net.URI;


public class RecentGamesTab extends Tab {

    private CsvMerger csv;
    private TabRenderUtils render = new TabRenderUtils(getStartY());
    private double scrollY;
    private int gamesLimit = 100;
    private LoggingGame gamemodeFilter;

    public RecentGamesTab() {
        super(ForgeMessage.translate("gui.news.tab.recent"), new ResourceLocation("beezigforge/gui/recent.png"));
    }

    @Override
    protected void init(int windowWidth, int windowHeight) {
        super.init(windowWidth, windowHeight);
        new Thread(() -> csv = new CsvMerger(windowWidth)).start();
        getButtonList().add(new TabGuiButton(this,40, windowWidth / 2 + 200, getStartY() + 10,
                100, 20, ForgeMessage.translate("gui.news.tab.recent.games", gamesLimit)));
        getButtonList().add(new TabGuiButton(this,41, windowWidth / 2 + 200, getStartY() + 40,
                100, 20, gamemodeFilter == null ? ForgeMessage.translate("gui.news.tab.recent.filter.all")
                : ForgeMessage.translate("gui.news.tab.recent.filter", gamemodeFilter.name().toUpperCase())));

    }

    @Override
    protected void onActionPerformed(GuiButton btn) {
        if(csv == null || csv.getRecentGames() == null) return;
        switch(btn.id) {
            case 40 /* Game limit */:
                if(gamesLimit == 400) {
                    gamesLimit = csv.getRecentGames().size();
                    btn.displayString = ForgeMessage.translate("gui.news.tab.recent.games.all");
                }
                else if(gamesLimit == csv.getRecentGames().size()) {
                    gamesLimit = 100;
                    btn.displayString = ForgeMessage.translate("gui.news.tab.recent.games", gamesLimit);
                }
                else {
                    gamesLimit += 100;
                    btn.displayString = ForgeMessage.translate("gui.news.tab.recent.games", gamesLimit);
                }
                break;
            case 41 /* Filter */:
                if(gamemodeFilter == null) {
                    gamemodeFilter = LoggingGame.values()[0];
                    btn.displayString = ForgeMessage.translate("gui.news.tab.recent.filter", gamemodeFilter.name().toUpperCase());
                }
                else {
                    if(gamemodeFilter.ordinal() + 1 >= LoggingGame.values().length) {
                        gamemodeFilter = null;
                        btn.displayString = ForgeMessage.translate("gui.news.tab.recent.filter.all");
                    }
                    else {
                        gamemodeFilter = LoggingGame.values()[gamemodeFilter.ordinal() + 1];
                        btn.displayString = ForgeMessage.translate("gui.news.tab.recent.filter", gamemodeFilter.name().toUpperCase());
                    }
                }
                break;
        }
    }

    @Override
    protected void drawTab(int mouseX, int mouseY) {
        super.drawTab(mouseX, mouseY);

        if(csv == null)
            centered(ForgeMessage.translate("gui.news.loading"), windowWidth / 2, 0, Color.WHITE.getRGB());
        else {
            final int[] y = {getStartY() + (int) scrollY};
            csv.getRecentGames().stream().filter(g -> gamemodeFilter == null || g.getGamemode() == gamemodeFilter)
                    .limit(gamesLimit).forEach(game -> {
                int stringY = y[0];

                // Adapt strings to fit into the box
                String[] title = game.getTitle();
                stringY += title.length * 12;

                if(game.getGameId() != null) stringY += 12;

                stringY += game.getContent().length * 12;

                String[] author = game.getBelow();
                stringY += author.length * 12;

                stringY += 12;
                render.drawRectBorder(windowWidth / 3D - 25, y[0], windowWidth / 3D * 2 + 25, stringY < getStartY() ? 0 : stringY, Color.GRAY.getRGB(), 1.0);

                Minecraft.getMinecraft().getTextureManager().bindTexture(game.getGamemode().getIcon());
                if(getStartY() < y[0] + 7)
                    render.drawTexture(windowWidth / 3.0 - 15.0, y[0] + 7.0, 256, 256, 20, 20);

                stringY = y[0];
                stringY += 6.5;
                for(String s : title) {
                    if(stringY > getStartY())
                        render.drawString(s, windowWidth / 3D + 15, stringY, 1.2);
                    stringY += 12;
                }
                for(String s : author) {
                    if(stringY > getStartY())
                        render.drawString("§3" + s, windowWidth / 3D + 15, stringY);
                    stringY += 12;
                }
                if(stringY > getStartY())
                    drawHorizontalLine(windowWidth / 3 - 10, windowWidth / 3 * 2 + 10, stringY, Color.GRAY.getRGB());
                stringY += 5;
                for(String s : game.getContent()) {
                    if(stringY > getStartY())
                        render.drawCenteredString(s, windowWidth / 2D, stringY, 0.9);
                    stringY += 12;
                }
                if(stringY > getStartY()) {
                    int sWidth = render.getStringWidth(game.getChatComponent());
                    game.setPosition(windowWidth / 2 - sWidth / 2, stringY, sWidth,
                            12);
                    game.setShown(true);
                    render.drawCenteredString(game.getChatComponent(), windowWidth / 2D, stringY, 1.2);
                    stringY += 15;
                }
                else game.setShown(false);
                stringY += 8;
                y[0] = stringY;
            });
        }
    }

    @Override
    protected void handleMouse() {
        super.handleMouse();
        final int wheel = Mouse.getEventDWheel();
        if (wheel > 0) {
            this.scrollY += 30;
        }
        else if (wheel < 0) {
            this.scrollY -= 30;
        }
        if (wheel != 0) {
            this.checkOutOfBorders();
        }
    }

    private void checkOutOfBorders() {
        if (this.scrollY > 0.0) {
            this.scrollY = 0.0;
        }
    }

    @Override
    protected void onMouseClick(int mouseX, int mouseY) {
        super.onMouseClick(mouseX, mouseY);
        activateComponent(mouseX, mouseY);
    }

    private void activateComponent(int mouseX, int mouseY) {
        if(csv == null) return;
        for(GameData game : csv.getRecentGames()) {
            if(game.isShown() && game.isHovered(mouseX, mouseY)) {
                try {
                    Desktop.getDesktop().browse(new URI(game.getLink()));
                    break;
                } catch (Exception e) {
                    System.err.println("Couldn't open URL: ");
                    e.printStackTrace();
                }
            }

        }
    }
}
