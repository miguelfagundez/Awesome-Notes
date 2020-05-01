package com.devproject.miguelfagundez.awesomenotes.adapters;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devproject.miguelfagundez.awesomenotes.R;
import com.devproject.miguelfagundez.awesomenotes.listeners.NotesListeners;
import com.devproject.miguelfagundez.awesomenotes.model.pojo.Note;
import com.devproject.miguelfagundez.awesomenotes.utils.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/********************************************
 * Class- NotesAdapter
 * This class implements the Firestore recycler adapter
 *
 * @author: Miguel Fagundez
 * @date: April 24th, 2020
 * @version: 1.0
 * *******************************************/
public class NotesAdapter extends FirestoreRecyclerAdapter<Note, NotesAdapter.NotesViewHolder> {

    private NotesListeners listeners;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<Note> options, NotesListeners listeners) {
        super(options);
        this.listeners = listeners;
    }

    @Override
    protected void onBindViewHolder(@NonNull final NotesViewHolder holder, final int position, @NonNull final Note note) {
        holder.tvTitle.setText(note.getTitle());
        CharSequence date = DateFormat.format(Constants.DATE_FORMAT, note.getCreation().toDate());
        holder.tvCreationDate.setText(date);
        holder.ivNotePhoto.setImageResource(R.drawable.awesomenote);

        // Detecting the itemView clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                listeners.updateNote(note.getTitle(), note.getPhoto(), note.getCreation(), note.getBody(), snapshot.getId());
            }
        });
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NotesViewHolder(view);
    }

    // inner class view holder
    public class NotesViewHolder extends RecyclerView.ViewHolder{

        // Views declaration: note_item layout
        TextView tvTitle;
        TextView tvCreationDate;
        ImageView ivNotePhoto;

        public NotesViewHolder(View itemView){
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvCreationDate = itemView.findViewById(R.id.tvNoteCreation);
            ivNotePhoto = itemView.findViewById(R.id.ivNoteImage);

        }

        // Connecting with MainActivity
        public void deleteNoteFromRecyclerView(){
            listeners.deleteNote(getSnapshots().getSnapshot(getAdapterPosition()));
        }

    }
}
