package bg.tu_varna.sit.threadrunnabletasks;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Task 1 variables
    EditText TaskOneEmail;
    EditText TaskOnePassword;
    TextView TaskOneTextView;
    Button TaskOneButton;

    //Task 2 variables
    Button TaskTwoButton;
    TextView TaskTwoTextView;
    int Seconds = 0;
    boolean Counting;
    Handler Handler;

    //Task 3 variables
    Button TaskThreeButton;
    TextView TaskThreeTextView;
    ProgressBar progressBar;
    //Task 3 functions
    boolean isLoginSuccessful;
    boolean isDownloadSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TASK 1
        TaskOneEmail = findViewById(R.id.TaskOneEmail);
        TaskOnePassword = findViewById(R.id.TaskOnePassword);
        TaskOneTextView = findViewById(R.id.TaskOneTextView);
        TaskOneButton = findViewById(R.id.TaskOneButton);

        TaskOneTextView.setVisibility(View.INVISIBLE);

        TaskOneButton.setOnClickListener((e) -> {
            TaskOneTextView.setVisibility(View.INVISIBLE);
            new Thread(new TaskOneLogin(new TaskOneLoginValidator())).start();
        });
        //END TASK 1


        //TASK 2
        Handler = new Handler();

        TaskTwoTextView = findViewById(R.id.TaskTwoTextView);

        TaskTwoTextView.setText("");

        TaskTwoButton = findViewById(R.id.TaskTwoButton);

        TaskTwoButton.setOnClickListener((e) -> {
            if (!Counting) {
                Handler.postDelayed(countingRunnable, 1000);
                TaskTwoButton.setText("Pause");
                Counting = true;
            } else {
                Handler.removeCallbacks(countingRunnable);
                TaskTwoButton.setText("Resume");
                Counting = false;
            }
        });
        //END TASK 2


        //TASK 3
        TaskThreeTextView = findViewById(R.id.TaskThreeTextView);
        TaskThreeButton = findViewById(R.id.TaskThreeButton);
        progressBar = findViewById(R.id.progressBar);

        TaskThreeTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        TaskThreeButton.setOnClickListener((l) -> {
            progressBar.setVisibility(View.VISIBLE);
            TaskThreeTextView.setVisibility(View.INVISIBLE);
            Thread threadImage = new Thread(new TaskThreeDownloadImage());
            Thread threadLogin = new Thread(new TaskThreeLogin());

            threadImage.start();
            threadLogin.start();

            new Thread(() -> {
                try {
                    threadImage.join();
                    threadLogin.join();
                } catch (InterruptedException e) {
                    runOnUiThread(() -> TaskThreeTextView.setText("Error occurred."));
                    return;
                }

                if (isDownloadSuccessful && isLoginSuccessful) {
                    runOnUiThread(() -> TaskThreeTextView.setText("Success !"));
                } else {
                    runOnUiThread(() -> TaskThreeTextView.setText("Failed !"));
                }

                runOnUiThread(() -> {
                    TaskThreeTextView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                });

            }).start();

        });
        //END TASK 3
    }


    //TASK 1
    public class TaskOneLogin implements Runnable {

        private TaskOneLoginValidator validator;

        TaskOneLogin(TaskOneLoginValidator validator) {
            this.validator = validator;
        }

        @Override
        public void run() {
            boolean isEmailValid = validator.emailValidate(TaskOneEmail.getText().toString());
            boolean isPasswordValid = validator.passwordValidate(TaskOnePassword.getText().toString());

            String result;
            if (isEmailValid && isPasswordValid) {
                result = "Successful login !";
            } else {
                result = "Unsuccessful login !";
            }

            runOnUiThread(() -> {
                TaskOneTextView.setVisibility(View.VISIBLE);
                TaskOneTextView.setText(result);
            });
        }
    }
    //TASK 1 END

    //TASK 2
    private Runnable countingRunnable = new Runnable() {
        @Override
        public void run() {
            TaskTwoTextView.setText("Past Time:" + ++Seconds);
            Handler.postDelayed(this, 1000);
        }
    };
    //TASK 2 END

    //TASK 3
    class TaskThreeDownloadImage implements Runnable {
        @Override
        public void run() {
            Random random = new Random(System.currentTimeMillis());
            try {
                Thread.sleep((random.nextInt(4) + 2) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            isDownloadSuccessful = random.nextBoolean();
        }
    }

    class TaskThreeLogin implements Runnable {
        @Override
        public void run() {
            Random random = new Random(System.currentTimeMillis());
            try {
                Thread.sleep((random.nextInt(3) + 3) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            isLoginSuccessful = random.nextBoolean();
        }
    }
    //TASK 3 END
}
