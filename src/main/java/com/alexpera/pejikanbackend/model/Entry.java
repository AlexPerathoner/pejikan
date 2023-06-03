package com.alexpera.pejikanbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "entries")
public class Entry implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @NonNull
    @Column(name = "start_date")
    private Date start;
    @NonNull
    @Column(name = "end_date")
    private Date end;
    private Time correction;
    private String linkedId;
    private Time total;
}

