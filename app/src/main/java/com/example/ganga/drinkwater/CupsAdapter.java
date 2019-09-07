package com.example.ganga.drinkwater;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ganga.drinkwater.data.WaterContract;

/**
 * Created by ganga on 5/2/18.
 */

public class CupsAdapter extends RecyclerView.Adapter<CupsAdapter.NumberViewHolder> {

    Context mcontext;
    private Cursor mcursor;

    private final String TAG = CupsAdapter.class.getSimpleName();

    public CupsAdapter(Context context, Cursor cursor) {

        mcontext = context;
        mcursor = cursor;
        Log.v(TAG, "Cups Adapter Called" + cursor.getCount());

    }

    @Override
    public CupsAdapter.NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutIdForListItem = R.layout.list_item;

        mcontext = viewGroup.getContext();
        //int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CupsAdapter.NumberViewHolder holder, int position) {

        Log.v(TAG, "Bind View Holder Is Called ");

     if(!mcursor.moveToPosition(position)){

         Log.v(TAG, "Bind View Holder If Loop Is Called ");

         return;
     }

     String date = mcursor.getString(mcursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_DATE));

     int noOfCups = mcursor.getInt(mcursor.getColumnIndex(WaterContract.WaterEntry.COLUMN_TOTALCUPS));

        holder.topNumbertextView.setText(date);

        holder.bottomNumbertextView.setText(String.valueOf(noOfCups));
        holder.itemView.setTag(position);


    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "Cups Adapter in get Item Count Is Called" + mcursor.getCount());
        return mcursor.getCount();

    }

    public void swapCursor(Cursor newCursor){

        if(mcursor!= null){
            mcursor.close();
        }

        mcursor = newCursor;

        if(mcursor != null){
            notifyDataSetChanged();
        }
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RecyclerViewClickListener mListener;

        TextView topNumbertextView;
        TextView bottomNumbertextView;

        public NumberViewHolder (View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            topNumbertextView =  itemView.findViewById(R.id.textView1);
            bottomNumbertextView = itemView.findViewById(R.id.textView3);



        }

        @Override
        public void onClick(View view) {

            Toast.makeText(mcontext, "Clicked " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }


}
