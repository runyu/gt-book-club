package gov.tech.gtbookclub.service.impl;

import gov.tech.gtbookclub.enums.StatusEnum;
import gov.tech.gtbookclub.exception.ResourceNotFoundException;
import gov.tech.gtbookclub.model.entity.Book;
import gov.tech.gtbookclub.model.request.CreateBookRequest;
import gov.tech.gtbookclub.model.request.UpdateBookRequest;
import gov.tech.gtbookclub.model.response.BaseResponse;
import gov.tech.gtbookclub.repository.BookRepository;
import gov.tech.gtbookclub.service.BookService;
import gov.tech.gtbookclub.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final UserService userService;

    @Override
    public BaseResponse<List<Book>> getBooks() {
        List<Book> books = bookRepository.findAll();
        return BaseResponse.<List<Book>>builder().status(StatusEnum.SUCCESS.getValue()).data(books).build();
    }

    @Override
    public BaseResponse<Book> getBookByID(String id) {
        return BaseResponse.<Book>builder().status(StatusEnum.SUCCESS.getValue()).data(getBook(id)).build();
    }

    @Override
    public BaseResponse<Book> saveBook(CreateBookRequest bookRequest) {
        Book book = new Book();
        BeanUtils.copyProperties(bookRequest, book);
        book.setUpdatedAt(new Date());
        book.setCreatedAt(new Date());
        bookRepository.save(book);
        return BaseResponse.<Book>builder().status(StatusEnum.SUCCESS.getValue()).data(book).build();
    }

    private Book getBook(String id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book.get();
        }
        throw new ResourceNotFoundException("Book is not found for id = " + id);
    }

    @Override
    public BaseResponse<Book> updateBook(String id, UpdateBookRequest bookRequest) {
        Book exsitngBook = getBook(id);
        exsitngBook.setTitle(bookRequest.getTitle() != null ? bookRequest.getTitle() : exsitngBook.getTitle());
        exsitngBook.setDescription(bookRequest.getDescription() != null ? bookRequest.getDescription() : exsitngBook.getDescription());
        exsitngBook.setGenre(bookRequest.getGenre() != null ? bookRequest.getGenre() : exsitngBook.getGenre());
        exsitngBook.setAuthor(bookRequest.getAuthor() != null ? bookRequest.getAuthor() : exsitngBook.getAuthor());
        exsitngBook.setYearPublished(bookRequest.getYearPublished() != null ? bookRequest.getYearPublished() : exsitngBook.getYearPublished());
        exsitngBook.setUpdatedAt(new Date());
        Book bookUpdated = bookRepository.save(exsitngBook);
        return BaseResponse.<Book>builder().status(StatusEnum.SUCCESS.getValue()).data(bookUpdated).build();
    }

    @Override
    public void deleteBookById(String id) {
        Book book = getBook(id);
        bookRepository.delete(book);
    }

    @Override
    public BaseResponse<Book> returnBook(String id) {
        Book book = getBook(id);

        if(!book.getLastBorrower().equals(userService.getLoggedUser().getEmail())){
            throw new ResourceNotFoundException("Last Borrower does not match the current logged user");
        }

        book.setAvailability(String.valueOf(Integer.valueOf(book.getAvailability()) + 1));
        Book bookUpdated = bookRepository.save(book);
        return BaseResponse.<Book>builder().status(StatusEnum.SUCCESS.getValue()).data(bookUpdated).build();
    }

    @Override
    public BaseResponse<Book> borrowBook(String id) {
        Book book = getBook(id);

        if(Integer.valueOf(book.getAvailability()) <=0 ){
            throw new ResourceNotFoundException("Book id = " + id  + " is not available");
        }

        book.setAvailability(String.valueOf(Integer.valueOf(book.getAvailability()) -1));
        book.setLastBorrower(userService.getLoggedUser().getEmail());

        Book bookUpdated = bookRepository.save(book);
        return BaseResponse.<Book>builder().status(StatusEnum.SUCCESS.getValue()).data(bookUpdated).build();
    }
}
