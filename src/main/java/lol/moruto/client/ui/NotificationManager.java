package lol.moruto.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationManager {
    private static final List<Notification> notifications = new ArrayList<>();

    private static final int MARGIN = 10;
    private static final int PADDING = 8;
    private static final int BOX_HEIGHT = 28;
    private static final int GAP = 5;

    private static final long FADE_IN = 200;
    private static final long VISIBLE = 3000;
    private static final long FADE_OUT = 400;

    private static final int ACCENT = 0xFF9B59FF;
    private static final int BACKGROUND = 0xE814141A;

    public static void sendNotification(String message) {
        notifications.add(new Notification(message, System.currentTimeMillis()));
    }

    public static void render(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null) return;

        int screenWidth = client.getWindow().getScaledWidth();
        long now = System.currentTimeMillis();
        int y = MARGIN;

        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            Notification n = iterator.next();
            long age = now - n.startTime;

            if (age >= FADE_IN + VISIBLE + FADE_OUT) {
                iterator.remove();
                continue;
            }

            float alpha;
            if (age < FADE_IN) {
                alpha = easeOut((float) age / FADE_IN);
            } else if (age < FADE_IN + VISIBLE) {
                alpha = 1f;
            } else {
                alpha = 1f - easeIn((float) (age - FADE_IN - VISIBLE) / FADE_OUT);
            }

            String text = n.message;
            int textWidth = client.textRenderer.getWidth(text);
            int width = Math.min(textWidth + PADDING * 2 + 18, screenWidth - MARGIN * 2);
            int targetX = screenWidth - width - MARGIN;

            float slide = easeOut(Math.min(1f, (float) age / FADE_IN));
            int x = (int) (screenWidth + 10 - (screenWidth + 10 - targetX) * slide);
            int drawY = y + (int) (6 * (1f - slide));

            int bg = ((int) (0xE8 * alpha) << 24) | (BACKGROUND & 0xFFFFFF);
            int accent = ((int) (255 * alpha) << 24) | (ACCENT & 0xFFFFFF);
            int white = ((int) (255 * alpha) << 24) | 0xFFFFFF;

            drawContext.fill(x + 2, drawY + 2, x + width + 2, drawY + BOX_HEIGHT + 2, ((int) (70 * alpha) << 24));
            drawContext.fill(x, drawY, x + width, drawY + BOX_HEIGHT, bg);
            drawContext.fill(x, drawY, x + 3, drawY + BOX_HEIGHT, accent);
            drawContext.fill(x + 9, drawY + 11, x + 13, drawY + 15, accent);
            drawContext.drawText(client.textRenderer, text, x + 19, drawY + 9, white, false);

            long total = FADE_IN + VISIBLE + FADE_OUT;
            float progress = Math.min(1f, (float) age / total);
            int progressWidth = (int) ((width - 3) * (1f - progress));
            drawContext.fill(x + 3, drawY + BOX_HEIGHT - 2, x + 3 + progressWidth, drawY + BOX_HEIGHT, accent);

            y += BOX_HEIGHT + GAP;
        }
    }

    private static float easeOut(float t) {
        t = Math.max(0f, Math.min(1f, t));
        return 1f - (float) Math.pow(1f - t, 3);
    }

    private static float easeIn(float t) {
        t = Math.max(0f, Math.min(1f, t));
        return t * t * t;
    }

    private record Notification(String message, long startTime) {}
}