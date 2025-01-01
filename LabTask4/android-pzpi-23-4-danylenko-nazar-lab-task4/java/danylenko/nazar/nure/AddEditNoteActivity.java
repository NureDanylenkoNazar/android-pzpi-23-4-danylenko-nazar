package danylenko.nazar.nure;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditNoteActivity extends AppCompatActivity {
    private EditText titleInput, descriptionInput;
    private Spinner importanceSpinner;
    private ImageView imageView;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        importanceSpinner = findViewById(R.id.importanceSpinner);
        imageView = findViewById(R.id.imageView);
        saveButton = findViewById(R.id.saveButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.importance_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("note")) {
            Note note = (Note) intent.getSerializableExtra("note");
            if (note != null) {
                titleInput.setText(note.getTitle());
                descriptionInput.setText(note.getDescription());
                int spinnerPosition = adapter.getPosition(note.getImportance());
                importanceSpinner.setSelection(spinnerPosition);
            }
        }


        saveButton.setOnClickListener(v -> {
            String updatedTitle = titleInput.getText().toString();
            String updatedDescription = descriptionInput.getText().toString();
            String updatedImportance = importanceSpinner.getSelectedItem() != null
                    ? importanceSpinner.getSelectedItem().toString()
                    : "";


            String updatedDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
            String updatedImagePath = "";

            String id = getIntent().hasExtra("note")
                    ? ((Note) getIntent().getSerializableExtra("note")).getId()
                    : UUID.randomUUID().toString();

            Note updatedNote = new Note(updatedTitle, updatedDescription, updatedImportance, updatedDateTime, updatedImagePath);
            updatedNote.setId(id);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("note", updatedNote);
            setResult(RESULT_OK, resultIntent);
            finish();
        });





    }
}
