package hmmhmmmm.worldprotect;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.element.ElementStepSlider;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FormListener implements Listener{
   private WorldProtect plugin;
   
   static final int MENU = 0xAAA001;
   static final int WHITELIST = 0xAAA002;
   static final int EDIT = 0xAAA003;
   static final int EDIT2 = 0xAAA004;
   static final int BANITEM = 0xAAA005;
   static final int BANCMD = 0xAAA006;
   
   public FormListener(WorldProtect plugin){
      this.plugin = plugin;
   }
   public WorldProtect getPlugin(){
      return plugin;
   }
   public String getPrefix(){
      return plugin.getPrefix();
   }
   
   @EventHandler
   public void onFormResponse(PlayerFormRespondedEvent event){
      Player player = event.getPlayer();
      if(event.getWindow() instanceof FormWindowSimple){
         FormWindowSimple window = (FormWindowSimple) event.getWindow();
         String button;
         if(!event.wasClosed()){
            button = window.getResponse().getClickedButton().getText();
         }else{
            button = "";
         }
         if(event.getFormID() == MENU){
            if(event.wasClosed()){
               return;
            }
            switch(button){
               case "§fwhitelist":
                  Whitelist(player, "");
                  break;
               default:
                  Edit(player, button);
                  break;
            }
         }
         if(event.getFormID() == EDIT){
            if(event.wasClosed()){
               return;
            }
            String title = window.getTitle();
            String[] arr = title.split(" ");
            switch(window.getResponse().getClickedButtonId()){
               case 0:
                  if(!plugin.isset(arr[2])){
                     plugin.set(arr[2]);
                  }
                  Edit2(player, arr[2]);
                  break;
               case 1:
                  BanItem(player, arr[2], "");
                  break;
               case 2:
                  BanCmd(player, arr[2], "");
                  break;
               case 3:
                  plugin.set(arr[2]);
                  player.sendMessage(getPrefix()+" คุณได้รีเซ็ตการตั้งค่าทั้งหมดเรียบร้อย!");
                  break;
            }
         }
      }
      if(event.getWindow() instanceof FormWindowCustom){
         FormWindowCustom window = (FormWindowCustom) event.getWindow();
         if(event.getFormID() == WHITELIST){
            if(event.wasClosed()){
               return;
            }
            String message = window.getResponse().getInputResponse(2);
            String content = window.getResponse().getLabelResponse(0);
            List<String> whitelist = new ArrayList<String>(plugin.getWhiteList());
            String[] mArr = message.split(" ");
            if(window.getResponse().getDropdownResponse(1).getElementID() == 0){
               if(mArr[0] == ""){
                  Whitelist(player, "§cเกิดข้อผิดพลาด\n§eกรุณาอย่าว่างข้อความ");
                  return;
               }
               
               if(whitelist.contains(message.toLowerCase())){
                  Whitelist(player, "§cเกิดข้อผิดพลาด\n§eชื่อผู้เล่น "+message+" ได้มีอยู่แล้ว");
                  return;
               }
               plugin.addWhiteList(message.toLowerCase());
               player.sendMessage(getPrefix()+" อนุญาตให้ผู้เล่น "+message+" ใช้อีเว้นท์ต่างๆเรียบร้อย");
            }
            if(window.getResponse().getDropdownResponse(1).getElementID() == 1){
               if(mArr[0] == ""){
                  Whitelist(player, "§cเกิดข้อผิดพลาด\n§eกรุณาอย่าว่างข้อความ");
                  return;
               }
               if(!whitelist.contains(message.toLowerCase())){
                  Whitelist(player, "§cเกิดข้อผิดพลาด\n§eไม่พบชื่อผู้เล่น "+message);
                  return;
               }
               plugin.removeWhiteList(message.toLowerCase());
               player.sendMessage(getPrefix()+" ได้ปลดผู้เล่น "+message+" ไม่ให้ใช้อีเว้นท์ต่างๆเรียบร้อย");
            }
         }
         if(event.getFormID() == EDIT2){
            if(event.wasClosed()){
               return;
            }
            String title = window.getTitle();
            String[] arr = title.split(" ");
            String name = arr[2];
            int i = 0;
            Map<String, Boolean> eventBoolean = new HashMap<>();
            for(Map.Entry<String, String> map : plugin.event.entrySet()){
               String key = map.getKey();
               eventBoolean.put(key, window.getResponse().getToggleResponse(i));
               i++;
            }
            plugin.edit(name, eventBoolean);
            player.sendMessage(getPrefix()+" คุณได้แก้ไขโลก "+name+" เรียบร้อย");
         }
         if(event.getFormID() == BANITEM){
            if(event.wasClosed()){
               return;
            }
            String title = window.getTitle();
            String[] arr = title.split(" ");
            String name = arr[2];
            String itemId = window.getResponse().getInputResponse(2);
            String itemDamage = window.getResponse().getInputResponse(3);
            String content = window.getResponse().getLabelResponse(0);
            List<String> itemLists = new ArrayList<String>(plugin.getFlagList(name, "banitem"));
            String itemList;
            String[] idArr = itemId.split(" ");
            String[] damageArr = itemDamage.split(" ");
            int id;
            int damage;
            if(window.getResponse().getDropdownResponse(1).getElementID() == 0){
               if(idArr[0] == ""){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§e<itemId> จำเป็นต้องใส่");
                  return;
               }
               try{
                  id = Integer.parseInt(itemId);
               }catch(NumberFormatException e){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§e<itemId> กรุณาเขียนให้เป็นตัวเลข");
                  return;
               }
               if(damageArr[0] == ""){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§e<itemDamage> จำเป็นต้องใส่");
                  return;
               }
               try{
                  damage = Integer.parseInt(itemDamage);
               }catch(NumberFormatException e){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§e<itemDamage> กรุณาเขียนให้เป็นตัวเลข");
                  return;
               }
               itemList = id+":"+damage;
               Item item;
               item = Item.fromString(itemList);
               if(item.getId() == 0){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§eไม่สามารถแบนไอเทม "+itemList+" ได้");
                  return;
               }
               if(item.getName().equals("Unknown")){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§eไอเทม "+itemList+" ไม่ได้อยู่ในเกมส์");
                  return;
               }
               if(itemLists.contains(itemList)){
                  BanItem(player, name, "§cไอเทม "+itemList+" ได้แบนอยู่แล้ว");
                  return;
               }
               plugin.addFlagList(name, "banitem", itemList);
               player.sendMessage(getPrefix()+" ได้แบนไอเทม "+item.getName()+" "+itemList+" ในโลก "+name+" เรียบร้อย");
            }
            if(window.getResponse().getDropdownResponse(1).getElementID() == 1){
               if(idArr[0] == ""){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§e<itemId> จำเป็นต้องใส่");
                  return;
               }
               try{
                  id = Integer.parseInt(itemId);
               }catch(NumberFormatException e){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§e<itemId> กรุณาเขียนให้เป็นตัวเลข");
                  return;
               }
               if(damageArr[0] == ""){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§e<itemDamage> จำเป็นต้องใส่");
                  return;
               }
               try{
                  damage = Integer.parseInt(itemDamage);
               }catch(NumberFormatException e){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§e<itemDamage> กรุณาเขียนให้เป็นตัวเลข");
                  return;
               }
               itemList = id+":"+damage;
               if(!itemLists.contains(itemList)){
                  BanItem(player, name, "§cเกิดข้อผิดพลาด\n§eไม่พบไอเทม "+itemList+" ที่แบนอยู่");
                  return;
               }
               plugin.removeFlagList(name, "banitem", itemList);
               player.sendMessage(getPrefix()+" ได้ปลดแบนไอเทม "+itemList+" ในโลก "+name+" เรียบร้อย");
            }
         }
         if(event.getFormID() == BANCMD){
            if(event.wasClosed()){
               return;
            }
            String title = window.getTitle();
            String[] arr = title.split(" ");
            String name = arr[2];
            String cmd = window.getResponse().getInputResponse(2);
            String content = window.getResponse().getLabelResponse(0);
            List<String> cmdLists = new ArrayList<String>(plugin.getFlagList(name, "bancmd"));
            String[] cmdArr = cmd.split(" ");
            if(window.getResponse().getDropdownResponse(1).getElementID() == 0){
               if(cmdArr[0] == ""){
                  BanCmd(player, name, "§cเกิดข้อผิดพลาด\n§e<คำสั่ง> จำเป็นต้องใส่");
                  return;
               }
               if(cmdLists.contains(cmd)){
                  BanCmd(player, name, "§cคำสั่ง "+cmd+" ได้แบนอยู่แล้ว");
                  return;
               }
               plugin.addFlagList(name, "bancmd", cmd);
               player.sendMessage(getPrefix()+" แบนคำสั่ง "+cmd+" ในโลก "+name+" เรียบร้อย");
            }
            if(window.getResponse().getDropdownResponse(1).getElementID() == 1){
               if(cmdArr[0] == ""){
                  BanCmd(player, name, "§cเกิดข้อผิดพลาด\n§e<คำสั่ง> จำเป็นต้องใส่");
                  return;
               }
               if(!cmdLists.contains(cmd)){
                  BanCmd(player, name, "§cเกิดข้อผิดพลาด\n§eไม่พบคำสั่ง "+cmd+" ที่แบนอยู่");
                  return;
               }
               plugin.removeFlagList(name, "bancmd", cmd);
               player.sendMessage(getPrefix()+" ได้ปลดแบนคำสั่ง "+cmd+" ในโลก "+name+" เรียบร้อย");
            }
         }
      }
   }
   public void Menu(Player player){
      FormWindowSimple window = new FormWindowSimple(getPrefix()+" Menu", "§eโปรดเลือกเมนู.");
      window.addButton(new ElementButton("§fwhitelist"));
      Map<Integer, Level> level = plugin.getServer().getLevels();
      for (Level levels : level.values()) {
         window.addButton(new ElementButton(levels.getFolderName()));
      }
      player.showFormWindow(window, MENU);
   }
   public void Whitelist(Player player, String content){
      FormWindowCustom window = new FormWindowCustom(getPrefix()+" Whitelist");
      List<String> list = new ArrayList<String>(plugin.getWhiteList());
      if(list.size() == 0){
         content = content+"\n§b[§dWhitelist§b] §fรายชื่อผู้เล่นที่ให้อนุญาตให้ใช้อีเว้นท์ต่างๆ\n§cไม่มี";
      }else{
         content = content+"\n§b[§dWhitelist§b] §fรายชื่อผู้เล่นที่ให้อนุญาตให้ใช้อีเว้นท์ต่างๆ\nทั้งหมด §a("+list.size()+"คน)\n§e"+String.join("\n", list);
      }
      window.addElement(new ElementLabel(content));
      List<String> menu = Arrays.asList("Add (เพิ่มชื่อผู้เล่น)", "Remove (ลบชื่อผู้เล่น)");
      window.addElement(new ElementDropdown("เมนู", menu));
      window.addElement(new ElementInput("§eชื่อผู้เล่น", "Steve"));  
      player.showFormWindow(window, WHITELIST);
   }
   public void Edit(Player player, String worldname){
      FormWindowSimple window = new FormWindowSimple(getPrefix()+" Edit2 "+worldname, "§eโปรดเลือกเมนู.");
      window.addButton(new ElementButton("§fแก้ไขโลกและโพรเทตต่างๆ"));
      window.addButton(new ElementButton("§fแบนไอเทม"));
      window.addButton(new ElementButton("§fแบนคำสั่ง"));
      window.addButton(new ElementButton("§cรีเช็ตการตั้งค่าทั้งหมด"));
      player.showFormWindow(window, EDIT);
   }
   public void Edit2(Player player, String worldname){
      FormWindowCustom window = new FormWindowCustom(getPrefix()+" Edit2 "+worldname);
      for(Map.Entry<String, String> map : plugin.event.entrySet()){
         String key = map.getKey();
         String value = map.getValue();
         window.addElement(new ElementToggle("§f"+key+" ("+value+")", plugin.getFlagBoolean(worldname, key)));
      }
      player.showFormWindow(window, EDIT2);
   }
   public void BanItem(Player player, String worldname, String content){
      FormWindowCustom window = new FormWindowCustom(getPrefix()+" BanItem "+worldname);
      List<String> list = new ArrayList<String>(plugin.getFlagList(worldname, "banitem"));
      if(list.size() == 0){
         content = content+"\n§b[§4Ban§aItem§b] §fรายการไอเทมที่แบนไปแล้ว\n§cไม่มี";
      }else{
         content = content+"\n§b[§4Ban§aItem§b] §fรายการไอเทมที่แบนไปแล้ว\nทั้งหมด §a("+list.size()+"ไอเทม)\n§b"+String.join("\n", list);
      }
      window.addElement(new ElementLabel(content));
      List<String> menu = Arrays.asList("Add (เพิ่มไอเทม)", "Remove (ลบไอเทม)");
      window.addElement(new ElementDropdown("เมนู", menu));
      window.addElement(new ElementInput("§eitemId", "17"));  
      window.addElement(new ElementInput("§eitemDamage", "0"));  
      player.showFormWindow(window, BANITEM);
   }
   public void BanCmd(Player player, String worldname, String content){
      FormWindowCustom window = new FormWindowCustom(getPrefix()+" BanCmd "+worldname);
      List<String> list = new ArrayList<String>(plugin.getFlagList(worldname, "bancmd"));
      if(list.size() == 0){
         content = content+"\n§b[§4Ban§eCmd§b] §fรายการคำสั่งที่แบนไปแล้ว\n§cไม่มี";
      }else{
         content = content+"\n§b[§4Ban§eCmd§b] §fรายการคำสั่งที่แบนไปแล้ว\nทั้งหมด §a("+list.size()+"คำสั่ง)\n§b"+String.join("\n", list);
      }
      window.addElement(new ElementLabel(content));
      List<String> menu = Arrays.asList("Add (เพิ่มคำสั่ง)", "Remove (ลบคำสั่ง)");
      window.addElement(new ElementDropdown("เมนู", menu));
      window.addElement(new ElementInput("§eคำสั่ง", "/version"));
      player.showFormWindow(window, BANCMD);
   }
}