package lam.fooapp;

import com.google.gson.Gson;

public class Food {
    private Integer id;
    private String title;
    private String price;
    private String description;
    private String type;
    private static Gson gson = new Gson();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public static Food fromJson(String json)
    {
        return gson.fromJson(json,Food.class);
    }
}
