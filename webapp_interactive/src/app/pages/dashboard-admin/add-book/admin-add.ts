import { Component, OnInit } from '@angular/core';
import { BookService } from '../../../services/Inventary/Book/book.service';
import {MatButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
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
    MatDatepicker,
    MatDatepickerToggle,
    MatDatepickerInput,
    MatNativeDateModule,
    MatInput,
    MatCardContent,
    MatCardTitle,
    MatCard,
    MatDivider,
    MatButtonToggle,

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
              private snackBar: MatSnackBar   // 👈 ajout
  ) {}

  ngOnInit(): void {
    this.loadOptions();
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
        this.snackBar.open('✅ Book added successfully', 'Close', {
          duration: 3000, // 3 secondes
          horizontalPosition: 'center',
          verticalPosition: 'top',
        });
        this.resetForm();
      },
      error: err => console.error('❌ Erreur while adding book', err)
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
}


export const MY_DATE_FORMATS = {
  parse: { dateInput: 'DD/MM/YYYY' },
  display: {
    dateInput: 'dd/MM/yyyy',
    monthYearLabel: 'MMM yyyy',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM yyyy',
  },
};
