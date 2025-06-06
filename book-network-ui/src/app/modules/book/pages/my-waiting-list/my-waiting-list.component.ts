import {Component, OnInit} from '@angular/core';
import {BookService} from "../../../../services/services/book.service";
import {ReservationService} from "../../../../services/services/reservation.service";
import {PageResponseReservedBookResponse} from "../../../../services/models/page-response-reserved-book-response";


@Component({
  selector: 'app-my-waiting-list',
  templateUrl: './my-waiting-list.component.html',
  styleUrls: ['./my-waiting-list.component.scss']
})
export class MyWaitingListComponent implements OnInit{
  waitingBooks: PageResponseReservedBookResponse = {};
  page = 0;
  size = 5;
  pages: number[] = [];

  constructor(private reservationService: ReservationService,
              private bookService: BookService) {}

  ngOnInit(): void {
    this.loadWaitingList();
  }

  loadWaitingList(): void {
    this.reservationService.getReservedBooksByUser({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp) => {
        this.waitingBooks = resp;
        this.pages = Array(resp.totalPages).fill(0).map((x, i) => i);
      }
    });
  }

  borrowBook(bookId: number): void {
    this.bookService.borrowBook({ 'book-id': bookId }).subscribe({
      next: () => {
        this.loadWaitingList(); // Refresh list after borrowing
      }
    });
  }
  gotToPage(page: number) {
    this.page = page;
    this.loadWaitingList();
  }

  goToFirstPage() {
    this.page = 0;
    this.loadWaitingList();
  }

  goToPreviousPage() {
    this.page --;
    this.loadWaitingList();
  }

  goToLastPage() {
    this.page = this.waitingBooks.totalPages as number - 1;
    this.loadWaitingList();
  }

  goToNextPage() {
    this.page++;
    this.loadWaitingList();
  }

  get isLastPage() {
    return this.page === this.waitingBooks.totalPages as number - 1;
  }

}
