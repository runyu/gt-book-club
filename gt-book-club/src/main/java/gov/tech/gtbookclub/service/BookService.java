package gov.tech.gtbookclub.service;

import gov.tech.gtbookclub.model.entity.Book;
import gov.tech.gtbookclub.model.request.CreateBookRequest;
import gov.tech.gtbookclub.model.request.UpdateBookRequest;
import gov.tech.gtbookclub.model.response.BaseResponse;

import java.util.List;

public interface BookService {
    public BaseResponse<List<Book>> getBooks();

    public BaseResponse<Book> getBookByID(String id);

    public BaseResponse<Book> saveBook(CreateBookRequest bookRequest);

    public BaseResponse<Book> updateBook(String id, UpdateBookRequest bookRequest);

    public void deleteBookById(String id);

    public BaseResponse<Book> returnBook(String id);

    public BaseResponse<Book> borrowBook(String id);
}
