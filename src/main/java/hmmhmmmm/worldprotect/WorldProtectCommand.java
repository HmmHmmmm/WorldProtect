package hmmhmmmm.worldprotect;

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
      
   public WorldProtectCommand(String name, WorldProtect plugin){
      super(name);
      this.plugin = plugin;
      this.setPermission("worldprotect.command.worldprotect");
   }
   public String getPrefix(){
      return plugin.getPrefix();
   }
   public void sendConsoleError(CommandSender sender){
      sender.sendMessage("§cขออภัย: คำสั่งสามารถพิมพ์ได้เฉพาะในเกมส์");
   }
   public void sendPermissionError(CommandSender sender){
      sender.sendMessage("§cขออภัย: คุณไม่สามารถพิมพ์คำสั่งนี้ได้");
   }
   public void sendHelp(CommandSender sender){
      sender.sendMessage(getPrefix()+" : §fCommand");
      sender.sendMessage("§a/worldprotect info : §fเครดิตผู้สร้างปลั๊กอิน");
      sender.sendMessage("§a/worldprotect event : §fดูอีเว้นท์ต่างๆ");
      sender.sendMessage("§a/worldprotect set <worldname> <event> <true:false> : §fโพรเทตต่างๆ");
      sender.sendMessage("§a/worldprotect banitem <worldname> <itemId> <itemDamage> : §fแบนไอเทม");
      sender.sendMessage("§a/worldprotect unbanitem <worldname> <itemId> <itemDamage> : §fปลดแบนไอเทม");
      sender.sendMessage("§a/worldprotect bancmd <worldname> <command> : §fแบนคำสั่ง");
      sender.sendMessage("§a/worldprotect unbancmd <worldname> <command> : §fปลดแบนคำสั่ง");
      sender.sendMessage("§a/worldprotect whitelist <playerName> : §fอนุญาตให้ผู้เล่นใช้อีเว้นท์ต่างๆ");
      sender.sendMessage("§a/worldprotect unwhitelist <playerName> : §fปลดผู้เล่นไม่ให้ใช้อีเว้นท์ต่างๆ");
   }
   
   @Override
   public boolean execute(CommandSender sender, String commandLabel, String[] args){
      if(!this.testPermission(sender)){
         return true;
      }
      if(args.length == 0){
         if(sender instanceof Player){
            plugin.getForm().Menu((Player) sender);
            sender.sendMessage("§eคุณสามารถดูคำสั่งเพิ่มเติมได้โดยใช้ /worldprotect help");
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
            for(Map.Entry<String, String> map : plugin.event.entrySet()){
               String key = map.getKey();
               String value = map.getValue();
               sender.sendMessage("อีเว้นท์ §a"+key+" ("+value+")");
            }
            break;
         case "set":
            if(args.length < 4){
               sender.sendMessage("§cลอง: /worldprotect set <worldname> <event> <true:false>");
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" §cไม่พบชื่อโลก "+name+" หรือโลกไม่ได้ load");
               return true;
            }
            if(!plugin.isset(name)){
               plugin.set(name);
            }
            String flag = args[2];
            if(!plugin.event.containsKey(flag)){
               sender.sendMessage(getPrefix()+" §cไม่พบอีเว้นท์ "+flag);
               return true;
            }
            plugin.setFlagBoolean(name, flag, Boolean.parseBoolean(args[3]));
            sender.sendMessage(getPrefix()+" ชื่อโลก "+name+" ได้เซ็ต "+flag+" เป็น "+args[3]+" เรียบร้อย");
            break;
         case "banitem":                        
            if(args.length < 3){
               sender.sendMessage("§cลอง: /worldprotect banitem <worldname> <id> <damage>");
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" §cไม่พบชื่อโลก "+name+" หรือโลกไม่ได้ load");
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
               sender.sendMessage(getPrefix()+" §c<id> กรุณาเขียนให้เป็นตัวเลข");
               return true;
            }
            damage = args.length == 4 ? Integer.parseInt(args[3]) : 0;
            try{
               damage = damage;
            }catch(NumberFormatException e){
               sender.sendMessage(getPrefix()+" §c<damage> กรุณาเขียนให้เป็นตัวเลข");
               return true;
            }
            itemList = id+":"+damage;
            Item item;
            item = Item.fromString(itemList);
            if(item.getId() == 0){
               sender.sendMessage(getPrefix()+" §cไม่สามารถแบนไอเทม "+itemList+" ได้");
               return true;
            }
            if(item.getName().equals("Unknown")){
               sender.sendMessage(getPrefix()+" §cไอเทม "+itemList+" ไม่ได้อยู่ในเกมส์");
               return true;
            }
            itemLists = new ArrayList<String>(plugin.getFlagList(name, "banitem"));
            if(itemLists.contains(itemList)){
               sender.sendMessage(getPrefix()+" §cไอเทม "+itemList+" ได้แบนอยู่แล้ว");
               return true;
            }
            plugin.addFlagList(name, "banitem", itemList);
            sender.sendMessage(getPrefix()+" ได้แบนไอเทม "+item.getName()+" "+itemList+" ในโลก "+name+" เรียบร้อย");
            break;
         case "unbanitem":
            if(args.length < 3){
               sender.sendMessage("§cลอง: /worldprotect banitem <worldname> <id> <damage>");
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" §cไม่พบชื่อโลก "+name+" หรือโลกไม่ได้ load");
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
               sender.sendMessage(getPrefix()+" §c<id> กรุณาเขียนให้เป็นตัวเลข");
               return true;
            }
            damage = args.length == 4 ? Integer.parseInt(args[3]) : 0;
            try{
               damage = damage;
            }catch(NumberFormatException e){
               sender.sendMessage(getPrefix()+" §c<damage> กรุณาเขียนให้เป็นตัวเลข");
               return true;
            }
            itemList = id+":"+damage;
            itemLists = new ArrayList<String>(plugin.getFlagList(name, "banitem"));
            if(!itemLists.contains(itemList)){
               sender.sendMessage(getPrefix()+" §cไม่พบไอเทม "+itemList+" ที่แบนอยู่");
               return true;
            }
            plugin.removeFlagList(name, "banitem", itemList);
            sender.sendMessage(getPrefix()+" ได้ปลดแบนไอเทม "+itemList+" ในโลก "+name+" เรียบร้อย");
            break;
         case "bancmd":
            if(args.length < 3){
               sender.sendMessage("§cลอง: /worldprotect bancmd <worldname> <command>");
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" §cไม่พบชื่อโลก "+name+" หรือโลกไม่ได้ load");
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
               sender.sendMessage(getPrefix()+" §cคำสั่ง "+cmd+" ได้แบนอยู่แล้ว");
               return true;
            }
            plugin.addFlagList(name, "bancmd", cmd);
            sender.sendMessage(getPrefix()+" แบนคำสั่ง "+cmd+" ในโลก "+name+" เรียบร้อย");
            break;
         case "unbancmd":
            if(args.length < 3){
               sender.sendMessage("§cลอง: /worldprotect unbancmd <worldname> <command>");
               return true;
            }
            name = args[1];
            if(!plugin.getServer().isLevelLoaded(name)){
               sender.sendMessage(getPrefix()+" §cไม่พบชื่อโลก "+name+" หรือโลกไม่ได้ load");
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
               sender.sendMessage(getPrefix()+" §cไม่พบคำสั่ง "+cmd+" ที่แบนอยู่");
               return true;
            }
            plugin.removeFlagList(name, "bancmd", cmd);
            sender.sendMessage(getPrefix()+" ได้ปลดแบนคำสั่ง "+cmd+" ในโลก "+name+" เรียบร้อย");
            break;
         case "whitelist":
            if(args.length < 2){
               sender.sendMessage("§cลอง: /worldprotect addwhitelist <playerName>");
               return true;
            }
            playerName = args[1];
            playerName = playerName.toLowerCase();
            whiteLists = new ArrayList<String>(plugin.getWhiteList());
            if(whiteLists.contains(playerName)){
               sender.sendMessage(getPrefix()+" §cชื่อผู้เล่น "+playerName+" ได้มีอยู่แล้ว");
               return true;
            }
            plugin.addWhiteList(playerName);
            sender.sendMessage(getPrefix()+" อนุญาตให้ผู้เล่น "+playerName+" ใช้อีเว้นท์ต่างๆเรียบร้อย");
            break;
         case "unwhitelist":
            if(args.length < 2){
               sender.sendMessage("§cลอง: /worldprotect unwhitelist <playerName>");
               return true;
            }
            playerName = args[1];
            playerName = playerName.toLowerCase();
            whiteLists = new ArrayList<String>(plugin.getWhiteList());
            if(!whiteLists.contains(playerName)){
               sender.sendMessage(getPrefix()+" §cไม่พบชื่อผู้เล่น "+playerName);
               return true;
            }
            plugin.removeWhiteList(playerName);
            sender.sendMessage(getPrefix()+" ได้ปลดผู้เล่น "+playerName+" ไม่ให้ใช้อีเว้นท์ต่างๆเรียบร้อย");
            break;
         default:
            sendHelp(sender);
            break;
      }
      return true;
   }
}