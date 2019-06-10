package ru.mail.krivonos.al.repository.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_review")
@SQLDelete(sql =
        "UPDATE t_review " +
                "SET deleted = 1 " +
                "WHERE id = ?")
@Where(clause = "deleted = 0")
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @Column(name = "review", nullable = false)
    private String review;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_of_creation", nullable = false)
    private Date dateOfCreation;
    @Column(name = "hidden", nullable = false)
    private boolean isHidden = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        this.isHidden = hidden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review review1 = (Review) o;
        return isHidden == review1.isHidden &&
                id.equals(review1.id) &&
                review.equals(review1.review) &&
                dateOfCreation.equals(review1.dateOfCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, review, dateOfCreation, isHidden);
    }
}
