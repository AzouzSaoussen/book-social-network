package com.azouz.book_network_api.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {
    Page<BookTransactionHistory> findByUserId(Pageable pageable, Integer userId);

    Page<BookTransactionHistory> findByBookCreatedBy(Pageable pageable, String userId);

    @Query("""
            SELECT (COUNT(*) >0) AS isBorrowed
            FROM BookTransactionHistory bookTransactionHistory
            WHERE bookTransactionHistory.user.id = :userId
            AND bookTransactionHistory.book.id = :bookId
            AND  bookTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(@Param("bookId") Integer bookId, @Param("userId") Integer userId);

 @Query("""
        SELECT
        (COUNT (*) > 0) AS isBorrowed
        FROM BookTransactionHistory bookTransactionHistory
        WHERE bookTransactionHistory.book.id = :bookId
        AND bookTransactionHistory.returnApproved = false
        """)
    boolean isAlreadyBorrowed(@Param("bookId") Integer bookId);

    Optional<BookTransactionHistory> findByBookIdAndUserIdAndReturnedFalseAndReturnApprovedFalse(Integer bookId, Integer userId);


    Optional<BookTransactionHistory> findByBookIdAndBookOwnerIdAndReturnedTrueAndReturnApprovedFalse(Integer bookId, Integer userId);


    Optional<BookTransactionHistory> findFirstByBookIdOrderByCreatedDateDesc(Integer bookId);

}
