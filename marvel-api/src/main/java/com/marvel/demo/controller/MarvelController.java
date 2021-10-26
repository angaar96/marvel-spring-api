package com.marvel.demo.controller;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.marvel.demo.entity.MarvelCharacter;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

@RestController
public class MarvelController {
    @GetMapping("/{publickey}/{hash}/characters")
    // Uses gson serializer instead of jackson. Dependency must be copied over into pom.xml and the following line added to application.properties
    // spring.mvc.converters.preferred-json-mapper=gson
    // Code taken from: https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
    // Marvel API only returns 100 elements per request. Therefore, 10 requests are made to grab 1000 ids.
    // You need to make a marvel api account and grab a public key and a private key.
    // Go to MD5 hash generator and type 1 followed by your public key and your private key. No spaces.
    // This will give you your hash path variable.
    public ResponseEntity<ArrayList> get1000MarvelCharacterIds(@PathVariable String hash, @PathVariable String publickey) throws Exception {
        ArrayList<JsonElement> setOne = get100MarvelCharacterIds(0, publickey, hash);
        ArrayList<JsonElement> setTwo = get100MarvelCharacterIds(100, publickey, hash);
        ArrayList<JsonElement> setThree = get100MarvelCharacterIds(200, publickey, hash);
        ArrayList<JsonElement> setFour = get100MarvelCharacterIds(300, publickey, hash);
        ArrayList<JsonElement> setFive = get100MarvelCharacterIds(400, publickey, hash);
        ArrayList<JsonElement> setSix = get100MarvelCharacterIds(500, publickey, hash);
        ArrayList<JsonElement> setSeven = get100MarvelCharacterIds(600, publickey, hash);
        ArrayList<JsonElement> setEight = get100MarvelCharacterIds(700, publickey, hash);
        ArrayList<JsonElement> setNine = get100MarvelCharacterIds(800, publickey, hash);
        ArrayList<JsonElement> setTen = get100MarvelCharacterIds(900, publickey, hash);
        ArrayList[] setArray = {setTwo, setThree, setFour, setFive, setSix, setSeven, setEight, setNine, setTen};
        // Initialise final array
        ArrayList<JsonElement> allIdsArray = setOne;
        // Loop through all sets and concatenate to allIdsArray using addAll() method
        for (ArrayList set : setArray) {
            allIdsArray.addAll(set);
        }
        return ResponseEntity.status(HttpStatus.OK).body(allIdsArray);
    }

    public ArrayList<JsonElement> get100MarvelCharacterIds(int offset, String publickey, String hash) throws Exception {
        try (java.io.InputStream is = new java.net.URL("http://gateway.marvel.com/v1/public/characters?ts=1&apikey=" + publickey + "&hash=" + hash + "&limit=100" + "&offset=" + offset).openStream()) {
            String contents = new String(is.readAllBytes());
            com.google.gson.JsonElement allCharJsonElement = com.google.gson.JsonParser.parseString(contents); //from 'com.google.code.gson:gson:2.8.6'
            JsonObject allChars = allCharJsonElement.getAsJsonObject();
            JsonObject data = allChars.get("data").getAsJsonObject();
            JsonArray results = data.get("results").getAsJsonArray();
            ArrayList<JsonElement> idList = new ArrayList<>();
            for (JsonElement result : results) {
                JsonObject resultObject = (JsonObject) result;
                idList.add(resultObject.get("id"));
            }
            return idList;
        }
    }

    @GetMapping("/{publickey}/{hash}/characters/{id}")
    public ResponseEntity<MarvelCharacter> getOneMarvelCharById(@PathVariable int id, @PathVariable String hash, @PathVariable String publickey) throws Exception {
        try (java.io.InputStream is = new java.net.URL("http://gateway.marvel.com/v1/public/characters/" + id + "?ts=1&apikey=" + publickey + "&hash=" + hash).openStream()) {
            String contents = new String(is.readAllBytes());
            com.google.gson.JsonElement allCharJsonElement = com.google.gson.JsonParser.parseString(contents); //from 'com.google.code.gson:gson:2.8.6'
            JsonObject allChars = allCharJsonElement.getAsJsonObject();
            JsonObject data = allChars.get("data").getAsJsonObject();
            JsonArray results = data.get("results").getAsJsonArray();
            JsonObject characterData = results.get(0).getAsJsonObject();
            String name = characterData.get("name").getAsString();
            String description = characterData.get("description").getAsString();
            JsonObject thumbnail = characterData.get("thumbnail").getAsJsonObject();
            MarvelCharacter chosenCharacter = new MarvelCharacter(id, name, description, thumbnail);
            return ResponseEntity.status(HttpStatus.OK).body(chosenCharacter);
        }
    }

    @GetMapping("/{publickey}/{hash}/characters/{id}?language={languageCode}")
    public ResponseEntity<MarvelCharacter> getOneMarvelCharAndTranslateDescription(@PathVariable int id, @PathVariable String hash, @PathVariable String publickey, @PathVariable Object languageCode) throws Exception {
        try (java.io.InputStream is = new java.net.URL("http://gateway.marvel.com/v1/public/characters/" + id + "?ts=1&apikey=" + publickey + "&hash=" + hash).openStream()) {
            String contents = new String(is.readAllBytes());
            com.google.gson.JsonElement allCharJsonElement = com.google.gson.JsonParser.parseString(contents); //from 'com.google.code.gson:gson:2.8.6'
            JsonObject allChars = allCharJsonElement.getAsJsonObject();
            JsonObject data = allChars.get("data").getAsJsonObject();
            JsonArray results = data.get("results").getAsJsonArray();
            JsonObject characterData = results.get(0).getAsJsonObject();
            String name = characterData.get("name").getAsString();
            String description = characterData.get("description").getAsString();
            JsonObject thumbnail = characterData.get("thumbnail").getAsJsonObject();

            // translate the description here
            MarvelCharacter chosenCharacter = new MarvelCharacter(id, name, description, thumbnail);
            return ResponseEntity.status(HttpStatus.OK).body(chosenCharacter);
        }

    }
}
