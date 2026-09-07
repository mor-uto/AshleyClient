package lol.moruto.client.module.impl.misc;

import lol.moruto.client.module.Category;
import lol.moruto.client.module.Module;
import lol.moruto.client.module.impl.setting.StringSetting;

public class ClientBrand extends Module {
    public ClientBrand() {
        super("ClientBrand", "Customize your client's server side name", Category.MISC);
        addSetting(new StringSetting("Brand name", "AshleyClient"));
    }
}
