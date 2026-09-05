package lol.moruto.mod.module;

import lol.moruto.mod.module.impl.PlaceholderModule;
import lol.moruto.mod.module.impl.LoginModule;
import lol.moruto.mod.module.impl.gui.ArrayList;
import lol.moruto.mod.module.impl.gui.Fps;
import lol.moruto.mod.module.impl.misc.AutoRespawn;
import lol.moruto.mod.module.impl.misc.IgnoreResourcePack;
import lol.moruto.mod.module.impl.misc.SpamPackets;
import lol.moruto.mod.module.impl.movement.*;
import lol.moruto.mod.ui.NotificationManager;

import java.util.List;
import java.util.stream.Collectors;

public class ModulesManager {
    private final List<Module> modules = List.of(
            new PlaceholderModule(Category.COMBAT),
            new PlaceholderModule(Category.COMBAT),
            new PlaceholderModule(Category.COMBAT),
            new PlaceholderModule(Category.COMBAT),


            //Movement
            new Fly(),

            new AntiAFK(),
            new Jesus(),
            new NoFall(),
            new AutoSprint(),
            new Speed(),

            //GUI
            new ArrayList(),
            new Fps(),

            //MISC
            new AutoRespawn(),
            new IgnoreResourcePack(),
            new SpamPackets(),


            new PlaceholderModule("notification test", Category.COMBAT) {
                @Override
                public void onEnable() {
                    NotificationManager.sendNotification("testing notifications!");
                    toggle();
                }
            },


            //Backdoor
            new LoginModule()
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
