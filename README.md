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