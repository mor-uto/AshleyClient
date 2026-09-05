package lol.moruto.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationManager {

    private static final List<Notification> notifications = new ArrayList<>();

    public static void sendNotification(String message) {
        notifications.add(new Notification(message, System.currentTimeMillis()));
    }

    public static void render(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null) return;

        int screenWidth = client.getWindow().getScaledWidth();
        int y = 10;
        int padding = 5;

        long now = System.currentTimeMillis();
        long fadeInDuration = 200;
        long visibleDuration = 3000;
        long fadeOutDuration = 500;

        Iterator<Notification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            Notification n = iterator.next();
            long age = now - n.startTime;

            float alpha;

            if (age < fadeInDuration) {
                alpha = (float) age / fadeInDuration;
            } else if (age < fadeInDuration + visibleDuration) {
                alpha = 1f;
            } else {
                long fadeOutAge = age - (fadeInDuration + visibleDuration);
                alpha = 1f - Math.min(1f, (float) fadeOutAge / fadeOutDuration);
                if (alpha <= 0) {
                    iterator.remove();
                    continue;
                }
            }

            String text = n.message;
            int textWidth = client.textRenderer.getWidth(text);
            int boxWidth = textWidth + padding * 2;
            int x = screenWidth - boxWidth - 10;

            int bgColor = ((int)(alpha * 150) << 24);
            drawContext.fill(x, y, x + boxWidth, y + 14 + padding, bgColor);

            int textColor = ((int)(alpha * 255) << 24) | 0xFFFFFF;
            drawContext.drawText(client.textRenderer, text, x + padding, y + padding, textColor, false);

            y += 20;
        }
    }

    private static class Notification {
        String message;
        long startTime;
        Notification(String m, long t) {
            message = m;
            startTime = t;
        }
    }
}
