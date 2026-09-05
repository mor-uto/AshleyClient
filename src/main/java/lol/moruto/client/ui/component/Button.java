package lol.moruto.client.ui.component;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class Button {

    protected int x;
    protected int y;

    protected final int width;
    protected static final int HEIGHT = 22;

    protected final String text;

    private float scale = 1.0f;

    public Button(int x, int y, int width, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.text = text;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;

        boolean hovered =
                mouseX >= x &&
                        mouseX <= x + width &&
                        mouseY >= y &&
                        mouseY <= y + HEIGHT;

        float targetScale = hovered ? 0.97f : 1.0f;
        scale += (targetScale - scale) * 0.2f;

        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (HEIGHT * scale);

        int drawX = x + (width - scaledWidth) / 2;
        int drawY = y + (HEIGHT - scaledHeight) / 2;

        int background = hovered
                ? 0xFF292929
                : 0xFF202020;

        context.fill(
                drawX,
                drawY,
                drawX + scaledWidth,
                drawY + scaledHeight,
                background
        );

        // Subtle border
        context.drawBorder(
                drawX,
                drawY,
                scaledWidth,
                scaledHeight,
                hovered ? 0xFF555555 : 0xFF333333
        );

        int textWidth = tr.getWidth(text);

        int textX = x + (width - textWidth) / 2;
        int textY = y + (HEIGHT - 8) / 2;

        context.drawText(
                tr,
                text,
                textX,
                textY,
                hovered ? 0xFFFFFFFF : 0xFFCCCCCC,
                false
        );
    }

    public boolean isClicked(double mouseX, double mouseY, int button) {
        return mouseX >= x &&
                mouseX <= x + width &&
                mouseY >= y &&
                mouseY <= y + HEIGHT &&
                button == 0;
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    public String getText() {
        return text;
    }
}