package danylenko.nazar.nure;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.result_text);

        setButtonClickListeners();
    }

    private void setButtonClickListeners() {
        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
                R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
                R.id.btn_8, R.id.btn_9, R.id.btn_plus, R.id.btn_minus,
                R.id.btn_multiply, R.id.btn_divide, R.id.btn_clear
        };

        for (int id : buttonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(view -> {
                String buttonText = button.getText().toString();


                if (buttonText.equals("C")) {
                    resultTextView.setText("");
                } else {

                    resultTextView.append(buttonText);
                }
            });
        }
    }
}
