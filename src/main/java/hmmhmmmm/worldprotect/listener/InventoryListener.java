package hmmhmmmm.worldprotect.listener;

import hmmhmmmm.worldprotect.WorldProtect;
import hmmhmmmm.worldprotect.data.Language;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InventoryListener implements Listener{
   private WorldProtect plugin;
   private Language lang;

   public InventoryListener(WorldProtect plugin){
      this.plugin = plugin;
      this.lang = (Language) plugin.getLanguage();
   }
   public WorldProtect getPlugin(){
      return plugin;
   }
   public String getPrefix(){
      return plugin.getPrefix();
   }
}