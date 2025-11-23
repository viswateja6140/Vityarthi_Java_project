package services;

import models.Movie;
import models.Show;
import java.util.*;
import java.io.*;

public class Catalog {
    private List<Movie> movies = new ArrayList<>();
    private List<Show> shows = new ArrayList<>();
    private final String movieFile;
    private final String showFile;

    public Catalog(String movieFile, String showFile) {
        this.movieFile = movieFile;
        this.showFile = showFile;
    }

    // Load both CSVs
    public void load() {
        movies.clear(); shows.clear();
        loadMovies(); loadShows();
    }

    private void loadMovies() {
        File f = new File(movieFile);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Movie m = Movie.fromCSV(line);
                if (m != null) movies.add(m);
            }
        } catch (IOException e) { System.out.println("Load movies failed: " + e.getMessage()); }
    }

    private void loadShows() {
        File f = new File(showFile);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Show s = Show.fromCSV(line);
                if (s != null) shows.add(s);
            }
        } catch (IOException e) { System.out.println("Load shows failed: " + e.getMessage()); }
    }

    // Save both
    public void save() {
        saveMovies(); saveShows();
    }

    private void saveMovies() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieFile))) {
            for (Movie m : movies) bw.write(m.toCSV() + "\n");
        } catch (IOException e) { System.out.println("Save movies failed: " + e.getMessage()); }
    }

    private void saveShows() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(showFile))) {
            for (Show s : shows) bw.write(s.toCSV() + "\n");
        } catch (IOException e) { System.out.println("Save shows failed: " + e.getMessage()); }
    }

    // CRUD - Movies
    public boolean addMovie(Movie m) {
        if (findMovieById(m.getId()) != null) return false;
        movies.add(m); return true;
    }
    public Movie findMovieById(String id) {
        for (Movie m : movies) if (m.getId().equalsIgnoreCase(id)) return m;
        return null;
    }
    public boolean removeMovie(String id) {
        Movie m = findMovieById(id); if (m == null) return false; movies.remove(m); return true;
    }
    public List<Movie> listMovies() { return new ArrayList<>(movies); }
    public List<Movie> searchMovies(String q) {
        List<Movie> r = new ArrayList<>();
        for (Movie m : movies) if (m.getTitle().toLowerCase().contains(q.toLowerCase()) || m.getGenre().toLowerCase().contains(q.toLowerCase())) r.add(m);
        return r;
    }

    // CRUD - Shows
    public boolean addShow(Show s) {
        if (findShowById(s.getId()) != null) return false;
        shows.add(s); return true;
    }
    public Show findShowById(String id) {
        for (Show s : shows) if (s.getId().equalsIgnoreCase(id)) return s;
        return null;
    }
    public boolean removeShow(String id) {
        Show s = findShowById(id); if (s == null) return false; shows.remove(s); return true;
    }
    public List<Show> listShows() { return new ArrayList<>(shows); }
    public List<Show> searchShows(String q) {
        List<Show> r = new ArrayList<>();
        for (Show s : shows) if (s.getTitle().toLowerCase().contains(q.toLowerCase()) || s.getGenre().toLowerCase().contains(q.toLowerCase())) r.add(s);
        return r;
    }
}