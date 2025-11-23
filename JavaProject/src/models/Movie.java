package models;

public class Movie extends Media {
    private int durationMinutes;
    private boolean watched;       // true if watched
    private boolean inWatchlist;   // true if user added to watchlist

    public Movie(String id, String title, int year, String genre, int durationMinutes) {
        super(id, title, year, genre);
        this.durationMinutes = durationMinutes;
        this.watched = false;
        this.inWatchlist = false;
    }

    // Extended constructor used when loading from CSV
    public Movie(String id, String title, int year, String genre, int durationMinutes, boolean watched, boolean inWatchlist) {
        super(id, title, year, genre);
        this.durationMinutes = durationMinutes;
        this.watched = watched;
        this.inWatchlist = inWatchlist;
    }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int d) { this.durationMinutes = d; }

    public boolean isWatched() { return watched; }
    public void setWatched(boolean w) { this.watched = w; }

    public boolean isInWatchlist() { return inWatchlist; }
    public void setInWatchlist(boolean w) { this.inWatchlist = w; }

    @Override
    public String toCSV() {
        // id,title,year,genre,durationMinutes,watched,inWatchlist
        return id + "," + escape(title) + "," + year + "," + escape(genre) + "," + durationMinutes + "," + watched + "," + inWatchlist;
    }

    public static Movie fromCSV(String line) {
        String[] p = splitCSV(line);
        if (p.length < 5) return null;
        try {
            String id = p[0];
            String title = p[1];
            int year = Integer.parseInt(p[2]);
            String genre = p[3];
            int duration = Integer.parseInt(p[4]);
            boolean watched = false;
            boolean inWatchlist = false;
            if (p.length >= 6) watched = Boolean.parseBoolean(p[5]);
            if (p.length >= 7) inWatchlist = Boolean.parseBoolean(p[6]);
            return new Movie(id, title, year, genre, duration, watched, inWatchlist);
        } catch (Exception ex) {
            return null;
        }
    }

    private static String escape(String s) { return s.replace(",", "\\,"); }
    private static String unescape(String s) { return s.replace("\\,", ","); }

    private static String[] splitCSV(String line) {
        java.util.List<String> cols = new java.util.ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean esc = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\\' && !esc) { esc = true; continue; }
            if (c == ',' && !esc) { cols.add(cur.toString()); cur.setLength(0); }
            else { cur.append(c); esc = false; }
        }
        cols.add(cur.toString());
        for (int i = 0; i < cols.size(); i++) cols.set(i, unescape(cols.get(i)));
        return cols.toArray(new String[0]);
    }

    @Override
    public String toString() {
        String watchedStr = watched ? "Watched" : "Not watched";
        String wl = inWatchlist ? " (In watchlist)" : "";
        return String.format("Movie | ID:%s | %s (%d) | %s | %d min | %s%s", id, title, year, genre, durationMinutes, watchedStr, wl);
    }
}