# OpsyDistance [![Codacy Badge](https://api.codacy.com/project/badge/Grade/bd73cc1cd92b4758aa02c34b63c1bb2f)](https://app.codacy.com/manual/ZeTioZ/OpsyDistance?utm_source=github.com&utm_medium=referral&utm_content=ZeTioZ/OpsyDistance&utm_campaign=Badge_Grade_Dashboard)

Minecraft Java Plugin allowing players to know if someone is attacking him at an illegal distance and cancelling the hit if so

What is "OpsyDistance"
OpsyDistance is a plugin providing a lightweight solution to let your players detect cheater who use reach by sending them a message with the distance from which the cheater attacked him and giving to your player the possibility to report him.

Features
Enabling/Disabling the checker for each player (/pvpd on/off)
Cancel the hit if over the max hit distance
Player and Staff alert when a player is over the max hit distance
Mini database feature to save the activated players
Fully customisable with configs and messages files
How to install
Download the latest version of the plugin
Place the downloaded .jar file in your server plugin folder
Start your server
Enjoy !
Commands
/opsydistance » Enable/Disable the hit distance checker
/opsydistance on » Enable the hit distance checker
/opsydistance off » Disable the hit distance checker
/opsydistance help » Display help page of the plugin
/opsydistance reload » Reload the plugin
Messages and Configs Files
[SPOILER="Messages.yml"]
[code=YAML]
prefix: "&c[&aOpsyDistance&c] "
hit-checker-enabled:
  - "&2You &denabled &2the hit distance checker!"
hit-checker-disabled:
  - "&2You &cdisabled &2the hit distance checker!"
plugin-help:
  - "&2/opsyd &4» &dEnable/Disable OpsyDistance"
  - "&2/opsyd on &4» &dEnable OpsyDistance"
  - "&2/opsyd off &4» &dDisable OpsyDistance"
  - "&2/opsyd help &4» &dShow the help page"
  - "&2/opsyd reload &4» &dReload the plugin"
plugin-reloaded:
  - "&2Plugin reloaded successfully!"

alert:
  over-max:
    - "&2The player \"&b{attacker}&2\" hurt you."
    - "&2Distance of the attack &c» &4&l{distance} &cblocks."
    - "&4Cheat &c&o(Probably)&4 ? Bug ? Lag ? &c» &2Contact a staff member!"
  under-max:
    - "&2The player \"&b{attacker}&2\" hurt you."
    - "&2Distance of the attack &c» &a&l{distance} &eblocks."
  staff-alert:
    - "&2The player \"&b{attacker}&2\" hurt \"&b{victim}&2\"."
    - "&2Distance of the attack &c» &4&l{distance} &cblocks."
    - "&4Cheat &c&o(Probably)&4 ? Bug ? Lag ?"

errors:
  not-a-player:
    - "&cYou must be a player to use this command!"
  already-enabled:
    - "&cYou already enabled the hit distance checker!"
  already-disabled:
    - "&cYou already disabled the hit distance checker!"
  kick-distance-under-max:
    - "&cYou set the kick distance under the max distance!"
    - "&cPlease, change it to be more or equal to it!"
  not-enought-permissions:
    - "&cSorry, you don't have enought permissions to do that!"
[/code]
[/SPOILER]
[SPOILER="Configs.yml"]
[code=YAML]
# Max distance in blocks for a legit hit
max-distance: 5.00

kick:
  # Enable/Disable kick when the attacker is over the kick distance
  enabled: true
  # Kick distance in blocks
  distance: 5.8
  # Player ping treshold to avoid player lag false positives (in ms)
  ping-treshold: 80
  # Server TPS treshold to avoid server lag false positives (in tps value up to 20)
  tps-treshold: 18.0
  # Kick message
  message: "&cReach is not autorized on this server!"

# Cancel the damage if the attack is over the maximum distance
cancel-damage: true

# Enable the pvp-distance checker on join
on-join-enable: false

# Disable the pvp-distance checker on leave
on-leave-disable: false

# Send an alert to a staff member with "pvpdistance.alert" permission while a player exceed the max hit distance
staff-alert: true
[/code]
[/SPOILER]

Permissions
opsydistance.use (/opsydistance on)
opsydistance.use (/opsydistance off)
opsydistance.reload (/opsydistance reload)
opsydistance.alert (receive over hit distance alert)
Servers using this plugin
OpsyCraft
(if you want your server listed here, feel free to ask for it)

To Do List
Add an auto-kick with tps/lag check

Feel free to rate the plugin and send feedbacks !
And don't forget to leave a like ;)
