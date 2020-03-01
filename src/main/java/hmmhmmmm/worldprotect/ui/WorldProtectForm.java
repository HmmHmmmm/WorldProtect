package hmmhmmmm.worldprotect.ui;

import hmmhmmmm.worldprotect.WorldProtect;
import hmmhmmmm.worldprotect.data.Language;

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

public class WorldProtectForm implements Listener{
   private WorldProtect plugin;
   private Language lang;
   
   public WorldProtectForm(WorldProtect plugin){
      this.plugin = plugin;
      this.lang = (Language) plugin.getLanguage();
   }
   public WorldProtect getPlugin(){
      return plugin;
   }
   public String getPrefix(){
      return plugin.getPrefix();
   }
   
   //รหัส id form หากมีปลั๊กอินอื่นใช้รหัสนี้ form มันจะทับกัน
   static int MENU = 0xAAA101;
   static int WHITELIST = 0xAAA102;
   static int EDIT = 0xAAA103;
   static int EDIT2 = 0xAAA104;
   static int BANITEM = 0xAAA105;
   static int BANCMD = 0xAAA106;
   
   public void Menu(Player player){
      FormWindowSimple window = new FormWindowSimple(
         getPrefix()+" Menu",
         lang.getTranslate("form.menu.content")
      );
      window.addButton(new ElementButton("§fwhitelist"));
      Set<String> wps = plugin.getWorld();
      for(String worldname : wps){
         window.addButton(new ElementButton(worldname));
      }
      player.showFormWindow(window, MENU);
   }
   
   public void Whitelist(Player player, String content){
      FormWindowCustom window = new FormWindowCustom(
         getPrefix()+" Whitelist"
      );
      List<String> list = new ArrayList<String>(
         plugin.getWhiteList()
      );
      if(list.size() == 0){
         content = content+"\n§b[§dWhitelist§b] "+lang.getTranslate(
            "form.whitelist.content.title"
         )+"\n"+lang.getTranslate(
            "form.whitelist.content.error1"
         );
      }else{
         content = content+"\n§b[§dWhitelist§b] "+lang.getTranslate(
            "form.whitelist.content.title"
         )+"\n"+lang.getTranslate(
            "form.whitelist.content.complete",
            new String[]{Integer.toString(list.size())}
         )+"\n§e"+String.join("\n", list);
      }
      window.addElement(new ElementLabel(content));
      List<String> menu = Arrays.asList(
         lang.getTranslate("form.whitelist.dropdown1.step1"), 
         lang.getTranslate("form.whitelist.dropdown1.step2")
      );
      window.addElement(new ElementDropdown(
         lang.getTranslate("form.whitelist.dropdown1.title"),
         menu
      ));
      window.addElement(new ElementInput(
         lang.getTranslate("form.whitelist.input1.title"),
         "Steve"
      ));  
      player.showFormWindow(window, WHITELIST);
   }
   
   public void Edit(Player player, String worldname){
      FormWindowSimple window = new FormWindowSimple(
         getPrefix()+" Edit2 "+worldname,
         lang.getTranslate("form.edit.content")
      );
      List<String> menuList = Arrays.asList(
         lang.getTranslate("form.edit.button1"), 
         lang.getTranslate("form.edit.button2"),
         lang.getTranslate("form.edit.button3"),
         lang.getTranslate("form.edit.button4")
      );
      for(String menu : menuList){
         window.addButton(new ElementButton(menu));
      }
      player.showFormWindow(window, EDIT);
   }
   
   public void Edit2(Player player, String worldname){
      FormWindowCustom window = new FormWindowCustom(
         getPrefix()+" Edit2 "+worldname
      );
      for(Map.Entry<String, String> map : plugin.eventMap.entrySet()){
         window.addElement(new ElementToggle(
            "§f"+map.getKey()+" ("+map.getValue()+")",
            plugin.getFlagBoolean(worldname, map.getKey())
         ));
      }
      player.showFormWindow(window, EDIT2);
   }
   
