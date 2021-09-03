package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaapp.data.AnswerListAsyncResponse;
import com.example.triviaapp.data.QuestionBank;
import com.example.triviaapp.model.Question;
import com.example.triviaapp.util.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView question_textView;
    private TextView counter_textView;
    private TextView countdown_textView;
    private TextView totalNumberofQuestions_textView;
    private TextView highScore_textView;
    private ImageButton next_button;
    private ImageButton prev_button;
    private Button true_button;
    private Button false_button;
    private Button start_button;
    private Button restart_button;
    private Button pause_button;

    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private static final long DEFAULT_START_TIMER_MILLIS = 600000;
    private long timerLeftMillis = DEFAULT_START_TIMER_MILLIS;
    private boolean wasPaused = false;

    private int currentQuestionIndex = 0;
    private int corectlyAnswered = 0;
    private List<Question> myQuestions;
    private Prefs prefs;




    ////////////............Buttons........../////////

    public void buttonClicked(View view){
        switch(view.getId()) {
            case R.id.prev_button:
                Toast.makeText(this, "Sorry, there is no WAY BACK!, just like in life.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.next_button:
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
            case R.id.start_button:
                startQuiz();
                break;
            case R.id.restart_button:
                restartQuiz();
                break;
            case R.id.pause_button:
                pauseQuiz();
                break;
            default:
        }
    }

    ///////////............. Question Methods ............////////

    private void updateQuestion(){
        currentQuestionIndex++;
        counter_textView.setText("Score: " + corectlyAnswered+" / " +  currentQuestionIndex);
        question_textView.setText(myQuestions.get(currentQuestionIndex).getAnswer());

    }
    public void checkAnswer(Boolean guessedAnswer){
        if(guessedAnswer == myQuestions.get(currentQuestionIndex).isAnswerTrue()){
            corectlyAnswered++;
        }else{
            shakeAnimation();
        }
    }

    /////////.......... Quiz Methods..............//////////////

    private void startQuiz(){
        counter_textView.setText("Score: " + corectlyAnswered+" / " +  currentQuestionIndex);
        question_textView.setText(myQuestions.get(currentQuestionIndex).getAnswer());
        pause_button.setEnabled(true);
        fadeIn();

        start_button.setVisibility(View.INVISIBLE);
        startTimer();
    }
    private void restartQuiz(){
        start_button.setVisibility(View.VISIBLE);
        countDownTimer.cancel();
        updateTimer();
        timerLeftMillis = DEFAULT_START_TIMER_MILLIS;
        question_textView.setText("");
        currentQuestionIndex = 0;
        corectlyAnswered = 0;
        pause_button.setEnabled(false);
        fadeOut();
    }
    private void pauseQuiz(){
        if(timerRunning){
            countDownTimer.cancel();
            pause_button.setText("Continue");
            timerRunning = false;
            fadeOut();
        }else{
            startTimer();
            pause_button.setText("Pause");
            timerRunning = true;
            fadeIn();
        }
    }

    ///////////............. Timer Methods ................/////////

    public void startTimer(){
        countDownTimer = new CountDownTimer(timerLeftMillis, 1000) {
            @Override
            public void onTick(long l) {
                timerLeftMillis = l;
                updateTimer();
            }
            @Override
            public void onFinish() {
                timerRunning = false;
                prefs.saveHighestScore(corectlyAnswered);
                loadHighScore();
                restartQuiz();
            }
        }.start();
        timerRunning = true;
    }
    private void updateTimer(){
        int minutes = (int) (timerLeftMillis / 1000) / 60;
        int seconds = (int) (timerLeftMillis / 1000) % 60;
        String timeLeftFormated = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        countdown_textView.setText(timeLeftFormated);
    }
    public void loadHighScore(){
        int highScore = prefs.loadHighScore();
        highScore_textView.setText("HIGH SCORE : "+ highScore);
    }

    /////////.......... Animation Methods..............//////////////

    public void fadeOut(){
        true_button.setEnabled(false);
        false_button.setEnabled(false);
        next_button.setEnabled(false);
        prev_button.setEnabled(false);
        restart_button.setEnabled(false);
        totalNumberofQuestions_textView.setEnabled(false);
        countdown_textView.setEnabled(false);
        counter_textView.setEnabled(false);
        highScore_textView.setEnabled(false);
    }
    public void fadeIn(){
        true_button.setEnabled(true);
        false_button.setEnabled(true);
        next_button.setEnabled(true);
        prev_button.setEnabled(true);
        restart_button.setEnabled(true);
        totalNumberofQuestions_textView.setEnabled(true);
        countdown_textView.setEnabled(true);
        counter_textView.setEnabled(true);
        highScore_textView.setEnabled(true);
    }
    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    ////////////////............ Activity Lifecycle Methods .........///////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new Prefs(MainActivity.this);

        question_textView = findViewById(R.id.question_textView);
        counter_textView = findViewById(R.id.counter_textView);
        countdown_textView = findViewById(R.id.countdown_textView);
        totalNumberofQuestions_textView = findViewById(R.id.totalNumberofQuestions_textView);
        highScore_textView = findViewById(R.id.highScore_textView);
        next_button =  findViewById(R.id.next_button);
        prev_button = findViewById(R.id.prev_button);
        true_button = findViewById(R.id.true_button);
        false_button = findViewById(R.id.false_button);
        start_button = findViewById(R.id.start_button);
        restart_button = findViewById(R.id.restart_button);
        pause_button = findViewById(R.id.pause_button);

        fadeOut();
        pause_button.setEnabled(false);
        loadHighScore();

        myQuestions = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                totalNumberofQuestions_textView.setText("Total: " + myQuestions.size() + " questions");
            }
        });
        if(prefs.getIsPausedBool()) {
            currentQuestionIndex = prefs.getQuestionIndex();
            corectlyAnswered = prefs.getCorrectlyAns();
            timerLeftMillis = prefs.getTimeLeftMil();
            wasPaused = false;
            prefs.setIsPausedBool(wasPaused);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(prefs.getIsPausedBool()) {
            currentQuestionIndex = prefs.getQuestionIndex();
            corectlyAnswered = prefs.getCorrectlyAns();
            timerLeftMillis = prefs.getTimeLeftMil();
            wasPaused = false;
            pause_button.setText("Pause");
            prefs.setIsPausedBool(wasPaused);
            startQuiz();
        }

    }
    @Override
    protected void onPause() {
        countDownTimer.cancel();
        prefs.setState(currentQuestionIndex, corectlyAnswered );
        prefs.setTimer(timerLeftMillis);
        wasPaused = true;
        prefs.setIsPausedBool(wasPaused);
        super.onPause();
    }


}