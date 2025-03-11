package com.dreamct.tingfeng;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    
    private List<NotificationModel> notificationList;

    public void setNotifications(List<NotificationModel> notifications) {
        this.notificationList = notifications;
    }

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }
    /**绑定数据*/
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);
        holder.tvName.setText(notification.getAppName());
        holder.tvTitle.setText(notification.getTitle());
        holder.tvContent.setText(notification.getContent());
        holder.tvTime.setText(notification.getFormattedTime());
    }
    /**获取数据数量*/
    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNotificationName);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvContent = itemView.findViewById(R.id.tvNotificationContent);
            tvTime = itemView.findViewById(R.id.tvNotificationTime);
        }
    }

}