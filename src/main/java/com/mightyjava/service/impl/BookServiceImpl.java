package com.mightyjava.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mightyjava.domain.Book;
import com.mightyjava.repository.BookRepository;
import com.mightyjava.service.IService;

import java.util.logging.Logger;

@Service
public class BookServiceImpl implements IService<Book> {
	
	@Autowired
	private BookRepository bookRepository;

	Logger logger
			= Logger.getLogger(
			BookServiceImpl.class.getName());

	@Override
	public Page<Book> findAll(Pageable pageable, String searchText) {
		logger.info("caooo");
		return bookRepository.findAllBooks(pageable, searchText);
	}

	@Override
	public Page<Book> findAll(Pageable pageable) {
		logger.info("hellllo");

		return bookRepository.findAll(pageable);
	}

	@Override
	public Book findById(Long id) {
		return bookRepository.findById(id).get();
	}

	@Override
	public Book saveOrUpdate(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public String deleteById(Long id) {
		JSONObject jsonObject = new JSONObject();
		try {
			bookRepository.deleteById(id);
			jsonObject.put("message", "Book deleted successfully");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

}
