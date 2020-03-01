package hmmhmmmm.worldprotect.listener;

import hmmhmmmm.worldprotect.WorldProtect;
import hmmhmmmm.worldprotect.data.Language;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerListener implements Listener{
   private WorldProtect plugin;
   private Language lang;

   public PlayerListener(WorldProtect plugin){
      this.plugin = plugin;
      this.lang = (Language) plugin.getLanguage();
   }
   public WorldProtect getPlugin(){
      return plugin;
   }
   public String getPrefix(){
      return plugin.getPrefix();
   }
   
   @EventHandler
   public void onPlayerBedEnter(PlayerBedEnterEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "bedenter")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "bedenter")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.bedenter.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler   public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "bucketempty")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "bucketempty")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.bucketempty.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
  
   @EventHandler
   public void onPlayerBucketFill(PlayerBucketFillEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "bucketfill")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "bucketfill")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.bucketfill.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
      
   @EventHandler
   public void onPlayerChat(PlayerChatEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "chat")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "chat")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.chat.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
      
   @EventHandler   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
      Player player = event.getPlayer();
      String message = event.getMessage();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "bancmd")){
            List<String> cmdLists = plugin.getFlagList(
               worldname,
               "bancmd"
            );
            if(cmdLists.size() == 0){
               return;
            }
            for(String cmdList : cmdLists){
               List<String> whitelist = new ArrayList<String>(
                  plugin.getWhiteList()
               );
               if(!whitelist.contains(player.getName().toLowerCase())
                  && player.getLevel().getFolderName().equals(worldname)
                  && message.startsWith(cmdList)
               ){
                  player.sendMessage(getPrefix()+" "+lang.getTranslate(
                     "listener.bancmd.error1",
                     new String[]{message}
                  ));
                  event.setCancelled(true);
               }
            }
         }
      }
   }
   
   @EventHandler
   public void onPlayerDeath(PlayerDeathEvent event){
      Player player = event.getEntity();
      Set<String> wps = plugin.getWorld();
      if(player instanceof Player){
         for(String worldname : wps){
            if(plugin.isFlag(worldname, "keepinventory")){
               if(player.getLevel().getFolderName().equals(worldname)
                  && plugin.getFlagBoolean(worldname, "keepinventory")
               ){
                  event.setKeepInventory(true);
               }
            }
            if(plugin.isFlag(worldname, "keepexperience")){
               if(player.getLevel().getFolderName().equals(worldname)
                  && plugin.getFlagBoolean(worldname, "keepexperience")
               ){
                  event.setKeepExperience(true);
               }
            }
         }
      }
   }
   
   @EventHandler
   public void onPlayerEatFood(PlayerEatFoodEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "eatfood")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "eatfood")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.eatfood.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler
   public void onPlayerFoodLevelChange(PlayerFoodLevelChangeEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "food")){
            if(player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "food")
            ){
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler
   public void onPlayerDropItem(PlayerDropItemEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "item-drop")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "item-drop")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.item-drop.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event){
      Player player = event.getPlayer();
      Item item = event.getItem();
      Set<String> wps = plugin.getWorld();
      List<String> whitelist;
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "interact")){
            whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "interact")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.interact.error1"
               ));
               event.setCancelled(true);
            }
         }
         if(plugin.isFlag(worldname, "banitem")){
            List<String> itemLists = plugin.getFlagList(
               worldname,
               "banitem"
            );
            if(itemLists.size() == 0){
               return;
            }
            for(String itemList : itemLists){
               String[] items = itemList.split(":");
               int id = Integer.parseInt(items[0]);
               int damage = Integer.parseInt(items[1]);
               whitelist = new ArrayList<String>(
                  plugin.getWhiteList()
               );
               if(!whitelist.contains(player.getName().toLowerCase())
                  && player.getLevel().getFolderName().equals(worldname)
                  && item.getId() == id 
                  && item.getDamage() == damage
               ){
                  player.sendPopup(getPrefix()+" "+lang.getTranslate(
                     "listener.banitem.error1",
                     new String[]{item.getName()}
                  ));
                  event.setCancelled(true);
               }
            }
         }
      }
   }
   
   @EventHandler
   public void onPlayerMoveEvent(PlayerMoveEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "move")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "move")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.move.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler
   public void onPlayerTeleport(PlayerTeleportEvent event){
      Player player = event.getPlayer();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "teleport")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "teleport")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.teleport.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
   
}