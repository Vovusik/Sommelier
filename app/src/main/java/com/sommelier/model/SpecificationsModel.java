package com.sommelier.model;

import java.io.Serializable;

public class SpecificationsModel implements Serializable {

    private int id;
    private String name;
    private String sort;
    private int sort_int;
    private String description;
    private String photoSmall;
    private String photoLarge;
    private String link;
    private String term;
    private int frost;
    private String color;
    private String growth;
    private double weight;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public SpecificationsModel() {
    }

    public SpecificationsModel(int id, String name, String sort, int sort_int, String description, String photoSmall, String photoLarge, String link, String term, int frost, String color, String growth, double weight) {

        this.id = id;
        this.name = name;
        this.sort = sort;
        this.sort_int = sort_int;
        this.description = description;
        this.photoSmall = photoSmall;
        this.photoLarge = photoLarge;
        this.link = link;
        this.term = term;
        this.frost = frost;
        this.color = color;
        this.growth = growth;
        this.weight = weight;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getSort_int() {
        return sort_int;
    }

    public void setSort_int(int sort_int) {
        this.sort_int = sort_int;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoSmall() {
        return photoSmall;
    }

    public void setPhotoSmall(String photoSmall) {
        this.photoSmall = photoSmall;
    }

    public String getPhotoLarge() {
        return photoLarge;
    }

    public void setPhotoLarge(String photoLarge) {
        this.photoLarge = photoLarge;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getFrost() {
        return frost;
    }

    public void setFrost(int frost) {
        this.frost = frost;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "SpecificationsModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", sort_int=" + sort_int +
                ", description='" + description + '\'' +
                ", photoSmall='" + photoSmall + '\'' +
                ", photoLarge='" + photoLarge + '\'' +
                ", link='" + link + '\'' +
                ", term='" + term + '\'' +
                ", frost=" + frost +
                ", color='" + color + '\'' +
                ", growth='" + growth + '\'' +
                ", weight=" + weight  +
                '}';
    }





//    public static ArrayList<SpecificationsModel> getGrapes() {
//
//        ArrayList<SpecificationsModel> singleHorizontals = new ArrayList<>();
//
//        singleHorizontals.add(new SpecificationsModel(
//                1, "name 1", "sort", 1, "description 1", "https://goo.gl/S3SeS8", "https://goo.gl/S3SeS8", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                2, "name 2", "sort", 1, "description 2", "https://i.imgur.com/ZcLLrkY.jpg", "https://i.imgur.com/ZcLLrkY.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                3, "name 3", "sort", 1, "description 3", "https://i.imgur.com/ZcLLrkY.jpg", "https://i.imgur.com/ZcLLrkY.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                4, "name 4", "sort", 1, "description 4", "https://i.redd.it/k98uzl68eh501.jpg", "https://i.redd.it/k98uzl68eh501.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                5, "name 5", "sort", 1, "description 5", "https://i.redd.it/j6myfqglup501.jpg", "https://i.redd.it/j6myfqglup501.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                6, "name 6", "sort", 1, "description 6", "https://i.redd.it/obx4zydshg601.jpg", "https://i.redd.it/obx4zydshg601.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                7, "name 7", "sort", 1, "description 7", "https://i.redd.it/qn7f9oqu7o501.jpg", "https://i.redd.it/qn7f9oqu7o501.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                8, "name 8", "sort", 1, "description 8", "https://i.redd.it/0h2gm1ix6p501.jpg", "https://i.redd.it/0h2gm1ix6p501.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                9, "name 9", "sort", 1, "description 9", "https://i.redd.it/obx4zydshg601.jpg", "https://i.redd.it/obx4zydshg601.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//        singleHorizontals.add(new SpecificationsModel(
//                10, "name 10", "sort", 1, "description 10", "https://i.imgur.com/ZcLLrkY.jpg", "https://i.imgur.com/ZcLLrkY.jpg", "link", "term", 21, "color", "growth", 2.5, 1));
//
//
//        return singleHorizontals;
//    }
}

