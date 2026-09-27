# skybender-anglesnap
Fire control mod for the Orbital Skybender platform in Minecraft

Requirements:
 - Minecraft Java Edition 1.21.11, 26.1, 26.1.1, 26.1.2, 26.2, or 26.3
 - Fabric Loader 0.19.5 or newer
 - Fabric API for the same Minecraft version
 - Java 21 for 1.21.11, or Java 25 for 26.x (the official Minecraft Launcher includes the right Java automatically)

The mod only needs to be installed on your own game. It works in singleplayer and on multiplayer servers without being installed on the server.

Each Minecraft release has its own matching mod JAR. This is required because Fabric uses different runtime mappings for 1.21.11 and the 26.x releases.

Installation (official Minecraft Launcher):
1. Install Fabric Loader:
   - Download the installer from [fabricmc.net/use/installer](https://fabricmc.net/use/installer/) and run it.
   - On the Client tab, choose your Minecraft version and click Install.
   - This adds a `fabric-loader-...` profile to the Minecraft Launcher.
2. Start Minecraft once with the new Fabric profile, then quit. This creates the `mods` folder.
3. Download Fabric API from [Modrinth](https://modrinth.com/mod/fabric-api). Pick the file listed for your Minecraft version.
4. Download this mod from the [Releases page](https://github.com/cubicmetre/skybender-anglesnap/releases):
   - Download the JAR whose `+<version>` suffix exactly matches your Minecraft version: `+1.21.11`, `+26.1`, `+26.1.1`, `+26.1.2`, `+26.2`, or `+26.3`.
   - Don't download the "Source code" zip files; those are for developers.
5. Open your `mods` folder:
   - Windows: press Win+R, paste `%APPDATA%\.minecraft\mods` and press Enter.
   - macOS: in Finder, choose Go > Go to Folder and paste `~/Library/Application Support/minecraft/mods`.
   - Linux: `~/.minecraft/mods`.
6. Copy both jar files (Fabric API and this mod) into the `mods` folder.
7. Start Minecraft with the Fabric profile.
8. To check it worked, open chat and type `/skybend` followed by a space. The suggestions should list `set`, `time` and `fire`.

Installation (Prism Launcher or MultiMC):
1. Create a new instance for a supported Minecraft version and choose Fabric as the mod loader.
2. Open the instance's Mods page and add Fabric API. Use "Download mods" to search for it, or "Add file" if you downloaded it already.
3. Download the matching jar from the [Releases page](https://github.com/cubicmetre/skybender-anglesnap/releases) and add it with "Add file".
4. Make sure the instance uses Java 21 for 1.21.11 or Java 25 for 26.x (Settings > Java). Prism can download it for you.
5. Launch the instance and check for `/skybend` in chat as above.

Troubleshooting:
 - If Minecraft says the mod is incompatible or won't start, check that the jar's `+<version>` suffix exactly matches your Minecraft version and that Fabric API is in the `mods` folder.
 - If `/skybend` doesn't appear, make sure you launched the Fabric profile, not the plain Minecraft one.

Building:
 - Run `./gradlew buildAll` to build all supported targets. Each release JAR is written to its target project's `build/libs` directory.

In Game:
 - Run command `/skybend set <n> <ox> <oz>` where `<n>` is the size of the tnt warhead (1-15), `<ox>` is the X coordinate of the cannon origin and `<oz>` is the z coordinate of the cannon origin.
 - Run command `/skybend time <tx> <tz>` where `<tx>` is the target x coordinate, and `<tz>` is the target z coordinate. Running this command without a defined target, i.e. `tx` and `tz` intentionally left blank, will instead use the location that the player is looking at in the world. The command will then return a time estimate for delivery of a tnt warhead to the target location.
 - Run command `/skybend fire <tx> <tz>` where `<tx>` is the target x coordinate, and `<tz>` is the target z coordinate. Running this command without a defined target, i.e. `tx` and `tz` intentionally left blank, will instead use the location that the player is looking at in the world. The command will then compell the client to look at a specific sequence of view directions to transfer target information to the cannon remotely using the cannons wireless interface.

When Firing:
- Once the time estimate reaches zero, the cannon will wait for the player to look straight down before dispatching the tnt warhead. Additional firing instructions sent to the cannon in between the time a sequence is input and the time the warhead is ready will be ignored.
