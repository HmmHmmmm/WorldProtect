package hmmhmmmm.worldprotect.listener;

import hmmhmmmm.worldprotect.WorldProtect;
import hmmhmmmm.worldprotect.data.Language;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.ItemFrameDropItemEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BlockListener implements Listener{
   private WorldProtect plugin;
   private Language lang;

   public BlockListener(WorldProtect plugin){
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
   public void onBlockBreak(BlockBreakEvent event){
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "break")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "break")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.blockbreak.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler
   public void onBlockPlace(BlockPlaceEvent event){
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "place")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "place")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.blockplace.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
  
   @EventHandler
   public void onItemFrameDropItem(ItemFrameDropItemEvent event){
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "itemframe-drop")){
            List<String> whitelist = new ArrayList<String>(
               plugin.getWhiteList()
            );
            if(!whitelist.contains(player.getName().toLowerCase())
               && player.getLevel().getFolderName().equals(worldname) 
               && plugin.getFlagBoolean(worldname, "itemframe-drop")
            ){
               player.sendPopup(getPrefix()+" "+lang.getTranslate(
                  "listener.itemframedrop.error1"
               ));
               event.setCancelled(true);
            }
         }
      }
   }
   
}