package com.azouz.book_network_api.book;

import com.azouz.book_network_api.common.BaseEntity;
import com.azouz.book_network_api.feedback.Feedback;
import com.azouz.book_network_api.history.BookTransactionHistory;
import com.azouz.book_network_api.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Book extends BaseEntity {

    private String title;
    private  String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;
    @Transient
    public double getRate(){
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);

        // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
        return Math.round(rate * 10.0) / 10.0;
    }
}
