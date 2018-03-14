package com.example.ruturaj.scarnesdice_workshop1;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private TextView yourScoreTextView, computerScoreTextView;
  private ImageView diceImageView;
  private Button rollButton, holdButton, resetButton;
  private int yourTurn = 0, yourOverall = 0, compTurn = 0, compOverall = 0;
  private List<Drawable> diceImageList;
  private Boolean swap;
  private int diceFace, turns;
  private Animation rotateDice;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    yourScoreTextView = (TextView) findViewById(R.id.your_score_text_view);
    computerScoreTextView = (TextView) findViewById(R.id.computer_score_text_view);
    diceImageView = (ImageView) findViewById(R.id.dice_image_view);
    rollButton = (Button) findViewById(R.id.roll_button);
    holdButton = (Button) findViewById(R.id.hold_button);
    resetButton = (Button) findViewById(R.id.reset_button);
    diceImageList = new ArrayList<>();
    diceImageList.add(getResources().getDrawable(R.drawable.dice1));
    diceImageList.add(getResources().getDrawable(R.drawable.dice2));
    diceImageList.add(getResources().getDrawable(R.drawable.dice3));
    diceImageList.add(getResources().getDrawable(R.drawable.dice4));
    diceImageList.add(getResources().getDrawable(R.drawable.dice5));
    diceImageList.add(getResources().getDrawable(R.drawable.dice6));
    swap = true;
    rotateDice = AnimationUtils.loadAnimation(this, R.anim.rotate_dice);
    yourScoreTextView.setText(getResources().getString(R.string.your_score) + 0);
    computerScoreTextView.setText(getResources().getString(R.string.computer_score) + 0);
  }

  public void rollADice(View view) {
    AnimationSet set = new AnimationSet(false);
    set.addAnimation(rotateDice);
    int diceFace2 = (int) (Math.random() * 6);
    diceImageView.startAnimation(rotateDice);
//    diceImageView.animate().rotation(720f).setDuration(800);
    diceImageView.setImageDrawable(diceImageList.get(diceFace2));

    if (diceFace2 != 0) {
      yourTurn += diceFace2 + 1;
      Toast.makeText(this, "Your score : " + yourTurn, Toast.LENGTH_SHORT).show();
      holdButton.setEnabled(true);
    } else {
      Toast.makeText(getApplicationContext(), "Computer's turn", Toast.LENGTH_SHORT).show();
      rollButton.setEnabled(false);
      holdButton.setEnabled(false);
      new CountDownTimer(2000, 1000){

        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
          yourTurn = 0;
          holdButton.setEnabled(false);
          computerTurn();
        }
      }.start();
    }

    checkWinner();
  }

  public void holdTheScore(View view) {
    yourOverall += yourTurn;
    yourTurn = 0;
    yourScoreTextView.setText(getResources().getString(R.string.your_score) + yourOverall);
    holdButton.setEnabled(false);
    computerTurn();
    checkWinner();
  }

  public void resetTheScore(View view) {
    yourTurn = 0;
    compTurn = 0;
    yourOverall = 0;
    compOverall = 0;

    yourScoreTextView.setText(getResources().getString(R.string.your_score) + 0);
    computerScoreTextView.setText(getResources().getString(R.string.computer_score) + 0);
    diceImageView.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
    holdButton.setEnabled(true);
    rollButton.setEnabled(true);
  }

  private void computerTurn() {
    rollButton.setEnabled(false);
    holdButton.setEnabled(false);

    diceFace = (int) (Math.random() * 6);
    turns = (int) (Math.random() * 10) + 1;

    new CountDownTimer(turns * 1000 + 100, 900) {
      @Override
      public void onTick(long l) {
        diceImageView.startAnimation(rotateDice);
//        diceImageView.animate().rotation(720f).setDuration(500);
        diceImageView.setImageDrawable(diceImageList.get(diceFace));
        if (diceFace > 0 && turns > 0) {
          compTurn += diceFace + 1;
          Toast.makeText(getApplicationContext(), "Computer rolled " + ++diceFace,
              Toast.LENGTH_SHORT).show();
          diceFace = (int) (Math.random() * 6);
          turns--;
        } else {
          this.onFinish();
        }
      }
      @Override
      public void onFinish() {
        if (diceFace == 0) {
          compTurn = 0;
          Toast.makeText(getApplicationContext(), "Computer rolled a 1", Toast.LENGTH_SHORT).show();
        } else {
          compOverall += compTurn;
          computerScoreTextView
              .setText(getResources().getString(R.string.computer_score) + compOverall);
          Toast.makeText(getApplicationContext(), "Computer chose to hold", Toast.LENGTH_SHORT)
              .show();
        }
        checkWinner();
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
        this.cancel();
      }
    }.start();


    /*while (diceFace > 0 && turns > 0) {
      compTurn += diceFace + 1;
      diceFace = (int) (Math.random() * 6);
      turns--;
    }*/

  }

  private void checkWinner() {
    if (yourOverall >= 100) {
      Toast.makeText(this, "You won!", Toast.LENGTH_SHORT).show();
      rollButton.setEnabled(false);
      holdButton.setEnabled(false);
    } else if (compOverall >= 100) {
      Toast.makeText(this, "Computer won!", Toast.LENGTH_SHORT).show();
      rollButton.setEnabled(false);
      holdButton.setEnabled(false);
    }
  }
}
