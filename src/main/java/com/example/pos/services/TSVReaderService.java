package com.example.pos.services;

import com.example.pos.model.Item;
import com.example.pos.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

@Component
@AllArgsConstructor
public class TSVReaderService {

    @Autowired
    ItemRepository itemRepository;

    public void readTSV() {
        File file = getResourceFile("pricebook.tsv");
        ArrayList<Item> priceBookItems = new ArrayList<>();

        try{
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] fields = data.split("\t");
                for (String string : fields){
                    string = string.trim();
                }
                priceBookItems.add(new Item(fields));
            }
            myReader.close();
        }
        catch (Exception ignored){

        }
        itemRepository.deleteAll();
        itemRepository.saveAll(priceBookItems);
    }
    private File getResourceFile ( final String fileName){
        URL url = this.getClass()
                .getClassLoader()
                .getResource(fileName);

        if (url == null) {
            throw new IllegalArgumentException(fileName + " is not found 1");
        }

        return new File(url.getFile());
    }
}
