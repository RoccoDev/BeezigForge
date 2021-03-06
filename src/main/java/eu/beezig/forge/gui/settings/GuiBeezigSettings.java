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

package eu.beezig.forge.gui.settings;

import eu.beezig.core.api.SettingInfo;
import eu.beezig.forge.api.BeezigAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GuiBeezigSettings extends GuiScreen {
    private final GuiScreen parentScreen;
    protected final SettingsList settings;

    public GuiBeezigSettings(GuiScreen parentScreen, Map<String, List<SettingInfo>> settings) {
        this.parentScreen = parentScreen;
        this.settings = new SettingsList(this, width, height, 32, height - 48, 16);
        if(settings != null) this.settings.populate(settings);
    }

    public SettingsList getSettings() {
        return settings;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        BeezigAPI.saveConfig();
    }

    @Override
    public void initGui() {
        super.initGui();
        settings.setDimensions(width, height, 32, height - 48);
        buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 29, I18n.format("gui.done")));
    }

    @Override
    public void handleMouseInput() throws IOException {
        settings.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        settings.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2f, 2f, 2f);
        drawCenteredString(fontRendererObj, "Beezig", width / 4, 5, 0x1a7ef0);
        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        settings.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        settings.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 200) {
            Minecraft.getMinecraft().displayGuiScreen(parentScreen);
        }
    }

    public void saveEntry(SettingEntry entry, Object value) {
        BeezigAPI.setSettingAsIs(entry.getKey(), value);
    }

    public void show() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void handleComponentHover(IChatComponent p_175272_1_, int p_175272_2_, int p_175272_3_) {
        super.handleComponentHover(p_175272_1_, p_175272_2_, p_175272_3_);
    }
}
