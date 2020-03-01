package hmmhmmmm.worldprotect;

import hmmhmmmm.worldprotect.cmd.WorldProtectCommand;
import hmmhmmmm.worldprotect.data.Language;
import hmmhmmmm.worldprotect.listener.BlockListener;
import hmmhmmmm.worldprotect.listener.EntityListener;
import hmmhmmmm.worldprotect.listener.InventoryListener;
import hmmhmmmm.worldprotect.listener.LevelListener;
import hmmhmmmm.worldprotect.listener.PlayerListener;
import hmmhmmmm.worldprotect.ui.WorldProtectForm;

import cn.nukkit.command.Command;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;

public class WorldProtect extends PluginBase{
   private String prefix = "?";
   private String facebook = "§cwithout";
   private String youtube = "§cwithout";
   private String discord = "§cwithout";
   private Config data = null;
   private WorldProtectForm form;
   private Language language = null;
   
   private List<String> langClass = Arrays.asList(
      "english",
      "thai"
   );
   
   public Map<String, String> eventMap = new TreeMap<String, String>();
   
   
   public void onEnable(){
      getDataFolder().mkdirs();
      saveDefaultConfig();
      this.data = new Config(new File(getDataFolder(), "worldprotect.yml"), Config.YAML);
      if(!this.data.exists("whitelist")){
         this.data.set("whitelist", new ArrayList<String>());
         this.data.save();
      }
      File path = new File(getServer().getDataPath(), "worlds");
      String[] paths = path.list();
      for(String worldname : paths){
         if(!this.data.exists("worlds."+worldname)){
            set(worldname);
         }
      }
      this.prefix = "WorldProtect";
      this.facebook = "https://bit.ly/39ULjqk";
      this.youtube = "https://bit.ly/2HL1j28";
      this.discord = "https://discord.gg/n6CmNr";
      String langConfig = getConfig().getString("language");
      if(!langClass.contains(langConfig)){
         getLogger().error("§cNot found language "+langConfig+", Please try "+String.join(", ", langClass));
         getServer().getPluginManager().disablePlugin(this);
         return;
      }else{
         this.language = new Language(this, langConfig);
         this.form = new WorldProtectForm(this);
         getServer().getCommandMap().register("WorldProtect", new WorldProtectCommand(this));
         getServer().getPluginManager().registerEvents(new WorldProtectForm(this), this);
         getServer().getPluginManager().registerEvents(new BlockListener(this), this);
         getServer().getPluginManager().registerEvents(new EntityListener(this), this);
         getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
         getServer().getPluginManager().registerEvents(new LevelListener(this), this);
         getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
      }
      for(String eventList : getEventListBoolean()){
         String[] arr = eventList.split(":");
         eventMap.put(arr[0], arr[1]);
      }
   }      
   public Config getData(){
      return data;   }
   public WorldProtectForm getWorldProtectForm(){
      return form;   }
   public String getPrefix(){
      return "§e[§b"+prefix+"§e]§f";
   }
   public String getFacebook(){
      return facebook;
   } 
   public String getYoutube(){
      return youtube;
   } 
   public String getDiscord(){
      return discord;
   } 
   public Language getLanguage(){
      return language;   }
   public String getPluginInfo(){
      String author = String.join(", ", getDescription().getAuthors());
      List<String> arrayList = Arrays.asList(
         getPrefix()+" "+getLanguage().getTranslate("plugininfo.name", new String[]{getDescription().getName()}),
         getPrefix()+" "+getLanguage().getTranslate("plugininfo.version", new String[]{getDescription().getVersion()}),
         getPrefix()+" "+getLanguage().getTranslate("plugininfo.author", new String[]{author}),
         getPrefix()+" "+getLanguage().getTranslate("plugininfo.description"),
         getPrefix()+" "+getLanguage().getTranslate("plugininfo.facebook", new String[]{getFacebook()}),
         getPrefix()+" "+getLanguage().getTranslate("plugininfo.youtube", new String[]{getYoutube()}),
         getPrefix()+" "+getLanguage().getTranslate("plugininfo.website", new String[]{getDescription().getWebsite()}),
         getPrefix()+" "+getLanguage().getTranslate("plugininfo.discord", new String[]{getDiscord()})
      );
      return String.join("\n", arrayList);
   }
   public List<String> getEventListBoolean(){
      List<String> listBoolean = Arrays.asList(
         "place:"+getLanguage().getTranslate("event.place"),
         "break:"+getLanguage().getTranslate("event.break"),
         "interact:"+getLanguage().getTranslate("event.interact"),
         "damage:"+getLanguage().getTranslate("event.damage"),
         "pvp:"+getLanguage().getTranslate("event.pvp"),
         "explode:"+getLanguage().getTranslate("event.explode"),
         "weather:"+getLanguage().getTranslate("event.weather"),
         "item-drop:"+getLanguage().getTranslate("event.item-drop"),
         "move:"+getLanguage().getTranslate("event.move"),
         "fly:"+getLanguage().getTranslate("event.fly"),
         "keepinventory:"+getLanguage().getTranslate("event.keepinventory"),
         "keepexperience:"+getLanguage().getTranslate("event.keepexperience"),
         "bucketfill:"+getLanguage().getTranslate("event.bucketfill"),
         "bucketempty:"+getLanguage().getTranslate("event.bucketempty"),
         "chat:"+getLanguage().getTranslate("event.chat"),
         "bedenter:"+getLanguage().getTranslate("event.bedenter"),
         "eatfood:"+getLanguage().getTranslate("event.eatfood"),
         "food:"+getLanguage().getTranslate("event.food"),
         "teleport:"+getLanguage().getTranslate("event.teleport"),
         "itemframe-drop:"+getLanguage().getTranslate("event.itemframe-drop")
      );
      return listBoolean;   }
   public boolean isset(String name){
      return data.exists("worlds."+name);
   }
   public Set<String> getWorld(){
      Set<String> key = data.getSection("worlds").getKeys();
      key.removeIf(s -> s.contains("."));
      return key;
   }
   public void set(String name){
      data.set("worlds."+name+".place", false);
      data.set("worlds."+name+".break", false);
      data.set("worlds."+name+".interact", false);
      data.set("worlds."+name+".damage", false);
      data.set("worlds."+name+".pvp", false);
      data.set("worlds."+name+".explode", false);
      data.set("worlds."+name+".weather", false);
      data.set("worlds."+name+".item-drop", false);
      data.set("worlds."+name+".move", false);
      data.set("worlds."+name+".fly", false);
      data.set("worlds."+name+".keepinventory", false);
      data.set("worlds."+name+".keepexperience", false);
      data.set("worlds."+name+".bucketfill", false);
      data.set("worlds."+name+".bucketempty", false);
      data.set("worlds."+name+".chat", false);
      data.set("worlds."+name+".bedenter", false);
      data.set("worlds."+name+".eatfood", false);
      data.set("worlds."+name+".food", false);
      data.set("worlds."+name+".teleport", false);
      data.set("worlds."+name+".itemframe-drop", false);
      data.set("worlds."+name+".banitem", new ArrayList<String>());
      data.set("worlds."+name+".bancmd", new ArrayList<String>());
      data.save();
   }
   public void edit(String name, Map<String, Boolean> eventBoolean){
      for(Map.Entry<String, Boolean> map : eventBoolean.entrySet()){
         String key = map.getKey();
         Boolean value = map.getValue();
         if(isFlag(name, key)){
            data.set("worlds."+name+"."+key, value);
         }
      }
      data.save();
   }
   public void remove(String name){
      Map<String, Object> map = (Map<String, Object>) data.get("worlds");
      if(map == null){
         return;
      }
      map.remove(name);
      data.set("worlds", map);
      data.save();
   }
   public boolean isFlag(String name, String flag){
      return data.exists("worlds."+name+"."+flag);
   }
   public boolean getFlagBoolean(String name, String flag){
      return (boolean) data.get("worlds."+name+"."+flag);
   }
   public void setFlagBoolean(String name, String flag, boolean enabled){
      data.set("worlds."+name+"."+flag, enabled);
      data.save();
   }
   public List<String> getFlagList(String name, String flag){
      return (List) data.get("worlds."+name+"."+flag);
   }
   public void addFlagList(String name, String flag, String list){
      List<String> array = new ArrayList<String>(getFlagList(name, flag));
      array.add(list);
      data.set("worlds."+name+"."+flag, array);
      data.save();
   }
   public void resetFlagList(String name, String flag){
      data.set("worlds."+name+"."+flag, new ArrayList<String>());
      data.save();
   }
   public void removeFlagList(String name, String flag, String list){
      List<String> array = new ArrayList<String>(getFlagList(name, flag));
      array.remove(list);
      data.set("worlds."+name+"."+flag, array);
      data.save();
   }
   public List<String> getWhiteList(){
      return (List) data.get("whitelist");
   }
   public void addWhiteList(String list){
      List<String> array = new ArrayList<String>(getWhiteList());
      array.add(list);
      data.set("whitelist", array);
      data.save();
   }
   public void removeWhiteList(String list){
      List<String> array = new ArrayList<String>(getWhiteList());
      array.remove(list);
      data.set("whitelist", array);
      data.save();
   }
   
   
}