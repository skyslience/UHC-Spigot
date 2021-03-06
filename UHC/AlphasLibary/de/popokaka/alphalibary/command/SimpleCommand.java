package de.popokaka.alphalibary.command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SimpleCommand<P extends JavaPlugin> extends Command {
	
	private static String VERSION;
	   
	   static {
	      String path = Bukkit.getServer().getClass().getPackage().getName();
	      
	      SimpleCommand.VERSION = path.substring(path.lastIndexOf(".") + 1, path.length());
	      
	   }
	   
	   private final P plugin;
	   private final String command;
	   
	   String getCommand() {
		   return command;
	   }
	   
	   protected SimpleCommand(P plugin, String command, String description, String... aliases) {
	      super(command);
	      this.plugin = plugin;
	      this.command = command;
	      
	      super.setDescription(description);
	      List<String> aliasList = new ArrayList<>();
		   Collections.addAll(aliasList, aliases);
	      super.setAliases(aliasList);
	      
	      this.register();
	   }
	   
	   void register() {
	      try {
	         Field f = Class.forName("org.bukkit.craftbukkit." + SimpleCommand.VERSION + ".CraftServer").getDeclaredField("commandMap");
	         f.setAccessible(true);
	         
	         CommandMap map = (CommandMap) f.get(Bukkit.getServer());
	         map.register(this.plugin.getName(), this);
	      } catch (Exception exc) {
	         exc.printStackTrace();
	      }
	   }
	   
	   @Override
	public abstract boolean execute(CommandSender cs, String label, String[] args);
	   
	   @Override
	public abstract List<String> tabComplete(CommandSender cs, String label, String[] args);
	   
	   public String buildString(String[] args, int start) {
	      String str = "";
	      if (args.length > start) {
	         str += args[start];
	         for (int i = start + 1; i < args.length; i++) {
	            str += " " + args[i];
	         }
	      }
	      return str;
	   }
	   
	   public P getPlugin() {
	      return this.plugin;
	   }

}
