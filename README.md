# skybender-anglesnap
Fire control mod for the Orbital Skybender platform in Minecraft

In Game:
 - Run command /skybend set <n> <ox> <oz> where <n> is the size of the tnt warhead (1-15), <ox> is the X coordinate of the cannon origin and <oz> is the z coordinate of the cannon origin.
 - Run command /skybend time <tx> <tz> where <tx> is the target x coordinate, and <tz> is the target z coordinate. Running this command without a defined target, i.e. tx and tz intentionally left blank, will instead use the location that the player is looking at in the world. The command will then return a time estimate for delivery of a tnt warhead to the target location.
 - Run command /skybend fire <tx> <tz> where tx is the target x coordinate, and tz is the target z coordinate. Running this command without a defined target, i.e. tx and tz intentionally left blank, will instead use the location that the player is looking at in the world. The command will then compell the client to look at a specific sequence of view directions to transfer target information to the cannon remotely using the cannons wireless interface.

When Firing:
- Once the time estimate reaches zero, the cannon will wait for the player to look straight down before dispatching the tnt warhead. Additional firing instructions sent to the cannon in between the time a sequence is input and the time the warhead is ready will be ignored.
