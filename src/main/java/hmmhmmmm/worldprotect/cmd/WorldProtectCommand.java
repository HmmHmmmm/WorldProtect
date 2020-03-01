package hmmhmmmm.worldprotect.cmd;

import hmmhmmmm.worldprotect.WorldProtect;
import hmmhmmmm.worldprotect.data.Language;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WorldProtectCommand extends Command{
   private final WorldProtect plugin;
   private String name;
   private int id;
   private int damage;
   private String itemList;
   private List<String> itemLists;
   private String cmd;
   private List<String> cmdLists;
   private String playerName;
   private List<String> whiteLists;
   private Language lang;
      
   public WorldProtectCommand(WorldProtect plugin){
      super("worldprotect");
      this.plugin = plugin;
      this.lang = (Language) plugin.getLanguage();
      this.setPermission("worldprotect.command");
   }
   public String getPrefix(){
      return plugin.getPrefix();
   }
   public void sendConsoleError(CommandSender sender){
      sender.sendMessage(lang.getTranslate(
         "worldprotect.command.consoleError"
      ));
   }
   public void sendPermissionError(CommandSender sender){
      sender.sendMessage(lang.getTranslate(
         "worldprotect.command.permissionError"
      ));
   }
   public void sendHelp(CommandSender sender){
      sender.sendMessage(getPrefix()+" : §fCommand");
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.info.usage")+" : "+lang.getTranslate("worldprotect.command.info.description"));
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.event.usage")+" : "+lang.getTranslate("worldprotect.command.event.description"));
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.set.usage")+" : "+lang.getTranslate("worldprotect.command.set.description"));
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.banitem.usage")+" : "+lang.getTranslate("worldprotect.command.banitem.description"));
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.unbanitem.usage")+" : "+lang.getTranslate("worldprotect.command.unbanitem.description"));
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.bancmd.usage")+" : "+lang.getTranslate("worldprotect.command.bancmd.description"));
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.unbancmd.usage")+" : "+lang.getTranslate("worldprotect.command.unbancmd.description"));
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.whitelist.usage")+" : "+lang.getTranslate("worldprotect.command.whitelist.description"));
      sender.sendMessage("§a"+lang.getTranslate("worldprotect.command.unwhitelist.usage")+" : "+lang.getTranslate("worldprotect.command.unwhitelist.description"));
   }
   
   @Override
   public boolean execute(CommandSender sender, String commandLabel, String[] args){
      if(!this.testPermission(sender)){
         return true;
      }
      if(args.length == 0){
         if(sender instanceof Player){
            plugin.getWorldProtectForm().Menu((Player) sender);
            sender.sendMessage(lang.getTranslate(
               "worldprotect.command.sendHelp.empty"
            ));
         }else{
            sendHelp(sender);
         }
         return true;
      }
            
      switch(args[0].toLowerCase()){
         case "help":
            this.sendHelp(sender);
            break;
         case "info":
            sender.sendMessage(plugin.getPluginInfo());
            break;
         case "event":
            for(Map.Entry<String, String> map : plugin.eventMap.entrySet()){
               sender.sendMessage(lang.getTranslate(
                  "worldprotect.command.event.complete",
                  new String[]{map.getKey(), map.getValue()}
               ));
            }
            break;
         case "set":
            if(args.length < 4){
               sender.sendMessage(lang.getTranslate(
                  "worldprotect.command.set.error1",
                  new String[]{lang.getTranslate("worldprotect.command.set.usage")}
               ));
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.set.error2",
                  new String[]{name}
               ));
               return true;
            }
            if(!plugin.isset(name)){
               plugin.set(name);
            }
            String flag = args[2];
            if(!plugin.eventMap.containsKey(flag)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.set.error3",
                  new String[]{flag}
               ));
               return true;
            }
            plugin.setFlagBoolean(name, flag, Boolean.parseBoolean(args[3]));
            sender.sendMessage(getPrefix()+" "+lang.getTranslate(
               "worldprotect.command.set.complete",
               new String[]{name, flag, args[3]}
            ));
            break;
         case "banitem":
            if(args.length < 3){
               sender.sendMessage(lang.getTranslate(
                  "worldprotect.command.banitem.error1",
                  new String[]{lang.getTranslate("worldprotect.command.banitem.usage")}
               ));
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.banitem.error2",
                  new String[]{name}
               ));
               return true;
            }
            if(!plugin.isset(name)){
               plugin.set(name);
            }
            if(!plugin.isFlag(name, "banitem")){
               plugin.resetFlagList(name, "banitem");
            }
            try{
               id = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.banitem.error3"
               ));
               return true;
            }
            damage = args.length == 4 ? Integer.parseInt(args[3]) : 0;
            try{
               damage = damage;
            }catch(NumberFormatException e){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.banitem.error4"
               ));
               return true;
            }
            itemList = id+":"+damage;
            Item item;
            item = Item.fromString(itemList);
            if(item.getId() == 0){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.banitem.error5",
                  new String[]{itemList}
               ));
               return true;
            }
            if(item.getName().equals("Unknown")){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.banitem.error6",
                  new String[]{itemList}
               ));
               return true;
            }
            itemLists = new ArrayList<String>(plugin.getFlagList(name, "banitem"));
            if(itemLists.contains(itemList)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.banitem.error7",
                  new String[]{itemList}
               ));
               return true;
            }
            plugin.addFlagList(name, "banitem", itemList);
            sender.sendMessage(getPrefix()+" "+lang.getTranslate(
               "worldprotect.command.banitem.complete",
               new String[]{item.getName(), itemList, name}
            ));
            break;
         case "unbanitem":
            if(args.length < 3){
               sender.sendMessage(lang.getTranslate(
                  "worldprotect.command.unbanitem.error1",
                  new String[]{lang.getTranslate("worldprotect.command.unbanitem.usage")}
               ));
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unbanitem.error2",
                  new String[]{name}
               ));
               return true;
            }
            if(!plugin.isset(name)){
               plugin.set(name);
            }
            if(!plugin.isFlag(name, "banitem")){
               plugin.resetFlagList(name, "banitem");
            }
            try{
               id = Integer.parseInt(args[2]);
            }catch(NumberFormatException e){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unbanitem.error3"
               ));
               return true;
            }
            damage = args.length == 4 ? Integer.parseInt(args[3]) : 0;
            try{
               damage = damage;
            }catch(NumberFormatException e){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unbanitem.error4"
               ));
               return true;
            }
            itemList = id+":"+damage;
            itemLists = new ArrayList<String>(plugin.getFlagList(name, "banitem"));
            if(!itemLists.contains(itemList)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unbanitem.error5",
                  new String[]{itemList}
               ));
               return true;
            }
            plugin.removeFlagList(name, "banitem", itemList);
            sender.sendMessage(getPrefix()+" "+lang.getTranslate(
               "worldprotect.command.unbanitem.complete",
               new String[]{itemList, name}
            ));
            break;
         case "bancmd":
            if(args.length < 3){
               sender.sendMessage(lang.getTranslate(
                  "worldprotect.command.bancmd.error1", 
                  new String[]{lang.getTranslate("worldprotect.command.bancmd.usage")}
               ));
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.bancmd.error2",
                  new String[]{name}
               ));
               return true;
            }
            if(!plugin.isset(name)){
               plugin.set(name);
            }
            if(!plugin.isFlag(name, "bancmd")){
               plugin.resetFlagList(name, "bancmd");
            }
            cmd = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            cmdLists = new ArrayList<String>(plugin.getFlagList(name, "bancmd"));
            if(cmdLists.contains(cmd)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.bancmd.error3",
                  new String[]{cmd}
               ));
               return true;
            }
            plugin.addFlagList(name, "bancmd", cmd);
            sender.sendMessage(getPrefix()+" "+lang.getTranslate(
               "worldprotect.command.bancmd.complete",
               new String[]{cmd, name}
            ));
            break;
         case "unbancmd":
            if(args.length < 3){
               sender.sendMessage(lang.getTranslate(
                  "worldprotect.command.unbancmd.error1",
                  new String[]{lang.getTranslate("worldprotect.command.unbancmd.usage")}
               ));
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unbancmd.error2",
                  new String[]{name}
               ));
               return true;
            }
            if(!plugin.isset(name)){
               plugin.set(name);
            }
            if(!plugin.isFlag(name, "bancmd")){
               plugin.resetFlagList(name, "bancmd");
            }
            cmd = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            cmdLists = new ArrayList<String>(plugin.getFlagList(name, "bancmd"));
            if(!cmdLists.contains(cmd)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unbancmd.error3",
                  new String[]{cmd}
               ));
               return true;
            }
            plugin.removeFlagList(name, "bancmd", cmd);
            sender.sendMessage(getPrefix()+" "+lang.getTranslate(
               "worldprotect.command.unbancmd.complete",
               new String[]{cmd, name}
            ));
            break;
         case "whitelist":
            if(args.length < 2){
               sender.sendMessage(lang.getTranslate(
                  "worldprotect.command.whitelist.error1",
                  new String[]{lang.getTranslate("worldprotect.command.whitelist.usage")}
               ));
               return true;
            }
            playerName = args[1];
            playerName = playerName.toLowerCase();
            whiteLists = new ArrayList<String>(plugin.getWhiteList());
            if(whiteLists.contains(playerName)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.whitelist.error2",
                  new String[]{playerName}
               ));
               return true;
            }
            plugin.addWhiteList(playerName);
            sender.sendMessage(getPrefix()+" "+lang.getTranslate(
               "worldprotect.command.whitelist.complete",
               new String[]{playerName}
            ));
            break;
         case "unwhitelist":
            if(args.length < 2){
               sender.sendMessage(lang.getTranslate(
                  "worldprotect.command.unwhitelist.error1",
                  new String[]{lang.getTranslate("worldprotect.command.unwhitelist.usage")}
               ));
               return true;
            }
            playerName = args[1];
            playerName = playerName.toLowerCase();
            whiteLists = new ArrayList<String>(plugin.getWhiteList());
            if(!whiteLists.contains(playerName)){
               sender.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unwhitelist.error2",
                  new String[]{playerName}
               ));
               return true;
            }
            plugin.removeWhiteList(playerName);
            sender.sendMessage(getPrefix()+" "+lang.getTranslate(
               "worldprotect.command.unwhitelist.complete",
               new String[]{playerName}
            ));
            break;
         default:
            sendHelp(sender);
            break;
      }
      return true;
   }
}