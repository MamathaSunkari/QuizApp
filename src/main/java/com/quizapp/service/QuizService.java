package com.quizapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quizapp.entity.Question;
import com.quizapp.entity.QuestionWrapper;
import com.quizapp.entity.Quiz;
import com.quizapp.entity.Response;
import com.quizapp.exception.ResourceNotFoundException;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizRepository;

@Service
public class QuizService {
  
	@Autowired
	QuizRepository quizRepository;
	
	@Autowired
	QuestionRepository questionRepository;

	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
		
		List<Question> questions = questionRepository.findRandomQuestionsByCategory(category, numQ);
	   Quiz quiz = new Quiz();
	   quiz.setTitle(title);
	   quiz.setQuestions(questions);
	   quizRepository.save(quiz);
		return new ResponseEntity<>("Success", HttpStatus.CREATED);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuesions(Integer id) {
		
		Optional<Quiz> quiz = quizRepository.findById(id);
		
		List<Question> questionFromDB = quiz.get().getQuestions();
		
		List<QuestionWrapper> questionsForUser = new ArrayList<>();
		
		for(Question q : questionFromDB)
		{
			QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
		   questionsForUser.add(qw);
		
		}
		return new ResponseEntity<>(questionsForUser,HttpStatus.OK);
		
	
	
	}

	public ResponseEntity<Integer> calculateResult(int id, List<Response> responses) throws ResourceNotFoundException {

	    Quiz quiz = quizRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + id));

	    List<Question> questions = quiz.getQuestions();

	    int right = 0;
	    int i = 0;

	    if (responses == null || responses.isEmpty()) {
	        throw new IllegalArgumentException("Responses cannot be null or empty.");
	    }

	    for (Response response : responses) {
	        if (i >= questions.size()) {
	         
	            throw new IllegalArgumentException("More responses than available questions.");
	        }

	        if( response.getResponse() != null &&  response.getResponse().equals(questions.get(i).getRightAnswer())) 
	        right++;
	        i++;
	    }
	    return new ResponseEntity<>(right, HttpStatus.OK);
	}

}
