package models;

public class Show extends Media {
    private int seasons;
    private int episodesPerSeason;
    private int watchedSeasons;     // how many full seasons watched
    private int watchedEpisode;     // last episode watched in current season (1-based), 0 means none
    private boolean inWatchlist;

    public Show(String id, String title, int year, String genre, int seasons, int episodesPerSeason) {
        super(id, title, year, genre);
        this.seasons = seasons;
        this.episodesPerSeason = episodesPerSeason;
        this.watchedSeasons = 0;
        this.watchedEpisode = 0;
        this.inWatchlist = false;
    }

    // Extended constructor for CSV loading
    public Show(String id, String title, int year, String genre, int seasons, int episodesPerSeason,
                int watchedSeasons, int watchedEpisode, boolean inWatchlist) {
        super(id, title, year, genre);
        this.seasons = seasons;
        this.episodesPerSeason = episodesPerSeason;
        this.watchedSeasons = watchedSeasons;
        this.watchedEpisode = watchedEpisode;
        this.inWatchlist = inWatchlist;
    }

    public int getSeasons() { return seasons; }
    public int getEpisodesPerSeason() { return episodesPerSeason; }
    public void setSeasons(int s) { seasons = s; }
    public void setEpisodesPerSeason(int e) { episodesPerSeason = e; }

    public int getWatchedSeasons() { return watchedSeasons; }
    public void setWatchedSeasons(int w) { watchedSeasons = w; }

    public int getWatchedEpisode() { return watchedEpisode; }
    public void setWatchedEpisode(int e) { watchedEpisode = e; }

    public boolean isInWatchlist() { return inWatchlist; }
    public void setInWatchlist(boolean w) { inWatchlist = w; }

    @Override
    public String toCSV() {
        // id,title,year,genre,seasons,episodesPerSeason,watchedSeasons,watchedEpisode,inWatchlist
        return id + "," + escape(title) + "," + year + "," + escape(genre) + "," +
               seasons + "," + episodesPerSeason + "," + watchedSeasons + "," + watchedEpisode + "," + inWatchlist;
    }

    public static Show fromCSV(String line) {
        String[] p = splitCSV(line);
        if (p.length < 6) return null;
        try {
            String id = p[0];
            String title = p[1];
            int year = Integer.parseInt(p[2]);
            String genre = p[3];
            int seasons = Integer.parseInt(p[4]);
            int eps = Integer.parseInt(p[5]);
            int watchedSeasons = 0;
            int watchedEpisode = 0;
            boolean inWatchlist = false;
            if (p.length >= 7) watchedSeasons = Integer.parseInt(p[6]);
            if (p.length >= 8) watchedEpisode = Integer.parseInt(p[7]);
            if (p.length >= 9) inWatchlist = Boolean.parseBoolean(p[8]);
            return new Show(id, title, year, genre, seasons, eps, watchedSeasons, watchedEpisode, inWatchlist);
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
        String progress = "";
        if (watchedSeasons > 0 || watchedEpisode > 0) {
            progress = String.format(" | Progress: S%d E%d", watchedSeasons, watchedEpisode);
        } else {
            progress = " | Progress: Not started";
        }
        String wl = inWatchlist ? " (In watchlist)" : "";
        return String.format("Show  | ID:%s | %s (%d) | %s | %d seasons x %d eps%s%s",
                id, title, year, genre, seasons, episodesPerSeason, progress, wl);
    }
}