   public void BanItem(Player player, String worldname, String content){
      FormWindowCustom window = new FormWindowCustom(
         getPrefix()+" BanItem "+worldname
      );
      List<String> list = new ArrayList<String>(plugin.getFlagList(
         worldname,
         "banitem"
      ));
      if(list.size() == 0){
         content = content+"\n§b[§4Ban§aItem§b] "+lang.getTranslate(
            "form.banitem.content.title"
         )+"\n"+lang.getTranslate(
            "form.banitem.content.error1"
         );
      }else{
         content = content+"\n§b[§4Ban§aItem§b] "+lang.getTranslate(
            "form.banitem.content.title"
         )+"\n"+lang.getTranslate(
            "form.banitem.content.complete",
            new String[]{Integer.toString(list.size())}
         )+"\n§e"+String.join("\n", list);
      }
      window.addElement(new ElementLabel(content));
      List<String> menu = Arrays.asList(
         lang.getTranslate("form.banitem.dropdown1.step1.title"), 
         lang.getTranslate("form.banitem.dropdown1.step2.title")
      );
      window.addElement(new ElementDropdown(
         lang.getTranslate("form.banitem.dropdown1.title"), 
         menu
      ));
      window.addElement(new ElementInput(
         lang.getTranslate("form.banitem.input1.title"),
         "17"
      ));  
      window.addElement(new ElementInput(
         lang.getTranslate("form.banitem.input2.title"),
         "0"
      ));  
      player.showFormWindow(window, BANITEM);
   }
   
