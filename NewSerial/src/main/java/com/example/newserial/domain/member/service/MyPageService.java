package com.example.newserial.domain.member.service;

import com.example.newserial.domain.bookmark.repository.Bookmark;
import com.example.newserial.domain.bookmark.repository.BookmarkRepository;
import com.example.newserial.domain.member.dto.response.MyBookmarkDto;
import com.example.newserial.domain.member.dto.response.MyPageDto;
import com.example.newserial.domain.member.dto.response.MyPetDto;
import com.example.newserial.domain.member.dto.response.MyQuizDto;
import com.example.newserial.domain.member.repository.Member;
import com.example.newserial.domain.news.repository.News;
import com.example.newserial.domain.pet.repository.Pet;
import com.example.newserial.domain.pet.repository.PetCondition;
import com.example.newserial.domain.pet.repository.PetConditionRepository;
import com.example.newserial.domain.pet.repository.PetRepository;
import com.example.newserial.domain.quiz.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
public class MyPageService {
    private final BookmarkRepository bookmarkRepository;
    private final NewsQuizRepository newsQuizRepository;
    private final NewsQuizAttemptRepository newsQuizAttemptRepository;
    private final OxQuizRepository oxQuizRepository;
    private final OxQuizAttemptRepository oxQuizAttemptRepository;
    private final PetRepository petRepository;
    private final PetConditionRepository petConditionRepository;

    @Autowired
    public MyPageService(BookmarkRepository bookmarkRepository, NewsQuizRepository newsQuizRepository, NewsQuizAttemptRepository newsQuizAttemptRepository,
                         OxQuizRepository oxQuizRepository, OxQuizAttemptRepository oxQuizAttemptRepository, PetRepository petRepository,
                         PetConditionRepository petConditionRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.newsQuizRepository=newsQuizRepository;
        this.newsQuizAttemptRepository=newsQuizAttemptRepository;
        this.oxQuizRepository=oxQuizRepository;
        this.oxQuizAttemptRepository=oxQuizAttemptRepository;
        this.petRepository=petRepository;
        this.petConditionRepository=petConditionRepository;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //마이페이지: 유저가 북마크한 뉴스 조회
    public List<MyBookmarkDto> getBookmarkNews(Member member) {
        List<MyBookmarkDto> myBookmarkDtoList = new ArrayList<>();

        List<Bookmark> bookmarkList = bookmarkRepository.findByMember(member);

        for (Bookmark bookmark : bookmarkList) {
            String title = bookmark.getNews().getTitle();
            LocalDateTime createdTime = bookmark.getCreatedTime();
            String bookmarkDate = createdTime.format(formatter);
            MyBookmarkDto myBookmarkDto = new MyBookmarkDto(title, bookmarkDate);
            myBookmarkDtoList.add(myBookmarkDto);
        }

        return myBookmarkDtoList;
    }

    //마이페이지: 유저 퀴즈 기록 조회
    public List<MyQuizDto> getMemberQuiz(Member member){
        List<MyQuizDto> myQuizDtoList=new ArrayList<>();

        List<NewsQuizAttempt> newsQuizAttemptList=newsQuizAttemptRepository.findByMember(member);

        List<OxQuizAttempt> oxQuizAttemptList=oxQuizAttemptRepository.findByMember(member);

        if (!newsQuizAttemptList.isEmpty()){
            for(NewsQuizAttempt newsQuizAttempt : newsQuizAttemptList){
                News news=newsQuizAttempt.getNews();
                NewsQuiz newsQuiz=newsQuizRepository.findByNews(news).get();
                String quizQuestion=newsQuiz.getNews_question();
                String quizAnswer=newsQuiz.getNews_answer();
                String userAnswer=newsQuizAttempt.getNews_submitted();
                LocalDateTime createdTime=newsQuizAttempt.getLastModifiedTime();
                String quizDate = createdTime.format(formatter);
                MyQuizDto myQuizDto=new MyQuizDto(quizQuestion,quizAnswer,userAnswer,quizDate);
                myQuizDtoList.add(myQuizDto);
            }
        }

        if (!oxQuizAttemptList.isEmpty()){
            for(OxQuizAttempt oxQuizAttempt : oxQuizAttemptList){
                OxQuiz oxQuiz=oxQuizRepository.findByWords(oxQuizAttempt.getWords()).get();
                String quizQuestion=oxQuiz.getOxQuestion();
                String quizAnswer=oxQuiz.getOxAnswer();
                String userAnswer=oxQuizAttempt.getOxSubmitted();
                LocalDateTime createdTime=oxQuizAttempt.getLastModifiedTime();
                String quizDate = createdTime.format(formatter);
                MyQuizDto myQuizDto=new MyQuizDto(quizQuestion,quizAnswer,userAnswer,quizDate);
                myQuizDtoList.add(myQuizDto);
            }
        }

        return myQuizDtoList;
    }

    //마이페이지: 유저 펫 상태 조회
    public MyPetDto getMemberPet(Member member){
        Pet pet=petRepository.findByMember(member).get();
        PetCondition nextPetCondition=petConditionRepository.findById((long) (pet.getPetCondition().getId()+1)).get();

        String petImage=pet.getPetImage();
        String houseImage=pet.getHouseImage();
        String currentPet=pet.getPetCondition().getRanks();
        String nextPet=nextPetCondition.getRanks();
        int count=nextPetCondition.getCount()-pet.getScore();

        MyPetDto myPetDto=new MyPetDto(petImage, houseImage, currentPet, nextPet, count);

        return myPetDto;
    }

    //마이페이지: 유저 정보 조회(유저 이메일, 펫 상태, 푼 퀴즈 수, 북마크 개수)
    public MyPageDto getMemberInfo(Member member){
        Pet pet=petRepository.findByMember(member).get();
        List<Bookmark> bookmarkList = bookmarkRepository.findByMember(member);
        List<NewsQuizAttempt> newsQuizAttemptList=newsQuizAttemptRepository.findByMember(member);
        List<OxQuizAttempt> oxQuizAttemptList=oxQuizAttemptRepository.findByMember(member);

        String email=member.getEmail();
        String currentPet=pet.getPetCondition().getRanks();
        int quizCount=newsQuizAttemptList.size()+oxQuizAttemptList.size();
        int bookmarkCount= bookmarkList.size();

        MyPageDto myPageDto=new MyPageDto(email, currentPet, quizCount, bookmarkCount);

        return myPageDto;
    }
}