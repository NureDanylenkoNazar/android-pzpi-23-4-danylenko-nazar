package danylenko.nazar.nure;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private double firstNumber = 0, secondNumber = 0;
    private String operator = "";
    private boolean isOperatorPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                String buttonText = button.getText().toString();

                if (buttonText.matches("[0-9.]")) {
                    if (isOperatorPressed) {
                        display.setText("");
                        isOperatorPressed = false;
                    }
                    display.append(buttonText);
                } else if (buttonText.matches("[+\\-*/]")) {
                    firstNumber = Double.parseDouble(display.getText().toString());
                    operator = buttonText;
                    isOperatorPressed = true;
                } else if (buttonText.equals("=")) {
                    secondNumber = Double.parseDouble(display.getText().toString());
                    double result = calculateResult(firstNumber, secondNumber, operator);
                    display.setText(String.valueOf(result));
                } else if (buttonText.equals("C")) {
                    display.setText("");
                    firstNumber = 0;
                    secondNumber = 0;
                    operator = "";
                }
            }
        };

        // Bind buttons
        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide,
                R.id.btn_equals, R.id.btn_clear
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private double calculateResult(double first, double second, String operator) {
        switch (operator) {
            case "+": return first + second;
            case "-": return first - second;
            case "*": return first * second;
            case "/": return second != 0 ? first / second : 0;
            default: return 0;
        }
    }
}