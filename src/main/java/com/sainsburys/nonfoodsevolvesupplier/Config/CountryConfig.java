package com.sainsburys.nonfoodsevolvesupplier.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Configuration
public class CountryConfig {

    @Bean

    public Map<String, String> Country(){
        Map<String, String> map = new HashMap<String, String>();
    Scanner scanner = null;

    {
        try {
            scanner = new Scanner(new FileReader("/tmp/countries.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

   
        //map.put("United Kingdom", "GB");
        //map.put("Hong Kong", "HK");
        //map.put("China", "CN");
        while (scanner.hasNextLine()) {
            String[] columns = scanner.nextLine().split("=");

            map.put(columns[0],columns[1]);
        }


        System.out.println(map);

        return map;

    }

}
