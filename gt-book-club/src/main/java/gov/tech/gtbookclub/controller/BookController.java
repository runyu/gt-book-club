package gov.tech.gtbookclub.controller;

import gov.tech.gtbookclub.model.entity.Book;
import gov.tech.gtbookclub.model.request.CreateBookRequest;
import gov.tech.gtbookclub.model.request.UpdateBookRequest;
import gov.tech.gtbookclub.model.response.BaseResponse;
import gov.tech.gtbookclub.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR', 'USER')")
    public ResponseEntity<BaseResponse<List<Book>>> getBooks(){
        return ResponseEntity.ok(bookService.getBooks());
    }

    @PostMapping("/book")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<BaseResponse<Book>> saveBook(@Valid @RequestBody CreateBookRequest bookRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(bookRequest));
    }

    @PutMapping("/book/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<BaseResponse<Book>> updateBook(@PathVariable String id, @RequestBody UpdateBookRequest bookRequest){
        return ResponseEntity.ok(bookService.updateBook(id, bookRequest));
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/book/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public void deleteBook(@PathVariable String id){
        bookService.deleteBookById(id);
    }

    @PutMapping("/book/{id}/borrow")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR', 'USER')")
    public ResponseEntity<BaseResponse<Book>> borrowBook(@PathVariable String id){
        return ResponseEntity.ok(bookService.borrowBook(id));
    }

    @PutMapping("/book/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR', 'USER')")
    public ResponseEntity<BaseResponse<Book>> returnBook(@PathVariable String id){
        return ResponseEntity.ok(bookService.returnBook(id));
    }

}
