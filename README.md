# staffprofiles

staffprofiles is a Minecraft plugin that assigns staff members a different game profile if
they are joining using a specific hostname.
It's an evolution of my proof-of-concept plugin [hostprofiles](https://github.com/MCMDEV/hostprofiles)
into a more practical, specialized solution for staff management.

## Non-technical example

The plugin essentially does the following when a player is logging in:

1. It checks if the address that the player is using to connect matches a configured staff address
2. If the address matches, it verifies that the player has a configured permission
3. If both are true, the player UUID and username are changed, causing the server to treat the player as if
   they were on a separate Minecraft account.

An example:
If your server address is `example.org`, you could configure `staff.example.org` (the host) as a staff host
so that all players connecting using that host, if they have the permission `staffprofile`, are
connected using what is treated by the server like a separate account.

## Configuration

The plugin is configured using a JSON file as opposed to a YAML file to reduce plugin jar size. Unfortunately, this
means that the plugin configuration file cannot contain any comments.

Please see the reference below to learn about available configuration options:

**hostRegex**: A regular expression pattern that determines if a hostname should be treated as a staff hostname by matching
against the entire address \
**permission**: The permission the player must have to be allowed to join using a staff hostname \
**uuidTransformer**: Specifies the transformation of player UUIDs into staff profile UUIDs using a regex pattern. The format
is "pattern/replacement" where a pattern matches parts of the original UUID and replacement defines how to construct the
new UUID. \
**usernameTransformer**: Specifies the transformation of player username into staff profile username using a regex pattern.
The format is "pattern/replacement" where a pattern matches parts of the original username and replacement defines
how to construct the new username.

## Additional notes

### LuckPerms warnings

When using this plugin, LuckPerms will send print warning messages such as these info the server log:
```
[LuckPerms] LuckPerms already has data for player 'MCMDEV' - but this data is stored under a different UUID.
[LuckPerms] 'MCMDEV' has previously used the unique ids [2fbe3e3e-50c3-482c-872b-28386fd91704] but is now connecting with '00000000-7777-0000-872b-28386fd91704'
[LuckPerms] The UUID the player is connecting with now is NOT Mojang-assigned (type 0). This implies that THIS server is not authenticating correctly, but one (or more) of the other servers/proxies in the network are.
[LuckPerms] If you're using BungeeCord/Velocity, please ensure that IP-Forwarding is setup correctly on all of your backend servers!
[17:09:39 WARN]: [LuckPerms] See here for more info: https://luckperms.net/wiki/Network-Installation#pre-setup
```
These warnings can be safely ignored if you have a secure setup. As the warning states, LuckPerms
noticed that data for the given name is already available under a different username, which is precisely what this plugin does.
Most well-made plugins, including LuckPerms, can handle this properly, as this scenario can actually occur naturally
when two players swap usernames.
If you're still concerned or annoyed by the warning, you can configure a username transformer.

## Secure profiles

When having secure profiles enabled, sending chat messages when a staff profile is active will not work.
This can't be fixed because the plugin is essentially impersonating another account for which the client does not have
the cryptographic keys.