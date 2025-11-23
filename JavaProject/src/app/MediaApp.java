package app;

import models.Movie;
import models.Show;
import services.Catalog;
import java.util.*;

public class MediaApp {
    private static final Scanner sc = new Scanner(System.in);
    private static Catalog catalog = new Catalog("movies.csv", "shows.csv");

    public static void main(String[] args) {
        catalog.load();
        System.out.println("=== Welcome to Media Catalog ===");
        boolean run = true;
        while (run) {
            printMenu();
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": addMovie(); break;
                case "2": addShow(); break;
                case "3": listAll(); break;
                case "4": search(); break;
                case "5": remove(); break;
                case "6": update(); break;
                case "7": viewWatchlist(); break;
                case "8": catalog.save(); System.out.println("Saved. Goodbye!"); run = false; break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1) Add Movie");
        System.out.println("2) Add Show");
        System.out.println("3) List All Media");
        System.out.println("4) Search (title/genre)");
        System.out.println("5) Remove by ID");
        System.out.println("6) Update Media");
        System.out.println("7) View Watchlist");
        System.out.println("8) Save & Exit");
        System.out.print("Choice: ");
    }

    // ----------------------- ADD MOVIE -----------------------
    private static void addMovie() {
        System.out.print("Enter ID: ");
        String id = sc.nextLine().trim();
        if (catalog.findMovieById(id) != null) {
            System.out.println("Movie ID already exists.");
            return;
        }

        System.out.print("Title: ");
        String t = sc.nextLine().trim();
        System.out.print("Year: ");
        int y = parseIntOr(-1, sc.nextLine().trim()); if (y < 0) { System.out.println("Invalid year."); return; }
        System.out.print("Genre: ");
        String g = sc.nextLine().trim();
        System.out.print("Duration (minutes): ");
        int d = parseIntOr(-1, sc.nextLine().trim()); if (d < 0) { System.out.println("Invalid duration."); return; }

        Movie m = new Movie(id, t, y, g, d);

        // watched?
        System.out.print("Have you watched it? (y/n): ");
        String watchedAns = sc.nextLine().trim().toLowerCase();
        if (watchedAns.equals("y") || watchedAns.equals("yes")) {
            m.setWatched(true);
            m.setInWatchlist(false);
        } else {
            m.setWatched(false);
            System.out.print("Add to watchlist? (y/n): ");
            String wl = sc.nextLine().trim().toLowerCase();
            m.setInWatchlist(wl.equals("y") || wl.equals("yes"));
        }

        catalog.addMovie(m);
        System.out.println("Movie added.");
    }

    // ----------------------- ADD SHOW -----------------------
    private static void addShow() {
        System.out.print("Enter ID: ");
        String id = sc.nextLine().trim();
        if (catalog.findShowById(id) != null) {
            System.out.println("Show ID already exists.");
            return;
        }

        System.out.print("Title: ");
        String t = sc.nextLine().trim();
        System.out.print("Year: ");
        int y = parseIntOr(-1, sc.nextLine().trim()); if (y < 0) { System.out.println("Invalid year."); return; }
        System.out.print("Genre: ");
        String g = sc.nextLine().trim();
        System.out.print("Seasons: ");
        int s = parseIntOr(-1, sc.nextLine().trim()); if (s < 0) { System.out.println("Invalid seasons."); return; }
        System.out.print("Episodes per Season: ");
        int e = parseIntOr(-1, sc.nextLine().trim()); if (e < 0) { System.out.println("Invalid episodes."); return; }

        Show sh = new Show(id, t, y, g, s, e);

        // Ask whether user has watched anything
        System.out.print("Have you watched any episodes? Enter as 'S E' (season episode), or leave blank: ");
        String prog = sc.nextLine().trim();
        if (!prog.isEmpty()) {
            String[] parts = prog.split("\\s+");
            try {
                int ws = Integer.parseInt(parts[0]);
                int we = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                // basic bounds checking
                if (ws < 0) ws = 0;
                if (we < 0) we = 0;
                sh.setWatchedSeasons(ws);
                sh.setWatchedEpisode(we);
                sh.setInWatchlist(false);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid progress input. Skipping progress.");
                askAddShowToWatchlistIfNeeded(sh);
            }
        } else {
            askAddShowToWatchlistIfNeeded(sh);
        }

        catalog.addShow(sh);
        System.out.println("Show added.");
    }

    private static void askAddShowToWatchlistIfNeeded(Show sh) {
        System.out.print("Add to watchlist? (y/n): ");
        String wl = sc.nextLine().trim().toLowerCase();
        sh.setInWatchlist(wl.equals("y") || wl.equals("yes"));
    }

    // ----------------------- LIST ALL -----------------------
    private static void listAll() {
        List<Movie> movies = catalog.listMovies();
        List<Show> shows = catalog.listShows();

        if (movies.isEmpty() && shows.isEmpty()) {
            System.out.println("No media found.");
            return;
        }

        System.out.println("\nMovies:");
        for (Movie m : movies) System.out.println(m);

        System.out.println("\nShows:");
        for (Show sh : shows) System.out.println(sh);
    }

