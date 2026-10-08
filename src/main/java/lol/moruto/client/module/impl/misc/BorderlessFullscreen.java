package lol.moruto.client.module.impl.misc;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class BorderlessFullscreen extends Module {

    private int oldX;
    private int oldY;
    private int oldWidth;
    private int oldHeight;

    public BorderlessFullscreen() {
        super("Borderless Fullscreen", "Borderless fullscreen window", Category.MISC);
    }

    @Override
    public void onEnable() {
        long window = MinecraftClient.getInstance().getWindow().getHandle();

        int[] x = {0};
        int[] y = {0};
        int[] width = {0};
        int[] height = {0};

        GLFW.glfwGetWindowPos(window, x, y);
        GLFW.glfwGetWindowSize(window, width, height);

        oldX = x[0];
        oldY = y[0];
        oldWidth = width[0];
        oldHeight = height[0];

        long monitor = getMonitor(window);

        if (monitor == 0L)
            monitor = GLFW.glfwGetPrimaryMonitor();

        GLFW.glfwGetMonitorPos(monitor, x, y);

        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(monitor);

        if (videoMode == null)
            return;

        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
        GLFW.glfwSetWindowPos(window, x[0], y[0]);
        GLFW.glfwSetWindowSize(window, videoMode.width(), videoMode.height());
    }

    @Override
    public void onDisable() {
        long window = MinecraftClient.getInstance().getWindow().getHandle();

        GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
        GLFW.glfwSetWindowPos(window, oldX, oldY);
        GLFW.glfwSetWindowSize(window, oldWidth, oldHeight);
    }

    private long getMonitor(long window) {
        int[] wx = {0};
        int[] wy = {0};
        int[] ww = {0};
        int[] wh = {0};

        GLFW.glfwGetWindowPos(window, wx, wy);
        GLFW.glfwGetWindowSize(window, ww, wh);

        int centerX = wx[0] + ww[0] / 2;
        int centerY = wy[0] + wh[0] / 2;

        PointerBuffer monitors = GLFW.glfwGetMonitors();

        if (monitors == null)
            return 0L;

        while (monitors.hasRemaining()) {
            long monitor = monitors.get();

            int[] mx = {0};
            int[] my = {0};

            GLFW.glfwGetMonitorPos(monitor, mx, my);

            GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);

            if (mode == null)
                continue;

            if (centerX >= mx[0] && centerX < mx[0] + mode.width() && centerY >= my[0] && centerY < my[0] + mode.height()) {
                return monitor;
            }
        }

        return 0L;
    }
}