package com.quizapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizapp.entity.Question;
import com.quizapp.exception.EntityNotFoundException;
import com.quizapp.repository.QuestionRepository;


@Service
public class QuestionService {
	
	@Autowired
	QuestionRepository questionRepository;

	public ResponseEntity<List<Question>> getAllQuestions() {
		try {
		return new  ResponseEntity<List<Question>> (questionRepository.findAll(), HttpStatus.OK);
	    }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Question>> getQuestionsBycategory(String category) {
		
		try {
			return new ResponseEntity<List<Question>>( questionRepository.findByCategory(category),HttpStatus.OK);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> addQuestion(Question question) {
		// TODO Auto-generated method stub
		 questionRepository.save(question);
		
		return new ResponseEntity<>("success", HttpStatus.CREATED);
	}

	public void deleteById(int id) throws EntityNotFoundException {
	    Optional<Question> question = questionRepository.findById(id);
	    if (question.isPresent()) {
	        questionRepository.deleteById(id);  // Delete the question
	    } else {
	        throw new EntityNotFoundException("Question with id " + id + " not found.");
	    }
	}



}
