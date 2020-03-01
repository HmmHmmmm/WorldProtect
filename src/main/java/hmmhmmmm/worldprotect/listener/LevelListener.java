package hmmhmmmm.worldprotect.listener;

import hmmhmmmm.worldprotect.WorldProtect;
import hmmhmmmm.worldprotect.data.Language;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.level.ThunderChangeEvent;
import cn.nukkit.event.level.WeatherChangeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LevelListener implements Listener{
   private WorldProtect plugin;
   private Language lang;

   public LevelListener(WorldProtect plugin){
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
   public void onLevelLoad(LevelLoadEvent event){
      Level level = event.getLevel();
      
   }
   
   @EventHandler
   public void onWeatherChange(WeatherChangeEvent event){
      Level level = event.getLevel();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "weather")){
            if(level.getFolderName().equals(worldname) 
               && plugin.getFlagBoolean(worldname, "weather")
            ){
               event.setCancelled(true);
            }
         }
      }
   }
  
   @EventHandler
   public void onThunderChange(ThunderChangeEvent event){
      Level level = event.getLevel();
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         if(plugin.isFlag(worldname, "weather")){
            if(level.getFolderName().equals(worldname)
               && plugin.getFlagBoolean(worldname, "weather")
            ){
               event.setCancelled(true);
            }
         }
      }
   }
   
}