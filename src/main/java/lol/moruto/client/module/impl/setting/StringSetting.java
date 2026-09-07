package lol.moruto.client.module.impl.setting;

import lol.moruto.client.module.ModuleSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class StringSetting extends ModuleSetting<String> {
    private boolean focused = false;
    private int cursorPosition = 0;
    private long lastBlinkTime = 0;
    private boolean cursorVisible = true;

    private int lastX, lastY, lastWidth, lastHeight;

    public StringSetting(String name, String defaultValue) {
        super(name, defaultValue);
        cursorPosition = value.length();
    }

    @Override
    public String getDisplayString() {
        return name + ": " + value;
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;

        lastX = x;
        lastY = y;
        lastWidth = width;
        lastHeight = height;

        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        int background = focused ? 0xFF292929 : hovered ? 0xFF272727 : 0xFF202020;
        int border = focused ? 0xFF6C63FF : hovered ? 0xFF454545 : 0xFF333333;

        context.fill(x, y, x + width, y + height, background);
        context.drawBorder(x, y, width, height, border);
        context.fill(x, y, x + 3, y + height, focused ? 0xFF8B83FF : 0xFF6C63FF);

        context.drawText(tr, name, x + 10, y + 9, 0xFFFFFFFF, false);

        int inputX = x + width / 2;
        int inputY = y + 5;
        int inputWidth = width - (inputX - x) - 8;
        int inputHeight = height - 10;

        context.fill(inputX, inputY, inputX + inputWidth, inputY + inputHeight, 0xFF181818);
        context.drawBorder(inputX, inputY, inputWidth, inputHeight, focused ? 0xFF6C63FF : 0xFF303030);

        context.enableScissor(inputX + 4, inputY, inputX + inputWidth - 4, inputY + inputHeight);

        String displayValue = value.isEmpty() ? "Type something..." : value;
        int textColor = value.isEmpty() ? 0xFF666666 : 0xFFDDDDDD;

        context.drawText(tr, displayValue, inputX + 6, inputY + 4, textColor, false);

        if (focused && cursorVisible) {
            String beforeCursor = value.substring(0, Math.min(cursorPosition, value.length()));
            int cursorX = inputX + 6 + tr.getWidth(beforeCursor);
            context.fill(cursorX, inputY + 3, cursorX + 1, inputY + inputHeight - 3, 0xFF6C63FF);
        }

        context.disableScissor();

        if (focused) {
            context.fill(inputX + 1, inputY + inputHeight - 2, inputX + inputWidth - 1, inputY + inputHeight - 1, 0xFF6C63FF);
        }

        updateCursor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        boolean hovered = mouseX >= lastX && mouseX <= lastX + lastWidth && mouseY >= lastY && mouseY <= lastY + lastHeight;

        if (!hovered) {
            focused = false;
            return false;
        }

        focused = true;
        cursorPosition = value.length();
        resetCursorBlink();
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (cursorPosition > 0 && !value.isEmpty()) {
                value = value.substring(0, cursorPosition - 1) + value.substring(cursorPosition);
                cursorPosition--;
                resetCursorBlink();
            }
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_DELETE) {
            if (cursorPosition < value.length()) {
                value = value.substring(0, cursorPosition) + value.substring(cursorPosition + 1);
                resetCursorBlink();
            }
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_LEFT) {
            if (cursorPosition > 0) cursorPosition--;
            resetCursorBlink();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (cursorPosition < value.length()) cursorPosition++;
            resetCursorBlink();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_HOME) {
            cursorPosition = 0;
            resetCursorBlink();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_END) {
            cursorPosition = value.length();
            resetCursorBlink();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_ESCAPE) {
            focused = false;
            return true;
        }

        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!focused || Character.isISOControl(chr)) return false;

        value = value.substring(0, cursorPosition) + chr + value.substring(cursorPosition);
        cursorPosition++;
        resetCursorBlink();

        return true;
    }

    private void resetCursorBlink() {
        lastBlinkTime = System.currentTimeMillis();
        cursorVisible = true;
    }

    public void updateCursor() {
        if (!focused) {
            cursorVisible = false;
            return;
        }

        long elapsed = System.currentTimeMillis() - lastBlinkTime;
        cursorVisible = (elapsed / 500) % 2 == 0;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;

        if (focused) {
            cursorPosition = value.length();
            resetCursorBlink();
        }
    }
}
