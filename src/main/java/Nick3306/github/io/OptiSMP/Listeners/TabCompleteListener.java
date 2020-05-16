package Nick3306.github.io.OptiSMP.Listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import Nick3306.github.io.OptiSMP.Main;
import Nick3306.github.io.OptiSMP.Components.OptiProtect.ProtectionField;

public class TabCompleteListener implements Listener {

	private Main plugin;

	public TabCompleteListener(Main plugin) {
		this.plugin = plugin;
	}

	public void tabComplete(String[] args, List<String> completions, Collection<String> contents) {
		if (args.length == 1) {
			completions.addAll(contents);
		} else if (args.length == 2) {
			for (String string : contents) {
				if (!args[1].equalsIgnoreCase(string)) {
					if (string.toLowerCase().startsWith(args[1].toLowerCase())) {
						completions.add(string);
					}
				}
			}
		}
	}

	@EventHandler
	public void onTabComplete(TabCompleteEvent event) {

		CommandSender sender = event.getSender();

		if (sender instanceof Player) {

			Player player = (Player) sender;
			UUID playerUUID = player.getUniqueId();

			String buffer = event.getBuffer();
			String[] args = buffer.split("\\s+");

			List<String> completions = event.getCompletions();

			if (buffer.toLowerCase().startsWith("/pfield ")) {
				completions.clear();
				List<String> commands = new ArrayList<String>();
				commands.add("create");
				commands.add("remove");
				commands.add("info");
				commands.add("list");
				commands.add("addmember");
				commands.add("removemember");
				commands.add("members");
				commands.add("togglemessages");
				commands.add("visualize");
				commands.add("yes");
				commands.add("no");
				tabComplete(args, completions, commands);
			}

			if (buffer.toLowerCase().startsWith("/pfield create ")) {
				completions.clear();
			}

			if (buffer.toLowerCase().startsWith("/pfield remove ")) {
				completions.clear();

				List<String> list = new ArrayList<String>();
				for (ProtectionField field : plugin.fields) {
					if (field.getOwner().equals(playerUUID)) {
						list.add(field.getName());
					}
				}
				tabComplete(args, completions, list);
			}

			if (buffer.toLowerCase().startsWith("/pfield info ")) {
				completions.clear();

				List<String> list = new ArrayList<String>();
				for (ProtectionField field : plugin.fields) {
					if (field.getOwner().equals(playerUUID)) {
						list.add(field.getName());
					}
				}
				tabComplete(args, completions, list);
			}

			if (buffer.toLowerCase().startsWith("/pfield addmember ")) {
				completions.clear();
			}

			if (buffer.toLowerCase().startsWith("/pfield removemember ")) {
				completions.clear();
			}

			if (buffer.toLowerCase().startsWith("/stats ")) {
				completions.clear();

				List<String> list = new ArrayList<String>();
				for (Player online : plugin.getServer().getOnlinePlayers()) {
					list.add(online.getName());
				}
				tabComplete(args, completions, list);
			}
		}
	}
}
