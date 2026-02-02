package com.example.latszat_csal;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    ImageView imageLeft, imageRight;
    TextView roundText, resultText;
    LinearLayout imageLayout;

    int round = 0;
    int score = 0;

    List<Integer> aiImageList = new ArrayList<>();
    List<Integer> realImageList = new ArrayList<>();

    int MAX_ROUNDS;

    boolean isLeftAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        imageLeft = findViewById(R.id.imageLeft);
        imageRight = findViewById(R.id.imageRight);
        roundText = findViewById(R.id.roundText);
        resultText = findViewById(R.id.resultText);
        imageLayout = findViewById(R.id.imageLayout);

        aiImageList.add(R.drawable.ai_1);
        aiImageList.add(R.drawable.ai_2);
        aiImageList.add(R.drawable.ai_3);

        realImageList.add(R.drawable.real_1);
        realImageList.add(R.drawable.real_2);
        realImageList.add(R.drawable.real_3);

        MAX_ROUNDS = Math.min(aiImageList.size(), realImageList.size());

        startButton.setOnClickListener(v -> startGame());

        imageLeft.setOnClickListener(v -> checkAnswer(true));
        imageRight.setOnClickListener(v -> checkAnswer(false));
    }

    void startGame() {
        round = 0;
        score = 0;

        Collections.shuffle(aiImageList);
        Collections.shuffle(realImageList);

        startButton.setVisibility(View.GONE);
        resultText.setVisibility(View.GONE);
        roundText.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.VISIBLE);

        nextRound();
    }

    void nextRound() {
        if (round >= MAX_ROUNDS) {
            endGame();
            return;
        }

        round++;
        roundText.setText("Kör: " + round + "/" + MAX_ROUNDS);

        int aiImage = aiImageList.get(round - 1);
        int realImage = realImageList.get(round - 1);

        isLeftAI = new Random().nextBoolean();

        if (isLeftAI) {
            imageLeft.setImageResource(aiImage);
            imageRight.setImageResource(realImage);
        } else {
            imageLeft.setImageResource(realImage);
            imageRight.setImageResource(aiImage);
        }
    }

    void checkAnswer(boolean clickedLeft) {
        if (clickedLeft == isLeftAI) {
            score++;
        }
        nextRound();
    }

    void endGame() {
        imageLayout.setVisibility(View.GONE);
        roundText.setVisibility(View.GONE);
        resultText.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);

        resultText.setText("Eredmény: " + score + " / " + MAX_ROUNDS);
    }
}