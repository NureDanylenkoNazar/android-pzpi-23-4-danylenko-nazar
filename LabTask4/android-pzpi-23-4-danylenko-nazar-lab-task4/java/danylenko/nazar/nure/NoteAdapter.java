package danylenko.nazar.nure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private final Context context;
    private final List<Note> notes;
    private final OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onNoteClick(Note note);

        void onDeleteClick(Note note);
    }

    public NoteAdapter(Context context, List<Note> notes, OnNoteClickListener listener) {
        this.context = context;
        this.notes = notes;
        this.listener = listener;



    }
    public void updateNotes(List<Note> newNotes) {
        notes.clear();
        notes.addAll(newNotes);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.dateTimeTextView.setText(note.getDateTime());
        switch (note.getImportance()) {
            case "High":
                holder.importanceImageView.setImageResource(R.drawable.ic_high_importance);
                break;
            case "Medium":
                holder.importanceImageView.setImageResource(R.drawable.ic_medium_importance);
                break;
            case "Low":
                holder.importanceImageView.setImageResource(R.drawable.ic_low_importance);
                break;
            default:
                holder.importanceImageView.setImageResource(R.drawable.ic_low_importance);
                break;
        }

        holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(note));
    }




    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTimeTextView;
        ImageView importanceImageView;
        ImageView noteImageView;
        ImageView deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteTitle);
            dateTimeTextView = itemView.findViewById(R.id.noteDateTime);
            importanceImageView = itemView.findViewById(R.id.importanceIcon);
            noteImageView = itemView.findViewById(R.id.noteImage);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
