package cg.dieudonne.nieme.travelmantics.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cg.dieudonne.nieme.travelmantics.R;
import cg.dieudonne.nieme.travelmantics.activities.DealActivity;
import cg.dieudonne.nieme.travelmantics.models.HolidayDeal;
import cg.dieudonne.nieme.travelmantics.utils.FirebaseUtil;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.HolidayDealViewHolder>{
    ArrayList<HolidayDeal> holidayDeals;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ImageView imageDeal;

    public HolidayAdapter() {
        //FirebaseUtil.openFbReference("traveldeals");
        firebaseDatabase = FirebaseUtil.firebaseDatabase;
        databaseReference = FirebaseUtil.databaseReference;
        this.holidayDeals = FirebaseUtil.holidayDeals;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HolidayDeal hd = dataSnapshot.getValue(HolidayDeal.class);
                hd.setId(dataSnapshot.getKey());
                holidayDeals.add(hd);
                notifyItemInserted(holidayDeals.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    @Override
    public HolidayDealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.holidaydeal_item, parent, false);
        return new HolidayDealViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(HolidayDealViewHolder holder, int position) {
        HolidayDeal deal = holidayDeals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return holidayDeals.size();
    }

    public class HolidayDealViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        TextView tv_title;
        TextView tv_description;
        TextView tv_price;

        public HolidayDealViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            imageDeal = (ImageView) itemView.findViewById(R.id.imageDeal);
            itemView.setOnClickListener(this);
        }

        public void bind(HolidayDeal deal) {
            tv_title.setText(deal.getTitle());
            tv_description.setText(deal.getDescription());
            tv_price.setText(deal.getPrice());
            showImage(deal.getImageUrl());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            HolidayDeal selectedDeal = holidayDeals.get(position);
            Intent intent = new Intent(view.getContext(), DealActivity.class);
            intent.putExtra("Deal", selectedDeal);
            view.getContext().startActivity(intent);
        }

        private void showImage(String url) {
            if (url != null && !url.isEmpty()) {
                Picasso.with(imageDeal.getContext())
                        .load(url)
                        .resize(160, 160)
                        .centerCrop()
                        .into(imageDeal);
            }
        }
    }
}
