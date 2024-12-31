package danylenko.nazar.nure;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> noteList;
    private List<Note> originalNoteList;

    private static final int REQUEST_CODE_ADD_NOTE = 1;
    private static final int REQUEST_CODE_EDIT_NOTE = 2;

    private int findNotePosition(Note updatedNote) {
        for (int i = 0; i < originalNoteList.size(); i++) {
            if (originalNoteList.get(i).getId().equals(updatedNote.getId())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        originalNoteList = new ArrayList<>();
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(this, noteList, new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra("note", note);
                startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
            }

            @Override
            public void onDeleteClick(Note note) {
                deleteNoteAt(note);
            }
        });
        recyclerView.setAdapter(adapter);
        originalNoteList.add(new Note("Title 1", "Description 1", "High", "01-01-2024 12:00", ""));
        originalNoteList.add(new Note("Title 2", "Description 2", "Medium", "02-01-2024 14:00", ""));
        originalNoteList.add(new Note("Title 3", "Description 3", "Low", "03-01-2024 16:00", ""));
        noteList.addAll(originalNoteList); // Ініціалізація відображуваного списку
        adapter.notifyDataSetChanged();
        registerForContextMenu(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterNotes(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterNotes(newText);
                    return true;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addNote) {
            Intent intent = new Intent(this, AddEditNoteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
            return true;
        } else if (id == R.id.filter_high) {
            filterByImportance("High");
            return true;
        } else if (id == R.id.filter_medium) {
            filterByImportance("Medium");
            return true;
        } else if (id == R.id.filter_low) {
            filterByImportance("Low");
            return true;
        } else if (id == R.id.filter_all) {
            showAllNotes();
            return true;
        } else if (id == R.id.change_language_en) {
            setLocale("en");
            return true;
        } else if (id == R.id.change_language_uk) {
            setLocale("uk");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteNoteAt(Note note) {
        if (noteList.contains(note)) {
            int position = noteList.indexOf(note);
            noteList.remove(position);
            originalNoteList.remove(note);
            adapter.notifyItemRemoved(position);
        }
    }

    private void filterNotes(String query) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : originalNoteList) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    note.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredNotes.add(note);
            }
        }
        updateDisplayedNotes(filteredNotes);
    }

    private void filterByImportance(String importance) {
        List<Note> filteredNotes = new ArrayList<>();
        for (Note note : originalNoteList) {
            if (note.getImportance().equals(importance)) {
                filteredNotes.add(note);
            }
        }
        updateDisplayedNotes(filteredNotes);
    }

    private void showAllNotes() {
        updateDisplayedNotes(originalNoteList);
    }

    private void updateDisplayedNotes(List<Note> notesToShow) {
        noteList.clear();
        noteList.addAll(notesToShow);
        adapter.notifyDataSetChanged();
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MainActivity", "onActivityResult called. Request code: " + requestCode);

        if (resultCode == RESULT_OK && data != null) {
            Note newNote = (Note) data.getSerializableExtra("note");
            Log.d("MainActivity", "Note received: " + (newNote != null ? newNote.getTitle() : "null"));

            if (newNote != null) {
                if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    originalNoteList.add(newNote);
                    noteList.add(newNote);
                    adapter.notifyItemInserted(noteList.size() - 1);
                } else if (requestCode == REQUEST_CODE_EDIT_NOTE) {
                    int position = findNotePosition(newNote);
                    if (position >= 0) {
                        originalNoteList.set(position, newNote);
                        noteList.set(position, newNote);
                        adapter.notifyItemChanged(position);
                    }
                }
            }
        } else {
            Log.d("MainActivity", "Result code: " + resultCode);
        }
    }
}
