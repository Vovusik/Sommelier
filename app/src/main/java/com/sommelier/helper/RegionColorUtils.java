package com.sommelier.helper;

public class RegionColorUtils {

    public static int getMapColor(String id) {
        int mapColor = 0xFFFFFFFF;
        switch (id) {

            // CAT 3100-3400
            case "UKR283"://Crimea
            case "UKR284"://Mykolayiv
            case "UKR322"://Odessa
            case "UKR331"://Zaporizhzhya
            case "UKR4827"://Kherson
            case "UKR5482"://Sevastopol
                mapColor = 0xFFE57373;
                break;

            // CAT 2850-3000
            case "UKR293"://Transcarpathia
            case "UKR326"://Dnipropetrovs'k
            case "UKR327"://Donets'k
            case "UKR328"://Kharkiv
            case "UKR329"://Luhans'k
            case "UKR330"://Poltava
            case "UKR320"://Кропивни́цкий
                mapColor = 0xFFFFB74D;
                break;

            // CAT 2700-2800
            case "UKR288"://Chernivtsi
            case "UKR321"://Kiev
            case "UKR4826"://Kiev City
            case "UKR319"://Cherkasy
            case "UKR323"://Vinnytsya
                mapColor = 0xFF81C784;
                break;

            // CAT 2500-2600
            case "UKR285"://Chernihiv
            case "UKR286"://Rivne
            case "UKR289"://Ivano-Frankivs'k
            case "UKR290"://Khmel'nyts'kyy
            case "UKR291"://L'viv
            case "UKR292"://Ternopil'
            case "UKR318"://Volyn
            case "UKR324"://Zhytomy
            case "UKR325"://Sumy
                mapColor = 0xFF64B5F6;
                break;
        }
        return mapColor;
    }
}
