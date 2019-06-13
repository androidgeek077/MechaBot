package app.uos.mechabot.Admin;

import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.CardView;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.DisplayMetrics;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.ViewFlipper;

        import com.bumptech.glide.Glide;


import app.uos.mechabot.Models.UserModel;
import app.uos.mechabot.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUsersFragment extends Fragment {


//    int images[]={R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};
    private ViewFlipper simpleViewFlipper;
    private ArrayList<String> mCategories=new ArrayList<>();


    int countInt, incrementalCount;
    DatabaseReference CustomerReference;
//    CustomerProfileAdapter mProductAdapter;
    RecyclerView mCustomerRecycVw;
    String count;

    public ViewUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_users, container, false);

        getActivity().setTitle("Users");


        CustomerReference= FirebaseDatabase.getInstance().getReference().child("user");
        mCustomerRecycVw=view.findViewById(R.id.main_recycler_vw);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mCustomerRecycVw.setLayoutManager(mLayoutManager);
        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<UserModel> options=new FirebaseRecyclerOptions.Builder<UserModel>()
                .setQuery(CustomerReference, UserModel.class)
                .build();

        final FirebaseRecyclerAdapter<UserModel, CustomersViewHolder> adapter=new FirebaseRecyclerAdapter<UserModel, ViewUsersFragment.CustomersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewUsersFragment.CustomersViewHolder holder, final int position, @NonNull final UserModel model) {


                DisplayMetrics displaymetrics = new DisplayMetrics();
                (getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width

                holder.postTitle.setText(model.getname());

                holder.postDescription.setText(model.getemail());
                Glide.with(getActivity().getApplicationContext()).load(model.getImageUrl()).into(holder.postImage);



                holder.mDelCustomerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference key= getRef(position);
                        key.removeValue();
                    }
                });



//                holder.mMinusBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        count=holder.mTotalCount.getText().toString();
//                        countInt=Integer.parseInt(count);
//                        incrementalCount=countInt--;
//
//                        holder.mTotalCount.setText(countInt+"");
//                    }
//                });


            }

            @NonNull
            @Override
            public ViewUsersFragment.CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_item_layout, viewGroup,false);
                ViewUsersFragment.CustomersViewHolder customersViewHolder=new ViewUsersFragment.CustomersViewHolder(view);
                return customersViewHolder;
            }
        };

        mCustomerRecycVw.setAdapter(adapter);
        adapter.startListening();

    }


    public static class CustomersViewHolder extends  RecyclerView.ViewHolder{


        ImageView postImage,mDelCustomerBtn;
        TextView postTitle;
        TextView postDescription;
        LinearLayout mItemCountLin;
        Button btnOrderNow;
        CardView cardView;

        ImageView mPlusBtn, mMinusBtn;
        TextView mTotalCount;
        public CustomersViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.UserImage);
            postTitle = (TextView) itemView.findViewById(R.id.userName);
            postDescription = (TextView) itemView.findViewById(R.id.userEmail);
            mDelCustomerBtn=itemView.findViewById(R.id.DeleteImgVw);
//            cardView=itemView.findViewById(R.id.cardVBtn);


        }
    }

}
