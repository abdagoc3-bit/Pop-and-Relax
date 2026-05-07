package com.dagoc.finals;

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GridLayout bubbleGrid;
    private SoundPool soundPool;
    private int popSoundId;
    private Vibrator vibrator;
    private TextView txtPoppedCount;
    private int poppedCount = 0;
    private final int TOTAL_BUBBLES = 48;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bubbleGrid = findViewById(R.id.bubbleGrid);
        txtPoppedCount = findViewById(R.id.txtPoppedCount);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder().setMaxStreams(10).setAudioAttributes(attrs).build();
        popSoundId = soundPool.load(this, R.raw.popsoundeffect, 1);

        FrameLayout btnHome = findViewById(R.id.btnHome);
        FrameLayout btnRegain = findViewById(R.id.btnRegain);

        btnRegain.setOnClickListener(v -> resetGame());
        btnHome.setOnClickListener(v -> finish());

        createBoard();
    }

    private void resetGame() {
        poppedCount = 0;
        updateScoreText();
        createBoard();
    }

    private void updateScoreText() {
        txtPoppedCount.setText("Popped: " + poppedCount + " / " + TOTAL_BUBBLES);
    }

    private void createBoard() {
        bubbleGrid.removeAllViews();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 6; c++) {
                addBubble(r, c);
            }
        }
    }

    private void addBubble(int row, int col) {
        ImageButton bubble = new ImageButton(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                GridLayout.spec(row), GridLayout.spec(col));

        params.width = 150;
        params.height = 130;
        params.setMargins(1, 1, 1, 1);

        bubble.setLayoutParams(params);
        bubble.setBackgroundColor(Color.TRANSPARENT);
        bubble.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bubble.setImageResource(R.drawable.unpopped_bubble);

        bubble.setOnClickListener(v -> {
            soundPool.play(popSoundId, 1, 1, 0, 0, 1);
            if (vibrator != null) vibrator.vibrate(40);

            bubble.setImageResource(R.drawable.popped_bubble);
            bubble.setEnabled(false);

            poppedCount++;
            updateScoreText();
        });

        bubbleGrid.addView(bubble);
    }
}