package lol.moruto.client.module.impl.setting;

import lol.moruto.client.module.ModuleSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class KeybindSetting extends ModuleSetting<Integer> {
    private boolean listening = false;

    public KeybindSetting(String name, int defaultKey) {
        super(name, defaultKey);
    }

    public int getKey() {
        return value;
    }

    public void setKey(int key) {
        value = key;
    }

    public boolean isListening() {
        return listening;
    }

    @Override
    public String getDisplayString() {
        return name + ": " + getKeyName();
    }

    private String getKeyName() {
        if (value == GLFW.GLFW_KEY_UNKNOWN || value == -1) return "None";
        String name = GLFW.glfwGetKeyName(value, 0);
        if (name != null) return name.toUpperCase();
        return switch (value) {
            case GLFW.GLFW_KEY_SPACE -> "SPACE";
            case GLFW.GLFW_KEY_ENTER -> "ENTER";
            case GLFW.GLFW_KEY_ESCAPE -> "ESC";
            case GLFW.GLFW_KEY_TAB -> "TAB";
            case GLFW.GLFW_KEY_BACKSPACE -> "BACKSPACE";
            case GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT";
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT";
            case GLFW.GLFW_KEY_LEFT_CONTROL -> "LCTRL";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCTRL";
            case GLFW.GLFW_KEY_LEFT_ALT -> "LALT";
            case GLFW.GLFW_KEY_RIGHT_ALT -> "RALT";
            default -> "KEY " + value;
        };
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;

        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        int background = listening ? 0xFF292929 : hovered ? 0xFF272727 : 0xFF202020;
        int border = listening ? 0xFF6C63FF : hovered ? 0xFF454545 : 0xFF333333;

        context.fill(x, y, x + width, y + height, background);
        context.drawBorder(x, y, width, height, border);
        context.fill(x, y, x + 3, y + height, listening ? 0xFF8B83FF : 0xFF6C63FF);

        context.drawText(tr, name, x + 10, y + 9, 0xFFFFFFFF, false);

        String keyName = listening ? "Press a key..." : getKeyName();
        int keyWidth = tr.getWidth(keyName);

        context.drawText(tr, keyName, x + width - keyWidth - 10, y + 9, listening ? 0xFF6C63FF : 0xFFBBBBBB, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            listening = true;
            return true;
        }

        if (button == 1) {
            value = -1;
            listening = false;
            return true;
        }

        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!listening) return false;

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            value = -1;
            listening = false;
            return true;
        }

        value = keyCode;
        listening = false;
        return true;
    }

}