    // ----------------------- SEARCH -----------------------
    private static void search() {
        System.out.print("Enter query (title or genre): ");
        String q = sc.nextLine().trim();

        List<Movie> mv = catalog.searchMovies(q);
        List<Show> sv = catalog.searchShows(q);

        if (mv.isEmpty() && sv.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }

        System.out.println("\nMovie Matches:");
        for (Movie m : mv) System.out.println(m);

        System.out.println("\nShow Matches:");
        for (Show s : sv) System.out.println(s);
    }

    // ----------------------- REMOVE -----------------------
    private static void remove() {
        System.out.print("Enter ID to remove: ");
        String id = sc.nextLine().trim();

        boolean removed = catalog.removeMovie(id) || catalog.removeShow(id);
        System.out.println(removed ? "Removed successfully." : "ID not found.");
    }

    // ----------------------- UPDATE -----------------------
    private static void update() {
        System.out.print("Enter ID to update: ");
        String id = sc.nextLine().trim();

        // Try movie
        Movie m = catalog.findMovieById(id);
        if (m != null) {
            System.out.print("New Year (or blank to skip): ");
            String y = sc.nextLine().trim();
            if (!y.isEmpty()) { int yy = parseIntOr(-1, y); if (yy >= 0) m.setYear(yy); }

            System.out.print("New Genre (or blank to skip): ");
            String g = sc.nextLine().trim();
            if (!g.isEmpty()) m.setGenre(g);

            System.out.print("New Duration (mins) (or blank to skip): ");
            String d = sc.nextLine().trim();
            if (!d.isEmpty()) { int dd = parseIntOr(-1, d); if (dd >= 0) m.setDurationMinutes(dd); }

            System.out.print("Mark as watched? (y/n or blank to skip): ");
            String w = sc.nextLine().trim().toLowerCase();
            if (w.equals("y") || w.equals("yes")) { m.setWatched(true); m.setInWatchlist(false); }
            else if (w.equals("n") || w.equals("no")) { m.setWatched(false); System.out.print("Add to watchlist? (y/n): "); String wl = sc.nextLine().trim().toLowerCase(); m.setInWatchlist(wl.equals("y")||wl.equals("yes")); }

            System.out.println("Movie updated.");
            return;
        }

        // Try show
        Show s = catalog.findShowById(id);
        if (s != null) {
            System.out.print("New Year (or blank): ");
            String y = sc.nextLine().trim();
            if (!y.isEmpty()) { int yy = parseIntOr(-1, y); if (yy >= 0) s.setYear(yy); }

            System.out.print("New Genre (or blank): ");
            String g = sc.nextLine().trim();
            if (!g.isEmpty()) s.setGenre(g);

            System.out.print("New Seasons (or blank): ");
            String se = sc.nextLine().trim();
            if (!se.isEmpty()) { int ss = parseIntOr(-1,se); if (ss>=0) s.setSeasons(ss); }

            System.out.print("New Episodes/Season (or blank): ");
            String ep = sc.nextLine().trim();
            if (!ep.isEmpty()) { int ee = parseIntOr(-1,ep); if (ee>=0) s.setEpisodesPerSeason(ee); }

            System.out.print("Update progress? Enter 'S E' (season episode) or blank to skip: ");
            String prog = sc.nextLine().trim();
            if (!prog.isEmpty()) {
                String[] parts = prog.split("\\s+");
                try {
                    int ws = Integer.parseInt(parts[0]);
                    int we = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                    s.setWatchedSeasons(Math.max(0, ws));
                    s.setWatchedEpisode(Math.max(0, we));
                    s.setInWatchlist(false);
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid progress. Skipped.");
                }
            } else {
                System.out.print("Add/remove from watchlist? (a to add, r to remove, blank to skip): ");
                String wr = sc.nextLine().trim().toLowerCase();
                if (wr.equals("a")) s.setInWatchlist(true);
                else if (wr.equals("r")) s.setInWatchlist(false);
            }

            System.out.println("Show updated.");
            return;
        }

        System.out.println("ID not found.");
    }

    // ----------------------- VIEW WATCHLIST -----------------------
    private static void viewWatchlist() {
        List<Movie> movies = catalog.listMovies();
        List<Show> shows = catalog.listShows();

        boolean any = false;
        System.out.println("\nWatchlist - Movies:");
        for (Movie m : movies) {
            if (m.isInWatchlist()) {
                any = true;
                System.out.println(m);
            }
        }

        System.out.println("\nWatchlist - Shows:");
        for (Show s : shows) {
            if (s.isInWatchlist()) {
                any = true;
                System.out.println(s);
            }
        }

        if (!any) System.out.println("Your watchlist is empty.");
    }

    // ----------------------- UTIL -----------------------
    private static int parseIntOr(int fallback, String s) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return fallback; }
    }
}