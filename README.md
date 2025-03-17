# PatchExport

---

This plugin converts latitude and longitude information into pixel coordinates of an image when saving in OSM format.

---

## Installation

### Installing JOSM

1. Open JOSM and go to **"Preferences (F12)" → "Plugins"**.
2. Find **PatchExport** and install it.
3. Restart JOSM.

---

## Usage

1. Prepare an image as the background layer and the map to be edited as the foreground layer.
2. Make changes to the layer (delete, add, or modify elements or their parameters).
3. In the **"Save as"** field, specify the **.osc** extension.
4. Save the file.

---

## How to Build

### Setting up the Environment

1. Install Java 17 and necessary tools:
   ```sh
   sudo apt install openjdk-17-jre openjdk-17-jdk gradle
   ```
2. Create a workspace directory:
   ```sh
   mkdir -p ~/workspace
   cd ~/workspace
   ```

---

### Cloning the Repository

```sh
git clone https://github.com/CZBackendUser1234/exportPatch.git
mv ~/workspace/exportPatch ~/workspace/josm/plugins
```

---

### Building the Plugin

```sh
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
cd ~/workspace/josm/plugins/exportPatch
./gradlew jarExp
```

---

### Building JOSM (Optional)

If you need to build JOSM:

```sh
cd ~/workspace
svn co https://josm.openstreetmap.de/osmsvn/applications/editors/josm
cd josm/core
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ant ant
```

After building JOSM and the **PatchExport** plugin, activate it in JOSM and restart the application.

---

https://josm.openstreetmap.de/wiki/DevelopersGuide/DevelopingPlugins
https://josm.openstreetmap.de/doc/

---

Copyright (c) 2025, MT Inc. Released under the GPL License.