# AreaReloader
![Core Icon](https://media.discordapp.net/attachments/595194807932944385/614115793382146058/AR.png)

An easy to use Spigot plugin to roll back areas at the stage they were saved!

![Core Icon](https://cdn.discordapp.com/attachments/595194807932944385/614124252387606565/ARCommands.png)

/ar - Shows plugin's help lines

/ar help <Command> - Shows general help or specific help when a command is spefied

/ar version - Shows the current plugin's version

/ar create <AreaName> - Creates a new copy of the selected area

/ar load <AreaName> - Loads an existing area

/ar delete <AreaName> - Deletes an existing area

/ar list - Lists all existing areas

/ar hook - Shows a help interface for the plugin's hooks and dependencies

/ar info - Shows information about a specific existing area

/ar reload - Reloads AreaReloader's configuration file

/ar display <AreaName> - Displays particles around an area

![Core Icon](https://cdn.discordapp.com/attachments/595194807932944385/614124189229514772/ARPermissions.png)

areareloader.command.help - Gives access to the /ar help command

areareloader.command.version - Gives access to the /ar version command

areareloader.command.create - Gives access to the /ar create command

areareloader.command.load - Gives access to the /ar load command

areareloader.command.delete - Gives access to the /ar delete command

areareloader.command.list - Gives access to the /ar list command

areareloader.command.hook - Gives access to the /ar hook command

areareloader.command.info - Gives access to the /ar info command

areareloader.command.reload - Gives access to the /ar reload command

areareloader.command.admin - Gives access to all commands

![Core Icon](https://i.imgur.com/v2uHY9wh.png)

The main configuration file where you will be able to edit your own language settings is named as "config.yml".
The config is very easy to understand as it is composed by regular language strings, however, the plugin brings some variables and new configuration settings that affect the plugin.

~~------------------------------------------------------------------------------------------------------------------------------------~~

*Guide to debugging*: 
Open a new issue in the [Issues category](https://github.com/Hetag1216/AreaReloader/issues)
When opening a new issue please specify:
- Spigot's version - /version;
- WorldEdit's version;
- Where/when you met the issue by specifying the command which threw an error and specify where the operations stop, debugging will show every operation ran when executing commands.
- Paste console's error logs (if there are any)

The debug logs will be printed to the command sender.
Default value: false (turn 'false' to true and then restart the server to enable the debugging or any config changes)

~~------------------------------------------------------------------------------------------------------------------------------------~~

Area Interval: This is the amount of time set in milliseconds that decides the delay between each section loading whenever an area is being loaded.
I recommend keeping this value as the default one as a higher or lower value could affect your server's performance in bad or good, depending on the server's resources and tasks that are currently running at the moment of the area's load, so choose a wise value whenever changing this value.
Default value: 200
(0.2 seconds)

~~------------------------------------------------------------------------------------------------------------------------------------~~

AreaScheduler: This function has been implemented in version 1.3.
With this brand new function, you will be able to automatically reload areas after x amount of time which can be set for each existing area in the areas.yml config.
In order to use this function the global checker must be true (set true by default) and the AutoReload function under "Areas" in the areas.yml configuration file must be enabled for each area that has to automatically restore.

Join now the discord for live support and live updates on the projects!
![Core Icon](https://cdn.discordapp.com/attachments/595364073147728025/687819024457007140/discord_header.png)
https://discord.gg/yqs9UJs
