package com.example.latszat_csal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper; // Biztonságosabb időzítéshez
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    ImageView imageLeft, imageRight;
    TextView roundText, resultText, descriptionText;
    LinearLayout imageLayout;

    int round = 0;
    int score = 0;
    List<Integer> aiImageList = new ArrayList<>();
    List<Integer> realImageList = new ArrayList<>();
    int MAX_ROUNDS;
    boolean isLeftAI;
    boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI elemek összekötése
        startButton = findViewById(R.id.startButton);
        imageLeft = findViewById(R.id.imageLeft);
        imageRight = findViewById(R.id.imageRight);
        roundText = findViewById(R.id.roundText);
        resultText = findViewById(R.id.resultText);
        descriptionText = findViewById(R.id.descriptionText);
        imageLayout = findViewById(R.id.imageLayout);

        // Képek betöltése (Ellenőrizd, hogy a nevek pontosak-e a drawable mappában!)
        aiImageList.clear();
        aiImageList.add(R.drawable.ai_1);
        aiImageList.add(R.drawable.ai_2);
        aiImageList.add(R.drawable.ai_3);

        realImageList.clear();
        realImageList.add(R.drawable.real_1);
        realImageList.add(R.drawable.real_2);
        realImageList.add(R.drawable.real_3);

        MAX_ROUNDS = Math.min(aiImageList.size(), realImageList.size());

        // Kezdő állapot beállítása
        descriptionText.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);
        roundText.setVisibility(View.GONE);
        resultText.setVisibility(View.GONE);

        startButton.setOnClickListener(v -> startGame());
        imageLeft.setOnClickListener(v -> checkAnswer(true));
        imageRight.setOnClickListener(v -> checkAnswer(false));
    }

    void startGame() {
        round = 0;
        score = 0;
        Collections.shuffle(aiImageList);
        Collections.shuffle(realImageList);

        descriptionText.setVisibility(View.GONE);
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

        isProcessing = false;

        // Keretek és hátterek letakarítása
        imageLeft.setBackgroundResource(0);
        imageRight.setBackgroundResource(0);
        imageLeft.setPadding(0, 0, 0, 0);
        imageRight.setPadding(0, 0, 0, 0);

        int aiImage = aiImageList.get(round);
        int realImage = realImageList.get(round);

        round++;
        roundText.setText("Kör: " + round + "/" + MAX_ROUNDS);

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
        if (isProcessing) return;
        isProcessing = true;

        boolean hit = (clickedLeft == isLeftAI);
        if (hit) score++;

        // Kiválasztjuk az AI képet
        ImageView aiView = isLeftAI ? imageLeft : imageRight;

        // Szín beállítása: áttetsző zöld vagy piros (formátum: #AARRGGBB)
        // 88 = kb. 50% átlátszóság
        int color = hit ? 0x884CAF50 : 0x88F44336;

        // Ráfestjük a színt a képre
        aiView.setColorFilter(color);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            aiView.clearColorFilter(); // Szűrő eltávolítása
            nextRound();
        }, 1000);
    }

    void endGame() {
        imageLayout.setVisibility(View.GONE);
        roundText.setVisibility(View.GONE);

        resultText.setVisibility(View.VISIBLE);
        resultText.setText("Eredmény: " + score + " / " + MAX_ROUNDS);

        startButton.setText("Új játék");
        startButton.setVisibility(View.VISIBLE);
        descriptionText.setVisibility(View.VISIBLE);
    }
}