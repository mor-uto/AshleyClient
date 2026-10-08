package lol.moruto.client.module.impl.misc;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.text.Text;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptChat extends Module {

    private static final String PREFIX = "[encrypted]";
    private static final byte[] KEY = "cDGLebTt9BPiak8t".getBytes(StandardCharsets.UTF_8);
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    private final SecureRandom random = new SecureRandom();
    private static boolean registered = false;

    public EncryptChat() {
        super("EncryptChat", "Encrypts chat messages", Category.MISC);
        registerEvents();
    }

    private void registerEvents() {
        if (registered) return;
        registered = true;

        ClientSendMessageEvents.MODIFY_CHAT.register(message -> {
            if (!isToggled()) return message;
            if (!message.toLowerCase().startsWith(PREFIX)) return message;

            String plaintext = message.substring(PREFIX.length()).trim();
            if (plaintext.isEmpty()) return message;

            try {
                return PREFIX + " " + encrypt(plaintext);
            } catch (Exception e) {
                e.printStackTrace();
                return message;
            }
        });

        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (!isToggled()) return true;

            String text = message.getString();
            if (text == null || text.isEmpty()) return true;
            if (!text.toLowerCase().startsWith(PREFIX)) return true;

            String encrypted = text.substring(PREFIX.length()).trim();
            if (encrypted.isEmpty()) return true;

            try {
                String decrypted = decrypt(encrypted);
                mc.inGameHud.getChatHud().addMessage(Text.literal(PREFIX + " " + decrypted));
                return false;
            } catch (Exception e) {
                return true;
            }
        });
    }

    private String encrypt(String plaintext) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec key = new SecretKeySpec(KEY, "AES");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        ByteBuffer buffer = ByteBuffer.allocate(IV_LENGTH + ciphertext.length);
        buffer.put(iv);
        buffer.put(ciphertext);

        return Base64.getEncoder().encodeToString(buffer.array());
    }

    private String decrypt(String encoded) throws Exception {
        byte[] data;

        try {
            data = Base64.getDecoder().decode(encoded);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64", e);
        }

        if (data.length <= IV_LENGTH) {
            throw new IllegalArgumentException("Invalid encrypted message");
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);

        byte[] iv = new byte[IV_LENGTH];
        buffer.get(iv);

        byte[] ciphertext = new byte[buffer.remaining()];
        buffer.get(ciphertext);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec key = new SecretKeySpec(KEY, "AES");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] plaintext = cipher.doFinal(ciphertext);

        return new String(plaintext, StandardCharsets.UTF_8);
    }
}