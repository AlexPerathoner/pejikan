package com.alexpera.pejikan.model;

import com.alexpera.pejikan.converter.DurationConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "entries", schema = "public")
public class Entry implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_name", referencedColumnName = "name")
    private Category category;

    @NonNull
    @Column(name = "start_date")
    private Date start;
    @NonNull
    @Column(name = "end_date")
    private Date end;
    @Convert(converter = DurationConverter.class)
    private Duration correction;
    private String linkedId;
    @Convert(converter = DurationConverter.class)
    private Duration total;

    public Entry(String linkedId, String title, String description, Category category, Date start, Date end, Duration correction, Duration total) {
        this.linkedId = linkedId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.start = start;
        this.end = end;
        this.correction = correction;
        this.total = total;
    }
}

