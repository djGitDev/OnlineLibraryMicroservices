import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../services/Inventary/Book/book.service';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';
import {FormsModule} from '@angular/forms';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatNativeDateModule} from '@angular/material/core';
import {MatDivider} from '@angular/material/divider';
import {MatButtonToggle} from '@angular/material/button-toggle';
import {PublisherService} from '../../../services/Inventary/Publisher/publisher.service';
import {AuthorService} from '../../../services/Inventary/Author/author.service';
import {CategoryService} from '../../../services/Inventary/Category/category.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {AlertService} from '../../../services/Alert/alert.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-admin-dashboard-user',
  templateUrl: './admin-add.html',
  styleUrls: ['./admin-add.css'],
  imports: [
    MatIcon,
    MatButton,
    MatFormField,
    MatLabel,
    MatSelect,
    MatOption,
    FormsModule,
    MatNativeDateModule,
    MatInput,
    MatCardContent,
    MatCardTitle,
    MatCard,
    MatDivider,
    MatButtonToggle,
    MatError,

  ],
})


export class AdminAddComponent implements OnInit {
  newBook: any = {
    isbn: '',
    title: '',
    description: '',
    parutionDate: null,
    price: 0,
    quantity: 0,
    publisherId: null,
    authorNames : [],
    categoryNames: []
  };

  publishers: any[] = [];
  authors: any[] = [];
  categories: any[] = [];

  constructor(private bookService: BookService,
              private publisherService: PublisherService,
              private authorService: AuthorService,
              private categoryService: CategoryService,
              private alerteService : AlertService,
              private router : Router,
  ) {}

  ngOnInit(): void {
    this.loadOptions();
  }

  goBackToAdminMenu() {
    this.router.navigate(['/dashboard-admin']);
  }

  loadOptions() {
    this.publisherService.getPublishers().subscribe(pubs => this.publishers = pubs);
    this.authorService.getAuthors().subscribe(auth => this.authors = auth);
    this.categoryService.getCategories().subscribe(cats => this.categories = cats);
  }

  addBook() {
    console.log('📚 New book:', this.newBook);

    this.bookService.addBook(this.newBook).subscribe({
      next: res => {
        console.log('✅ Book added', res);
        this.alerteService.success('✅ Book added successfully');
        this.resetForm();
      },
      error: err => {
        this.alerteService.error(err);
      }
    });
  }

  resetForm() {
    this.newBook = { isbn: '', title: '', description: '', parutionDate: null, price: 0, quantity: 0, publisherId: null, authorNames: [], categoryNames: [] };
  }

  addPublisher() {
    const name = prompt('Enter new publisher name:');
    if (name) {
      this.publisherService.addPublisher({ name }).subscribe(pub => this.publishers.push(pub));
    }
  }

  addAuthor() {
    const name = prompt('Enter new author name:');
    if (name) {
      this.authorService.addAuthor({ name }).subscribe(author => this.authors.push(author));
    }
  }

  addCategory() {
    const name = prompt('Enter new category name:');
    if (name) {
      this.categoryService.addCategory({ name }).subscribe(cat => this.categories.push(cat));
    }
  }

  isValidDate(dateStr: string): boolean {
    const match = dateStr.match(/^(\d{4})-(\d{2})-(\d{2})$/);
    if (!match) return false;

    const year = +match[1];
    const month = +match[2];
    const day = +match[3];

    if (month < 1 || month > 12) return false;
    if (day < 1 || day > 31) return false;

    const daysInMonth = new Date(year, month, 0).getDate();
    if (day > daysInMonth) return false;

    const today = new Date();
    const inputDate = new Date(year, month - 1, day);
    if (inputDate > today) return false;

    return true;
  }
}

