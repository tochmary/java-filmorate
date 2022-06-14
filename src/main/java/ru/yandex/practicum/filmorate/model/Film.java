package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private Set<Genre> genres;
    /**
     * Set if user ids who liked the film
     */
    private Set<Integer> like = new HashSet<>();

    public Film() {
    }

    public Film(Integer id,
                String name,
                String description,
                LocalDate releaseDate,
                Integer duration,
                Mpa mpa,
                Set<Genre> genres,
                Set<Integer> like) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.like = like;
    }

    public void addLike(Integer id) {
        like.add(id);
    }

    public void deleteLike(Integer id) {
        like.remove(id);
    }

    public int compareTo(Film other) {
        return other.getLike().size() - this.getLike().size();
    }
}