   public void BanCmd(Player player, String worldname, String content){
      FormWindowCustom window = new FormWindowCustom(
         getPrefix()+" BanCmd "+worldname
      );
      List<String> list = new ArrayList<String>(plugin.getFlagList(
         worldname,
         "bancmd"
      ));
      if(list.size() == 0){
         content = content+"\n§b[§4Ban§eCmd§b] "+lang.getTranslate(
            "form.bancmd.content.title"
         )+"\n"+lang.getTranslate(
            "form.bancmd.content.error1"
         );
      }else{
         content = content+"\n§b[§4Ban§eCmd§b] "+lang.getTranslate(
            "form.bancmd.content.title"
         )+"\n"+lang.getTranslate(
            "form.bancmd.content.complete",
            new String[]{Integer.toString(list.size())}
         )+"\n§e"+String.join("\n", list);
      }
      window.addElement(new ElementLabel(content));
      List<String> menu = Arrays.asList(
         lang.getTranslate("form.bancmd.dropdown1.step1.title"), 
         lang.getTranslate("form.bancmd.dropdown1.step2.title")
      );
      window.addElement(new ElementDropdown(
         lang.getTranslate("form.bancmd.dropdown1.title"), 
         menu
      ));
      window.addElement(new ElementInput(
         lang.getTranslate("form.bancmd.input1.title"),
         "/version"
      ));
      player.showFormWindow(window, BANCMD);
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
                  player.sendMessage(getPrefix()+" "+lang.getTranslate(
                     "form.edit.reset.complete"
                  ));
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
            if(window.getResponse().getDropdownResponse(1).getElementID() == 0){
               if(whitelist.contains(message.toLowerCase())){
                  Whitelist(player, lang.getTranslate(
                     "form.whitelist.error1",
                     new String[]{message}
                  ));
                  return;
               }
               plugin.addWhiteList(message.toLowerCase());
               player.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.whitelist.complete",
                  new String[]{message}
               ));
            }
            if(window.getResponse().getDropdownResponse(1).getElementID() == 1){
               if(!whitelist.contains(message.toLowerCase())){
                  Whitelist(player, lang.getTranslate(
                     "form.whitelist.error2",
                     new String[]{message}
                  ));
                  return;
               }
               plugin.removeWhiteList(message.toLowerCase());
               player.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unwhitelist.complete",
                  new String[]{message}
               ));
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
            for(Map.Entry<String, String> map : plugin.eventMap.entrySet()){
               eventBoolean.put(map.getKey(), window.getResponse().getToggleResponse(i));
               i++;
            }
            plugin.edit(name, eventBoolean);
            player.sendMessage(getPrefix()+" "+lang.getTranslate(
               "form.edit2.complete",
               new String[]{name}
            ));
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
            List<String> itemLists = new ArrayList<String>(plugin.getFlagList(
               name,
               "banitem"
            ));
            String itemList;
            int id;
            int damage;
            if(window.getResponse().getDropdownResponse(1).getElementID() == 0){
               try{
                  id = Integer.parseInt(itemId);
               }catch(NumberFormatException e){
                  BanItem(player, name, lang.getTranslate(
                     "form.banitem.dropdown1.step1.error1"
                  ));
                  return;
               }
               try{
                  damage = Integer.parseInt(itemDamage);
               }catch(NumberFormatException e){
                  BanItem(player, name, lang.getTranslate(
                     "form.banitem.dropdown1.step1.error2"
                  ));
                  return;
               }
               itemList = id+":"+damage;
               Item item;
               item = Item.fromString(itemList);
               if(item.getId() == 0){
                  BanItem(player, name, lang.getTranslate(
                     "form.banitem.dropdown1.step1.error3",
                     new String[]{itemList}
                  ));
                  return;
               }
               if(item.getName().equals("Unknown")){
                  BanItem(player, name, lang.getTranslate(
                     "form.banitem.dropdown1.step1.error4",
                     new String[]{itemList}
                  ));
                  return;
               }
               if(itemLists.contains(itemList)){
                  BanItem(player, name, lang.getTranslate(
                     "form.banitem.dropdown1.step1.error5",
                     new String[]{itemList}
                  ));
                  return;
               }
               plugin.addFlagList(name, "banitem", itemList);
               player.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.banitem.complete",
                  new String[]{item.getName(), itemList, name}
               ));
            }
            if(window.getResponse().getDropdownResponse(1).getElementID() == 1){
               try{
                  id = Integer.parseInt(itemId);
               }catch(NumberFormatException e){
                  BanItem(player, name, lang.getTranslate(
                     "form.banitem.dropdown1.step2.error1"
                  ));
                  return;
               }
               try{
                  damage = Integer.parseInt(itemDamage);
               }catch(NumberFormatException e){
                  BanItem(player, name, lang.getTranslate(
                     "form.banitem.dropdown1.step2.error2"
                  ));
                  return;
               }
               itemList = id+":"+damage;
               if(!itemLists.contains(itemList)){
                  BanItem(player, name, lang.getTranslate(
                     "form.banitem.dropdown1.step2.error3",
                     new String[]{itemList}
                  ));
                  return;
               }
               plugin.removeFlagList(name, "banitem", itemList);
               player.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unbanitem.complete",
                  new String[]{itemList, name}
               ));
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
            List<String> cmdLists = new ArrayList<String>(plugin.getFlagList(
               name,
               "bancmd"
            ));
            if(window.getResponse().getDropdownResponse(1).getElementID() == 0){
               if(cmdLists.contains(cmd)){
                  BanCmd(player, name, lang.getTranslate(
                     "form.bancmd.dropdown1.step1.error1",
                     new String[]{cmd}
                  ));
                  return;
               }
               plugin.addFlagList(name, "bancmd", cmd);
               player.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.bancmd.complete",
                  new String[]{cmd, name}
               ));
            }
            if(window.getResponse().getDropdownResponse(1).getElementID() == 1){
               if(!cmdLists.contains(cmd)){
                  BanCmd(player, name, lang.getTranslate(
                     "form.bancmd.dropdown1.step2.error1",
                     new String[]{cmd}
                  ));
                  return;
               }
               plugin.removeFlagList(name, "bancmd", cmd);
               player.sendMessage(getPrefix()+" "+lang.getTranslate(
                  "worldprotect.command.unbancmd.complete",
                  new String[]{cmd, name}
               ));
            }
         }
      }
   }
   
}