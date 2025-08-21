import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import {MatButton, MatMiniFabButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatOption, MatSelect} from '@angular/material/select';
import {FormsModule} from '@angular/forms';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatNativeDateModule} from '@angular/material/core';
import {MatDivider} from '@angular/material/divider';
import {MatButtonToggle} from '@angular/material/button-toggle';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './dashboard-admin.html',
  styleUrls: ['./dashboard-admin.css'],
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


export class DashboardAdminComponent implements OnInit {
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

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.loadOptions();
  }

  loadOptions() {
    this.bookService.getPublishers().subscribe(pubs => this.publishers = pubs);
    this.bookService.getAuthors().subscribe(auth => this.authors = auth);
    this.bookService.getCategories().subscribe(cats => this.categories = cats);
  }

  addBook() {
    console.log('📚 New book:', this.newBook);
    this.bookService.addBook(this.newBook).subscribe({
      next: res => console.log('✅ Livre ajouté', res),
      error: err => console.error('❌ Erreur ajout livre', err)
    });
  }

  resetForm() {
    this.newBook = { isbn: '', title: '', description: '', parutionDate: null, price: 0, quantity: 0, publisherId: null, authorIds: [], categoryIds: [] };
  }

  addPublisher() {
    const name = prompt('Enter new publisher name:');
    if (name) {
      this.bookService.addPublisher({ name }).subscribe(pub => this.publishers.push(pub));
    }
  }

  addAuthor() {
    const name = prompt('Enter new author name:');
    if (name) {
      this.bookService.addAuthor({ name }).subscribe(author => this.authors.push(author));
    }
  }

  addCategory() {
    const name = prompt('Enter new category name:');
    if (name) {
      this.bookService.addCategory({ name }).subscribe(cat => this.categories.push(cat));
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
