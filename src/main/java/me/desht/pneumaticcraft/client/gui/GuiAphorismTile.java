package me.desht.pneumaticcraft.client.gui;

import me.desht.pneumaticcraft.common.network.NetworkHandler;
import me.desht.pneumaticcraft.common.network.PacketAphorismTileUpdate;
import me.desht.pneumaticcraft.common.tileentity.TileEntityAphorismTile;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Random;

public class GuiAphorismTile extends GuiScreen {
    public final TileEntityAphorismTile tile;
    private String[] textLines;
    public int cursorY;
    public int cursorX;
    public int updateCounter;
    private static Random rand = new Random();
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");

    public GuiAphorismTile(TileEntityAphorismTile tile) {
        this.tile = tile;
        textLines = tile.getTextLines();
//        if (textLines.length == 1 && textLines[0].equals("")) {
//            textLines[0] = getSplashText();
//        }
        NetworkHandler.sendToServer(new PacketAphorismTileUpdate(tile));
    }

    @Override
    public void updateScreen() {
        updateCounter++;
    }

    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        if (par2 == Keyboard.KEY_ESCAPE) {
            NetworkHandler.sendToServer(new PacketAphorismTileUpdate(tile));
        } else if (par2 == Keyboard.KEY_LEFT) {
            cursorY--;
            if (cursorY < 0) cursorY = textLines.length - 1;
        } else if (par2 == Keyboard.KEY_DOWN || par2 == Keyboard.KEY_NUMPADENTER) {
            cursorY++;
            if (cursorY >= textLines.length) cursorY = 0;
        } else if (par2 == Keyboard.KEY_RETURN) {
            cursorY++;
            textLines = ArrayUtils.add(textLines, cursorY, "");
        } else if (par2 == Keyboard.KEY_BACK) {
            if (textLines[cursorY].length() > 0) {
                textLines[cursorY] = textLines[cursorY].substring(0, textLines[cursorY].length() - 1);
            } else if (textLines.length > 1) {
                textLines = ArrayUtils.remove(textLines, cursorY);
                cursorY--;
                if (cursorY < 0) cursorY = 0;
            }
        } else if (ChatAllowedCharacters.isAllowedCharacter(par1)) {
            textLines[cursorY] = textLines[cursorY] + par1;
        }
        tile.setTextLines(textLines);
        super.keyTyped(par1, par2);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    /*
    private String getSplashText() {
        String splashText = DramaSplash.cachedLine;
        if (splashText == null) {
            splashText = "";
            try {
                String s;
                ArrayList arraylist = new ArrayList();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));

                try {
                    while ((s = bufferedreader.readLine()) != null) {
                        s = s.trim();

                        if (!s.isEmpty()) {
                            arraylist.add(s);
                        }
                    }

                    do {
                        splashText = (String) arraylist.get(rand.nextInt(arraylist.size()));
                    } while (splashText.hashCode() == 125780783);
                } finally {
                    bufferedreader.close();
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        } else {
            DramaSplash.newDrama();
        }
        return splashText;
    }
    */
}
