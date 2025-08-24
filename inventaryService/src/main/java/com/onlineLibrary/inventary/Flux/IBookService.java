package com.onlineLibrary.inventary.Flux;

import com.onlineLibrary.inventary.Entities.DTO.BookDTO;
import com.onlineLibrary.inventary.Entities.DTO.BookQuantityResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.BookResponseDTO;
import com.onlineLibrary.inventary.Entities.DTO.BooksResponseDTO;

public interface IBookService {

     BooksResponseDTO getBooks() throws Exception;
     BookResponseDTO getBookById(int id) throws Exception;
     BookResponseDTO findBookByIsbn(String isbn) throws Exception;
     BookQuantityResponseDTO decreaseBookQuantity(int id, int quantity) throws Exception;
     BookResponseDTO addBook(BookDTO body);
     boolean removeBook(int id);
}
