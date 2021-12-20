package com.example.telegramclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
//
//    List<String> userList;
//    String userName;
//    Context mContext;
//
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return userList.size();
//    }
//
////    public class ViewHolder extends RecyclerView.ViewHolder{
////
//////        private TextView textViewUsers;
//////        private CircleImageView imageViewUsers;
//////        private CardView cardView;
//////
//////        public ViewHolder(@NonNull View itemView) {
//////            super(itemView);
//////        }
//////
//////        public class ViewHolder extends RecyclerView.ViewHolder {
//////
//////            public ViewHolder(View itemView){
//////                super(itemView);
//////
//////                textViewUsers = itemView.findViewById(R.id.textViewUsers);
//////                imageViewUsers = itemView.findViewById(R.id.textViewUsers);
//////                cardView = itemView.findViewById(R.id.cardView);
//////            }
////
////        }
//    }
//}
