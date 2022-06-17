package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
