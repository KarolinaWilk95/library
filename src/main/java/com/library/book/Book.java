package com.library.book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @Column
    private long EAN;
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String genre;
    @Column
    private String publisher;
    @Column
    private LocalDate dateOfPublication;
}
