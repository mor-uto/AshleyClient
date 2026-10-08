package lol.moruto.client.module;

import lol.moruto.client.module.impl.*;
import lol.moruto.client.module.impl.combat.AnchorSpam;
import lol.moruto.client.module.impl.combat.AutoClicker;
import lol.moruto.client.module.impl.combat.AutoTotem;
import lol.moruto.client.module.impl.grief.BanAll;
import lol.moruto.client.module.impl.grief.KickAll;
import lol.moruto.client.module.impl.grief.MuteAll;
import lol.moruto.client.module.impl.grief.OPEveryone;
import lol.moruto.client.module.impl.gui.*;
import lol.moruto.client.module.impl.misc.*;
import lol.moruto.client.module.impl.movement.*;
import lol.moruto.client.ui.NotificationManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModulesManager {
    private final List<Module> modules = Arrays.asList(
            //Combat
            new AutoTotem(),
            new AnchorSpam(),
            new AutoClicker(),

            //Movement
            new Fly(),
            new AntiAFK(),
            new Jesus(),
            new NoFall(),
            new AutoSprint(),
            new Speed(),
            new ClickTP(),
            new Safewalk(),

            //GUI
            new ArrayList(),
            new Fps(),

            //MISC
            new AutoRespawn(),
            new IgnoreResourcePack(),
            new SpamPackets(),
            new ClientBrand(),
            new Zoom(),
            new PluginScanner(),
            new EncryptChat(),

            //GRIEF
            new OPEveryone(),
            new KickAll(),
            new BanAll(),
            new MuteAll(),


            new PlaceholderModule("notification test", "Testing the notification system", Category.MISC) {
                @Override
                public void onEnable() {
                    NotificationManager.sendNotification("testing notifications!");
                    toggle();
                }
            }
    );

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getEnabledModules() {
        return modules.stream().filter(Module::isToggled).collect(Collectors.toList());
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream().filter(module -> module.getCategory() == category).toList();
    }

    public Module getModule(String name) {
        return modules.stream().filter(module -> module.getName().equals(name)).findFirst().orElse(null);
    }
}
