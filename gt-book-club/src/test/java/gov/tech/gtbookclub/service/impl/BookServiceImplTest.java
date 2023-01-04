package gov.tech.gtbookclub.service.impl;

import gov.tech.gtbookclub.exception.ResourceNotFoundException;
import gov.tech.gtbookclub.model.entity.Book;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.model.request.CreateBookRequest;
import gov.tech.gtbookclub.model.request.UpdateBookRequest;
import gov.tech.gtbookclub.model.response.BaseResponse;
import gov.tech.gtbookclub.repository.BookRepository;
import gov.tech.gtbookclub.service.BookService;
import gov.tech.gtbookclub.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    BookRepository bookRepository;

    @Mock
    UserService userService;

    @InjectMocks
    BookServiceImpl bookServiceImpl;

    public Book mock_book(){
        Book book = new Book();
        book.setId("1");
        book.setTitle("title1");
        book.setDescription("description1");
        book.setGenre("genre1");
        book.setAuthor("author1");
        book.setYearPublished("2020");
        book.setAvailability("2");
        book.setLastBorrower("user@email.com");
        book.setCreatedAt(new Date());
        book.setUpdatedAt(new Date());
        return book;
    }

    public Book mock_book2(){
        Book book = new Book();
        book.setId("2");
        book.setTitle("title2");
        book.setDescription("description2");
        book.setGenre("genre2");
        book.setAuthor("author2");
        book.setYearPublished("2021");
        book.setAvailability("2");
        book.setLastBorrower("user@email.com");
        book.setCreatedAt(new Date());
        book.setUpdatedAt(new Date());
        return book;
    }

    @Test
    public void getBooks_Success(){
        List<Book> list = new ArrayList<>();
        list.add(mock_book());
        Mockito.when(bookRepository.findAll()).thenReturn(list);
        BaseResponse<List<Book>> listBaseResponse = bookServiceImpl.getBooks();
        assertEquals(listBaseResponse.getStatus(), "SUCCESS");

        List<Book> bookList = listBaseResponse.getData();
        assertEquals(bookList.get(0).getId(), "1");
        assertEquals(bookList.get(0).getTitle(), "title1");
        assertEquals(bookList.get(0).getDescription(), "description1");
        assertEquals(bookList.get(0).getGenre(), "genre1");
        assertEquals(bookList.get(0).getAuthor(), "author1");
        assertEquals(bookList.get(0).getYearPublished(), "2020");
        assertEquals(bookList.get(0).getAvailability(), "2");
        assertEquals(bookList.get(0).getLastBorrower(), "user@email.com");
    }

    public void mock_bookrepo_findByID_SUCCESS(){
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(mock_book()));
    }

    public void mock_bookrepo_findByID_EXCEPTION(){
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(null));
    }

    @Test
    public void getBookByID_SUCCESS(){
        mock_bookrepo_findByID_SUCCESS();
        BaseResponse<Book> bookBaseResponse = bookServiceImpl.getBookByID("1");
        assertEquals(bookBaseResponse.getStatus(), "SUCCESS");

        assertEquals(bookBaseResponse.getData().getId(), "1");
        assertEquals(bookBaseResponse.getData().getTitle(), "title1");
        assertEquals(bookBaseResponse.getData().getDescription(), "description1");
        assertEquals(bookBaseResponse.getData().getGenre(), "genre1");
        assertEquals(bookBaseResponse.getData().getAuthor(), "author1");
        assertEquals(bookBaseResponse.getData().getYearPublished(), "2020");
        assertEquals(bookBaseResponse.getData().getAvailability(), "2");
        assertEquals(bookBaseResponse.getData().getLastBorrower(), "user@email.com");
    }

    @Test
    public void getBookByID_ResourceNotFoundException(){
        mock_bookrepo_findByID_EXCEPTION();
        assertThrows(ResourceNotFoundException.class, ()-> bookServiceImpl.getBookByID("2"));
    }

    @Test
    public void saveBook_SUCCESS(){
        CreateBookRequest bookRequest = new CreateBookRequest();
        bookRequest.setTitle("title1");
        bookRequest.setDescription("description1");
        bookRequest.setGenre("genre1");
        bookRequest.setAuthor("author1");
        bookRequest.setAvailability("2");
        bookRequest.setYearPublished("2020");
        Mockito.when(bookRepository.save(any())).thenReturn(mock_book());

        BaseResponse<Book> bookBaseResponse = bookServiceImpl.saveBook(bookRequest);
        assertEquals(bookBaseResponse.getStatus(), "SUCCESS");
        assertEquals(bookBaseResponse.getData().getTitle(), "title1");
        assertEquals(bookBaseResponse.getData().getDescription(), "description1");
        assertEquals(bookBaseResponse.getData().getGenre(), "genre1");
        assertEquals(bookBaseResponse.getData().getAuthor(), "author1");
        assertEquals(bookBaseResponse.getData().getYearPublished(), "2020");
        assertEquals(bookBaseResponse.getData().getAvailability(), "2");
    }

    @Test
    public void updateBook_SUCCESS(){
        mock_bookrepo_findByID_SUCCESS();

        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        updateBookRequest.setTitle("title2");
        updateBookRequest.setDescription("description2");
        updateBookRequest.setGenre("genre2");
        updateBookRequest.setAuthor("author2");
        updateBookRequest.setYearPublished("2021");
        Mockito.when(bookRepository.save(any())).thenReturn(mock_book2());

        BaseResponse<Book> bookBaseResponse = bookServiceImpl.updateBook("1", updateBookRequest);
        assertEquals(bookBaseResponse.getStatus(), "SUCCESS");

        assertEquals(bookBaseResponse.getData().getTitle(), "title2");
        assertEquals(bookBaseResponse.getData().getDescription(), "description2");
        assertEquals(bookBaseResponse.getData().getGenre(), "genre2");
        assertEquals(bookBaseResponse.getData().getAuthor(), "author2");
        assertEquals(bookBaseResponse.getData().getYearPublished(), "2021");
        assertEquals(bookBaseResponse.getData().getAvailability(), "2");
    }

    @Test
    public void deleteBookById_SUCCESS(){
        mock_bookrepo_findByID_SUCCESS();
        bookServiceImpl.deleteBookById("2");
    }

    @Test
    public void returnBook_SUCCESS(){
        mock_bookrepo_findByID_SUCCESS();
        User loggedUser = new User();
        loggedUser.setEmail("user@email.com");
        Mockito.when(userService.getLoggedUser()).thenReturn(loggedUser);
        Mockito.when(bookRepository.save(any())).thenReturn(mock_book());

        BaseResponse<Book> bookBaseResponse = bookServiceImpl.returnBook("2");
        assertEquals(bookBaseResponse.getStatus(), "SUCCESS");

        assertEquals(bookBaseResponse.getData().getTitle(), "title1");
        assertEquals(bookBaseResponse.getData().getDescription(), "description1");
        assertEquals(bookBaseResponse.getData().getGenre(), "genre1");
        assertEquals(bookBaseResponse.getData().getAuthor(), "author1");
        assertEquals(bookBaseResponse.getData().getYearPublished(), "2020");
        assertEquals(bookBaseResponse.getData().getAvailability(), "2");
    }

    @Test
    public void returnBook_ResourceNotFoundException(){
        mock_bookrepo_findByID_SUCCESS();
        User loggedUser = new User();
        loggedUser.setEmail("user2@email.com");
        Mockito.when(userService.getLoggedUser()).thenReturn(loggedUser);
        assertThrows(ResourceNotFoundException.class, ()-> bookServiceImpl.returnBook("2"));
    }

    @Test
    public void borrowBook_SUCCESS(){
        mock_bookrepo_findByID_SUCCESS();

        User loggedUser = new User();
        loggedUser.setEmail("user1@email.com");
        Mockito.when(userService.getLoggedUser()).thenReturn(loggedUser);
        Mockito.when(bookRepository.save(any())).thenReturn(mock_book());

        BaseResponse<Book> bookBaseResponse =bookServiceImpl.borrowBook("1");
        assertEquals(bookBaseResponse.getStatus(), "SUCCESS");
        assertEquals(bookBaseResponse.getData().getAvailability(), "2");
    }

    @Test
    public void borrowBook_ResourceNotFoundException(){
        Book book = new Book();
        book.setAvailability("0");

        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.of(book));
        assertThrows(ResourceNotFoundException.class, ()-> bookServiceImpl.borrowBook("1"));
    }
}