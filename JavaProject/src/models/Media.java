package models;

public abstract class Media {
    protected String id;
    protected String title;
    protected int year;
    protected String genre;

    public Media(String id, String title, int year, String genre) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getGenre() { return genre; }

    // --- Public setters so other packages can update safely ---
    public void setYear(int year) { this.year = year; }
    public void setGenre(String genre) { this.genre = genre; }

    public abstract String toCSV();
    public abstract String toString();
}