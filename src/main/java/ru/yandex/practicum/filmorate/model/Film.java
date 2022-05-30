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
    /**
     * Set if user ids who liked the film
     */
    private Set<Integer> like = new HashSet<>();

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
