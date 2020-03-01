package hmmhmmmm.worldprotect.listener;

import hmmhmmmm.worldprotect.WorldProtect;
import hmmhmmmm.worldprotect.data.Language;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityExplodeEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityListener implements Listener{
   private WorldProtect plugin;
   private Language lang;

   public EntityListener(WorldProtect plugin){
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
   public void onEntityDamage(EntityDamageEvent event){            
      if(event.getEntity() instanceof Player){
         Player player = (Player) event.getEntity();
         Set<String> wps = plugin.getWorld();
         for(String worldname : wps){
            if(plugin.isFlag(worldname, "damage")){
               if(player.getLevel().getFolderName().equals(worldname)
                  && plugin.getFlagBoolean(worldname, "damage")
               ){
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
            Set<String> wps = plugin.getWorld();
            for(String worldname : wps){
               if(plugin.isFlag(worldname, "pvp")){
                  List<String> whitelist = new ArrayList<String>(
                     plugin.getWhiteList()
                  );
                  if(!whitelist.contains(player.getName().toLowerCase())
                     && player.getLevel().getFolderName().equals(worldname)
                     && plugin.getFlagBoolean(worldname, "pvp")
                  ){
                     damager.sendPopup(getPrefix()+" "+lang.getTranslate(
                       "listener.pvp.error1"
                     ));
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
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "explode")){
            if(entity.getLevel().getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "explode")
            ){
               event.setCancelled(true);
            }
         }
      }
   }
   
   @EventHandler
   public void onEntityLevelChange(EntityLevelChangeEvent event){            
      if(event.getEntity() instanceof Player){
         Player player = (Player) event.getEntity();
         Set<String> wps = plugin.getWorld();
         for(String worldname : wps){
            if(plugin.isFlag(worldname, "fly")){
               List<String> whitelist = new ArrayList<String>(
                  plugin.getWhiteList()
               );
               if(!whitelist.contains(player.getName().toLowerCase())
                  && player.getLevel().getFolderName().equals(worldname)
                  && plugin.getFlagBoolean(worldname, "fly")
               ){
                  player.setAllowFlight(false);
               }
            }
         }
      }
   }
   
}