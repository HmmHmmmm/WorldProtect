package hmmhmmmm.worldprotect;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.level.ThunderChangeEvent;
import cn.nukkit.event.level.WeatherChangeEvent;
import cn.nukkit.event.player.PlayerBedEnterEvent;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import cn.nukkit.event.player.PlayerBucketFillEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerEatFoodEvent;
import cn.nukkit.event.player.PlayerFoodLevelChangeEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.Config;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WorldProtect extends PluginBase implements Listener{
   public static WorldProtect obj = null;
   private Config data = null;
   private FormListener form;
   private String prefix = "?";
   private String facebook = "ไม่มี";
   private String youtube = "ไม่มี";
   
   public Map<String, String> event = new HashMap<>();
   
   public List<String> eventInfo = Arrays.asList(
      "place:วางบล็อก",
      "break:ทุบบล็อก",
      "interact:แตะบล็อก",
      "damage:อมตะ",
      "pvp:ต่อสู่",
      "explode:ระเบิดบล็อกกระจาย",
      "weather:ฝนตก",
      "item-drop:ทิ้งไอเทม",
      "move:เดิน",
      "fly:บิน",
      "keepinventory:ของตก",
      "keepexperience:เอ็กพีตก",
      "bucketfill:เทถัง",
      "bucketempty:ตักถัง",
      "chat:แชท",
      "bedenter:นอน",
      "eatfood:กินอาหาร",
      "food:หลอดอาหาร",
      "teleport:วาร์ป",
      "itemframe-drop:ไอเทมโชว์ตก"
   );
   
   public static WorldProtect getThis(){
      return obj;   }
   
   public void onLoad(){
      obj = this;
   }
   
   public void onEnable(){
      getDataFolder().mkdirs();
      data = new Config(new File(getDataFolder(), "worldprotect.yml"), Config.YAML);
      if(!data.exists("whitelist")){
         data.set("whitelist", new ArrayList<String>());
         data.save();
      }
      this.form = new FormListener(this);
      this.prefix = "WorldProtect";
      this.facebook = "https://m.facebook.com/phonlakrit.knaongam.1";
      this.youtube = "https://m.youtube.com/channel/UCtjvLXDxDAUt-8CXV1eWevA";
      getServer().getCommandMap().register("worldprotect", new WorldProtectCommand("worldprotect", this));
      getServer().getPluginManager().registerEvents(this, this);
      getServer().getPluginManager().registerEvents(this.form, this);
      for(String list : eventInfo){
         String[] arr = list.split(":");
         String key = arr[0];
         String value = arr[1];
         event.put(key, value);
      }
      getLogger().info(getPluginInfo());
   }      
   public Config getData(){
      return data;   }
   public FormListener getForm(){
      return form;   }
   public String getPrefix(){
      return "§e[§b"+this.prefix+"§e]§f";
   }
   public String getFacebook(){
      return this.facebook;
   } 
   public String getYoutube(){
      return this.youtube;
   } 
   public String getPluginInfo(){
      List<String> author = new ArrayList<String>(getDescription().getAuthors());
      String text = "\n"+getPrefix()+" ชื่อปลั๊กอิน "+getDescription().getName()+"\n"+getPrefix()+" เวอร์ชั่น "+getDescription().getVersion()+"\n"+getPrefix()+" รายชื่อผู้สร้าง "+String.join(", ", author)+"\n"+getPrefix()+" คำอธิบายของปลั๊กอิน: ปลั๊กอินนี้ทำแจก โปรดอย่าเอาไปขาย *หากจะเอาไปแจกต่อโปรดให้เครดิตด้วย*\n"+getPrefix()+" เฟสบุ๊ค "+getFacebook()+"\n"+getPrefix()+" ยูทูป "+getYoutube()+"\n"+getPrefix()+" เว็บไซต์ "+getDescription().getWebsite();
      return text;   }
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
      data.set("worlds."+name+".explode", true);
      data.set("worlds."+name+".weather", true);
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
   @EventHandler
   public void onBlockBreak(BlockBreakEvent event){            
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "break")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "break")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถทุบบล็อกบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onBlockPlace(BlockPlaceEvent event){            
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "place")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "place")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถวางบล็อกบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onItemFrameDropItem(ItemFrameDropItemEvent event){            
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "itemframe-drop")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "itemframe-drop")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถเอาไอเท็มโชว์ออกบนโลกนี้ได้้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onEntityDamage(EntityDamageEvent event){            
      if(event.getEntity() instanceof Player){
         Player player = (Player) event.getEntity();
         Set<String> wps = getWorld();
         for(String worldname : wps){
            if(isFlag(worldname, "damage")){
               if(player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "damage")){
                  event.setCancelled(true);
               }
            }
         }
      }
   }
   @EventHandler
   public void onEntityDamageByEntity(EntityDamageByEntityEvent event){           
      if(event.getDamager() instanceof Player){
         Player damager = (Player) event.getDamager();                  
         if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            Set<String> wps = getWorld();
            for(String worldname : wps){
               if(isFlag(worldname, "pvp")){
                  List<String> whitelist = new ArrayList<String>(getWhiteList());
                  if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "pvp")){
                     damager.sendPopup(getPrefix()+" §cคุณไม่สามารถตีผู้เล่นบนโลกนี้ได้");
                     event.setCancelled(true);
                  }
               }
            }
         }
      }
   }
   @EventHandler
   public void onEntityExplode(EntityExplodeEvent event){
      Entity entity = event.getEntity();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "explode")){
            if(entity.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "explode")){
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onEntityLevelChange(EntityLevelChangeEvent event){            
      if(event.getEntity() instanceof Player){
         Player player = (Player) event.getEntity();
         Set<String> wps = getWorld();
         for(String worldname : wps){
            if(isFlag(worldname, "fly")){
               List<String> whitelist = new ArrayList<String>(getWhiteList());
               if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "fly")){
                  player.setAllowFlight(false);
               }
            }
         }
      }
   }
   @EventHandler
   public void onLevelLoad(LevelLoadEvent event){
      Level level = event.getLevel();
      if(!isset(level.getFolderName())){
         set(level.getFolderName());
      }
   }
   @EventHandler
   public void onWeatherChange(WeatherChangeEvent event){
      Level level = event.getLevel();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "weather")){
            if(level.getFolderName().equals(worldname) && getFlagBoolean(worldname, "weather")){
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onThunderChange(ThunderChangeEvent event){
      Level level = event.getLevel();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "weather")){
            if(level.getFolderName().equals(worldname) && getFlagBoolean(worldname, "weather")){
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onPlayerBedEnter(PlayerBedEnterEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "bedenter")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "bedenter")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถนอนบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler   public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "bucketempty")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "bucketempty")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถตักถังบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onPlayerBucketFill(PlayerBucketFillEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "bucketfill")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "bucketfill")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถเทถังบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onPlayerChat(PlayerChatEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "chat")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "chat")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถพิมแชทบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
      Player player = event.getPlayer();
      String message = event.getMessage();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "bancmd")){
            List<String> cmdLists = getFlagList(worldname, "bancmd");
            if(cmdLists.size() == 0){
               return;
            }
            for(String cmdList : cmdLists){
               List<String> whitelist = new ArrayList<String>(getWhiteList());
               if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && message.startsWith(cmdList)){
                  player.sendMessage(getPrefix()+" §cคุณไม่สามารถพิมคำสั่ง "+message+" บนโลกนี้ได้");
                  event.setCancelled(true);
               }
            }
         }
      }
   }
   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event){
      Player player = event.getEntity();
      Set<String> wps = getWorld();
      if(player instanceof Player){
         for(String worldname : wps){
            if(isFlag(worldname, "keepinventory")){
               if(player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "keepinventory")){
                  event.setKeepInventory(true);
               }
            }
            if(isFlag(worldname, "keepexperience")){
               if(player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "keepexperience")){
                  event.setKeepExperience(true);
               }
            }
         }
      }
   }
   @EventHandler
   public void onPlayerEatFood(PlayerEatFoodEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "eatfood")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "eatfood")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถกินอาหารบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onPlayerFoodLevelChange(PlayerFoodLevelChangeEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "food")){
            if(player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "food")){
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onPlayerDropItem(PlayerDropItemEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "item-drop")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "item-drop")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถทิ้งของบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event){
      Player player = event.getPlayer();
      Item item = event.getItem();
      Set<String> wps = getWorld();
      List<String> whitelist;
      for(String worldname : wps){
         if(isFlag(worldname, "interact")){
            whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "interact")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถใช้ของบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
         if(isFlag(worldname, "banitem")){
            List<String> itemLists = getFlagList(worldname, "banitem");
            if(itemLists.size() == 0){
               return;
            }
            for(String itemList : itemLists){
               String[] items = itemList.split(":");
               int id = Integer.parseInt(items[0]);
               int damage = Integer.parseInt(items[1]);
               whitelist = new ArrayList<String>(getWhiteList());
               if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && item.getId() == id && item.getDamage() == damage){
                  player.sendPopup(getPrefix()+" §cไอเทม "+item.getName()+" ถูกแบนบนโลกนี้");
                  event.setCancelled(true);
               }
            }
         }
      }
   }
   @EventHandler
   public void onPlayerMoveEvent(PlayerMoveEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "move")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "move")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถเดินบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
   @EventHandler
   public void onPlayerTeleport(PlayerTeleportEvent event){
      Player player = event.getPlayer();
      Set<String> wps = getWorld();
      for(String worldname : wps){
         if(isFlag(worldname, "teleport")){
            List<String> whitelist = new ArrayList<String>(getWhiteList());
            if(!whitelist.contains(player.getName().toLowerCase()) && player.getLevel().getFolderName().equals(worldname) && getFlagBoolean(worldname, "teleport")){
               player.sendPopup(getPrefix()+" §cคุณไม่สามารถวาร์ปบนโลกนี้ได้");
               event.setCancelled(true);
            }
         }
      }
   }
}