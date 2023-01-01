package gov.tech.gtbookclub.controller;

import gov.tech.gtbookclub.model.entity.Book;
import gov.tech.gtbookclub.model.request.CreateBookRequest;
import gov.tech.gtbookclub.model.request.UpdateBookRequest;
import gov.tech.gtbookclub.model.response.BaseResponse;
import gov.tech.gtbookclub.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<BaseResponse<List<Book>>> getBooks(){
        return ResponseEntity.ok(bookService.getBooks());
    }

    @PostMapping("/book")
    public ResponseEntity<BaseResponse<Book>> saveBook(@Valid @RequestBody CreateBookRequest bookRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(bookRequest));
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BaseResponse<Book>> updateBook(@PathVariable String id, @RequestBody UpdateBookRequest bookRequest){
        return ResponseEntity.ok(bookService.updateBook(id, bookRequest));
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable String id){
        bookService.deleteBookById(id);
    }

    @PutMapping("/book/{id}/borrow")
    public ResponseEntity<BaseResponse<Book>> borrowBook(@PathVariable String id){
        return ResponseEntity.ok(bookService.borrowBook(id));
    }

    @PutMapping("/book/{id}/return")
    public ResponseEntity<BaseResponse<Book>> returnBook(@PathVariable String id){
        return ResponseEntity.ok(bookService.returnBook(id));
    }

}
