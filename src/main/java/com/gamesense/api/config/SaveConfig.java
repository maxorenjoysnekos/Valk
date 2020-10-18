package com.gamesense.api.config;

import com.gamesense.api.util.players.enemy.Enemies;
import com.gamesense.api.util.players.enemy.Enemy;
import com.gamesense.api.util.players.friends.Friend;
import com.gamesense.api.util.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.clickgui.ClickGUI;
import com.gamesense.client.clickgui.frame.Frames;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.misc.AutoGG;
import com.gamesense.client.module.modules.misc.AutoReply;
import com.google.gson.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author Hoosiers on 10/15/2020
 */

public class SaveConfig {

    public SaveConfig() {
        try {
            saveConfig();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    String fileName = "GameSense3/";
    String moduleName = "Modules/";
    String mainName = "Main/";
    String miscName = "Misc/";

    public void saveConfig() throws IOException {
        if (!Files.exists(Paths.get(fileName))){
            Files.createDirectories(Paths.get(fileName));
        }
        if (!Files.exists(Paths.get(fileName + moduleName))){
            Files.createDirectories(Paths.get(fileName + moduleName));
        }
        if (!Files.exists(Paths.get(fileName + mainName))){
            Files.createDirectories(Paths.get(fileName + mainName));
        }
        if (!Files.exists(Paths.get(fileName + miscName))){
            Files.createDirectories(Paths.get(fileName + miscName));
        }
    }

    public void registerFiles(String location, String name) throws IOException{
        if (!Files.exists(Paths.get(fileName + location + name + ".json"))){
            Files.createFile(Paths.get(fileName + location + name + ".json"));
        }
        else {
            File file = new File(fileName + location + name + ".json");

            file.delete();

            Files.createFile(Paths.get(fileName + location +name + ".json"));
        }
    }

    public void saveModules() throws IOException {
        for (Module module : ModuleManager.getModules()){

            registerFiles(moduleName, module.getName());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + moduleName + module.getName() + ".json"), "UTF-8");
            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();
            moduleObject.add("Module", new JsonPrimitive(module.getName()));

            for (Setting setting : GameSenseMod.getInstance().settingsManager.getSettingsForMod(module)){
                //JsonObject dataObject = new JsonObject();
                switch (setting.getType()){
                    case BOOLEAN: {
                    	settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Boolean) setting).getValue()));
                        break;
                    }
                    case INT: {
                    	settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Integer) setting).getValue()));
                        break;
                    }
                    case DOUBLE: {
                    	settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Double) setting).getValue()));
                        break;
                    }
                    case COLOR: {
                    	settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.ColorSetting) setting).toInteger()));
                        break;
                    }
                    case MODE: {
                    	settingObject.add(setting.getConfigName(), new JsonPrimitive(((Setting.Mode) setting).getValue()));
                        break;
                    }
                }
                //settingObject.add(setting.getConfigName(), dataObject);
            }
            moduleObject.add("Settings", settingObject);
            String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }
    }

    public void saveEnabledModules() throws IOException {

        registerFiles(mainName, "Toggle");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + mainName + "Toggle" + ".json"), "UTF-8");
        JsonObject moduleObject = new JsonObject();
        JsonObject enabledObject = new JsonObject();

        for (Module module : ModuleManager.getModules()){

            enabledObject.add(module.getName(), new JsonPrimitive(module.isEnabled()));
        }
        moduleObject.add("Modules", enabledObject);
        String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public void saveModuleKeybinds() throws IOException {

        registerFiles(mainName, "Bind");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + mainName + "Bind" + ".json"), "UTF-8");
        JsonObject moduleObject = new JsonObject();
        JsonObject bindObject = new JsonObject();

        for (Module module : ModuleManager.getModules()){

            bindObject.add(module.getName(), new JsonPrimitive(module.getBind()));
        }
        moduleObject.add("Modules", bindObject);
        String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public void saveDrawnModules() throws IOException {

        registerFiles(mainName, "Drawn");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + mainName + "Drawn" + ".json"), "UTF-8");
        JsonObject moduleObject = new JsonObject();
        JsonObject drawnObject = new JsonObject();

        for (Module module : ModuleManager.getModules()){

            drawnObject.add(module.getName(), new JsonPrimitive(module.isDrawn()));
        }
        moduleObject.add("Modules", drawnObject);
        String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public void saveCommandPrefix() throws IOException {

        registerFiles(mainName, "CommandPrefix");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + mainName + "CommandPrefix" + ".json"), "UTF-8");
        JsonObject prefixObject = new JsonObject();

        prefixObject.add("Prefix", new JsonPrimitive(Command.getPrefix()));
        String jsonString = gson.toJson(new JsonParser().parse(prefixObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public void saveCustomFont() throws IOException {

        registerFiles(miscName, "CustomFont");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + miscName + "CustomFont" + ".json"), "UTF-8");
        JsonObject fontObject = new JsonObject();

        fontObject.add("Font Name", new JsonPrimitive(GameSenseMod.fontRenderer.getFontName()));
        fontObject.add("Font Size", new JsonPrimitive(GameSenseMod.fontRenderer.getFontSize()));
        String jsonString = gson.toJson(new JsonParser().parse(fontObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public void saveFriendsList() throws IOException {

        registerFiles(miscName, "Friends");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + miscName + "Friends" + ".json"), "UTF-8");
        JsonObject mainObject = new JsonObject();
        JsonArray friendsObject = new JsonArray();

        for (Friend friend : Friends.getFriends()){
            JsonObject dataObject = new JsonObject();
            dataObject.add("Name", new JsonPrimitive(friend.getName()));
            friendsObject.add(dataObject);
        }

        mainObject.add("Friends", friendsObject);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public void saveEnemiesList() throws IOException {

        registerFiles(miscName, "Enemies");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + miscName + "Enemies" + ".json"), "UTF-8");
        JsonObject mainObject = new JsonObject();
        JsonObject enemiesObject = new JsonObject();

        for (Enemy enemy : Enemies.getEnemies()){
            enemiesObject.add("Name", new JsonPrimitive(enemy.getName()));
        }

        mainObject.add("Enemies", enemiesObject);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    //todo: got some more work to do here
    public void saveClickGUIPositions() throws IOException {

        registerFiles(mainName, "ClickGUI");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + mainName + "ClickGUI" + ".json"), "UTF-8");
        JsonObject mainObject = new JsonObject();
        JsonObject panelObject = new JsonObject();

        for (Frames frame : ClickGUI.getFrames()){
            JsonObject valueObject = new JsonObject();

            valueObject.add("PosX", new JsonPrimitive(frame.getX()));
            valueObject.add("PosY", new JsonPrimitive(frame.getY()));
            valueObject.add("State", new JsonPrimitive(frame.isOpen()));

            panelObject.add(frame.category.name(), valueObject);
        }
        mainObject.add("Panels", panelObject);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public void saveAutoGG() throws IOException {

        registerFiles(miscName, "AutoGG");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + miscName + "AutoGG" + ".json"), "UTF-8");
        JsonObject mainObject = new JsonObject();
        JsonObject messageObject = new JsonObject();

        for (String autoGG : AutoGG.getAutoGgMessages()){
            messageObject.add("Message", new JsonPrimitive(autoGG));
        }
        mainObject.add("AutoGG", messageObject);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public void saveAutoReply() throws IOException {

        registerFiles(miscName, "AutoReply");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(fileName + miscName + "AutoReply" + ".json"), "UTF-8");
        JsonObject mainObject = new JsonObject();
        JsonObject messageObject = new JsonObject();

        messageObject.add("Message", new JsonPrimitive(AutoReply.getReply()));
        mainObject.add("AutoReply", messageObject);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }
}