# AreaReloader
![SpigotMC](https://img.shields.io/badge/platform-Spigot%20%7C%20Paper-yellow?style=flat-square)
![Made with 💙](https://img.shields.io/badge/Made%20with-%F0%9F%92%99-blue?style=flat-square)

<strong>AreaReloader-FAWE is AreaReloader's mirror plugin compatible with FastAsyncWorldEdit.</strong><br>
This plugin lets you turn any FastAsyncWorldEdit's selection into a restorable region.<br>
AreaReloader-FAWE creates a schematic copy of the selection, split into smaller sections in order to distribute server's load, allowing smoother pasting/regeneration.<br>
Each saved area can be restored manually with a command or automatically on a timed schedule.<br>
The restore interval is fully customizable, making it ideal for regenerating resource areas, PvP arenas, dungeons, or any place that needs resetting.
    
# Compatibility
This version of the plugin will only work with [FAWE](https://modrinth.com/plugin/fastasyncworldedit), if you use WorldEdit (standard WE) you must use the mirror plugin, [AreaReloader](https://modrinth.com/plugin/areareloader).

<details>
<summary>Commands</summary>

# Commands
- /ar - Shows plugin's help lines
- /ar help <Command> - Shows general help or specific help when a command is specified
- /ar version - Shows the current plugin's version
- /ar create <AreaName> <CopyEntities> - Creates a new copy of the selected area.
The copy entities parameter will accept a true or false value, which will decide whether or not to copy entities inside the selected area at the moment of creation.
If set to true, whenever the area gets loaded, saved entities will be respawned.
- /ar load <AreaName> - Loads an existing area
- /ar delete <AreaName> - Deletes an existing area
- /ar list - Lists all existing areas
- /ar hook - Shows a help interface for the plugin's hooks and
dependencies
- /ar info - Shows information about a specific existing area
- /ar reload - Reloads AreaReloader's configuration file
- /ar display <AreaName> - Displays particles around areas
- /ar cancel <AreaName, ALL> - Cancels the loading of one or all areas.
- /ar location <AreaName> <set, teleport> - Allows you to create a safe location for players.
- /ar placeholders - Lists all the available placeholders (requires PAPI).

</details>
<details>
<summary>Permissions</summary>

# Permissions
- areareloader.command.help - Gives access to the /ar help command
- areareloader.command.version - Gives access to the /ar version command
- areareloader.command.create - Gives access to the /ar create command
- areareloader.command.load - Gives access to the /ar load command
- areareloader.command.delete - Gives access to the /ar delete command
- areareloader.command.list - Gives access to the /ar list command
- areareloader.command.hook - Gives access to the /ar hook command
- areareloader.command.info - Gives access to the /ar info command
- areareloader.command.reload - Gives access to the /ar reload command
- areareloader.command.display - Gives access to /ar display command
- areareloader.command.cancel- Gives access to /ar cancel command
- areareloader.command.location - Gives access to /ar location command
- areareloader.command.placeholders - Gives access to /ar placeholders command
- areareloader.command.admin - Gives access to all commands

</details>

# Configuration
There are two main configuration files;
**config.yml** allows you to customise language and general settings, whereas the **areas.yml** config contains all the areas' details and custom settings per area, such as auto loading and intervals.
<details>
<summary>config.yml</summary>

<table>
  <tr>
    <th>Setting</th>
    <th>Default value</th>
    <th>Values</th>
    <th>Description</td>
  </tr>
  <tr>
    <td><strong>Debug</strong></td>
    <td>true</td>
    <td>true/false</td>
    <td>If enabled prints all the plugin's processes to the debug file located at /AreaRealoder-FAWE/debug.txt.</td>
  </tr>
  <tr>
    <td><strong>Updater</strong></td>
    <td>true</td>
    <td>true/false</td>
    <td>If enabled, automatically checks for plugin updates.</td>
  </tr>
  <tr>
    <td><strong>Metrics</strong></td>
    <td>true</td>
    <td>true/false</td>
    <td>If enabled retrieves anonymous server's statistics to help keep track of the plugin's usage.</td>
  </tr>
  <tr>
    <td><strong>Announcer</strong></td>
    <td>true</td>
    <td>true/false</td>
    <td>If enabled announces the plugin's branding message.</td>
  </tr>
</table>

**AreaLoading settings**
<table>
  <tr>
    <th>Setting</th>
    <th>Default value</th>
    <th>Values</th>
    <th>Description</td>
  </tr>
  <tr>
    <td><strong>Global interval</strong></td>
    <td>500</td>
    <td>Any value above 0</td>
    <td>Sets an interval <strong>(in milliseconds)</strong> all areas use to wait between section loads if using the global option.<br></td>
  </tr>
    <tr>
    <td><strong>Fast mode</strong></td>
      <td>true</td>
      <td>true/false</td>
      <td>Whether or not to use FAWE's fast mode; depending on your server performance you may enable or disable this function.</td>
  </tr>
    <tr>
    <td><strong>Percentage</strong></td>
      <td>15</td>
      <td>Any value above 0</td>
      <td>Sets the area loading progress percentage to broadcast to the player when loading an area.</td>
  </tr>
  </table>

  **AutoReload settings**
<table>
  <tr>
    <th>Setting</th>
    <th>Default value</th>
    <th>Values</th>
    <th>Description</td>
  </tr>
  <tr>
    <td><strong>Checker</strong></td>
    <td>true</td>
    <td>true/false</td>
    <td>Enable or disable the area scheduler function.</td>
  </tr>
    <tr>
    <td><strong>Notify admins</strong></td>
      <td>true</td>
      <td>true/false</td>
      <td>Whether or not to notify players with the admin permission whenever an area automatically loads.</td>
  </tr>
    <tr>
    <td><strong>Notify console</strong></td>
      <td>true</td>
      <td>true/false</td>
      <td>Whether or not to notify console whenever an area automatically loads.</td>
  </tr>
  </table>
</details>


<details>
<summary>areas.yml</summary>
  <strong>Loading interval settings</strong>
  <table>
  <tr>
    <th>Setting</th>
    <th>Default value</th>
    <th>Values</th>
    <th>Description</td>
  </tr>
  <tr>
    <td><strong>Global</strong></td>
    <td>true</td>
    <td>true/false</td>
    <td>Whether or not the area should use the global interval between section loads.</td>
  </tr>
    <tr>
    <td><strong>Time</strong></td>
      <td>200</td>
      <td>Any value above 0</td>
      <td>The time interval <strong>(in milliseconds)</strong> the area must wait between section loads.<br>
      <strong>Note:</strong> Global interval must be disabled for this area to use its custom interval.</td>
  </tr>
  </table>
  <strong> Auto reload settings</strong>
    <table>
      <tr>
        <th>Setting</th>
        <th>Default value</th>
        <th>Values</th>
        <th>Description</td>
      </tr>
      <tr>
        <td>Enabled</td>
        <td>true</td>
        <td>true/false</td>
        <td>Enables/disables the area to automatically reload.</td>
      </tr>
      <tr>
        <td>Time</td>
        <td>200000</td>
        <td>Any value above 0</td>
        <td>Sets the interval <strong>(in milliseconds)</strong> for the area to automatically reload.</td>
      </tr>
      </table>
      
</details>

# Issues
Open a new issue [here](https://github.com/Hetag1216/AreaReloader-FAWE/issues).
<br>
When opening a new issue please add and specify:

- Debug's file (located at AreaReloader-FAWE\debug.txt, can be enabled through the config).
- Provider's version (spigot, paper, bukkit) - /version;
- AreaReloader's version - /ar version;
- FAWE's version;
- Where/when you met the issue, as in what action had been fired at the time (command, auto reloading, etc.).
- Error log (either from console or debug file).

# Dependencies
The plugin depends on WorldEdit, it will work with any WE's version your server supports.

# Support

<p align="center">
    <a href="https://discord.com/invite/yqs9UJs">
        <img src="https://i.imgur.com/JgDt1Fl.png" width="300" alt="discord">
    </a>
    <br>
    <i>I do my best to provide support for my projects over discord.
      <br>If you'd have questions or support requests feel free to join!</i>
</p>

# Metrics
This plugin collects anonymous server statistics which helps me keep track of the plugin's usage.<br>
I invite you to keep this setting on as it contributes to boosting my dedication and work towards my projects!
Provided by [bStats](https://bstats.org/).
<img src="https://bstats.org/signatures/bukkit/AreaReloader-FAWE.svg">
